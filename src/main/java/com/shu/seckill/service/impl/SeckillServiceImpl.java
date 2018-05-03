package com.shu.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.shu.seckill.dao.SeckillMapper;
import com.shu.seckill.dao.SuccessKilledMapper;
import com.shu.seckill.dto.Exposer;
import com.shu.seckill.dto.SeckillExecution;
import com.shu.seckill.entity.Seckill;
import com.shu.seckill.entity.SeckillBo;
import com.shu.seckill.entity.SuccessKilled;
import com.shu.seckill.enums.SeckillStatEnum;
import com.shu.seckill.exception.RepeatKillException;
import com.shu.seckill.exception.SeckillCloseException;
import com.shu.seckill.exception.SeckillException;
import com.shu.seckill.redis.RedisDao;
import com.shu.seckill.service.ISeckillService;

@Service
public class SeckillServiceImpl implements ISeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SeckillMapper seckillMapper;
	@Autowired
	private SuccessKilledMapper successKilledMapper;
	@Autowired
	RedisDao redisDao;
	/* 加入一个盐值,用于混淆 */
	private final String salt = "thisIsASaltValue";

	@Override
	public List<Seckill> querySeckillList() {

		return seckillMapper.querySeckillList();
	}

	@Override
	public Seckill querySeckillById(Long seckillId) {
		return seckillMapper.selectByPrimaryKey(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(Long seckillId) {
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			// 访问数据库读取数据
			seckill = seckillMapper.selectByPrimaryKey(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				// 放入redis
				redisDao.putSeckill(seckill);
			}
		}

		// 判断是否还没到秒杀时间或者是过了秒杀时间
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();
		// 开始时间大于现在的时候说明没有开始秒杀活动；秒杀活动结束时间小于现在的时间说明秒杀已经结束了
		if (nowTime.getTime() > startTime.getTime() && nowTime.getTime() < endTime.getTime()) {
			// 秒杀开启,返回秒杀商品的id,用给接口加密的md5
			String md5 = getMd5(seckillId);
			return new Exposer(true, md5, seckillId);
		}
		return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());

	}

	private String getMd5(long seckillId) {
		String base = seckillId + "/" + salt;
		return DigestUtils.md5DigestAsHex(base.getBytes());
	}

	@Transactional
	@Override
	public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) {

		if (md5 == null || !md5.equals(getMd5(seckillId))) {
			logger.error("秒杀数据被篡改");
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀业务逻辑
		Date nowTime = new Date();

		try {
			SuccessKilled jj = new SuccessKilled();
			jj.setSeckillId(seckillId);
			jj.setUserPhone(userPhone);
			jj.setState((byte) 0);
			// 记录购买行为
			int insertCount = successKilledMapper.insert(jj);
			if (insertCount <= 0) {
				// 重复秒杀
				throw new RepeatKillException("seckill repeated");
			} else {
				SeckillBo seckillBo=new SeckillBo();
				seckillBo.setSeckillId(seckillId);
				seckillBo.setKillTime(nowTime);
				// 减库存 ,热点商品的竞争
				int reduceNumber = seckillMapper.reduceNumber(seckillBo);
				if (reduceNumber <= 0) {
					logger.warn("没有更新数据库记录,说明秒杀结束");
					throw new SeckillCloseException("seckill is closed");
				} else {
					SuccessKilled record=new SuccessKilled();
					record.setSeckillId(seckillId);
					record.setUserPhone(userPhone);
					// 秒杀成功了,返回那条插入成功秒杀的信息 进行commit
					SuccessKilled successKilled = successKilledMapper.queryByIdWithSeckill(record);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException | RepeatKillException  e1) {
			throw e1;
		} catch (Exception  e1) {
			throw new RepeatKillException("seckill repeated");
		}
	}
}
