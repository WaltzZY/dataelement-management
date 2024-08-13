package com.inspur.dsp.direct.enums;

import lombok.Getter;

@Getter
public enum FyLogTemplateEnum {
    /**
     * 操作场景: 从企业信息填报事项数据项表处理数据到部门数据项表的异步处理程序执行过程中
     * 涉及表: eifm_orgdataitem 部门数据项表, eifm_org_dataitem_rel
     * 记录日志条件: 在eifm_orgdataitem表中执行insert操作时
     * 必传属性: 部门名称 organName 事项名称 matterName 样表名称 attachmentName 数据项名称 dataelementName
     */
    SITUATION_01("01", "通过数据项名称查询到企业数据一张表中存在同名数据元"),
    SITUATION_02("02", "通过数据项名称查询到数据元基准库中存在同名数据元"),
    SITUATION_03("03", "同一部门同一人已经有同名称的数据项存在eifm_orgdataitem，只更新关系表eifm_org_dataitem_rel，不更新eifm_orgdataitem"),
    SITUATION_14("14", "根据事项数据项创建部门数据项"),
    /**
     * 操作场景: 在维护数据项页面执行保存操作
     * 涉及表: eifm_orgdataitem 部门数据项表
     * 记录日志条件: 在eifm_orgdataitem表中执行update操作时
     * 必传属性: 数据项名称 dataelementName 用户姓名 userName
     */
    SITUATION_04("04", "用户维护数据项信息并保存时，且和企业数据一张表不匹配"),
    SITUATION_05("05", "用户维护数据项信息并保存时，且和企业数据一张表数据元存在匹配"),
    /**
     * 操作场景: 在维护数据项页面执行提交审核操作
     * 涉及表: eifm_orgdataitem 部门数据项表
     * 记录日志条件: 在eifm_orgdataitem表中执行update操作时
     * 必传属性: 数据项名称 dataelementName 用户姓名 userName
     */
    SITUATION_06("06", "用户针对维护好的数据项信息提交审核时，且和企业数据一张表不匹配"),
    SITUATION_07("07", "用户针对维护好的数据项信息提交审核时，且和企业数据一张表数据元存在匹配"),
    /**
     * 操作场景: 在审核页面执行审核操作
     * 涉及表: EDReport_AllDataElement，企业数据一张表
     * 记录日志条件: 在eifm_orgdataitem表中执行update操作时，在在EDReport_AllDataElement中执行
     * 必传属性: 数据项名称 dataelementName 用户姓名 userName
     * 可选属性: 审核不通过时,需要传入 反馈信息 feedbackMsg
     */
    SITUATION_08("08", "审核通过&&有匹配&&数据源基准表"),
    SITUATION_16("16", "审核通过&&有匹配&&企业一张表"),
    SITUATION_09("09", "审核通过&&无匹配"),
    SITUATION_19("19", "审核通过&&无匹配，系统自动将新插入的企业数据一张表数据元进行匹配关联"),
    SITUATION_10("10", "审核不通过"),
    /**
     * 操作场景: 在企业数据一张表中新增数据元
     * 涉及表: EDReport_AllDataElement，企业数据一张表
     * 记录日志条件: 新增、修改、删除
     * 必传属性: 数据项名称 dataelementName 用户姓名 userName
     * 可选属性: 删除时需要传入 企业数据一张表删除id deleteId
     */
    SITUATION_11("11", "新增"),
    SITUATION_12("12", "修改"),
    SITUATION_13("13", "删除"),
    SITUATION_15("15", "删除企业事项一张表数据项，断开部门数据项表连接"),
    SITUATION_17("17", "修改企业事项一张表数据项，断开部门数据项表连接"),
    SITUATION_18("18", "新增企业事项一张表数据项，创建部门数据项表连接"),
    ;


    /**
     * 编码
     */
    private final String code;
    /**
     * 说明
     */
    private final String desc;


    FyLogTemplateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
