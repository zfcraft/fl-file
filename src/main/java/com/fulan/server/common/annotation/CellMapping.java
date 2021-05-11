package com.fulan.server.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CellMapping {
	/**
	 * 列的序号
	 * @return
	 */
	public abstract int[] cellNum();
	/**
	 * 导出的列名
	 * @return
	 */
	public abstract String[] tableHeader();

	/**
	 * 是否允许为空
	 * @return
	 */
	public boolean isNull() default true;
	/**
	 * 时间格式
	 * @return
	 */
	public String dateFormat() default "yyyy-MM-dd";
}