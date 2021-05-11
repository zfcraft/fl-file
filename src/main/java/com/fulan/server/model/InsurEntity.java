package com.fulan.server.model;

import com.fulan.server.common.annotation.CellMapping;
import com.fulan.server.common.annotation.ExcelMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;

import java.io.Serializable;

/**
 * @author chenzhifei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsurEntity implements Serializable {
    private Integer id;
    /**
     * 保单号
     */
    @CellMapping(isNull = true, cellNum = {1}, tableHeader = {"保单号"})
    private String insurNum;
    /**
     * 车架号
     */
    @CellMapping(isNull = true, cellNum = {2}, tableHeader = {"车架号"})
    private String carFrame;
    /**
     * 发动机号
     */
    @CellMapping(isNull = false, cellNum = {3}, tableHeader = {"发动机号"})
    private String carEng;
    /**
     * 保费
     */
    @CellMapping(isNull = true, cellNum = {4}, tableHeader = {"保费"})
    private String value;
    /**
     * 初登日期
     */
    @CellMapping(isNull = true, cellNum = {5}, tableHeader = {"初登日期"},dateFormat = "yyyy-MM-dd")
    private String loginTime;
    /**
     * 起保日期
     */
    @CellMapping(isNull = false, cellNum = {6}, tableHeader = {"起保日期"},dateFormat = "yyyy-MM-dd")
    private String insurTime;
}
