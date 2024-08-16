package com.inspur.dsp.direct.customfilter;

import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.direct.constant.Constants;
import com.inspur.dsp.direct.constant.HttpStatus;
import com.inspur.dsp.direct.domain.Resp;
import com.inspur.dsp.direct.enums.RespCode;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Component
public class UserInfoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 提前校验,用户是否过期
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        if (requestURI.contains("loginStatus") || requestURI.contains("login") || requestURI.contains("logout") || requestURI.contains("captcha")
                // 这个接口前端本地测试加不上cookie
                || requestURI.contains("/dataItem/join")
        ) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpSession session = request.getSession();
            Object dspSessionToken = session.getAttribute(Constants.DSP_SESSION_TOKEN);
            if (Objects.isNull(dspSessionToken)) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(HttpStatus.SUCCESS);
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JSONObject.toJSONString(Resp.fail("User expiration", RespCode.UNAUTHORIZED.getState())));
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}
