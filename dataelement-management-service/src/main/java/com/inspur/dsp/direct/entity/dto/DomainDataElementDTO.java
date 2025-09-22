package com.inspur.dsp.direct.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 领域数据元表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomainDataElementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dataid;

    /**
     * 数据元编码
     */
    private String dataElementId;

    /**
     * 数据元名称
     */
    private String name;

    /**
     * 数据元定义描述
     */
    private String definition;

    /**
     * 数据类型
     */
    private String datatype;

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * 值域
     */
    private String valueDomain;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 提供单位统一社会信用代码
     */
    private String sourceUnitCode;

    /**
     * 提供单位名称
     */
    private String sourceUnitName;

    /**
     * 基准数据元数据行标识
     */
    private String baseDataelementDataid;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 创建人账号
     */
    private String createAccount;

    /**
     * 最后修改日期
     */
    private Date lastModifyDate;

    /**
     * 最后修改人账号
     */
    private String lastModifyAccount;
}