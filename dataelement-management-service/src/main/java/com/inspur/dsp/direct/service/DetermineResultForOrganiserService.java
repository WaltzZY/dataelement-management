package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;

import java.util.List;

/**
 * 组织方查看已定源结果服务接口
 *
 * @author system
 * @date 2025-01-25
 */
public interface DetermineResultForOrganiserService {

    /**
     * 分页查询已定源结果列表数据
     *
     * @param baseDataElementSearchDTO 查询参数
     * @return 基准数据元列表
     */
    Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO);

    /**
     * 导出已定源结果数据
     *
     * @param baseDataElementSearchDTO 查询参数
     */
    void download(BaseDataElementSearchDTO baseDataElementSearchDTO);
}