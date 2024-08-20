package com.inspur.dsp.direct.service.Impl;

import com.inspur.dsp.direct.entity.vo.OrganInfoVO;
import com.inspur.dsp.direct.exception.CustomException;
import com.inspur.dsp.direct.httpService.BspService;
import com.inspur.dsp.direct.httpService.entity.bsp.BspOrganInfoBO;
import com.inspur.dsp.direct.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    private final BspService bspService;

    /**
     * 顶层部门code
     */
    @Value("${spring.bsp.root-organ-code}")
    private String rootOrganCode;

    /**
     * 顶层部门name
     */
    @Value("${spring.bsp.root-organ-name}")
    private String rootOrganName;

    /**
     * 查询部门详情
     *
     * @param organCode 部门code
     * @return
     */
    @Override
    public OrganInfoVO getOrganInfoByCode(String organCode) {
        BspOrganInfoBO organInfoByCode = bspService.getOrganInfoByCode(organCode);
        OrganInfoVO vo = Optional.ofNullable(organInfoByCode)
                .map(OrganInfoVO::toOrganInfoVO)
                .orElseThrow(() -> new CustomException("部门不存在"));
        String parentCode = vo.getParentCode();
        if (StringUtils.hasText(parentCode)) {
            if (rootOrganCode.equals(parentCode)) {
                vo.setParentName(rootOrganName);
            } else {
                BspOrganInfoBO parentOrgan = bspService.getOrganInfoByCode(parentCode);
                Optional.ofNullable(parentOrgan)
                        .ifPresent(parentOrganInfo -> vo.setParentName(parentOrganInfo.getNAME()));
            }
        }
        return vo;
    }
}
