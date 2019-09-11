package com.zk.dblock;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DbLock {

    Integer getIdByAuthcode(String authcode);

    int insertAuthcode(String authcode);
}
