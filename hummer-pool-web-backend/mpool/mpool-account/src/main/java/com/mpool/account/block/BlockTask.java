package com.mpool.account.block;


import com.googlecode.jsonrpc4j.Base64;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mpool.account.service.block.BlockDifficultyService;
import com.mpool.account.service.block.BlocksService;
import com.mpool.common.model.block.BlockDifficulty;
import com.mpool.common.model.block.Blocks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Slf4j
@Component
public class BlockTask {

    private final static String RPC_USER = "bitcoinrpc";
    private final static String RPC_PASSWORD = "xxxx";
    private final static String RPC_ALLOWIP = "39.104.57.172";//"192.168.0.123";
    private final static String RPC_PORT = "8332";

    private JsonRpcHttpClient client = null;
    private Integer curHeight = new Integer(0);
    private BigDecimal curDifficulty = new BigDecimal(0);

    @Autowired
    private BlocksService blocksService;
    @Autowired
    private BlockDifficultyService blockDifficultyService;

    public void initTask(){
        //身份认证
        String cred = Base64.encodeBytes((RPC_USER + ":" + RPC_PASSWORD).getBytes());
        Map<String,String> headers = new HashMap<>(1);
        headers.put("Authorization", "Basic " + cred);
        try {
            client = new JsonRpcHttpClient(new URL("http://" + RPC_ALLOWIP + ":" + RPC_PORT), headers);
        } catch (MalformedURLException e) {
            log.error("json rpc connect to bitcoind fail......");
            e.printStackTrace();
        }
        blockTask();
        getNewBlockTask();
    }

    /**
     * 获得历史区块且入库
     */
    private void blockTask(){
        //List<Blocks> blockList = new ArrayList<>();
        List<BlockDifficulty> difficultyBlockList = new ArrayList<>();
        try {
            //int dbCount = blocksService.getCount();
            Map<String, Object> map = blockDifficultyService.getNewBlock();
            int dbCount = 0;
            if(map != null){
                dbCount = Integer.parseInt(map.get("cur_height").toString());
                curDifficulty = new BigDecimal(map.get("difficulty").toString());
            }

            log.info("blocktask 1");
            //获取当前区块的数量
            Integer heigth = client.invoke("getblockcount", new Object[]{}, Integer.class);
            log.info("blocktask 2   height={}", heigth);
            if(dbCount >= heigth){
                curHeight = dbCount;
            }else{
                curHeight = heigth;
            }
            dbCount = (dbCount <= 0 ? -1 : dbCount);
            for(int i = dbCount + 1; i < heigth; i++) {
                //通过高度获取区块hash
                String blockHash = client.invoke("getblockhash", new Object[]{i}, Object.class).toString();
                //通过hash获得区块信息
                Map blockInfo = client.invoke("getblock", new Object[]{blockHash}, Map.class);
                if(blockInfo != null){
                    try {
                        Blocks blocks = new Blocks();
                        blocks.setHeight(Integer.parseInt(blockInfo.get("height").toString()));

                        Long time = Long.parseLong(blockInfo.get("time").toString());
                        blocks.setTime(new Date(time * 1000));

                        blocks.setDifficulty(new BigDecimal(blockInfo.get("difficulty").toString()));
                        blocks.setNonce(Long.parseLong(blockInfo.get("nonce").toString()));

                        //blockList.add(blocks);

                        boolean isSave = true;
                        if (!difficultyBlockList.isEmpty()) {
                            BlockDifficulty blockDifficulty = difficultyBlockList.get(difficultyBlockList.size() - 1);
                            BigDecimal diff = blocks.getDifficulty();
                            BigDecimal preDiff = blockDifficulty.getDifficulty();
                            if (preDiff.equals(diff)) {
                                isSave = false;
                            }
                        }
                        if (isSave) {
                            BlockDifficulty blockDifficulty = new BlockDifficulty();
                            blockDifficulty.setHeight(blocks.getHeight());
                            blockDifficulty.setTime(blocks.getTime());
                            blockDifficulty.setDifficulty(blocks.getDifficulty());
                            blockDifficulty.setCur_height(blocks.getHeight());
                            difficultyBlockList.add(blockDifficulty);
                        } else {
                            difficultyBlockList.get(difficultyBlockList.size() - 1).setCur_height(blocks.getHeight());
                        }
                    }catch (java.lang.NumberFormatException numberE){
                        numberE.printStackTrace();
                    }
                }
            }
//            if(!blockList.isEmpty()){
//                blocksService.insertBlocks(blockList);
//            }

            if(!difficultyBlockList.isEmpty()){
                blockDifficultyService.insertList(difficultyBlockList);
            }

        } catch (Throwable throwable) {
            log.info("blocktask exception message={}, cause={}", throwable.getMessage(), throwable.getCause());
            throwable.printStackTrace();
        }
    }

    /**
     * 5分钟获取一次新块,如果出现新块，则隔12分钟再次获取
     */
    private void getNewBlockTask(){
        while (true){
            String blockHash;
            try {
                //返回本地最优链上最后一个区块的hash
                blockHash = client.invoke("getbestblockhash", new Object[]{}, Object.class).toString();
                //返回本地最优链中指定哈希区块的头
                Map blockInfo = client.invoke("getblockheader", new Object[]{blockHash}, Map.class);
                if(blockInfo != null) {
                    Integer height = Integer.parseInt(blockInfo.get("height").toString());
                    log.info("getblock cur={}, heigth={}", curHeight, height);
                    if(curHeight.equals(height)){
                        Thread.sleep(1000 * 60 * 2);
                        continue;
                    }
                    curHeight = height;

                    Blocks blocks = new Blocks();
                    blocks.setHeight(Integer.parseInt(blockInfo.get("height").toString()));
                    blocks.setTime(new Date(Long.parseLong(blockInfo.get("time").toString())));
                    blocks.setDifficulty(new BigDecimal(blockInfo.get("difficulty").toString()));
                    blocks.setNonce(Long.parseLong(blockInfo.get("nonce").toString()));
                   // blocksService.insertOne(blocks);

                    BlockDifficulty blockDifficulty = new BlockDifficulty();
                    BeanUtils.copyProperties(blocks, blockDifficulty);
                    blockDifficulty.setCur_height(blocks.getHeight());

                    log.info("getblock svae  heigth={}",blockDifficulty.getCur_height());
                    List<BlockDifficulty> list = new ArrayList<>(1);
                    list.add(blockDifficulty);
                    blockDifficultyService.insertList(list);
                    //blockDifficultyService.insertOne(blockDifficulty);

                    curDifficulty = blocks.getDifficulty();

                    Thread.sleep(1000 * 60 * 3);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                break;
            }

        }
    }
}
