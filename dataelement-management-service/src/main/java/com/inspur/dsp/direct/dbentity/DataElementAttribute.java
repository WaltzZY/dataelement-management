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
 * 数据元属性表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_attribute")
public class DataElementAttribute implements Serializable {
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
     * 属性名称
     */
    @TableField(value = "attributename")
    @Size(max = 50,message = "属性名称最大长度要小于 50")
    private String attributename;

    /**
     * 属性值
     */
    @TableField(value = "attributevalue")
    @Size(max = 200,message = "属性值最大长度要小于 200")
    private String attributevalue;
}