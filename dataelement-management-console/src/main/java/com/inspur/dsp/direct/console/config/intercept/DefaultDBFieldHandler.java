package com.inspur.dsp.direct.console.config.intercept;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.inspur.dsp.direct.constant.Constants;
import com.inspur.dsp.direct.domain.UserInfo;
import com.inspur.dsp.direct.util.ServletUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class DefaultDBFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        UserInfo userInfo = ServletUtils.getUserInfo();
        String account = userInfo != null ? userInfo.getACCOUNT() : Constants.DEFAULT_USER_ACCOUNT;
        this.strictInsertFill(metaObject, "createUserId",() ->  account, String.class);
        this.strictInsertFill(metaObject, "modifyUserId", () -> account, String.class);
        this.strictInsertFill(metaObject, "createDate", Date::new, Date.class);
        this.strictInsertFill(metaObject, "modifyDate", Date::new, Date.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserInfo userInfo = ServletUtils.getUserInfo();
        String account = userInfo != null ? userInfo.getACCOUNT() : Constants.DEFAULT_USER_ACCOUNT;
        this.strictUpdateFill(metaObject, "modifyUserId", () ->  account, String.class);
        this.strictUpdateFill(metaObject, "modifyDate", Date::new, Date.class);
    }

}
