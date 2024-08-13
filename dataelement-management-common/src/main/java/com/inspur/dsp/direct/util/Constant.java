package com.inspur.dsp.direct.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class Constant {
    /**
     * 系统管理员角色编码
     */
    public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
    /**
     * 区划管理员角色编码
     */
    public static final String ROLE_REGION = "ROLE_REGION";
    /**
     * 部门管理员角色编码
     */
    public static final String ROLE_ORGAN = "ROLE_ORGAN";
    /**
     * 数据管理员角色编码
     */
    public static final String ROLE_DATA_MANAGER = "ROLE_DATA_MANAGER";

    /**
     * 数据提供者角色编码
     */
    public static final String ROLE_DATA_PROVIDER = "ROLE_DATA_PROVIDER";

    /**
     * 数据使用者角色编码
     */
    public static final String ROLE_DATA_CONSUMER = "ROLE_DATA_CONSUMER";

    /**
     * 资源注册操作员
     */
    public static final String RES_REGISTER_ROLE = "RES_REGISTER_ROLE";

    /**
     * 资源审核操作员
     */
    public static final String RES_CHECK_ROLE = "RES_CHECK_ROLE";

    /**
     * 资源中心管理员
     */
    public static final String RES_ADMIN = "RES_ADMIN";

    /**
     * 资源发布审核-简易流程编码
     */
    public static final String RESOURCE_PUBLISH_FLOW = "RESOURCE_PUBLISH_FLOW";

    /**
     * 资源撤销审核-简易流程编码
     */
    public static final String RESOURCE_REVOKE_FLOW = "RESOURCE_REVOKE_FLOW";

    /**
     * 发布审核员
     */
    public static final String ROLE_PUBAUDIT = "ROLE_PUBAUDIT";

    /**
     * 资源发布者
     */
    public static final String ROLE_RESOURCEPUB = "ROLE_RESOURCEPUB";
    
    /**
     * 资源类型代码
     */
    public static final String TABLE_RESOURCE = "table";
    
    public static final String FILE_RESOURCE = "file";
    
    public static final String API_RESOURCE = "api";
    
    public static final String DATASERVICE_RESOURCE = "dataService";
    
    public static final String FOLDER_RESOURCE = "folder";
    
    public static final String SYSTEM_MANAUL_CN = "人工填报";
    
    public static final String SYSTEM_MANAUL_EN = "manaul";
    /**
     * 目录系统的appCode
     */
    public static final String CATALOG_APP_CODE = "DSP-CATALOG-CONSOLE";
    /**
     * 需求系统的appCode
     */
    public static final String REQUIRE_APP_CODE = "DSP-REQUIRE-CONSOLE";


    private static String GET_RESOURCEURL_URL;
    //资源系统内网地址
    private static String RESOURCE_IN_CONTEXT;
    //资源系统外网地址
    private static String RESOURCE_OUTER_CONTEXT;
    //门户系统地址
    private static String DATAWEB_CONTEXT;
    //交换系统地址
    private static String EXCHANGE_CONTEXT;
    //统一登录地址
    private static String UCENTER_CONTEXT;
    //目录系统地址
    private static String CATALOG_CONTEXT;
    //网关地址
    private static String GATEWAY_CONTEXT;
    //rc外网地址
    private static String RC_OUTER_CONTEXT;
    //rc内网地址
    private static String RC_INNER_CONTEXT;
    //请求资源接口地址
    private static String GET_RESOURCE_URL;

    //请求事项接口地址
    private static String GET_ITEM_URL;
    //请求目录接口地址
    private static String GET_CATALOG_URL;
    //请求目录资源地址
    private static String GET_CATALOGRESOURCE_URL;
    //请求目录信息项地址
    private static String GET_CATALOGCLOUMN_URL;
    //系统编码
    private static String APP_CODE;
    //项目个性化编码
    private static String PROJECT_ID;
    //国办项目增加地方操作员角色
    private static String ROLE_LOCALOPERATOR;
    /**
     * 系统业务运行人员配置项
     */
    private static String ROLE_BUSIOPER;

    @Value("${spring.application.portal.dataweb:''}")
    public void setDatawebContext(String dATAWEB_CONTEXT) {
        DATAWEB_CONTEXT = dATAWEB_CONTEXT;
    }
    
    @Value("${spring.application.portal.innerusercenter:''}")
    public static void setPortalUsercenterInnerContext(String portalUsercenterInnerContext) {
        PORTAL_USERCENTER_INNER_CONTEXT = portalUsercenterInnerContext;
    }

    private static  String PORTAL_USERCENTER_INNER_CONTEXT;
    
    @Value("${spring.application.exchange:''}") 
    public String oldExchange;
    
    @Value("${global.exchange:''}")
    public void setEXCHANGE_CONTEXT(String eXCHANGE_CONTEXT) {
        EXCHANGE_CONTEXT = StringUtils.isNotBlank(oldExchange) ? oldExchange: eXCHANGE_CONTEXT;
    }
    
    @Value("${spring.application.ucenter:''}") 
    public String oldUcenter;
    
    @Value("${global.ucenter:''}")
    public void setUCENTER_CONTEXT(String uCENTER_CONTEXT) {
        UCENTER_CONTEXT = StringUtils.isNotBlank(oldUcenter) ? oldUcenter: uCENTER_CONTEXT;
    }
    
    @Value("${spring.application.catalog:''}")
    public String oldCatalog;
    
    @Value("${global.catalog-context:''}")
    public void setCATALOG_CONTEXT(String cATALOG_CONTEXT) {
        CATALOG_CONTEXT = StringUtils.isNotBlank(oldCatalog) ? oldCatalog: cATALOG_CONTEXT;
    }

    @Value("${spring.application.api.gateway:''}")
    public String oldGateway;
    
    @Value("${global.api.gateway:''}")
    public void setGATEWAY_CONTEXT(String gATEWAY_CONTEXT) {
        GATEWAY_CONTEXT = StringUtils.isNotBlank(oldGateway) ? oldGateway: gATEWAY_CONTEXT;
    }

    @Value("${spring.application.rc.outer_rcservice:''}")
    public String oldRcOuterContext;
    
    @Value("${global.rc.outer_rcservice:''}")
    public void setRC_OUTER_CONTEXT(String rC_OUTER_CONTEXT) {
        RC_OUTER_CONTEXT = StringUtils.isNotBlank(oldRcOuterContext) ? oldRcOuterContext: rC_OUTER_CONTEXT;
    }

    @Value("${spring.application.rc.filestore_rcservice:''}")
    public String oldRcInnerContext;
    
    @Value("${spring.application.rc.filestore_rcservice:''}")
    public void setRC_INNER_CONTEXT(String rC_INNER_CONTEXT) {
        RC_INNER_CONTEXT = StringUtils.isNotBlank(oldRcInnerContext) ? oldRcInnerContext: rC_INNER_CONTEXT;
    }
    @Value("${spring.application.getItemsUrl:''}")
    public void setGET_ITEM_URL(String gET_ITEM_URL){
        GET_ITEM_URL = StringUtils.isNotBlank(gET_ITEM_URL) ? gET_ITEM_URL : "";
    }
    @Value("${spring.application.getResourceUrl:''}")
    public void setGET_RESOURCE_URL(String gET_RESOURCE_URL){
        GET_RESOURCE_URL = StringUtils.isNotBlank(gET_RESOURCE_URL) ? gET_RESOURCE_URL : "";
    }
    @Value("${spring.application.getCatalogUrl:''}")
    public  void setGET_CATALOG_URL(String getCatalogUrl) {
        GET_CATALOG_URL = StringUtils.isNotBlank(getCatalogUrl) ? getCatalogUrl : "";
    }
    @Value("${spring.application.getCatalogResourceUrl:''}")
    public void setGetCatalogresourceUrl(String getCatalogresourceUrl) {
        GET_CATALOGRESOURCE_URL = getCatalogresourceUrl;
    }
    @Value("${spring.application.getCatalogColumnUrl:''}")
    public void setGetCatalogcloumnUrl(String getCatalogcloumnUrl) {
        GET_CATALOGCLOUMN_URL = getCatalogcloumnUrl;
    }
    @Value("${spring.application.id:''}")
    public  void setAppCode(String appCode) {
        APP_CODE = appCode;
    }

    @Value("${spring.application.require.project.id:''}")
    public void setPROJECT_ID(String value) {
        PROJECT_ID = value;
    }

    @Value("${spring.application.local.operate.role:''}")
    public void setRoleLocaloperator(String roleLocaloperator) {
        ROLE_LOCALOPERATOR = StringUtils.isNotBlank(roleLocaloperator) ? roleLocaloperator : "";
    }

    /**
     * 运营业务人员角色值
     */
    @Value("${spring.application.busioper.role:''}")
    public void setROLE_BUSIOPER(String value) {
        ROLE_BUSIOPER = value;
    }
    //资源系统内外网值
    @Value("${spring.application.in.resource:''}")
    public void setInResource(String inResource) {
    	RESOURCE_IN_CONTEXT = inResource;
    }
    
    //资源系统外网值
    @Value("${spring.application.out.resource:''}")
    public void setOutResource(String outResource){
    	RESOURCE_OUTER_CONTEXT = outResource;
    }
}
