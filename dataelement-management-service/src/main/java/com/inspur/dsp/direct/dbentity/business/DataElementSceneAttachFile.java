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
 * 数据元关联场景附件表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_scene_attach_file")
public class DataElementSceneAttachFile implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "id最大长度要小于 36")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 场景ID
     */
    @TableField(value = "scene_id")
    @Size(max = 36,message = "场景ID最大长度要小于 36")
    private String sceneId;

    /**
     * 附件ID
     */
    @TableField(value = "attach_file_id")
    @Size(max = 36,message = "附件ID最大长度要小于 36")
    private String attachFileId;

    /**
     * 附件名称
     */
    @TableField(value = "attach_file_name")
    @Size(max = 255,message = "附件名称最大长度要小于 255")
    private String attachFileName;

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