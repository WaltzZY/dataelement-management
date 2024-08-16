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
 * 词汇表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "vocabulary")
public class Vocabulary implements Serializable {
    /**
     * 词汇ID
     */
    @TableId(value = "vocabulary_id", type = IdType.INPUT)
    @Size(max = 36, message = "词汇ID最大长度要小于 36")
    @NotBlank(message = "词汇ID不能为空")
    private String vocabularyId;

    /**
     * 词汇名称
     */
    @TableField(value = "vocabulary")
    @Size(max = 200, message = "词汇名称最大长度要小于 200")
    private String vocabulary;

    /**
     * 词汇定义
     */
    @TableField(value = "`definition`")
    @Size(max = 2000, message = "词汇定义最大长度要小于 2000")
    private String definition;

    /**
     * 创建时间
     */
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    @Size(max = 30, message = "创建人账号最大长度要小于 30")
    private String createUserId;

    /**
     * 修改时间
     */
    @TableField(value = "modify_date", fill = FieldFill.UPDATE)
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id", fill = FieldFill.UPDATE)
    @Size(max = 30, message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}