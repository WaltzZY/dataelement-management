package com.inspur.dsp.direct.httpService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.common.utils.CollectionUtils;
import com.inspur.dsp.direct.common.HttpClient;
import com.inspur.dsp.direct.entity.bo.BspOraanInfoBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * @des bsp系统 dubbo调用转http调用
 **/
@Slf4j
@Service("bspService")
public class BSPService {

    @Value("${spring.bsp.url}")
    private String bspUrl;

    @Value("${spring.application.id}")
    private String appCode;

    @Value("${spring.bsp.root-organ-code}")
    private String rootOrganCode;

    /**
     * 通过字典类型以及字典编码获取字典名称
     **/
    public String getDictName(String kind, String code) {
        try {
            String url = bspUrl + "/restapi/dict/getDictName?kind=" + kind + "&code=" + code;
            JSONObject jsonObject = HttpClient.httpGetMethod(url);
            if (CollectionUtils.isNotEmpty(jsonObject) && 1 == jsonObject.getInteger("code")) {
                jsonObject.put("code", "200");
                jsonObject.put("rows", jsonObject.getJSONArray("data"));
                JSONArray data = jsonObject.getJSONArray("data");
                return data.getJSONObject(0).getString("NAME");
            } else {
                return "";
            }
        } catch (NullPointerException e) {
            log.error("BSPService method getDictName error: ", e);
            return "";
        }
    }

    /**
     * 通过字典类型查询字典项d
     **/
    public JSONObject getDictInfo(String kind) {
        try {
            String url = bspUrl + "/restapi/dict/getDictInfo?kind=" + kind + "&appCode=";
            JSONObject jsonObject = HttpClient.httpGetMethod(url);
            if (CollectionUtils.isNotEmpty(jsonObject) && 1 == jsonObject.getInteger("code")) {
                JSONArray data = jsonObject.getJSONArray("data");
                data.sort(Comparator.comparing(obj -> ((JSONObject) obj).getBigDecimal("CODE")));
                jsonObject.put("code", "200");
                jsonObject.put("data", data);
            }
            return jsonObject;
        } catch (Exception e) {
            log.error("BSPService method getDictInfo error: ", e);
            return new JSONObject();
        }
    }

    public JSONArray getDictDataInfo(String kind) {
        try {
            String url = bspUrl + "/restapi/dict/getDictInfo?kind=" + kind + "&appCode=";
            JSONObject jsonObject = HttpClient.httpGetMethod(url);
            if (CollectionUtils.isNotEmpty(jsonObject) && 1 == jsonObject.getInteger("code")) {
                jsonObject.put("code", "200");
                jsonObject.put("rows", jsonObject.getJSONArray("data"));
                return jsonObject.getJSONArray("data");
            }
            return new JSONArray();
        } catch (NullPointerException e) {
            log.error("BSPService method getDictDataInfo error: ", e);
            return new JSONArray();
        }
    }


