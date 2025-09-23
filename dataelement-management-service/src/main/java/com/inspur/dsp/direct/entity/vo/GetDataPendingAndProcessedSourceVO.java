package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetDataPendingAndProcessedSourceVO {
    /** 数据行唯一标识 */
    private String dataid;

    /** 数据元编码 */
    private String data_element_id;

    /** 数据元状态 */
    private String status;

    /** 数据元名称 */
    private String name;

    /** 数据元定义描述 */
    private String definition;

    /** 数据类型 */
    private String datatype;

    /** 数据格式 */
    private String data_format;

    /** 值域 */
    private String value_domain;

    /** 数源单位统一社会信用代码 */
    private String source_unit_code;

    /** 数源单位名称 */
    private String source_unit_name;

    /** 数据元发布日期 */
    private LocalDateTime publish_date;

    /** 发起定源时间 */
    private LocalDateTime send_date;

    /** 确定数源单位时间 */
    private LocalDateTime confirm_date;

    /** 采集单位数量 */
    private Integer collectunitqty;

    /** 备注 */
    private String remarks;

    /** 创建日期 */
    private LocalDateTime create_date;

    /** 创建人账号 */
    private String create_account;

    /** 最后修改日期 */
    private LocalDateTime last_modify_date;

    /** 最后修改人账号 */
    private String last_modify_account;

    /** 任务ID */
    private String task_id;

    /** 基准数据元唯一标识 */
    private String base_dataelement_dataid;

    /** 任务类型(确认任务/认领任务) */
    private String tasktype;

    /**
     * 任务状态
     */
    private String taskStatus;

    /**
     * 任务状态描述
     */
    private String taskStatusDesc;

    /** 发出单位统一社会信用代码 */
    private String send_unit_code;

    /** 发出单位名称 */
    private String send_unit_name;

    /** 发出人用户账号 */
    private String sender_account;

    /** 发出人姓名 */
    private String sender_name;

    /** 任务处理单位统一社会信用代码 */
    private String processing_unit_code;

    /** 任务处理单位名称 */
    private String processing_unit_name;

    /** 处理日期 */
    private LocalDateTime processing_date;

    /** 处理结果 */
    private String processing_result;

    /** 处理意见 */
    private String processing_opinio;

    /** 处理人账号 */
    private String processor_account;

    /** 处理人姓名 */
    private String processor_name;

//    /** 核定日期 */
//    private LocalDateTime approval_date;
//
//    /** 核定人账号 */
//    private String approval_account;
//
//    /** 核定人姓名 */
//    private String approval_name;

}
