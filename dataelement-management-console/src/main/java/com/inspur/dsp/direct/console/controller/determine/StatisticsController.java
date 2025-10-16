// StatisticsController.java
package com.inspur.dsp.direct.console.controller.determine;

import com.inspur.dsp.direct.annotation.RespAdvice;
import com.inspur.dsp.direct.entity.vo.HomeStatusNumVO;
import com.inspur.dsp.direct.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/getHomeStatusNum")
    @RespAdvice
    public HomeStatusNumVO getHomeStatusNum() {
        return statisticsService.getHomeStatusNum();
    }
}