package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * 数据导出实体类
 */
@Data
public class DataExport {
    
    /**
     * ID
     */
    private String dataId;
    
    /**
     * 数据元名称
     */
    private String name;
    
    /**
     * 定义
     */
    private String definition;
    
    /**
     * 数据类型
     */
    private String datatype;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 接收时间
     */
    private String receiveDate;
    
    /**
     * 处理时间
     */
    private String processingDate;
    
    /**
     * 定源时间
     */
    private Date sendDate;

    // Getters and Setters

}