package com.shu.seckill.service;

import java.util.List;

import com.shu.seckill.entity.Seckill;

public interface ISeckillService {
	public List<Seckill> querySeckillList();
	public Seckill querySeckillById(Long seckillId);
}
