package com.shu.seckill.dao;

import com.shu.seckill.entity.SuccessKilled;
import com.shu.seckill.entity.SuccessKilledKey;

public interface SuccessKilledMapper {
    int deleteByPrimaryKey(SuccessKilledKey key);

    int insert(SuccessKilled record);

    int insertSelective(SuccessKilled record);

    SuccessKilled selectByPrimaryKey(SuccessKilledKey key);

    int updateByPrimaryKeySelective(SuccessKilled record);

    int updateByPrimaryKey(SuccessKilled record);
}