package com.inspur.dsp.direct.dbentity.business;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据元与词汇关系表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_vocabulary")
public class DataElementVocabulary implements Serializable {
    /**
     * 唯一标识符
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "唯一标识符最大长度要小于 36")
    @NotBlank(message = "唯一标识符不能为空")
    private String id;

    /**
     * 数据元ID
     */
    @TableField(value = "data_element_id")
    @Size(max = 36,message = "数据元ID最大长度要小于 36")
    private String dataElementId;

    /**
     * 中文名称
     */
    @TableField(value = "data_element_name")
    @Size(max = 200,message = "中文名称最大长度要小于 200")
    private String dataElementName;

    /**
     * 词汇名称
     */
    @TableField(value = "vocabulary")
    @Size(max = 200,message = "词汇名称最大长度要小于 200")
    private String vocabulary;

    /**
     * 创建时间
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Size(max = 30,message = "创建人账号最大长度要小于 30")
    private String createUserId;

    /**
     * 修改时间
     */
    @TableField(value = "modify_date", fill = FieldFill.INSERT)
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id", fill = FieldFill.INSERT)
    @Size(max = 30,message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}