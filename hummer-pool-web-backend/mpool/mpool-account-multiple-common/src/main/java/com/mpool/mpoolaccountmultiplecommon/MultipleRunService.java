package com.mpool.mpoolaccountmultiplecommon;

import com.mpool.common.model.pool.utils.MathShare;
import com.mpool.common.properties.MultipleProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MultipleRunService implements CommandLineRunner {
    @Autowired(required = false)
    private MultipleProperties properties = null;

    @Override
    public void run(String... args) throws Exception {
        if(properties != null){
            MathShare.currencyId = properties.getId();
        }else{
            log.error(">>>file:MultipleRunService, multiple config error!!!!");
        }
    }
}
