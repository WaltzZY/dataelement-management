package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.common.StatusUtil;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.entity.dto.DetermineResultForOrganiserExportDTO;
import com.inspur.dsp.direct.entity.dto.GetDetermineResultListDTO;
import com.inspur.dsp.direct.entity.vo.GetDetermineResultVo;
import com.inspur.dsp.direct.enums.NePageSortFieldEnums;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.DetermineResultForOrganiserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class DetermineResultForOrganiserServiceImpl implements DetermineResultForOrganiserService {

    @Autowired
    private BaseDataElementMapper baseDataElementMapper;

    @Autowired
    private CommonService commonService;

    @Override
    public Page<GetDetermineResultVo> getDetermineResultList(GetDetermineResultListDTO dto) {
        String sortSql = NePageSortFieldEnums.getOrderByField(dto.getSortField(), dto.getSortOrder());
        dto.setSortSql(sortSql);
        Page<GetDetermineResultVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<GetDetermineResultVo> vos = baseDataElementMapper.getDetermineResultList(page, dto);
        if (CollectionUtils.isEmpty(vos)) {
            return page.setRecords(Collections.emptyList());
        }
        for (GetDetermineResultVo vo : vos) {
            String status = vo.getStatus();
            vo.setStatusChinese(StatusUtil.getStatusChinese(status));
        }
        return page.setRecords(vos);
    }


    @Override
    public void download(GetDetermineResultListDTO dto, HttpServletResponse response) {
        try {
            List<GetDetermineResultVo> baseDataElements = baseDataElementMapper.getDetermineResultList(null, dto);
            List<DetermineResultForOrganiserExportDTO> exportDTOList = new ArrayList<>();
            for (int i = 0; i < baseDataElements.size(); i++) {
                DetermineResultForOrganiserExportDTO exportDTO = new DetermineResultForOrganiserExportDTO();
                GetDetermineResultVo baseDataElement = baseDataElements.get(i);
                exportDTO.setId(i + 1);
                exportDTO.setName(baseDataElement.getName());
                exportDTO.setStatus(StatusUtil.getStatusChinese(baseDataElement.getStatus()));
                exportDTO.setDefinition(baseDataElement.getDefinition());
                exportDTO.setCollectunitqty(baseDataElement.getCollectunitqty());
                exportDTO.setPublicDate(baseDataElement.getPublishDate());
                exportDTO.setSendDate(baseDataElement.getSendDate());
                exportDTO.setSourceUnitName(baseDataElement.getSourceUnitName());
                exportDTOList.add(exportDTO);
            }
            try {
                commonService.exportExcelData(exportDTOList, response, "已定源基准数据元清单", DetermineResultForOrganiserExportDTO.class);
            } catch (IOException e) {
                log.error("导出数据[已定源基准数据元清单]失败", e);
                throw new RuntimeException("导出数据[已定源基准数据元清单]失败");
            }

        } catch (Exception e) {
            throw new RuntimeException("查询已定源基准数据元清单列表失败", e);
        }
    }
}