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
 * 数据元附件类型表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "data_element_attachment")
public class DataElementAttachment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 行唯一标识
     */
    @TableId(value = "attachfileid", type = IdType.INPUT)
    @Size(max = 36,message = "行唯一标识最大长度要小于 36")
    @NotBlank(message = "行唯一标识不能为空")
    private String attachfileid;

    /**
     * 基准数据元ID
     */
    @TableField(value = "base_dataelement_dataid")
    @Size(max = 36,message = "基准数据元ID最大长度要小于 36")
    private String baseDataelementDataid;

    /**
     * 附件类型: standardfile(标准文件), examplefile(样例文件)
     */
    @TableField(value = "attachfiletype")
    @Size(max = 20,message = "附件类型: standardfile(标准文件), examplefile(样例文件)最大长度要小于 20")
    private String attachfiletype;

    /**
     * 附件文件名称
     */
    @TableField(value = "attachfilename")
    @Size(max = 300,message = "附件文件名称最大长度要小于 300")
    private String attachfilename;

    /**
     * 附件文件地址
     */
    @TableField(value = "attachfileurl")
    @Size(max = 300,message = "附件文件地址最大长度要小于 300")
    private String attachfileurl;

    /**
     * 附件文件存储位置
     */
    @TableField(value = "attachfilelocation")
    @Size(max = 200,message = "附件文件存储位置最大长度要小于 200")
    private String attachfilelocation;
}