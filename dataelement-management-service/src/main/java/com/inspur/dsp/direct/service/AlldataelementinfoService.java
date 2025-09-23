package com.inspur.dsp.direct.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.direct.entity.dto.DataElementPageQueryDto;
import com.inspur.dsp.direct.entity.dto.ManualConfirmUnitDto;
import com.inspur.dsp.direct.entity.vo.DataElementPageInfoVo;
import com.inspur.dsp.direct.entity.vo.UploadConfirmResultVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数据元信息相关业务接口
 *
 * @author Claude Code
 * @since 2025-09-22
 */
public interface AlldataelementinfoService {

    /**
     * 数据元列表查询
     * @param queryDto 查询条件
     * @return 数据元列表
     */
    Page<DataElementPageInfoVo> getAllDataElementPage(DataElementPageQueryDto queryDto);

    /**
     * 上传定源文件
     * 解析并处理Excel文件进行批量定源操作
     *
     * @param file Excel上传文件，格式要求：
     *             - 文件格式：xlsx/xls
     *             - 文件内容：包含3列（序号、数据元名称、数源单位统一社会信用代码）
     * @return 上传处理结果，包含成功/失败统计和失败记录详情
     * @throws IllegalArgumentException 当文件格式不符合要求时抛出
     * @throws RuntimeException 当文件处理或数据库操作异常时抛出
     */
    UploadConfirmResultVo uploadconfirmunitfile(MultipartFile file);

    /**
     * 手动定源
     * 管理员手动为数据元指定数源单位
     *
     * @param confirmDto 手动定源请求DTO，包含数据元ID和数源单位ID
     * @throws IllegalArgumentException 当参数校验失败时抛出
     * @throws RuntimeException 当数据元不存在、数源单位不存在或数据库操作异常时抛出
     */
    void manualConfirmUnit(ManualConfirmUnitDto confirmDto);
}