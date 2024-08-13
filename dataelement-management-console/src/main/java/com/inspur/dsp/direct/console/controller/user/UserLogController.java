package com.inspur.dsp.direct.console.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspur.dsp.common.web.controller.BaseController;
import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.annotation.SysLog;
import com.inspur.dsp.direct.dao.user.UserOperationLogDao;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/userlog")
@RequiredArgsConstructor
public class UserLogController extends BaseController {

    private final UserOperationLogDao userOperationLogDao;

    @GetMapping("/test")
    // 响应增强注解,会自动包装返回对象
    @RespAdvice
    // 用户操作日志记录注解,会自动记录方法操作日志
    @SysLog(title = "测试页面", modelName = "测试接口")
    public String addUserLog() {
        // 获取用户信息
        JSONObject currentUser = super.getCurrentUser();
        Page<UserOperationLog> logPage = new Page<>();
        logPage.setSize(10);
        logPage.setCurrent(1);
        IPage<UserOperationLog> list = userOperationLogDao.getList(logPage);
        List<UserOperationLog> records = list.getRecords();
        log.info("records = " + records);
        return "test";
    }
}
