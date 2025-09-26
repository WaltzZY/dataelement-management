package com.inspur.dsp.direct.service.Impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dao.BaseDataElementMapper;
import com.inspur.dsp.direct.dao.ConfirmationTaskMapper;
import com.inspur.dsp.direct.dao.OrganisersClaim.ClaimBaseDataElementMapper;
import com.inspur.dsp.direct.dao.OrganisersClaim.ClaimDataElementMapper;
import com.inspur.dsp.direct.dao.OrganisersClaim.ClaimDomainDataElementMapper;
import com.inspur.dsp.direct.dbentity.BaseDataElement;
import com.inspur.dsp.direct.dbentity.ConfirmationTask;
import com.inspur.dsp.direct.dbentity.DomainDataElement;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import com.inspur.dsp.direct.entity.dto.GetDataElementDTO;
import com.inspur.dsp.direct.entity.excel.OrganisersClaimPendingExcel;
import com.inspur.dsp.direct.entity.excel.OrganisersClaimingExcel;
import com.inspur.dsp.direct.entity.vo.ClaimDataElementVO;
import com.inspur.dsp.direct.enums.ConfirmationTaskEnums;
import com.inspur.dsp.direct.enums.PapsSortFieldEnums;
import com.inspur.dsp.direct.enums.StatusEnums;
import com.inspur.dsp.direct.service.CommonService;
import com.inspur.dsp.direct.service.OrganisersClaimService;
import com.inspur.dsp.direct.util.BspLoginUserInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inspur.dsp.direct.enums.StatusEnums.CLAIMED;
import static com.inspur.dsp.direct.enums.TaskTypeEnums.CLAIM_TASK;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganisersClaimServiceImpl implements OrganisersClaimService {

    private final ClaimDataElementMapper claimDataElementMapper;

    private final ClaimDomainDataElementMapper claimDomainDataElementMapper;

    private final ConfirmationTaskMapper claimConfirmationTaskMapper;

    private final ClaimBaseDataElementMapper claimBaseDataElementMapper;

    private final BaseDataElementMapper baseDataElementMapper;

    private final CommonService commonService;

    @Override
    public Page<ClaimDataElementVO> getDataElement(GetDataElementDTO dto) {

        Page<ClaimDataElementVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());

        // 查询senddateBegin零点到senddateEnd第二天零点的数据​
        if (dto.getSendDateBegin() != null){
            dto.setSendDateBegin(dto.getSendDateBegin().toLocalDate().atStartOfDay());
        }
        if (dto.getSendDateEnd() != null){
            dto.setSendDateEnd(dto.getSendDateEnd().toLocalDate().plusDays(1).atStartOfDay());
        }

        // 使用ClaimDataElementMapper.selectBaseDataElementByStatus获取数据列表
        List<ClaimDataElementVO> list = claimDataElementMapper.selectBaseDataElementByStatus(page, dto,orderBySql);
        page.setRecords(list);
        // 循环遍历List<ClaimDataElementVO>
        for (ClaimDataElementVO vo : page.getRecords()) {
            // 使用ClaimDataElementVO.dataid作为参数，获取关联的DomainDataElement列表
            List<DomainDataElement> domainDataElements = claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(vo.getDataid());

            // 提取所有source_unit_name并用"|"分割组合
            String collectUnitNames = domainDataElements.stream().map(DomainDataElement::getSourceUnitName).collect(Collectors.joining("|"));

            // 将组合成的字符串赋值给ClaimDataElementVO.collectunitnames列
            vo.setCollectunitnames(collectUnitNames);

            // 确认任务状态描述
            vo.setStatusDesc(StatusEnums.getDescByCode(vo.getStatus()));
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startBatchClaim(List<String> ids) {
        // 获取当前登录用户
        UserLoginInfo userInfo = BspLoginUserInfoUtils.getUserInfo();

        // 循环ids，获取dataid
        for (String dataid : ids) {
            // 使用dataid查询所有相关的DomainDataElement
            List<DomainDataElement> domainDataElements = claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(dataid);

//                Date now = Date.from(Instant.now());
            Date now = new Date();
            // 循环DomainDataElement，创建认领任务
            for (DomainDataElement dde : domainDataElements) {
                // 创建confirmationtask对象
                ConfirmationTask confirmationTask = new ConfirmationTask();
                // 完善confirmationTask各字段并插入（类型取枚举 状态取枚举 TaskType：认领任务  剩下都不用加）
                confirmationTask.setTaskId(UUID.randomUUID().toString());
                confirmationTask.setBaseDataelementDataid(dataid);
                confirmationTask.setTasktype(CLAIM_TASK.getCode());
                confirmationTask.setSendUnitCode(userInfo.getOrgCode());
                confirmationTask.setSendUnitName(userInfo.getOrgName());
                confirmationTask.setSendDate(now);
                confirmationTask.setSenderAccount(userInfo.getAccount());
                confirmationTask.setSenderName(userInfo.getName());
                confirmationTask.setStatus(ConfirmationTaskEnums.PENDING_CLAIMED.getCode());
                confirmationTask.setProcessingUnitCode(dde.getSourceUnitCode());
                confirmationTask.setProcessingUnitName(dde.getSourceUnitName());
                // 插入confirmation_task表
                claimConfirmationTaskMapper.insert(confirmationTask);
            }

            BaseDataElement baseDataElement = baseDataElementMapper.selectById(dataid);
            baseDataElement.setStatus(CLAIMED.getCode());
            baseDataElement.setSendDate(now);
            baseDataElementMapper.updateById(baseDataElement);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exportData(GetDataElementDTO dto, HttpServletResponse response) {

        try {

            String orderBySql = PapsSortFieldEnums.getOrderBySql(dto.getSortField(), dto.getSortOrder());


            if (dto.getStatus() != null && dto.getStatus().contains("pending_source")) {


                // 查询待定源数据元列表
                List<ClaimDataElementVO> list_pendingSource = claimDataElementMapper.selectBaseDataElementByStatus(null, dto,orderBySql);

                // 循环遍历List<ClaimDataElementVO>
                for (ClaimDataElementVO vo : list_pendingSource) {
                    // 使用ClaimDataElementVO.dataid作为参数，获取关联的DomainDataElement列表
                    List<DomainDataElement> domainDataElements = claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(vo.getDataid());

                    // 提取所有source_unit_name并用"|"分割组合
                    String collectUnitNames = domainDataElements.stream().map(DomainDataElement::getSourceUnitName).collect(Collectors.joining("|"));

                    // 将组合成的字符串赋值给ClaimDataElementVO.collectunitnames列
                    vo.setCollectunitnames(collectUnitNames);
                }

                List<OrganisersClaimPendingExcel> pendingExcelList = list_pendingSource.stream().map(vo -> {
                    return OrganisersClaimPendingExcel.builder().name(vo.getName()).definition(vo.getDefinition()).collectQty(String.valueOf(vo.getCollectunitqty())).collectUnit(vo.getCollectunitnames()).status(StatusEnums.getDescByCode(vo.getStatus())).build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(pendingExcelList, response, "组织方发起认领-待定源列表", OrganisersClaimPendingExcel.class);

            } else if (dto.getStatus() != null && dto.getStatus().contains("claimed_ing")) {

                // 查询待定源数据元列表
                List<ClaimDataElementVO> claimDataElementVOS = claimDataElementMapper.selectBaseDataElementByStatus(null, dto,orderBySql);

                // 循环遍历List<ClaimDataElementVO>
                for (ClaimDataElementVO vo : claimDataElementVOS) {
                    // 使用ClaimDataElementVO.dataid作为参数，获取关联的DomainDataElement列表
                    List<DomainDataElement> domainDataElements = claimDomainDataElementMapper.selectDomainDataElementByBaseDataElementDataId(vo.getDataid());

                    // 提取所有source_unit_name并用"|"分割组合
                    String collectUnitNames = domainDataElements.stream().map(DomainDataElement::getSourceUnitName).collect(Collectors.joining("|"));

                    // 将组合成的字符串赋值给ClaimDataElementVO.collectunitnames列
                    vo.setCollectunitnames(collectUnitNames);
                }

                List<OrganisersClaimingExcel> pendingExcelList = claimDataElementVOS.stream().map(vo -> {
                    return OrganisersClaimingExcel.builder().name(vo.getName()).definition(vo.getDefinition()).collectQty(String.valueOf(vo.getCollectunitqty())).collectUnit(vo.getCollectunitnames()).status(StatusEnums.getDescByCode(vo.getStatus())).sendDate(vo.getSendDate()).build();
                }).collect(Collectors.toList());
                // 使用EasyExcel导出
                commonService.exportExcelData(pendingExcelList, response, "组织方发起认领-认领中列表", OrganisersClaimingExcel.class);
            }
        } catch (Exception e) {
            log.error("导出数据失败", e);
            throw new RuntimeException("导出数据失败");
        }
    }
}
