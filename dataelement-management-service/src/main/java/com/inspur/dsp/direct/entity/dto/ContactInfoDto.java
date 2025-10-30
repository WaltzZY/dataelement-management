package com.inspur.dsp.direct.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 联系人信息DTO
 */
@Data
public class ContactInfoDto {

    private String dataid;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系人电话不能为空")
    private String contactTel;
}