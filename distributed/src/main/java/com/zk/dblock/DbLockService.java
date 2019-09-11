package com.zk.dblock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("prototype")
public class DbLockService {

    @Autowired
    private DbLock dbLock;

    @Transactional(isolation = Isolation.REPEATABLE_READ,propagation= Propagation.REQUIRED)
    public int addAuthcode(String code) throws Exception {
        // 获取到锁执行更新操作
        if(dbLock.getIdByAuthcode(code) == null){
            int result = dbLock.insertAuthcode(code);
            return result;
        }
        return 0;
    }

}
