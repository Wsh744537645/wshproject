package com.mpool.share.admin.module.bill.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.common.model.billenem.BillTypeEnum;
import com.mpool.common.model.log.LogUserOperation;
import com.mpool.common.model.share.UserBill;
import com.mpool.common.model.share.UserBillToBePay;
import com.mpool.common.model.share.UserPayModel;
import com.mpool.common.util.DateUtil;
import com.mpool.common.model.billenem.BillEnableEnum;
import com.mpool.share.admin.exception.AdminException;
import com.mpool.share.admin.exception.ExceptionCode;
import com.mpool.share.admin.model.User;
import com.mpool.share.admin.model.UserBillToBePayVO;
import com.mpool.share.admin.module.bill.mapper.UserBillMapper;
import com.mpool.share.admin.module.bill.mapper.UserBillToBePaidMapper;
import com.mpool.share.admin.module.bill.mapper.UserPayMapper;
import com.mpool.share.admin.module.bill.service.RealTimeDataService;
import com.mpool.share.admin.module.bill.service.UserBillService;
import com.mpool.share.admin.module.bill.service.UserService;
import com.mpool.share.admin.module.log.mapper.LogAdminOperationMpper;
import com.mpool.share.admin.module.system.service.SysUserService;
import com.mpool.share.admin.utils.AccountSecurityUtils;
import com.mpool.share.admin.utils.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: stephen
 * @Date: 2019/6/18 15:23
 */

@Service
public class UserBillServiceImpl implements UserBillService {
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private UserBillToBePaidMapper userBillToBePaidMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPayMapper userPayMapper;
    @Autowired
    private LogAdminOperationMpper logAdminOperationMpper;
    @Autowired
    private RealTimeDataService realTimeDataService;

    @Override
    public IPage<Map<String, Object>> getSettlementBillList(IPage<Map<String, Object>> page, Long userId) {
        return userBillMapper.getSettlementBillList(page, userId);
    }

    @Override
    public IPage<UserBillToBePayVO> getToBePaidBillList(IPage<UserBillToBePayVO> page, Date begTime, Date endTime, Integer state) {
        return userBillToBePaidMapper.getToBePaidBillList(page, begTime, endTime, state);
    }

    @Override
    public UserBillToBePayVO findToBePaidBill(String paidId, String txid) {
        if(txid != null){
            return userBillToBePaidMapper.getToBePaidBillByTxid(txid);
        }
        if(paidId != null){
            return userBillToBePaidMapper.getToBePaidBillByPaidId(paidId);
        }
        return null;
    }

    @Override
    public IPage<Map<String, Object>> getHadPaidBillList(IPage<Map<String, Object>> page) {
        return userBillMapper.getHadPaidBillList(page);
    }

    @Override
    @Transactional
    public void createTobePaidBill(List<String> billIds) {
        UserBillToBePay pay = new UserBillToBePay();
        pay.setPaidId(KeyUtil.genUniqueKey());
        if(AccountSecurityUtils.getUser().getUsername() == null) {
            pay.setAdminName(sysUserService.getCurrentSysuser().getUsername());
        }else{
            pay.setAdminName(AccountSecurityUtils.getUser().getUsername());
        }

        Double total = userBillMapper.getBillEarnTotal(billIds);

        pay.setCreateTime(new Date());
        pay.setPayNum(total);
        pay.setState(0);
        userBillToBePaidMapper.insert(pay);

        userBillMapper.updateBillPaidId(billIds, pay.getPaidId());
    }

    @Override
    @Transactional
    public void createExtraBill(String username, Double payNum, String discrible) {
        User user = userService.findByUsername(username);
        if(user == null){
            throw new AdminException(ExceptionCode.user_not_exists);
        }
        Double balance = userPayMapper.getBalanceByUserId(user.getUserId());

        Date date = new Date();
        UserBill userBill = new UserBill();
        userBill.setId(KeyUtil.genUniqueKey());
        userBill.setPuid(user.getUserId());
        userBill.setEnabled(BillEnableEnum.BILL_ENABLED_HAD_SETTLEMENT.getCode());
        userBill.setEarn(payNum);
        userBill.setBillType(BillTypeEnum.BILL_TYPE_ADMIN_ADD_MONEY.getCode());
        userBill.setDay(DateUtil.getYYYYMMdd(date));
        userBill.setCreateTime(date);
        userBill.setBalance(balance + payNum);
        userBill.setDiscrible(discrible);
        userBillMapper.insert(userBill);

        userPayMapper.updateUserBalance(user.getUserId(), userBill.getBalance());

        LogUserOperation logUserOperation = new LogUserOperation();
        logUserOperation.setUserType("admin");
        logUserOperation.setUserId(AccountSecurityUtils.getUser().getUserId());
        logUserOperation.setContent("管理员新增账单[" + userBill.getId() + "]");
        logUserOperation.setLogType(3);
        logUserOperation.setCreatedTime(date);
        logAdminOperationMpper.insert(logUserOperation);
    }

