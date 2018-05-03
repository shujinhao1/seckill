package com.shu.seckill.dao;

import java.util.List;

import com.shu.seckill.entity.Seckill;
import com.shu.seckill.entity.SeckillBo;

public interface SeckillMapper {
    int deleteByPrimaryKey(Long seckillId);

    int insert(Seckill record);

    int insertSelective(Seckill record);

    Seckill selectByPrimaryKey(Long seckillId);

    int updateByPrimaryKeySelective(Seckill record);

    int updateByPrimaryKey(Seckill record);

	List<Seckill> querySeckillList();

	int reduceNumber(SeckillBo seckillBo);
}