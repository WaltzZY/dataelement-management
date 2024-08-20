package com.inspur.dsp.direct.console.controller.directManage;

import com.alibaba.fastjson.JSONObject;
import com.inspur.dsp.common.web.controller.BaseController;
import com.inspur.dsp.direct.httpService.BspService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/dict")
@Controller
public class DictController extends BaseController {

    private Log log = LogFactory.getLog(DictController.class);

    @Autowired
    private BspService bspService;

    @RequestMapping("/getDictInfo")
    @ResponseBody
    public void getDictInfo(String kind){
        JSONObject dictInfo = bspService.getDictInfo(kind);
        this.renderJson(dictInfo.toJSONString());
    }



}
