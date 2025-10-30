package com.inspur.dsp.direct.enums;

/**
 * 附件类型枚举
 */
public enum AttachfiletypeEnums {

    // standardfile(标准文件)
    STANDARDFILE("standardfile", "标准文件"),
    // examplefile(样例文件)
    EXAMPLEFILE("examplefile", "样例文件"),

    ;

    private final String value;
    private final String name;

    private AttachfiletypeEnums(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AttachfiletypeEnums getByValue(String value) {
        for (AttachfiletypeEnums item : values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
