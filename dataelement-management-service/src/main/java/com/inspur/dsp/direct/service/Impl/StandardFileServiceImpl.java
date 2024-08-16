package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspur.dsp.direct.dao.business.StandardFileDao;
import com.inspur.dsp.direct.dbentity.business.StandardFile;
import com.inspur.dsp.direct.service.StandardFileService;
import org.springframework.stereotype.Service;
/**
 * 标准文件表
 */
@Service
public class StandardFileServiceImpl extends ServiceImpl<StandardFileDao, StandardFile> implements StandardFileService {
}