    public JSONObject getMenuData(String userId) {
        JSONObject object;
        try {
            // 获取角色code
            String url = bspUrl + "/restapi/getUserAppRole?userId=" + userId + "&appCode=" + appCode;
            JSONObject jsonObject = HttpClient.httpGetMethod(url);
            if (CollectionUtils.isNotEmpty(jsonObject) && jsonObject.containsKey("ROLE_CODE")) {
                String roleCode = jsonObject.getString("ROLE_CODE");
                String url2 = bspUrl + "/restapi/getUserAppResource?roleCode=" + roleCode + "&appCode=" + appCode;
                object = HttpClient.httpGetMethod(url2);
                return object;
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            log.error("BSPService method getMenuData error: ", e);
            return null;
        }
    }

    public JSONObject userLogin(String username, String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("account", username);
        data.put("password", password);
        data.put("appCode", appCode);
        data.put("flag", true);
        String url = bspUrl + "/userLogin?account=" + username + "&password=" + password + "&appCode=" + appCode + "&flag=" + true;
        return HttpClient.httpGetMethod(url);
    }

    public JSONObject userSSOLogin(String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", username);
        jsonObject.put("appCode", appCode);
        String url = bspUrl + "/restapi/user/userSSOLogin";
        return HttpClient.httpPostMethod(url, JSONObject.toJSONString(jsonObject));
    }

    /**
     * 获取国家部委接口
     * 通过用户账号，获取用户信息。
     * 提交方式 GET
     * 接口协议 HTTP
     * 内容类型 application/json
     *
     * @param organCode 用户所属部门编码
     * @return 用户部门信息
     * @throws RuntimeException 自定义异常
     */
    public BspOraanInfoBO getOrganInfoByCode(String organCode) {
        if (!StringUtils.hasText(organCode)) {
            log.error("所查询用户所属部门编码未填写");
            return null;
        }
        String url = bspUrl + "/restapi/getOrganInfoByCode?organCode=" + organCode;
        JSONObject jsonObject = HttpClient.httpGetMethod(url);
        if (CollectionUtils.isNotEmpty(jsonObject) && 1 == jsonObject.getInteger("code")) {
            return jsonObject.getObject("data", BspOraanInfoBO.class);
        } else {
            log.error("接口获取失败");
            return null;
        }
    }

    /**
     * 获取用户的顶层部门编码
     *
     * @param organCode 用户部门编码
     * @return
     */

    public String getTopLevelOrganCode(String organCode) {
        if (!StringUtils.hasText(organCode)) {
            log.error("所查询用户所属部门编码未填写");
            return null;
        }
        // 初始化为当前部门code,如果查询不到父级部门,则返回当前部门编号
        String topLevelOrganCode = organCode;
        // 循环查询父级部门,直到父级部门为根部门编码则不查询
        while (true) {
            BspOraanInfoBO bspOraanInfoBO = getOrganInfoByCode(organCode);
            if (bspOraanInfoBO != null) {
                // 更改下一次查询的部门id
                organCode = bspOraanInfoBO.getPARENT_CODE();
                // 查询id为根部门id,则结束循环
                if (rootOrganCode.equals(organCode)) {
                    break;
                }
                // 保存每次查询的父级部门id
                topLevelOrganCode = bspOraanInfoBO.getPARENT_CODE();
            } else {
                // 查不到对象,停止循环
                break;
            }
        }
        return topLevelOrganCode;
    }

    /**
     * 获取用户的顶层部门名称
     *
     * @param organCode 用户部门编码
     * @return
     */

    public String getTopLevelOrganName(String organCode) {
        if (!StringUtils.hasText(organCode)) {
            log.error("所查询用户所属部门编码未填写");
            return null;
        }
        // 初始化为当前部门code,如果查询不到父级部门,则返回当前部门编号
        String topLevelOrganName = organCode;
        // 循环查询父级部门,直到父级部门为根部门编码则不查询
        while (true) {
            BspOraanInfoBO bspOraanInfoBO = getOrganInfoByCode(organCode);
            if (bspOraanInfoBO != null) {
                // 更改下一次查询的部门id
                organCode = bspOraanInfoBO.getPARENT_CODE();
                // 查询id为根部门id,则结束循环
                if (rootOrganCode.equals(organCode)) {
                    break;
                }
                // 保存每次查询的父级部门id
                topLevelOrganName = bspOraanInfoBO.getNAME();
            } else {
                // 查不到对象,停止循环
                break;
            }
        }
        return topLevelOrganName;
    }

    /**
     * 查询bsp闭门数据
     **/
    public JSONObject getOrganTree() {
        try {
            String url = bspUrl + "/restapi/getOrganTree?appCode=&regionCode=200427114503";
            JSONObject jsonObject = HttpClient.httpGetMethod(url);
            return jsonObject;
        } catch (Exception e) {
            log.error("BSPService method getOrganTree error: ", e);
            return new JSONObject();
        }

    }

}
