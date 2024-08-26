package com.inspur.dsp.direct.httpService.entity.bsp;

import com.inspur.dsp.direct.entity.vo.GovDeptVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BspOrganPageColumnsResp {
    private BspOrganPageInfoResp columns;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BspOrganPageInfoResp {
        /**
         * 区域code
         */
        private String REGION_CODE;
        /**
         * 更新时间
         */
        private String UPDATE_TIME;
        /**
         * 简称
         */
        private String SHORT_NAME;
        /**
         * 名称
         */
        private String NAME;
        /**
         * 编码
         */
        private String CODE;
        /**
         * 组织类型
         */
        private String ORGAN_TYPE;
        /**
         * 状态
         */
        private String STATUS;
        /**
         * 父编码
         */
        private String PARENT_CODE;
        /**
         * 区域名称
         */
        private String REGION_NAME;
        /**
         * 创建人
         */
        private String CREATOR;
        private Integer CHILDS;
        /**
         * 主键
         */
        private String ID;
        /**
         * 创建时间
         */
        private String CREATE_TIME;
        /**
         * 类型 0 = 机关
         */
        private String TYPE;
        /**
         * 类型名称
         */
        private String TYPE_NAME;
    }

    public static GovDeptVO toGovDeptVO(BspOrganPageInfoResp resp) {
        GovDeptVO govDeptVO = new GovDeptVO();
        govDeptVO.setCode(resp.getCODE());
        govDeptVO.setName(resp.getNAME());
        return govDeptVO;
    }
}


