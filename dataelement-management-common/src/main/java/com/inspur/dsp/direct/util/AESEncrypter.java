package com.inspur.dsp.direct.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * AES工具类，提供加密解密、生成Key等方法
 *
 * @author jolestar
 */
public class AESEncrypter {
    /**
     * 日志信息
     */
    private static final Logger logger = LoggerFactory.getLogger(AESEncrypter.class);

    private static final String AESKEYSTR = "ZTZjZGVjZDcxNmMwMWQzZTIzOWE4ZjNkZjk3ZTJiZTM=";

    private final SecretKey aesKey;

    private AESEncrypter() {
        aesKey = loadAesKey();
    }

    private AESEncrypter(String aes) {
        aesKey = loadAesKey(aes);
    }

    // 私有的静态实例，在类加载时就完成了实例化
    private static final AESEncrypter INSTANCE = new AESEncrypter();

    private static final Map<String, AESEncrypter> INSTANCES = new HashMap<>();

    public static AESEncrypter getInstance() {
        return INSTANCE;
    }

    public synchronized static AESEncrypter getInstance(String aes) {
        if (INSTANCES.getOrDefault(aes, null) == null) {
            INSTANCES.put(aes, new AESEncrypter(aes));
        }
        return INSTANCES.get(aes);
    }

    public String encrypt(String msg) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        try {
            Cipher ecipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return Hex.encodeHexString(ecipher.doFinal(msg.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            String errMsg = "decrypt error, data:" + msg;
            logger.error(errMsg);
            throw e;
        }


    }

    public byte[] decrypt(char[] msgCharArray) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException {
        try {
            Cipher dcipher = Cipher.getInstance("AES");
            dcipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return dcipher.doFinal(Hex.decodeHex(msgCharArray));
        } catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | DecoderException e) {
            String errMsg = "decrypt error, data:" + Arrays.toString(msgCharArray);
            logger.error(errMsg);
            throw e;
        }


    }

    public String decryptAsString(char[] msgCharArray) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException {
        return new String(this.decrypt(msgCharArray), Charset.defaultCharset());
    }

    private static SecretKey loadAesKey() {
        return loadAesKey(AESKEYSTR);
    }

    private static SecretKey loadAesKey(String aesKeyStr) {
        String buffer = new String(Base64.decodeBase64(aesKeyStr));
        byte[] keyStr = new byte[0];
        try {
            keyStr = Hex.decodeHex(buffer.toCharArray());
        } catch (DecoderException e) {
            logger.error("初始化aes加密工具失败:", e);
        }
        return new SecretKeySpec(keyStr, "AES");
    }

//	private static String generateAesKey() throws Exception {
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128);
//            // 产生密钥
//            SecretKey secretKey = keyGenerator.generateKey();
//            // 获取密钥
//            byte[] keyBytes = secretKey.getEncoded();
//            return Base64.encodeBase64String(Hex.encodeHexString(keyBytes).getBytes());
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception(e);
//        }
//    }
}
