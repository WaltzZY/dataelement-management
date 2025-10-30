package com.inspur.dsp.direct.entity.vo;

import lombok.Data;

/**
 * 下一状态VO
 */
@Data
public class NextStatusVo {

    private String nextStatus;
    
    private String nextStatusName;
    
    private Boolean isValid;
    
    public static NextStatusVo invalid() {
        NextStatusVo vo = new NextStatusVo();
        vo.setIsValid(false);
        return vo;
    }
}