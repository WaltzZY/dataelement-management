package com.inspur.dsp.direct.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.DataExport;
import com.inspur.dsp.direct.service.DetermineResultForOrganiserService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 组织方查看已定源结果服务实现类
 *
 * @author system
 * @date 2025-01-25
 */
@Service
public class DetermineResultForOrganiserServiceImpl implements DetermineResultForOrganiserService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private HttpServletResponse response;

    @Override
    public Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        try {
            UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
            //获取登录人账户
            // String orgCode = userInfo.getOrgCode();
            QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
            String sendDateBegin = baseDataElementSearchDTO.getSendDateBegin();
            String sendDateEnd = baseDataElementSearchDTO.getSendDateEnd();
            if (StringUtils.isNotBlank(sendDateBegin) && StringUtils.isNotBlank(sendDateEnd)) {
                queryWrapper.between("send_date", sendDateBegin, sendDateEnd);
            }

            List<String> sourceUnitCodeList = baseDataElementSearchDTO.getSourceUnitCodeList();
            if (sourceUnitCodeList != null && !sourceUnitCodeList.isEmpty()) {
                queryWrapper.in("source_unit_code", sourceUnitCodeList);
            }

            String keyword = baseDataElementSearchDTO.getKeyword();
            String value = baseDataElementSearchDTO.getValue();
            if (StringUtils.isNotBlank(keyword)) {
                queryWrapper.like(keyword, value);
            }
            // queryWrapper.eq("source_unit_code", orgCode);
            queryWrapper.eq("status", "designated_source");
            Page<BaseDataElement> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());
            Page<BaseDataElement> baseDataElementPage = baseDataElementMapper.selectPage(page, queryWrapper);
            List<BaseDataElement> records = baseDataElementPage.getRecords();
            if (CollectionUtils.isEmpty(records)) {
                page.setRecords(Collections.emptyList());
            }
            for (BaseDataElement baseDataElement : records) {
                String status = baseDataElement.getStatus();
                String statusChinese = StatusUtil.getStatusChinese(status);
                baseDataElement.setStatusChinese(statusChinese);
            }
            return page;
        } catch (Exception e) {
            throw new RuntimeException("查询已定源结果列表失败", e);
        }
    }


    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        try {
            List<BaseDataElement> resultList = baseDataElementMapper.getDetermineResultList(baseDataElementSearchDTO);

            if (CollectionUtils.isEmpty(resultList)) {
                throw new IllegalArgumentException("数据元不存在");
            }
            // 转换为导出对象
            List<DataExport> exportList = new ArrayList<>();
            for (BaseDataElement element : resultList) {
                DataExport dataExport = new DataExport();
                dataExport.setDataId(element.getDataid());
                dataExport.setName(element.getName());
                dataExport.setDefinition(element.getDefinition());
                dataExport.setDatatype(element.getDatatype());
//                dataExport.setSourceUnitName(element.getSourceUnitName());
                dataExport.setSendDate(element.getSendDate() != null ? element.getSendDate() : null);
                // 设置采集单位数量，这里需要根据实际业务逻辑设置
//                dataExport.setCollectunitqty("0");
                exportList.add(dataExport);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = "已定源结果数据.xlsx";
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

            // 使用EasyExcel导出
            EasyExcel.write(response.getOutputStream(), DataExport.class)
                    .sheet("已定源结果")
                    .doWrite(exportList);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("导出文件失败", e);
        } catch (Exception e) {
            throw new RuntimeException("导出数据失败", e);
        }
    }
}