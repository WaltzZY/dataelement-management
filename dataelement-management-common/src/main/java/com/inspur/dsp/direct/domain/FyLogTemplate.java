package com.inspur.dsp.direct.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FyLogTemplate {

    /**
     * 部门名称
     */
    private String organName;

    /**
     * 事项名称
     */
    private String matterName;

    /**
     * 样表名称
     */
    private String attachmentName;

    /**
     * 数据项名称
     */
    private String dataelementName;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 审核不通过信息
     */
    private String feedbackMsg;

    /**
     * 企业数据一张表删除id
     */
    private String deleteId;
    /**
     * 操作时间
     */
    private String dateTimeStr;

}
