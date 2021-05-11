package com.fulan.server.model;

import lombok.Data;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;

@Data
public class ParamentEntity {
	
	private PropertyDescriptor fieldDescriptor;
	private String tableHeader;
	private boolean isNull;
	private Type type;
	private String dateFormat;
	
}

