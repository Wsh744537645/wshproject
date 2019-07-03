package com.mpool.account.block;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BlockRunService implements CommandLineRunner {
    @Autowired
    private BlockTask blockTask = null;

    @Override
    public void run(String... args) throws Exception {
        new Thread(()->{
            blockTask.initTask();
        }).start();
    }
}