    @Override
    @Transactional
    public void updateBillStateByTxid(String paidId, String txid) {
        UserBillToBePay userBillToBePay = userBillToBePaidMapper.getBillToBePaidByTxid(txid);
        if(userBillToBePay != null){
            throw new AdminException(ExceptionCode.txid_exists);
        }

        List<UserBill> userBills = userBillMapper.getBillListByPaidId(paidId);

        //更新用户余额
        Map<Long, Double> userBalances = new HashMap<>();
        userBills.forEach(userBill -> {
            if(!userBalances.containsKey(userBill.getPuid())){
                userBalances.put(userBill.getPuid(), 0D);
            }
            userBalances.put(userBill.getPuid(), userBalances.get(userBill.getPuid()) + userBill.getEarn());
        });
        List<UserPayModel> userPayModelList = userPayMapper.getUsersPay(userBalances.keySet());
        List<UserBill> userBillList = new ArrayList<>();
        userPayModelList.forEach(userPayModel -> {
            Double balance = userPayModel.getBalance() - userBalances.get(userPayModel.getPuid());
            if(balance < 0D){
                throw new AdminException(ExceptionCode.balance_not_enough);
            }
            userPayModel.setBalance(balance);

            Date date = new Date();
            UserBill userBill = new UserBill();
            userBill.setId(KeyUtil.genUniqueKey());
            userBill.setPuid(userPayModel.getPuid());
            userBill.setEnabled(BillEnableEnum.BILL_ENABLED_OTHER.getCode());
            userBill.setEarn(userBalances.get(userPayModel.getPuid()));
            userBill.setBillType(BillTypeEnum.BILL_TYPE_PAY_COMPLETE.getCode());
            userBill.setDay(DateUtil.getYYYYMMdd(date));
            userBill.setCreateTime(date);
            userBill.setBalance(balance);
            userBill.setPaidId(paidId);
            userBill.setDiscrible("管理员打款记录txid=[" + txid + "]");
            userBillList.add(userBill);
        });
        userPayMapper.updateUserBalanceBatch(userPayModelList);

        //增加账单
        userBillMapper.insertBills(userBillList);

        //更新订单状态
        userBillMapper.updateBillState(userBills, BillEnableEnum.BILL_ENABLED_HAD_PAY.getCode());
        userBillToBePaidMapper.updateBillState(paidId, txid, new Date(), realTimeDataService.getCurUsdToCurrency(1));

        LogUserOperation logUserOperation = new LogUserOperation();
        logUserOperation.setUserType("admin");
        logUserOperation.setUserId(AccountSecurityUtils.getUser().getUserId());
        logUserOperation.setContent("管理员完成打款[txid:" + txid + "]");
        logUserOperation.setLogType(4);
        logUserOperation.setCreatedTime(new Date());
        logAdminOperationMpper.insert(logUserOperation);
    }

    @Override
    public List<Map<String, Object>> getBillItems(String paidId) {
        return userBillMapper.getBillDetailsListByPaidId(paidId);
    }

    @Override
    @Transactional
    public void canalToPayBill(String billId) {
        UserBill userBill = userBillMapper.getPaidIdByBillId(billId);
        if(userBill == null){
            throw new AdminException(ExceptionCode.bill_not_exists);
        }
        if(!userBill.getEnabled().equals(BillEnableEnum.BILL_ENABLED_WILL_PAY.getCode())){
            throw new AdminException(ExceptionCode.bill_state_error);
        }

        UserBillToBePayVO userBillToBePay = userBillToBePaidMapper.getToBePaidBillByPaidId(userBill.getPaidId());
        List<UserBill> userBillList = userBillMapper.getBillListByPaidId(userBill.getPaidId());
        if(userBillList.size() > 1){  //如果待支付记录不止一个订单，则扣除当前账单收益。否则删除该记录
            userBillToBePay.setPayNum(userBillToBePay.getPayNum() - userBill.getEarn());
            userBillToBePaidMapper.updatePayNumByPaidId(userBill.getPaidId(), userBillToBePay.getPayNum());
        }else {
            userBillToBePaidMapper.deleteOne(userBillToBePay.getPaidId());
        }
        userBillMapper.canalBillPaidId(userBill.getId());
    }

    @Override
    public Double getRecommendShareTotal() {
        return userBillMapper.getRecommendShareTotal();
    }

    @Override
    public Map<String, Object> getAdminPay() {
        return userBillMapper.getAcceptOrderAdminPay();
    }

    @Override
    public IPage<Map<String, Object>> getRecommendList(IPage<Map<String, Object>> page, Date begTime, Date endTime, String username) {
        return userBillMapper.getRecommendList(page, begTime, endTime, username);
    }

    @Override
    public IPage<Map<String, Object>> getAcceptOrderAdminPayList(IPage<Map<String, Object>> page, Date begTime, Date endTime, String productName) {
        return userBillMapper.getAcceptOrderAdminPayList(page, begTime, endTime, productName);
    }

    @Override
    public IPage<Map<String, Object>> getAdminPayLog(IPage<Map<String, Object>> page) {
        return userBillToBePaidMapper.getAdminPayLog(page);
    }
}
