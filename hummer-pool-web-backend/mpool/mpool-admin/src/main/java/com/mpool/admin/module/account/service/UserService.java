package com.mpool.admin.module.account.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mpool.admin.module.account.model.MasterUserShaerModel;
import com.mpool.admin.module.account.model.UserPayBillOut;
import com.mpool.common.BaseService;
import com.mpool.common.model.account.User;
import com.mpool.common.model.pool.StatsUsersDay;
import com.mpool.common.model.pool.StatsWorkersDay;

/**
 * account 用户
 * 
 * @author chenjian2
 *
 */
public interface UserService extends BaseService<User> {

	User findByUsername(String username);

	IPage<Map<String, Object>> listPage(IPage<User> page, User user);

	/**
	 * 获得主账号列表
	 * 
	 * @param page
	 * @param user
	 * @return
	 */
	IPage<UserPayBillOut> masterListPage(IPage<User> page, User user);

	/**
	 * 获得子账号列表
	 * 
	 * @param page
	 * @param user
	 * @return
	 */
	IPage<UserPayBillOut> memberListPage(IPage<User> page, User user);

	/**
	 * 获取用户30天活跃矿机
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUser30DayWorker(Long userId);

	/**
	 * 获取用户24小时活跃矿机
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUser24HWorker(Long userId);

	/**
	 * 获得用户30天算力
	 * 
	 * @param userId
	 * @return
	 */
	List<StatsUsersDay> getUser30DayShare(Long userId);

	/**
	 * 获得总账号30天算力变化
	 * 
	 * @param userId
	 * @return
	 */
	List<MasterUserShaerModel> getMasterUser30DayShare(Long userId);

//	/**
//	 * wgg
//	 * 根据用户名查询用户的信息（当前算力，过去一天算力，账号余额，昨日收益，总收益，累计转账...）
//	 * @param username
//	 * @return
//	 */
	List<UserPayBillOut> getUserListByUsername(String username);

	/**
	 * wgg 获取全部主账号信息（当前算力，过去一天算力，账号余额，昨日收益，总收益，累计转账...）
	 * 
	 * @param user
	 * @return
	 */
	List<UserPayBillOut> masterAllList(User user);

	/**
	 * wgg 导出主账号列表
	 * 
	 * @param user
	 * @return
	 */
	List<List<Object>> exportMasterList(User user);

	/**
	 * wgg 获取主子账号信息，可以根据推荐人 子账号 主账号搜索 带分页
	 * 
	 * @param username
	 * @param page
	 * @return
	 */
	IPage<User> getUserInfos(IPage<User> page, String username);

	/**
	 * wgg 帮用户修改密码
	 * 
	 * @param usrename
	 */
	void updatePasswordByUsername(String usrename);

	/**
	 * wgg 导出主账号信息（账户名，注册时间，最后登录时间，注册手机，邮箱）
	 * 
	 * @param username
	 * @return
	 */
	List<List<Object>> exportMasterUserInfoList(String username);

	/**
	 * 检测用户是否存在
	 * 
	 * @param username
	 * @return
	 */
	boolean getCheckUsername(String username);
}
