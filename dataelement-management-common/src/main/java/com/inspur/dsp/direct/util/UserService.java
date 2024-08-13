/*
package com.inspur.dsp.direct.util;

import com.alibaba.fastjson.JSONObject;
import com.inspur.service.OrganizationService;
import com.inspur.service.UserAuthorityService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userServiceNew")
public class UserService {
	*/
/**
	 * 引入用户RPC服务接口
	 *//*

	@DubboReference(group="bsp",check=false)
	private UserAuthorityService userAuthorityService;
	
	*/
/**
	 * 引入用户RPC服务接口
	 *//*

	@DubboReference(group="bsp",check=false)
	private OrganizationService organizationService;
	
	*/
/**
	 * 引用应用配置常量
	 *//*

	@Value("${spring.application.id}")
	private String appCode;

	*/
/**
	 * 登录方法
	 * @param username
	 * @param password
	 * @return
	 *//*

	public JSONObject userLogin(String username, String password) {
		return this.userAuthorityService.userLogin(username, password, this.appCode, true);
	}
	
	public JSONObject userSSOLogin(String userId) {
		return this.userAuthorityService.userSSOLogin(userId, this.appCode);
	}
	
	
	*/
/**
	 * 根据部门查用户
	 * @param org_code
	 * @return
	 *//*

	public List<Map<String,Object>> findUserByOrg(String org_code ) {
		return this.userAuthorityService.findUserByOrg(org_code);
	}
	
	public JSONObject getOranInfoById(String org_code) {
		JSONObject json = organizationService.getOrganMap(org_code);
		if (json != null && "200".equals(json.getString("code")) && json.getJSONObject("rows") != null) {
			return json.getJSONObject("rows");
		}
		json = organizationService.getRegionInfoByRegionCode(org_code);
		if (json != null && "200".equals(json.getString("code")) && json.getJSONArray("rows") != null && json.getJSONArray("rows").size() > 0) {
			return json.getJSONArray("rows").getJSONObject(0);
		}
		return null;
	}
	
	public JSONObject getUserByRoleAndOrg(String roleid,String orgid){
		return this.userAuthorityService.getUserByRoleAndOrg(roleid,orgid);
	}
	public Map<String, Object> findUserInfo(String id){
		return this.userAuthorityService.findUserInfo(id);
	}
	*/
/**
	 * 
	 * @param regionCode
	 * @param flag
	 * @return
	 *//*

	public List<Map<String, Object>> findOrganRegionTree(String regionCode, boolean flag) {
		return this.organizationService.findOrganRegionTree(regionCode, flag);
	}
	
	*/
/**
	 * 找回密码-根据用户账号查找用户信息
	 * 
	 * @param account
	 * @return
	 *//*

	public JSONObject getUserInfoByAccount(String account) {
		return this.userAuthorityService.getUserInfoByAccount(account);
	}
}
*/
