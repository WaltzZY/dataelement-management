package com.inspur.dsp.direct.console.controller.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.hutool.extra.servlet.ServletUtil;
import com.inspur.dsp.common.utils.constant.ServiceConstant;
import com.inspur.dsp.common.web.controller.BaseController;
import com.inspur.dsp.direct.httpService.BSPService;
import com.inspur.dsp.direct.util.AESEncrypter;
import com.inspur.dsp.direct.util.CTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;

/**
 * 网站首页资源(开发者控制器)
 *
 * @author jamesli
 * @version 2.0
 * @date 2015-05-31
 * @description
 */
@Slf4j
@Controller
public class MainController extends BaseController {
    /**
     * 注入验证码服务
     */
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private BSPService bspService;

    @Value("${spring.application.login.type:0}")
    private String loginType;
    @Value("${spring.application.rootContext:''}")
    private String rootUrl;
    @Value("${spring.application.outRootContext:''}")
    private String outRootUrl;
    @Value("${spring.innerIp.start:''}")
    private String ipStart;
    @Value("${spring.application.ucenter:''}")
    private String ucenterUrl;
    @Value("${spring.application.id:''}")
    private String appCode;


    /**
     * 登录状态查询
     *
     * @param request  请求体
     * @param response 响应体
     * @return 登录状态
     */
    @RequestMapping(value = "/loginStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject loginStatus(HttpServletRequest request, HttpServletResponse response) {
        log.info("method loginStatus start");
        JSONObject jsonObject = new JSONObject();
        // 1. 判断是否已经登录了(check session已经存在，直接登录)
        HttpSession session = request.getSession();
        if (log.isDebugEnabled()) {
            log.debug("准备获取_Dsp_Session_Token_");
        }
        JSONObject jsonUser = (JSONObject) session.getAttribute(ServiceConstant.USER_TOCKEN);
        log.info("11111111111111获取_Dsp_Session_Token_成功，jsonUser = {}", jsonUser);
        if (log.isDebugEnabled()) {
            log.debug("获取_Dsp_Session_Token_成功，jsonUser = {}", jsonUser);
        }
        if (jsonUser != null) {
            if (log.isDebugEnabled()) {
                log.debug("进入准备单点登录校验，jsonUser不等于空");
            }
            // 1-1. 已登录取出当前用户登录的会话信息及权限等
            String username = jsonUser.getString("ACCOUNT");
            String password = jsonUser.getString("PASSWORD");
            String userId = jsonUser.getString("ID");
            log.info("ACCOUNT, username = {}", username);
            log.info("ACCOUNT, PASSWORD = {}", password);
            log.info("ACCOUNT, userId = {}", userId);
            // 无使用,暂时注掉
            // String ip = IpHelper.getIpAddr(request);
            log.info("loginHandler method start");
            jsonObject = this.loginHandler(request, response, username, password, userId);
            log.info("loginHandler method end");
        } else {
            if ("2".equals(loginType)) {
                String url = rootUrl;
                String ip = ServletUtil.getClientIP(request);
                if (!ip.startsWith(ipStart)) {
                    url = outRootUrl;
                }
                jsonObject.put("msg", "Ucenter登录");
                jsonObject.put("code", "2001");
                jsonObject.put("url", ucenterUrl + "?app_code=" + appCode + "&url=" + url);
                return jsonObject;
            }
            jsonObject.put("code", "-1");
            jsonObject.put("msg", "未登录");
        }
        log.info("method loginStatus end");
        return jsonObject;
    }

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(HttpServletRequest request, HttpServletResponse reponse, @RequestBody Map map) {
        log.info("method login start");
        JSONObject returnData = new JSONObject();
        try {
            request.getSession().invalidate();
            if (request.getCookies() != null) {
                Cookie cookie = request.getCookies()[0];// 获取cookie
                cookie.setMaxAge(0);// 让cookie过期
            }
        } catch (Exception e) {
            log.error("用户会话注销出现异常", e);
        }
        // 执行登录逻辑
        String username = String.valueOf(map.get("username"));
        String password = String.valueOf(map.get("password"));
//		JSONObject jsonUser = this.userService.userLogin(username, password);
        JSONObject jsonUser = bspService.userLogin(username, password);
        String state = jsonUser.getString("state");
//		登录成功
        if (ServiceConstant.SYSTEM_SUCCESS.equals(state)) {
            HttpSession session = request.getSession(true);
            //处理用户菜单结构
            JSONArray array = jsonUser.getJSONArray("menus");
            if (array == null || array.size() == 0) {
                //设置登录失败标志位
                returnData.put("msg", jsonUser.getString("error"));
                returnData.put("code", "4003");
                return returnData;
            }
            JSONObject user = jsonUser.getJSONObject("user");
            // cookie设置开始-----------------------------
            String ACCOUNT = user.getString("ACCOUNT");
            String ID = user.getString("ID");
            try {
                CTools.setCookie("sso_token", AESEncrypter.getInstance().encrypt(ID), -1, "/", "", reponse);
                CTools.setCookie("sso_token_account", AESEncrypter.getInstance().encrypt(ACCOUNT), -1, "/", "", reponse);
            }catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                    IllegalBlockSizeException | BadPaddingException e) {
                log.error("cookie设置出现异常: ");
            }
            // cookie设置结束-----------------------------
            session.setAttribute(ServiceConstant.USER_TOCKEN, user);
            if (log.isDebugEnabled()) {
                log.debug("session中赋值成功，结果：{}", user);
                log.debug("获取session中用户数据，结果：{}", session.getAttribute(ServiceConstant.USER_TOCKEN));
            }
            // 设置登录用户会话
            String userTicket = (String) session.getAttribute("UserTicket");
            if (userTicket != null && !"".equals(userTicket)) {
                this.setAttr("UserTicket", session.getAttribute("UserTicket"));
            }
            // 验证通过,移除Session中的验证码信息
            session.removeAttribute(ServiceConstant.KAPTCHA_SESSION_KEY);
            returnData.put("code", "200");
            returnData.put("msg", jsonUser.getString("error"));
            returnData.put("currentUser", user);
            log.info("method login end success");
            return returnData;
        } else {
            returnData.put("msg", jsonUser.getString("error"));
            returnData.put("code", "9999");
            log.info("method login end fail");
            return returnData;
        }
    }

    /**
     * 获取用户下指定应用内的菜单
     *
     * @param map
     * @return
     */
    @PostMapping("/getMenuData")
    @ResponseBody
    public JSONObject getMenuData(@RequestBody Map map) {
        JSONObject returnData = new JSONObject();
        String userID = String.valueOf(map.get("userID"));
        JSONObject menuData = bspService.getMenuData(userID);
        JSONArray menus = menuData.getJSONArray("menus");
        if (menus == null || menus.size() == 0) {
            //设置登录失败标志位
            returnData.put("msg", "对不起您没有访问权限！");
            returnData.put("code", "4003");
            return returnData;
        } else {
            //一级菜单
            List<JSONObject> heads = this.fechHeadMenu(menus);
            log.info("heads = {}", heads);
            returnData.put("code", "200");
            returnData.put("msg", "获取菜单数据成功");
            returnData.put("navMenus", heads);
        }
        return returnData;
    }


    /**
     * 查找一级菜单
     *
     * @param menus
     * @return
     */
    private List<JSONObject> fechHeadMenu(JSONArray menus) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        int size = menus.size();
        for (int i = 0; i < size; i++) {
            JSONObject menu = menus.getJSONObject(i);
            String isFolder = (String) menu.get("PARENT_CODE");
            //parent_code为#号，则为一级菜单
            if ("#".equalsIgnoreCase(isFolder)) {
                String menuId = menu.getString("ID");
                JSONArray array = getChildMenu(menuId, menus);
                if (!array.isEmpty()) {
                    menu.put("child", array);
                }
                menu.put("level", 1);
                list.add(menu);
            }
        }
        //排序
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject arg0, JSONObject arg1) {
                return arg0.getString("SORT_ORDER").compareTo(arg1.getString("SORT_ORDER"));
            }
        });
        return list;
    }

    /**
     * 读取子菜单
     *
     * @param menuId
     * @param menus
     * @return
     */
    private JSONArray getChildMenu(String menuId, JSONArray menus) {
        int size = menus.size();
        JSONArray array = new JSONArray();
        for (int i = 0; i < size; i++) {
            JSONObject menu = menus.getJSONObject(i);
            String parentId = (String) menu.get("PARENT_CODE");
            //parent_code为#号，则为一级菜单
            if (menuId.equalsIgnoreCase(parentId)) {
                JSONArray childMenu = getChildMenu(menu.getString("ID"), menus);
                if (childMenu.size() > 0) {
                    menu.put("child", childMenu);
                }
                String resPath = menu.getString("RES_PATH");
                String[] resPaths = resPath.split("#");
                menu.put("level", resPaths.length);
                array.add(menu);
            }
        }
        return array;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        JSONObject jsonObject = new JSONObject();
        //如果用户已登录，清除session
        if (null != session) {
            session.removeAttribute(ServiceConstant.USER_TOCKEN);
            session.invalidate();
        }
        jsonObject.put("code", "200");
        jsonObject.put("msg", "登出成功");
        return jsonObject;
    }


    /**
     * 验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void handleRequest(HttpServletRequest request,
                              HttpServletResponse response) {
        // 防站点攻击
        super.checkCrsfToken(request, response);
        try {
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            HttpSession session = request.getSession();
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            session.setAttribute(ServiceConstant.KAPTCHA_SESSION_KEY, createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("image/jpeg");

            ImageIO.write(challenge, "jpeg", jpegOutputStream);
            byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

            ServletOutputStream respOs = response.getOutputStream();
            respOs.write(captchaChallengeAsJpeg);
            respOs.flush();
            respOs.close();
        } catch (IOException e) {
            logger.error("generate captcha image error: ", e);
        }
    }

    /**
     * 验证码一致性检查
     *
     * @param response
     */
    @RequestMapping(value = "/captcha/code", method = RequestMethod.POST)
    @ResponseBody
    public String checkCode(HttpServletResponse response,
                            HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            // 需要sessionId 来验证校验码
            String captchaValue = this.getPara("captcha");
            String captchaId = (String) request.getSession().getAttribute(ServiceConstant.KAPTCHA_SESSION_KEY);
            // 校验验证码
            if (captchaId != null && captchaValue != null && captchaValue.equals(captchaId)) {
                json.put("code", "200");
                json.put("msg", "验证码匹配成功");
            } else {
                json.put("code", "9999");
                json.put("msg", "验证码匹配失败");
            }
            return json.toJSONString();

        } catch (NullPointerException e) {
            // e.printStackTrace();
            log.error("验证码匹配失败");
            json.put("code", "9999");
            json.put("msg", "验证码匹配失败");
            return json.toJSONString();
        }
        catch (Exception e) {
            // e.printStackTrace();
            log.error("验证码匹配失败");
            json.put("code", "9999");
            json.put("msg", "验证码匹配失败");
            return json.toJSONString();
        }
    }

    @SuppressWarnings("unchecked")
    private JSONObject loginHandler(HttpServletRequest request,
                                    HttpServletResponse reponse, String username,
                                    String password, String userId) {
        JSONObject jsonUser = null;
        JSONObject data = null;
        JSONObject user = null;
        String state = "";
        JSONArray array = new JSONArray();
        JSONObject returnData = new JSONObject();
        if (!"0".equals(loginType)) {
//			jsonUser = this.userService.userSSOLogin(userId);
            log.info("请求参数：用户username：{}",username);
            log.info("请求参数：用户password：{}",password);
            log.info("请求参数：用户ID：{}",userId);
            jsonUser = bspService.userSSOLogin(userId);
            if (log.isDebugEnabled()) {
                log.debug("ucenter登录方式，结果：{}", jsonUser);
            }
            log.info("ucenter登录方式，结果："+jsonUser);
            data = jsonUser.getJSONObject("data");
            user = data.getJSONObject("user");
            state = data.getString("state");
            array = data.getJSONArray("menus");
            log.info("SSOLoginjson访问user结果 = " + user);
            // 设置cookie开始---------------------
            /*JSONObject user = jsonUser.getJSONObject("user");
            log.info("111111111111从数据库获取用户信息为：{}",user);
            String ACCOUNT = user.getString("ACCOUNT");
            String ID = user.getString("ID");
            try {
                CTools.setCookie("sso_token", AESEncrypter.getInstance().encrypt(ID), -1, "/", "", reponse);
                CTools.setCookie("sso_token_account", AESEncrypter.getInstance().encrypt(ACCOUNT), -1, "/", "", reponse);
            } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                     IllegalBlockSizeException | BadPaddingException e) {
                log.error("loginHandler method set cookie error: ");
            }*/
            // 设置cookie结束---------------------
        } else {
            jsonUser = bspService.userLogin(username, password);
            log.info("正常登录，结果："+jsonUser);
            state = jsonUser.getString("state");
            user = jsonUser.getJSONObject("user");
            array = jsonUser.getJSONArray("menus");
            log.info("正常登录");
            if (log.isDebugEnabled()) {
                log.debug("正常登录，结果：{}", jsonUser);
            }
        }
        if (ServiceConstant.SYSTEM_SUCCESS.equals(state)) {
            log.info("登录成功");
            HttpSession session = request.getSession(true);
            //处理用户菜单结构
            if (array == null || array.size() == 0) {
                //设置登录失败标志位
                returnData.put("msg", jsonUser.getString("error"));
                returnData.put("code", "4003");
                return returnData;
            }
            // 设置cookie开始--------------------------------
            String ACCOUNT = user.getString("ACCOUNT");
            String ID = user.getString("ID");
            try {
                CTools.setCookie("sso_token", AESEncrypter.getInstance().encrypt(ID), -1, "/", "", reponse);
                CTools.setCookie("sso_token_account", AESEncrypter.getInstance().encrypt(ACCOUNT), -1, "/", "", reponse);
            } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                    IllegalBlockSizeException | BadPaddingException e) {
                log.error("loginHandler method set cookie error: ");
            }
            // 设置cookie结束--------------------------------
            session.setAttribute(ServiceConstant.USER_TOCKEN, user);
            if (log.isDebugEnabled()) {
                log.debug("缓存中放入user-token，结果：{}", user);
            }
            log.info("缓存中放入user-token");
            // 设置登录用户会话
            String userTicket = (String) session.getAttribute("UserTicket");
            if (userTicket != null && !"".equals(userTicket)) {
                this.setAttr("UserTicket", session.getAttribute("UserTicket"));
            }
            // 验证通过,移除Session中的验证码信息
            session.removeAttribute(ServiceConstant.KAPTCHA_SESSION_KEY);
            returnData.put("code", "200");
            returnData.put("msg", jsonUser.getString("error"));
            returnData.put("currentUser", user);
            return returnData;
        } else {
            returnData.put("msg", jsonUser.getString("error"));
            returnData.put("code", "9999");
            return returnData;
        }

    }
}
