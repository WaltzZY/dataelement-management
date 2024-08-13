package com.inspur.dsp.direct.util;

import com.inspur.dsp.direct.domain.FyLogTemplate;
import com.inspur.dsp.direct.enums.FyLogTemplateEnum;

import java.text.MessageFormat;

import static com.inspur.dsp.direct.enums.FyLogTemplateEnum.*;

public class FyLogTemplateUtil {

    /**
     * 生成日志所需要的用户查看内容
     *
     * @param fyLogTemplateEnum 日志模板枚举
     * @param fyLogTemplate     日志参数对象
     * @return 用户查看内容
     */
    public static String generateFyLogStr(FyLogTemplateEnum fyLogTemplateEnum, FyLogTemplate fyLogTemplate) {
        switch (fyLogTemplateEnum) {
            // 从企业信息填报事项数据项表处理数据到部门数据项表的异步处理程序执行过程中
            case SITUATION_01:
                return MessageFormat.format("系统自动将来自于部门：{0}，事项：{1}，样表：{2}中的数据项【{3}】和企业一张表中的同名称数据元建立匹配关系。", fyLogTemplate.getOrganName(), fyLogTemplate.getMatterName(), fyLogTemplate.getAttachmentName(), fyLogTemplate.getDataelementName());
            case SITUATION_02:
                return MessageFormat.format("系统查询数据元基准库，找到和部门：{0}，事项：{1}，样表：{2}中的数据项【{3}】同名称的基准数据元，自动提取基准数据元定义等信息赋予数据项【{4}】。", fyLogTemplate.getOrganName(), fyLogTemplate.getMatterName(), fyLogTemplate.getAttachmentName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDataelementName());
            case SITUATION_03:
                return "已进行过同一部门同一用户提交的同名数据项，系统自动建立关联关系，不增加部门数据项表数据。";
            case SITUATION_14:
                return MessageFormat.format("系统自动将来自于部门：{0}，事项：{1}，样表：{2}中的数据项【{3}】数据创建到部门数据项中,并关联。", fyLogTemplate.getOrganName(), fyLogTemplate.getMatterName(), fyLogTemplate.getAttachmentName(), fyLogTemplate.getDataelementName());
            // 在维护数据项页面执行保存操作
            case SITUATION_04:
                return MessageFormat.format("用户{0}修改【{1}】相关内容并保存，保存内容和企业数据一张表数据元不存在匹配。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_05:
                return MessageFormat.format("用户{0}修改【{1}】相关内容并保存，保存内容和企业数据一张表数据元存在匹配。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            // 在维护数据项页面执行提交审核操作
            case SITUATION_06:
                return MessageFormat.format("用户{0}提交【{1}】相关信息至审核方，提交内容与企业数据一张表不存在匹配。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_07:
                return MessageFormat.format("用户{0}提交【{1}】相关信息至审核方，提交内容和企业数据一张表数据元不存在匹配。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            // 在审核页面执行审核操作
            case SITUATION_08:
                return MessageFormat.format("用户{0}审核数据元【{1}】，审核结果为通过。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_16:
                return MessageFormat.format("用户{0}审核数据元【{1}】，审核结果为通过。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_09:
                return MessageFormat.format("用户{0}审核数据元【{1}】，审核结果为通过，在企业数据一张表中新增数据元【{2}】相关信息。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDataelementName());
            case SITUATION_19:
                return MessageFormat.format("用户{0}审核数据元【{1}】，审核结果为通过，在企业数据一张表中新增数据元【{2}】相关信息。系统自动将新插入的企业数据一张表数据元进行匹配关联。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDataelementName());
            case SITUATION_10:
                return MessageFormat.format("用户{0}审核数据元【{1}】，审核结果为不通过。原因为：{2}。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getFeedbackMsg());
            // 在企业数据一张表中新增数据元
            case SITUATION_11:
                return MessageFormat.format("用户{0}在企业数据一张表中新增数据元【{1}】。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_12:
                return MessageFormat.format("用户{0}修改企业数据一张表中数据元【{1}】信息。", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName());
            case SITUATION_13:
                return MessageFormat.format("用户{0}删除企业数据一张表中数据元【{1}】，唯一标识为：{2}", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDeleteId());
            case SITUATION_15:
                return MessageFormat.format("用户{0}删除企业数据一张表中数据项：【{1}】，该部门数据项连接企业数据项断开，操作时间：{2}", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDateTimeStr());
            case SITUATION_17:
                return MessageFormat.format("用户{0}修改企业数据一张表中数据项：【{1}】，该部门数据项连接企业数据项断开，操作时间：{2}", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDateTimeStr());
            case SITUATION_18:
                return MessageFormat.format("用户{0}保存企业数据一张表中数据项：【{1}】，创建该部门数据项连接企业数据项，操作时间：{2}", fyLogTemplate.getUserName(), fyLogTemplate.getDataelementName(), fyLogTemplate.getDateTimeStr());
        }
        return null;
    }

//    public static void main(String[] args) {
////        System.out.println(generateFyLogStr(SITUATION_01, new FyLogTemplate("1", "2", "3", "4", "5", "6", "7")));
//    }
}
