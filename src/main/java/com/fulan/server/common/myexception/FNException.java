package com.fulan.server.common.myexception;

/**
 * 
 * @Description 异常处理
 * @author
 * @date
 * @version V1.0 
 * =================Modify Record=================
 * @Modifier			@date			@Content
 *
 */
public class FNException  extends java.lang.Exception {

	private static final long serialVersionUID = -8447604713459226621L;

	public FNException() {
	}

	public FNException(String errorMsg) {
		super(errorMsg);
	}

	public FNException(Throwable throwable) {
		super(throwable);
	}

	public FNException(String s, Throwable throwable) {
		super(s, throwable);
	}
}

