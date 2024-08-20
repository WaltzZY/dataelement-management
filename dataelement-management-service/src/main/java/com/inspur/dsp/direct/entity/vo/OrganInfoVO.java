package com.inspur.dsp.direct.entity.vo;

import com.inspur.dsp.direct.enums.DeptTypeEnums;
import com.inspur.dsp.direct.httpService.entity.bsp.BspOrganInfoBO;
import lombok.Data;

@Data
public class OrganInfoVO {
    /**
     * 组织机构编码
     */
    private String code;
    /**
     * 组织机构名称
     */
    private String name;
    /**
     * 所属部门的上级部门编码
     */
    private String parentCode;
    /**
     * 所属部门的上级部门名称
     */
    private String parentName;
    /**
     * 行政区划code
     */
    private String regionCode;
    /**
     * 行政区划name
     */
    private String regionName;
    /**
     * 组织机构简称
     */
    private String shortName;
    /**
     * 组织机构类型，0=部门
     */
    private String type;
    /**
     * 统一社会信用代码
     */
    private String societyCode;

    public static OrganInfoVO toOrganInfoVO(BspOrganInfoBO bspOrganInfoBO) {
        OrganInfoVO organInfoVO = new OrganInfoVO();
        organInfoVO.setCode(bspOrganInfoBO.getCODE());
        organInfoVO.setName(bspOrganInfoBO.getNAME());
        organInfoVO.setParentCode(bspOrganInfoBO.getPARENT_CODE());
        organInfoVO.setRegionCode(bspOrganInfoBO.getREGION_CODE());
        organInfoVO.setRegionName(bspOrganInfoBO.getREGION_NAME());
        organInfoVO.setShortName(bspOrganInfoBO.getSHORT_NAME());
        String typeCode = bspOrganInfoBO.getTYPE();
        organInfoVO.setType(DeptTypeEnums.getName(typeCode));
        organInfoVO.setSocietyCode(bspOrganInfoBO.getCODE());
        return organInfoVO;
    }
}