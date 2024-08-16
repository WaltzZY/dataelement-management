package com.inspur.dsp.direct.dao.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.business.ServiceInterfaceBaseInfo;
import java.util.List;

import com.inspur.dsp.direct.entity.dto.ServiceInterfaceDocumentDTO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentDetailVO;
import com.inspur.dsp.direct.entity.vo.ServiceInterfaceDocumentListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceInterfaceBaseInfoDao extends BaseMapper<ServiceInterfaceBaseInfo> {
    int updateBatch(List<ServiceInterfaceBaseInfo> list);

    int updateBatchSelective(List<ServiceInterfaceBaseInfo> list);

    int batchInsert(@Param("list") List<ServiceInterfaceBaseInfo> list);

    int deleteByPrimaryKeyIn(List<String> list);

    List<ServiceInterfaceDocumentListVO> getList(Page page, ServiceInterfaceDocumentDTO serviceInterfaceDocumentDTO);

    ServiceInterfaceDocumentDetailVO getDetail(String id);
}