package com.shu.seckill.service;

import java.util.List;

import com.shu.seckill.dto.Exposer;
import com.shu.seckill.dto.SeckillExecution;
import com.shu.seckill.entity.Seckill;

public interface ISeckillService {
	public List<Seckill> querySeckillList();
	public Seckill querySeckillById(Long seckillId);
	public Exposer exportSeckillUrl(Long seckillId);
	public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5);
}
