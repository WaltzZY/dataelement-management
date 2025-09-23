package com.inspur.dsp.direct.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.dbentity.NegotiationRecord;
import com.inspur.dsp.direct.dbentity.NegotiationRecordDetail;
import com.inspur.dsp.direct.entity.dto.NegotiationParmDTO;
import com.inspur.dsp.direct.entity.vo.NegotiationDataElementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface NegotiationRecordMapper extends BaseMapper<NegotiationRecord> {

    NegotiationRecord selectFirstByBaseDataelementDataid(@Param("baseDataelementDataid")String baseDataelementDataid);

    /**
     * 重新selectById方法,将协商相关的详情也查询出来
     */
    NegotiationRecord selectById(String id);

    /**
     * 插入数据同时插入详情
     *
     * @param negotiationRecord
     * @return
     */
    default int insertRecordAndDetail(NegotiationRecord negotiationRecord) {
        // 插入协商主表数据
        int insert = insert(negotiationRecord);
        if (!CollectionUtils.isEmpty(negotiationRecord.getNegotiationRecordDetailList())){
            // 批量插入协商详情数据
            insert += insertSelectiveDetail(negotiationRecord.getNegotiationRecordDetailList());
        }
        return insert;
    }

    int insertSelectiveDetail(@Param("negotiationRecordDetailList") Collection<NegotiationRecordDetail> negotiationRecordDetailList);


    /**
     * 分页查询待协商基准数据元列表
     * @param page 分页对象
     * @param negDTO 查询参数
     * @param sortSql 排序SQL
     * @return 协商数据元列表
     */
    List<NegotiationDataElementVO> getNegotiationDataElementList(
            @Param("page") Page<?> page,
            @Param("dto") NegotiationParmDTO negDTO,
            @Param("sortSql") String sortSql
    );

    /**
     * 查询待协商记主表记录,不带详情
     * @param baseDataelementDataid
     * @return
     */
    NegotiationRecord selectFirstByBaseDataelementDataidNotDetail(@Param("baseDataelementDataid")String baseDataelementDataid);
}