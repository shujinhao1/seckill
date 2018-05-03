package com.shu.seckill.exception;

/**
 *  秒杀基础的异常
 * Created by nnngu
 */
public class SeckillException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8936107505298442385L;

	public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
