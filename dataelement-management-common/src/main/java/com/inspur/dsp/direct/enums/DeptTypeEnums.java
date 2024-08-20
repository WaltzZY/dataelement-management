package com.inspur.dsp.direct.enums;

import com.inspur.dsp.direct.constant.Constants;
import lombok.Getter;

/**
 * 机构,地方枚举
 */
@Getter
public enum DeptTypeEnums {
    REGINON("1", "region", "地方"),
    ORGAN("0", "organ", "部门");
    ;

    DeptTypeEnums(String code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    private final String code;
    private final String name;
    private final String desc;

    public static String getName(String code)
    {
        DeptTypeEnums[] values = DeptTypeEnums.values();
        for (DeptTypeEnums value : values) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return Constants.EMPTY_STRING;
    }

}
