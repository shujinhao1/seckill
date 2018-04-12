package com.shu.seckill.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shu.seckill.dao.SeckillMapper;
import com.shu.seckill.entity.Seckill;
import com.shu.seckill.service.ISeckillService;
@Service
public class SeckillServiceImpl implements ISeckillService {
	@Autowired
	SeckillMapper seckillMapper;
	@Override
	public List<Seckill> querySeckillList() {
		
		return seckillMapper.querySeckillList();
	}
	@Override
	public Seckill querySeckillById(Long seckillId) {
		return seckillMapper.selectByPrimaryKey(seckillId);
	}

}
