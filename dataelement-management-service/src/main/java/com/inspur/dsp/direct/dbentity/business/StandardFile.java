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
 * 标准文件表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "standard_file")
public class StandardFile implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Size(max = 36,message = "id最大长度要小于 36")
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 标准编号
     */
    @TableField(value = "standard_no")
    @Size(max = 100,message = "标准编号最大长度要小于 100")
    private String standardNo;

    /**
     * 标准名称
     */
    @TableField(value = "standard_name")
    @Size(max = 300,message = "标准名称最大长度要小于 300")
    private String standardName;

    /**
     * 标准文件id
     */
    @TableField(value = "standard_file_id")
    @Size(max = 36,message = "标准文件id最大长度要小于 36")
    private String standardFileId;

    /**
     * 标准文件描述
     */
    @TableField(value = "standard_desc")
    @Size(max = 2000,message = "标准文件描述最大长度要小于 2000")
    private String standardDesc;

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