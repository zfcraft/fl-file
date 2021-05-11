package com.fulan.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenzhifei
 * 页面返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelEntity implements Serializable {
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 导入人
     */
    private String import_peo;
    /**
     * 导入时间
     */
    private String import_time;
    /**
     * 解析时间
     */
    private String load_time;
    /**
     * 状态
     */
    private String status;
    /**
     * 处理结果
     */
    private String result;
    /**
     * 操作
     */
    private String operation;
}
