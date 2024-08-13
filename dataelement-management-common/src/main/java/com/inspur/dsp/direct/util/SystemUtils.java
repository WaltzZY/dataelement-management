package com.inspur.dsp.direct.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * 系统管理接口公共类
 * 2016-5-20
 */
public class SystemUtils {
	private static Log log = LogFactory.getLog(SystemUtils.class);

	public static final String System_Organization = "System_Organization";
	public static final String System_Region = "System_Region";
	public static final String System_Dict = "System_Dict";

	/**
	 * 获取用户登录标识 <br>
	 * <p>
	 * Description: <br>
	 * <p>
	 * Date: 2015年3月27日 上午10:24:41<br/>
	 * <p>
	 *
	 * @param request
	 * @return
	 * @see String
	 */
	public static String getUid(HttpServletRequest request) {
		try {
			if(request.getSession().getAttribute("uid") != null) {
				return request.getSession().getAttribute("uid").toString() ;
			}
		} catch (NullPointerException e) {
			log.error("SystemUtils method getUid error: ", e);
		}
		return null;
	}

	/**
	 * 获取访问者IP 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 *
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}

		ip = request.getHeader("X-Real-IP");
		if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}else {
			return request.getRemoteAddr();
		}

	}

	/**
	 * 获取来访者的浏览器版本
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestBrowserInfo(HttpServletRequest request) {
		String header = request.getHeader("user-agent").toLowerCase(Locale.ENGLISH);
		if (header == null || header.equals("")) {
			return "";
		}
		if (header.indexOf("msie") > 0) {
			if(header.indexOf("msie 9.0")>-1){
                return "IE 9.0";
            }else if(header.indexOf("msie 10.0")>-1){
                return "IE 10.0";
            }else if(header.indexOf("msie 8.0")>-1){
                return "IE 8.0";
            }else if(header.indexOf("msie 7.0")>-1){
                return "IE 7.0";
            }else if(header.indexOf("msie 6.0")>-1){
                return "IE 6.0";
            }
			return "IE";
		} else if (header.indexOf("firefox") > 0) {
			return "Firefox";
		} else if (header.indexOf("chrome") > 0) {
			return "Chrome";
		} else if (header.indexOf("safari") > 0) {
			return "Safari";
		} else if (header.indexOf("camino") > 0) {
			return "Camino";
		} else if (header.indexOf("Konqueror") > 0) {
			return "Konqueror";
		} else {
			return "Others";
		}
	}

	/**
	 * 获取系统版本信息
	 *
	 * @param request
	 * @return
	 */
	public static String getRequestSystemInfo(HttpServletRequest request) {
		String systenInfo = null;
		String header = request.getHeader("user-agent");
		if (header == null || header.equals("")) {
			return "";
		}
		// 得到用户的操作系统
		if (header.indexOf("NT 10.0") > 0) {
			systenInfo = "Windows 10";
		} else if (header.indexOf("NT 6.0") > 0) {
			systenInfo = "Windows Vista/Server";
		} else if (header.indexOf("NT 5.2") > 0) {
			systenInfo = "Windows Server ";
		} else if (header.indexOf("NT 5.1") > 0) {
			systenInfo = "Windows XP";
		} else if (header.indexOf("NT 6.0") > 0) {
			systenInfo = "Windows Vista";
		} else if (header.indexOf("NT 6.1") > 0) {
			systenInfo = "Windows 7";
		} else if (header.indexOf("NT 6.2") > 0) {
			systenInfo = "Windows 8";
		} else if (header.indexOf("NT 6.3") > 0) {
			systenInfo = "Windows 8.1";
		} else if (header.indexOf("NT 5") > 0) {
			systenInfo = "Windows ";
		} else if (header.indexOf("NT 4") > 0) {
			systenInfo = "Windows NT4";
		} else if (header.indexOf("Me") > 0) {
			systenInfo = "Windows Me";
		} else if (header.indexOf("98") > 0) {
			systenInfo = "Windows 98";
		} else if (header.indexOf("95") > 0) {
			systenInfo = "Windows 95";
		} else if (header.indexOf("Mac") > 0) {
			systenInfo = "Mac";
		} else if (header.indexOf("Unix") > 0) {
			systenInfo = "UNIX";
		} else if (header.indexOf("Linux") > 0) {
			systenInfo = "Linux";
		} else if (header.indexOf("SunOS") > 0) {
			systenInfo = "SunOS";
		}
		return systenInfo;
	}

	/**
	 * 获取用户登陆信息 <br>
	 * <p>
	 * Description: <br>
	 * <p>
	 * Date: 2015年3月27日 上午10:21:08<br/>
	 * <p>
	 *
	 * @param request
	 * @return
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getUserInfo(HttpServletRequest request) {
		try {
			if (request.getSession().getAttribute("userInfo") != null) {
				Map<String,Object> userInfo = (Map<String,Object>) request.getSession()
						.getAttribute("userInfo");
				return userInfo;
			} else {
				return null;
			}
		} catch (NullPointerException e) {
			log.error("SystemUtils method getUserInfo error: ", e);
		}
		return null;
	}

}
