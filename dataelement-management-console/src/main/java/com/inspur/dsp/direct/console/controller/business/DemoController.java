package com.inspur.dsp.direct.console.controller.business;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    @GetMapping("/test")
    // 响应增强注解,会自动包装返回对象
    @RespAdvice
    // 用户操作日志记录注解,会自动记录方法操作日志
    @SysLog(title = "测试页面", modelName = "测试接口")
    public String test() {
        return "测试页面";
    }
}
