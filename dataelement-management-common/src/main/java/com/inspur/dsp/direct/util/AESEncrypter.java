package com.inspur.dsp.direct.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * AES工具类，提供加密解密、生成Key等方法
 *
 * @author jolestar
 */
public class AESEncrypter {
    private static final Logger logger = LoggerFactory.getLogger(AESEncrypter.class);

    /**
     * 这个值必须是 16/24/32 字节长度！下面给你一个正确的 128bit 密钥（16 字节）
     * 你也可以用 generateAesKey() 重新生成
     */
    private static final String AES_KEY_HEX = "0333d8f9e92822f5bc02887c7eeb210a"; // 16字节

    /**
     * 固定 16 字节 IV（前端必须一致）
     */
    private static final String AES_IV = "abcdef9876543210";

    private final SecretKey aesKey;
    private final IvParameterSpec ivSpec;

    private AESEncrypter() {
        this.aesKey = loadHexKey(AES_KEY_HEX);
        this.ivSpec = new IvParameterSpec(AES_IV.getBytes(StandardCharsets.UTF_8));
    }

    private static final AESEncrypter INSTANCE = new AESEncrypter();

    public static AESEncrypter getInstance() {
        return INSTANCE;
    }

    /** 加密（前端 crypto-js 可以解密） */
    public String encrypt(String msg) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encrypted);
        } catch (Exception e) {
            logger.error("AES 加密失败", e);
            throw new RuntimeException(e);
        }
    }

    /** 解密 */
    public String decryptAsString(String hexCipher) {
        try {
            byte[] cipherBytes = Hex.decodeHex(hexCipher.toCharArray());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("AES 解密失败", e);
            throw new RuntimeException(e);
        }
    }

    /** 加载 Hex 密钥 */
    private SecretKey loadHexKey(String hexStr) {
        try {
            byte[] keyBytes = Hex.decodeHex(hexStr.toCharArray());
            return new SecretKeySpec(keyBytes, "AES");
        } catch (DecoderException e) {
            logger.error("加载 AES Key 失败", e);
            throw new RuntimeException(e);
        }
    }

    /** 生成新的 AES Key（Hex 格式） */
    public static String generateAesKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128); // 16 字节 Key
            SecretKey key = kg.generateKey();
            return Hex.encodeHexString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
