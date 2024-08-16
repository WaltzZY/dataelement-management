package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspur.dsp.direct.dbentity.business.DataElementScene;
import com.inspur.dsp.direct.entity.vo.DataElementSceneVO;

import java.util.List;

/**
 * 数据元关联场景表
 */
public interface DataElementSceneService extends IService<DataElementScene> {

    List<DataElementSceneVO> getList(String dataElementId);
}
