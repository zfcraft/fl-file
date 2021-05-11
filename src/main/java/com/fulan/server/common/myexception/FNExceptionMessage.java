package com.fulan.server.common.myexception;

import java.text.MessageFormat;

/**
 * 
 * @Description 异常消息
 * @author
 * @date
 * @version V1.0 
 * =================Modify Record=================
 * @Modifier			@date			@Content
 *
 */
public enum FNExceptionMessage {
	
	FIELD_CHECK_STRING_ISNULL {
		@Override
		public String getErrorMessageFromProperites() {
			return "[不能为空 ！]";
		}
	}, 
	FIELD_CHECK_INT_ERROR {
		@Override
		public String getErrorMessageFromProperites() {
			return "[必须为整数！]";
		}
	}, 
	FIELD_CHECK_DOUBLE_ERROR {
		@Override
		public String getErrorMessageFromProperites() {
			return "[必须为小数！]";
		}
	}, 
	FIELD_CHECK_DATE_ERROR {
		@Override
		public String getErrorMessageFromProperites() {
			return "[时间格式错误！格式:'yyyy-MM-dd']";
		}
	}, 
	FIELD_CHECK_DATE_PARSE_ERROR {
		@Override
		public String getErrorMessageFromProperites() {
			return "时间格式转换错误！";
		}
	}, 
	TOEXCEL_SETVALUE_ERROR {
		@Override
		public String getErrorMessageFromProperites() {
			return "第{0}行,第{1}列，格式错误！";
		}
	}, 
	CELL_CHECK_ISNULL {
		@Override
		public String getErrorMessageFromProperites() {
			return "[Cell 不能为空！]";
		}
	};

	public abstract String getErrorMessageFromProperites();

	public String getErrorMessageFromProperites(Object[] arguments) {
		if ((arguments == null) || (arguments.length < 1)) {
			return getErrorMessageFromProperites();
		}
		return MessageFormat.format(getErrorMessageFromProperites(), arguments);
	}
}

