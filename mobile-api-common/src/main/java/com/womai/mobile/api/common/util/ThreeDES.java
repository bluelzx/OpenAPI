package com.womai.mobile.api.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 13-6-19
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public class ThreeDES {


    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish

    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 对称密钥加密并做base64转码
     *
     * @param originalKeyString
     * @param data
     * @return
     * @throws Exception
     */
    public static String orginalEncoded(String originalKeyString, String data) throws Exception {
        // 3des加密后的密文
        byte[] encoded = ThreeDES.encryptMode(originalKeyString.getBytes(),
                data.getBytes());
        // base64 编码 发送
        String base64str = new String(Base64.encodeBase64(encoded), "UTF-8");
        return base64str;
    }

    /**
     * 先base64再解密
     *
     * @param duichenKey
     * @param datas
     * @return
     */
    public static String orginalDecryptMode(String duichenKey, String datas) {
        //解密
        byte[] Miwen = Base64.decodeBase64(datas.getBytes());

        byte[] srcBytes = ThreeDES.decryptMode(duichenKey.getBytes(), Miwen);
        return new String(srcBytes);
    }

    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) hs = hs + "0" + stmp;
            else hs = hs + stmp;
            if (n < b.length - 1) hs = hs + ":";
        }
        return hs.toUpperCase();
    }
}
