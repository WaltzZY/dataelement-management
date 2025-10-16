package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.common.utils.StrUtil;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.SourceEventRecordMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.BaseDataElementSearchDTO;
import com.inspur.dsp.direct.entity.dto.DetermineResultForCollectorExportDTO;
import com.inspur.dsp.direct.entity.vo.GetDetermineResultVo;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.DetermineResultForCollectorService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 采集方查看已定源结果服务实现类
 */
@Slf4j
@Service
public class DetermineResultForCollectorServiceImpl implements DetermineResultForCollectorService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private SourceEventRecordMapper sourceEventRecordMapper;

    @Autowired
    private CommonService commonService;

    /**
     * 获取指定已定源结果列表数据
     */
    @Override
    public Page<BaseDataElement> getDetermineResultList(BaseDataElementSearchDTO baseDataElementSearchDTO) {
        // 调用Mapper方法查询基准数据元列表
        Page<BaseDataElement> page = new Page<>(baseDataElementSearchDTO.getPageNum(), baseDataElementSearchDTO.getPageSize());

        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        QueryWrapper<BaseDataElement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("source_unit_code", orgCode);
        queryWrapper.eq("status", "designated_source");

        String sendDateBegin = baseDataElementSearchDTO.getConfirmDateBegin();
        String sendDateEnd = baseDataElementSearchDTO.getConfirmDateEnd();
        ZoneId zoneId = ZoneId.systemDefault();
        // 转换开始时间：毫秒时间戳 → 当天零点
        LocalDate beginDate = null;
        if (sendDateBegin != null) {
            Instant beginInstant = Instant.ofEpochMilli(Long.parseLong(sendDateBegin));
            ZonedDateTime beginZdt = beginInstant.atZone(zoneId);
            beginDate = beginZdt.toLocalDate(); // 提取日期部分（自动丢弃时间）
        }
        // 转换结束时间：毫秒时间戳 → 次日零点
        LocalDate endDate = null;
        if (sendDateEnd != null) {
            Instant endInstant = Instant.ofEpochMilli(Long.parseLong(sendDateEnd));
            ZonedDateTime endZdt = endInstant.atZone(zoneId);
            endDate = endZdt.toLocalDate().plusDays(1); // 加1天得到次日零点
        }
        // 构建查询条件
        if (beginDate != null && endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String beginStr = beginDate.format(formatter) + " 00:00:00";
            String endStr = endDate.format(formatter) + " 00:00:00";
            // 使用MyBatis-Plus的queryWrapper
            queryWrapper.between("send_date", beginStr, endStr);
        }
        String keywords = baseDataElementSearchDTO.getKeyword();
        if (StringUtils.isNotBlank(keywords)) {
            queryWrapper.and(i -> i
                    .like(StringUtils.isNotBlank(keywords), "name", keywords)
                    .or()
                    .like(StringUtils.isNotBlank(keywords), "definition", keywords)
            );
        }
        String sortOrder = baseDataElementSearchDTO.getSortOrder();
        String sortField = baseDataElementSearchDTO.getSortField();
        if (StringUtils.isNotBlank(sortOrder) && StringUtils.isNotBlank(sortField)) {
            if (sortOrder.equalsIgnoreCase("asc")) {
                queryWrapper.orderByAsc(StrUtil.toUnderlineCase(sortField));
            } else {
                queryWrapper.orderByDesc(StrUtil.toUnderlineCase(sortField));
            }
        }
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
        return page.setRecords(records);
    }


    /**
     * 导出指定已定源结果数据
     *
     * @param baseDataElementSearchDTO 查询参数DTO
     * @param response                 HTTP响应对象
     */
    @Override
    public void download(BaseDataElementSearchDTO baseDataElementSearchDTO, HttpServletResponse response) {

        // 调用Mapper方法查询基准数据元列表（包含所有采集单位）
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();
        // 获取登录人账户
        String orgCode = userInfo.getOrgCode();
        
        List<GetDetermineResultVo> baseDataElements = baseDataElementMapper.getDetermineResultListForCollector(orgCode, baseDataElementSearchDTO);
        if (baseDataElements.isEmpty()) {
            log.error("当前导出数据为空!");
            return;
        }
        List<DetermineResultForCollectorExportDTO> exportDTOList = new ArrayList<>();
        for (int i = 0; i < baseDataElements.size(); i++) {
            GetDetermineResultVo baseDataElement = baseDataElements.get(i);
            DetermineResultForCollectorExportDTO determineResultForCollectorExportDTO = new DetermineResultForCollectorExportDTO();
            determineResultForCollectorExportDTO.setId(i + 1);
            determineResultForCollectorExportDTO.setName(baseDataElement.getName());
            determineResultForCollectorExportDTO.setDefinition(baseDataElement.getDefinition());
            // 使用collectUnitName字段，包含所有采集单位，用|分隔
            determineResultForCollectorExportDTO.setSourceUnitName(baseDataElement.getCollectUnitName());
            determineResultForCollectorExportDTO.setDatatype(baseDataElement.getDatatype());
            determineResultForCollectorExportDTO.setSendDate(baseDataElement.getSendDate());
            exportDTOList.add(determineResultForCollectorExportDTO);
        }
        // 调用CommonService.exportExcelData
        try {
            commonService.exportExcelData(exportDTOList, response, "本单位作为数源单位的基准数据元清单", DetermineResultForCollectorExportDTO.class);
        } catch (IOException e) {
            log.error("导出数据[本单位作为数源单位的基准数据元清单]失败", e);
            throw new RuntimeException("导出数据[本单位作为数源单位的基准数据元清单]失败");
        }

    }

}