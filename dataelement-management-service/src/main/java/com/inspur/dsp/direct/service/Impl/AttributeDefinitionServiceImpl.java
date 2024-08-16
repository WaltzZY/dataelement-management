package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.AttributeDefinitionDao;
import com.inspur.dsp.direct.dbentity.business.AttributeDefinition;
import com.inspur.dsp.direct.service.AttributeDefinitionService;
import org.springframework.stereotype.Service;
/**
 * 属性定义表
 */
@Service
public class AttributeDefinitionServiceImpl extends ServiceImpl<AttributeDefinitionDao, AttributeDefinition> implements AttributeDefinitionService {

}
