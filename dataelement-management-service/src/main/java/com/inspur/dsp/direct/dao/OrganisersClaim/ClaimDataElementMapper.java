package com.inspur.dsp.direct.dao.OrganisersClaim;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.GetDataElementDTO;
import com.inspur.dsp.direct.entity.vo.ClaimDataElementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClaimDataElementMapper {

    /**
     * 根据状态查询数据元列表
     * @param dto 查询条件
     * @return 数据元列表
     */
    List<ClaimDataElementVO> selectBaseDataElementByStatus(Page<ClaimDataElementVO> page, GetDataElementDTO dto,@Param("orderBySql") String orderBySql);

    List<ClaimDataElementVO> selectBaseDataElementByStatus(@Param("dto") GetDataElementDTO dto);
}