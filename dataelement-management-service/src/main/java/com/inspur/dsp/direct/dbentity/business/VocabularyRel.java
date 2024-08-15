package com.inspur.dsp.direct.dbentity.business;

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
 * 词汇关系表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "vocabulary_rel")
public class VocabularyRel implements Serializable {
    /**
     * 关系ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "关系ID最大长度要小于 36")
    @NotBlank(message = "关系ID不能为空")
    private String id;

    /**
     * 主位词ID
     */
    @TableField(value = "subject_vocabulary_id")
    @Size(max = 200,message = "主位词ID最大长度要小于 200")
    private String subjectVocabularyId;

    /**
     * 主位词
     */
    @TableField(value = "subject_vocabulary")
    @Size(max = 200,message = "主位词最大长度要小于 200")
    private String subjectVocabulary;

    /**
     * 宾位词ID
     */
    @TableField(value = "object_vocabulary_id")
    @Size(max = 200,message = "宾位词ID最大长度要小于 200")
    private String objectVocabularyId;

    /**
     * 宾位词
     */
    @TableField(value = "object_vocabulary")
    @Size(max = 200,message = "宾位词最大长度要小于 200")
    private String objectVocabulary;

    /**
     * 关系
     */
    @TableField(value = "relationship")
    @Size(max = 200,message = "关系最大长度要小于 200")
    private String relationship;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 创建人账号
     */
    @TableField(value = "createuse_rid")
    @Size(max = 30,message = "创建人账号最大长度要小于 30")
    private String createuseRid;

    /**
     * 修改时间
     */
    @TableField(value = "modify_date")
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id")
    @Size(max = 30,message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

    private static final long serialVersionUID = 1L;
}