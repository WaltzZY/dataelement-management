package com.inspur.dsp.direct.entity.bo.bsp;

import lombok.Data;

@Data
public class DictInfoVO {
    /**
     * 编码
     */
    private String code;
    private String createTime;
    private String id;
    /**
     * 字典类型编码
     */
    private String kind;
    /**
     * 字典名称
     */
    private String name;
    private String parentCode;
    private String remark;
    private Integer sortOrder;
    private String type;

    public static DictInfoVO toDictInfoVO(DictInfoBO dictInfoBO) {
        DictInfoVO dictInfoVO = new DictInfoVO();
        dictInfoVO.setCode(dictInfoBO.getCODE());
        dictInfoVO.setCreateTime(dictInfoBO.getCREATE_TIME());
        dictInfoVO.setId(dictInfoBO.getID());
        dictInfoVO.setKind(dictInfoBO.getKIND());
        dictInfoVO.setName(dictInfoBO.getNAME());
        dictInfoVO.setParentCode(dictInfoBO.getPARENT_CODE());
        dictInfoVO.setRemark(dictInfoBO.getREMARK());
        dictInfoVO.setSortOrder(dictInfoBO.getSORT_ORDER());
        dictInfoVO.setType(dictInfoBO.getTYPE());
        return dictInfoVO;
    }
}