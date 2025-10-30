package com.inspur.dsp.direct.enums;

/**
 * 模板类型枚举
 * 用于区分不同类型的导入模板
 */
public enum TemplateTypeEnums {

    IMPORT_DETERMINE_RESULT("import_determine_result", "导入定数结果模板"),
    IMPORT_DATASOURCE_RESULT("import_datasource_result", "导入定源结果模板"),
    
    ;
    
    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    TemplateTypeEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (TemplateTypeEnums templateType : TemplateTypeEnums.values()) {
            if (templateType.getCode().equals(code)) {
                return templateType.getDesc();
            }
        }
        return null;
    }
    
    public static TemplateTypeEnums getByCode(String code) {
        for (TemplateTypeEnums templateType : TemplateTypeEnums.values()) {
            if (templateType.getCode().equals(code)) {
                return templateType;
            }
        }
        return null;
    }
}