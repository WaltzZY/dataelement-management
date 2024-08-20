package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.entity.vo.OrganInfoVO;

public interface CommonService {

    /**
     * 查询部门详情
     * @param organCode 部门code
     * @return
     */
    OrganInfoVO getOrganInfoByCode(String organCode);
}
