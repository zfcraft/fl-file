package com.fulan.server.common.myexception;

import com.fulan.server.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Exception extends RuntimeException {
    private ExceptionEnums exceptionEnums;
}