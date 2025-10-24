package com.inspur.dsp.direct.dbentity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据元标准联系人表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_contact")
public class DataElementContact implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 行唯一标识
     */
    @TableId(value = "dataid", type = IdType.INPUT)
    @Size(max = 36,message = "行唯一标识最大长度要小于 36")
    @NotBlank(message = "行唯一标识不能为空")
    private String dataid;

    /**
     * 基准数据元ID
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36,message = "基准数据元ID最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 联系人姓名
     */
    @TableField(value = "contactname")
    @Size(max = 30,message = "联系人姓名最大长度要小于 30")
    private String contactname;

    /**
     * 联系人电话
     */
    @TableField(value = "contacttel")
    @Size(max = 30,message = "联系人电话最大长度要小于 30")
    private String contacttel;
}