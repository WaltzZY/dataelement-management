package com.inspur.dsp.direct.console.test;


import com.inspur.dsp.direct.dao.user.UserOperationLogDao;
import com.inspur.dsp.direct.dbentity.user.UserOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple App.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private UserOperationLogDao auditLogUtilServiceDao;

    @Test
    public void test() {
        UserOperationLog userOperationLog = auditLogUtilServiceDao.selectById("00038df54ccc46419411b70a24953256");
        log.info("userOperationLog = {}", userOperationLog);
    }


    //    /**
//     * 加密内容,用于生成配置文件密码
//     */
//    @Test
//    public void encryption() {
//        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
//        System.out.println("当前加密方式: 加密类: BasicTextEncryptor, 加密算法: PBEWithMD5AndDES ");
//        // 1.设置秘钥
//        String salt = "DataServicePlatform";
//        textEncryptor.setPassword(salt);
//        // 2.加密
//        // 2.1加密内容
//        String pd = "123";
//        log.info("加密前:  {}", pd);
//        // 2.2加密操作
//        String pdAfterEncrypt = textEncryptor.encrypt(pd);
//        log.info("加密后:  {}", pdAfterEncrypt);
//        // 3.解密操作
//        String pdAfterDecrypt = textEncryptor.decrypt(pdAfterEncrypt);
//        log.info("解密后:  {}", pdAfterDecrypt);
//    }
}
