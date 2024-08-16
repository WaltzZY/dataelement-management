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
 * 数据元关联场景表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_scene")
public class DataElementScene implements Serializable {
    /**
     * 关系ID
     */
    @TableId(value = "scene_id", type = IdType.INPUT)
    @Size(max = 36,message = "关系ID最大长度要小于 36")
    @NotBlank(message = "关系ID不能为空")
    private String sceneId;

    /**
     * 数据元id
     */
    @TableField(value = "data_element_id")
    @Size(max = 36,message = "数据元id最大长度要小于 36")
    private String dataElementId;

    /**
     * 场景名称
     */
    @TableField(value = "scene_name")
    @Size(max = 200,message = "场景名称最大长度要小于 200")
    private String sceneName;

    /**
     * 场景描述
     */
    @TableField(value = "scene_desc")
    @Size(max = 2000,message = "场景描述最大长度要小于 2000")
    private String sceneDesc;

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
    @TableField(value = "modify_date", fill = FieldFill.UPDATE)
    private Date modifyDate;

    /**
     * 修改人账号
     */
    @TableField(value = "modify_user_id", fill = FieldFill.UPDATE)
    @Size(max = 30,message = "修改人账号最大长度要小于 30")
    private String modifyUserId;

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
    @Size(max = 200,message = "附件名称最大长度要小于 200")
    private String attachFileName;

    private static final long serialVersionUID = 1L;
}