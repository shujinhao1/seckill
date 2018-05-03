package com.shu.seckill.exception;

/**
 * 重复秒杀异常，不需要我们手动去try catch
 * Created by nnngu
 */
public class RepeatKillException extends SeckillException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4934483901080527444L;

	public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
