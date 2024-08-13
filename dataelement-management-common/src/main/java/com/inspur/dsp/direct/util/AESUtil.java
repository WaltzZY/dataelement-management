package com.inspur.dsp.direct.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

/**
 * AES 对称加密算法
 */
public class AESUtil {

    private static Log log = LogFactory.getLog(AESUtil.class);
    private static String aes_key_path = "aes.key";

    // 加载密钥文件
    private static SecretKey privateKey = null;

    static {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(aes_key_path)) {
            byte[] bytes = toByteArray(inputStream);
            privateKey = new SecretKeySpec(bytes, "aes");
        } catch (FileNotFoundException e) {
            log.error("未找到密钥文件：" + aes_key_path, e);
        } catch (IOException e) {
            log.error("读取密钥文件出错＿" + aes_key_path, e);
        }
    }

    /**
     * AES加密
     *
     * @param content
     * @param key
     * @return String
     */
    public static String encode(String content, SecretKey key) {
        try {
            // 加密大于块的数据时，避免使用 ECB 和 CBC 操作模式。CBC 模式效
            // 率较低，并且在和 SSL 一起使用时会造成严重风险 [1]。请改用 CCM
            // (Counter with CBC-MAC) 模式，或者如果更注重性能，则使用 GCM
            //（Galois/Counter Mode）模式（如可用）。
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(b);
        } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                 NoSuchAlgorithmException e) {
            log.error("aes加密出错: ", e);
        }
        return null;
    }

    // 使用密钥文件中的密钥进行加密解密
    public static String encode(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        } else {
            return encode(content, privateKey);
        }

    }

    public static String decode(String miwen) {
        if (StringUtils.isEmpty(miwen)) {
            return "";
        } else {
            return decode(miwen, privateKey);
        }

    }

//    public static void main(String[] args) {
//        System.out.println(decode(decode("otx8gJE9Mem5aYnY/nZpp9QEICNSzswOjlnQCw3kBi8=")));
//    }

    /**
     * AES解密
     *
     * @param miwen
     * @param key
     * @return String
     */
    public static String decode(String miwen, SecretKey key) {
        try {
            // 加密大于块的数据时，避免使用 ECB 和 CBC 操作模式。CBC 模式效
            // 率较低，并且在和 SSL 一起使用时会造成严重风险 [1]。请改用 CCM
            // (Counter with CBC-MAC) 模式，或者如果更注重性能，则使用 GCM
            //（Galois/Counter Mode）模式（如可用）。
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.decodeBase64(miwen)), StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                 NoSuchAlgorithmException e) {
            log.error("解密" + miwen + "出错", e);
        }
        return miwen;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
