package com.inspur.dsp.direct.service;

import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.entity.vo.AssociatedDataItemListVO;
import com.inspur.dsp.direct.entity.vo.BaseDataAndOrganizationUnitVO;
import com.inspur.dsp.direct.entity.vo.ProcessMessageVO;

import java.util.List;

public interface DataSourceFileCardService {
    
    BaseDataElement getBaseMessage(String dataid);
    
    BaseDataAndOrganizationUnitVO getDataSourceUnit(String dataid);
    
    List<DomainDataElement> getCollectUnit(String dataid);
    
    List<ProcessMessageVO> getProcessMessage(String dataid);
    
    List<AssociatedDataItemListVO> getShareMessage(String dataid, String sourceUnitCode);
}