package com.inspur.dsp.direct.httpService;

import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.httpService.entity.bsp.BspOragePageReq;
import com.inspur.dsp.direct.httpService.entity.bsp.BspOrganInfoBO;
import com.inspur.dsp.direct.httpService.entity.bsp.BspOrganPageResp;
import com.inspur.dsp.direct.httpService.entity.bsp.DictInfoVO;
import com.inspur.dsp.direct.httpService.entity.bsp.OrganTreeBO;

import java.util.List;
import java.util.Map;

/**
 * bsp系统 dubbo调用转http调用
 */
public interface BspService {

    /**
     * 通过字典类型以及字典编码获取字典名称
     *
     * @param kind
     * @param code
     * @return
     */
    String getDictName(String kind, String code);

    /**
     * 通过字典类型查询字典项
     *
     * @param kind
     * @return
     */
    JSONObject getDictInfo(String kind);

    /**
     * 通过字典类型查询字典项
     *
     * @param kind
     * @return
     */
    List<DictInfoVO> getDictDataInfo(String kind);

    /**
     * 获取菜单数据
     *
     * @param userId
     * @return
     */
    JSONObject getMenuData(String userId);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    JSONObject userLogin(String username, String password);

    /**
     * 单点登录
     *
     * @param username
     * @return
     */
    JSONObject userSSOLogin(String username);

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
    BspOrganInfoBO getOrganInfoByCode(String organCode);

    /**
     * 获取用户的顶层部门编码
     *
     * @param organCode 用户部门编码
     * @return
     */
    String getTopLevelOrganCode(String organCode);

    /**
     * 获取用户的顶层部门名称
     *
     * @param organCode 用户部门编码
     * @return
     */
    String getTopLevelOrganName(String organCode);

    /**
     * 查询bsp部门数据
     *
     * @return
     */
    JSONObject getOrganTree();

    /**
     * 获取数据类型字典数据
     *
     * @return
     */
    Map<String, String> getDataTypeDictMap();

    /**
     * 获取国家部委
     *
     * @return
     */
    OrganTreeBO getOrganAll();

    /**
     * 组织接口--获取下级组织机构树--地方
     **/
    OrganTreeBO getOrganTree(String regionCode);

    /**
     * 组织接口--获取组织机构列表--分页
     * @param bspOragePageReq
     * @return
     */
    BspOrganPageResp getOrganPage(BspOragePageReq bspOragePageReq);
}
