package com.inspur.dsp.direct.entity.vo;

import com.inspur.dsp.direct.dbentity.DomainDataElement;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class GetDuPontInfoVo {
    /**
     * 数据行唯一标识
     */
    private String dataid;

    /**
     * 数据元编码
     */


    private String dataElementId;

    /**
     * 数据元状态
     */


    private String status;

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
     * 数源单位统一社会信用代码
     */


    private String sourceUnitCode;

    /**
     * 数源单位名称
     */


    private String sourceUnitName;

    /**
     * 数据元发布日期
     */

    private Date publishDate;

    /**
     * 发起定源时间
     */

    private Date sendDate;

    /**
     * 确定数源单位时间
     */

    private Date confirmDate;

    /**
     * 采集单位数量
     */

    private Integer collectunitqty;

    /**
     * 备注
     */

    private String remarks;

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
    private List<DomainDataElement> childList;
}