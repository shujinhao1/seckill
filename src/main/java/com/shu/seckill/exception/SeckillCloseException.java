package com.shu.seckill.exception;

/**
 * 秒杀已经关闭异常，当秒杀结束就会出现这个异常
 * Created by nnngu
 */
public class SeckillCloseException extends SeckillException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -453069911440703879L;

	public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
