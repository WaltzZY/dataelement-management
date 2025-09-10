package com.inspur.dsp.direct.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.dsp.direct.constant.Constants;
import com.inspur.dsp.direct.domain.UserLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class BspLoginUserInfoUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取当前登录人信息
     * @return
     */
    public static UserLoginInfo getUserInfo() {
        HttpSession session = ServletUtils.getSession();
        Object dspSessionToken = session.getAttribute("_Dsp_Session_Token_");
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(dspSessionToken), UserLoginInfo.class);
        } catch (JsonProcessingException e) {
            log.error("BspLoginUserInfoUtils method getUserInfo error: ", e);
        }
        throw new RuntimeException("获取当前登录人信息失败");
    }

    /**
     * 获取当前登录人在系统下角色
     * @param appCode 应用系统code
     * @return
     */
    public static List<String> getUserRoleList(String appCode) {
    	UserLoginInfo userInfo = getUserInfo();
        if (userInfo == null) {
            return null;
        }
        String roleValue = userInfo.getLoginRoleValue().getOrDefault(appCode, Constants.EMPTY_STRING);
        if (StringUtils.hasText(roleValue)) {
            return Arrays.asList(roleValue.split(Constants.COMMA));
        }
        log.warn("当前登录人在此系统中未分配角色");
        return new ArrayList<>();
    }
}
