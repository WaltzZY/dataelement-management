package com.inspur.dsp.direct.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author catalog
 */
@Slf4j
public class CTools {
    /**
     * 计数器
     */
    private static int stringCount = 1000;
    /**
     * 进制
     */
    private static final int RADIX = 16;
    /**
     * 密钥
     */
    private static final String SEED = "0933910847463829232312312";
    /**
     * 字节转化为字符串
     * @param bytes
     * @return
     */
    public static String bytesToString(byte[] bytes) {
        if ((bytes == null) || (bytes.length < 1))
            return null;
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("CTools method bytesToString error: ");
        }
        return "";
    }
    /**
     * InputStream 转化为 Byte[]
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch = 0;
        byte[] imgdata = null;
        byte[] b = new byte[1024];
        //读取输入流
        while ((ch = is.read(b)) != -1) {
            //转为输出流
            bytestream.write(b,0,ch);
        }
        //输出流转为数组
        imgdata = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
    /**
     * Byte 转化为 InputStream
     * @return
     */
    public static InputStream byteToInputStream(byte[] data){
        InputStream is = new ByteArrayInputStream(data);
        return is;
    }
    /**
     * 获取前端状态
     * @param status
     * @return
     */
    public static String getStatus(Object status){
        if(status!=null&&status.equals("on")){
            return "1";
        }
        return "0";
    }
    /**
     * 获取前端是否发布成绩
     * @param status
     * @return
     */
    public static String getIsPublis(Object is_publish){
        if(is_publish!=null&&is_publish.equals("on")){
            return "1";
        }
        return "0";
    }
    /**
     * 获取5位排序
     * @param order
     * @return
     */
    public static String getSortOrder(Object order){
        StringBuffer result = new StringBuffer("00001");

        if(order!=null){
            result = new StringBuffer();
            String or = String.valueOf(order);
            if(or.length()<5){
                for(int i=0;i<5-or.length();i++){
                    result.append("0");
                }
            }
            return result.append(or).toString();
        }
        return result.toString();
    }
    /**
     * 获取MD5加密字符串[密码]
     * @param source
     * @return
     */
    public static String getMD5(String source){
        if(source==null){
            return "";
        }else{
            return getMD5(source.getBytes());
        }
    }

    /**
     * MD5
     * @param source
     * @return
     */
    public static String getMD5(byte[] source) {
        String s = null;
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();

            char[] str = new char[32];

            int k = 0;
            for (int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];

                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            log.error("CTools method getMD5 error: ");
        }
        return s;
    }
    /**
     * 异或加密
     * @param source
     * @return
     */
    public static final String encrypt(String source) {
        if (source == null)
            return "";
        if (source.length() == 0)
            return "";
        BigInteger bi_passwd = new BigInteger(source.getBytes());
        BigInteger bi_r0 = new BigInteger(SEED);
        BigInteger bi_r1 = bi_r0.xor(bi_passwd);
        return bi_r1.toString(RADIX);
    }
    /**
     * 异或解密
     * @param source
     * @return
     */
    public static final String decrypt(String source) {
        if (source == null)
            return "";
        if (source.length() == 0)
            return "";
        BigInteger bi_confuse = new BigInteger(SEED);

        BigInteger bi_r1 = new BigInteger(source, RADIX);
        BigInteger bi_r0 = bi_r1.xor(bi_confuse);

        return new String(bi_r0.toByteArray(), Charset.defaultCharset());

    }
    /**
     * 获取登录字符串
     * @return
     */
    public static final String getLoginSQL(String username){
        String sql = "select t.id,t.code,t.account,t.password,t.gender,t.birthday,t.name,t.identity_num,t.phone,t.mobile,t.email,t.photo,t.is_lawer,t.region_code,t.region_name,t.org_code,t.org_name,t.agent_code,t.agent_name,t.power_code,t.role_code,t.position,t.style,t.tip_way"
                +" from pub_user t where 1=1";
        if(isEmail(username)){//邮箱
            sql += " and t.email=?";
        }else if(isMobile(username)){
            sql += " and t.mobile=?";
        }else{
            sql += " and t.account=?";
        }
        return sql;
    }
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles){
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    /**
     * 验证是否邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        //Pattern pattern = Pattern.compile("^\\w+([-.]\w+)*@\w+([-]\w+)*\.(\w+([-]\w+)*\.)*[a-z]{2,3}$");
        //Matcher matcher = pattern.matcher(email);
        //if (matcher.matches()) {
        if(StringUtils.isNotBlank(email)&&email.indexOf("@")!=-1){
            return true;
        }
        return false;
    }
    /**
     * 验证是否日期
     * @param date
     * @param format
     * @return
     */
    public static boolean isDate(String date,String format){
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try{
            d = df.parse(date);
        }catch(ParseException e){
            return false;
        }
        String s1 = df.format(d);
        return date.equals(s1);
    }
    /**
     * 验证是否日期类型
     * @param strDate
     * @return
     */
    public static boolean isDate(String strDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try{
            df.parse(strDate);
        }catch(ParseException e){
            log.error("CTools method isDate error: ");
            return false;
        }
        return true;
    }
    /**
     * 验证是否数字
     * @param str
     * @return
     */
    public boolean isNumber(String str){
        Pattern pattern=Pattern.compile("[0-9]*");
        Matcher isNum=pattern.matcher(str);
        if(isNum.matches()){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 从身份证号里面判断性别
     * @param idCard
     * @return
     */
    public static String getSexFromIdCard(String idCard){
        String sex = "";
        if(idCard.length()==15){
            sex = idCard.substring(idCard.length()-3,idCard.length());
        }
        if(idCard.length()==18){
            sex = idCard.substring(idCard.length()-4,idCard.length()-1);
        }
        //System.out.println(sex);
        int sexNum = Integer.parseInt(sex)%2;
        if(sexNum == 0){
            return "女";
        }
        return "男";
    }
    /**
     * 从身份证号中提取出生日期
     * @param idCard
     * @return
     */
    public String getBirthdayFromIdCard(String idCard){
        String Ain = "";
        if(idCard.length()==18){
            Ain=idCard.substring(0,17);
        } else if(idCard.length()==15){
            Ain=idCard.substring(0,6)+"19"+idCard.substring(6,15);
        }
        //================ 出生年月是否有效 ================
        String strYear =Ain.substring(6 ,10);//年份
        String strMonth=Ain.substring(10,12);//月份
        String strDay        =Ain.substring(12,14);//日期
        return strYear+"-"+strMonth+"-"+strDay;
    }
    /**
     * 获取区划编码
     * @return
     */
    public static Hashtable<String,String> getAreaCode(){
        Hashtable<String,String> hashtable=new Hashtable<String,String>();
        hashtable.put("11","北京");
        hashtable.put("12","天津");
        hashtable.put("13","河北");
        hashtable.put("14","山西");
        hashtable.put("15","内蒙古");
        hashtable.put("21","辽宁");
        hashtable.put("22","吉林");
        hashtable.put("23","黑龙江");
        hashtable.put("31","上海");
        hashtable.put("32","江苏");
        hashtable.put("33","浙江");
        hashtable.put("34","安徽");
        hashtable.put("35","福建");
        hashtable.put("36","江西");
        hashtable.put("37","山东");
        hashtable.put("41","河南");
        hashtable.put("42","湖北");
        hashtable.put("43","湖南");
        hashtable.put("44","广东");
        hashtable.put("45","广西");
        hashtable.put("46","海南");
        hashtable.put("50","重庆");
        hashtable.put("51","四川");
        hashtable.put("52","贵州");
        hashtable.put("53","云南");
        hashtable.put("54","西藏");
        hashtable.put("61","陕西");
        hashtable.put("62","甘肃");
        hashtable.put("63","青海");
        hashtable.put("64","宁夏");
        hashtable.put("65","新疆");
        hashtable.put("71","台湾");
        hashtable.put("81","香港");
        hashtable.put("82","澳门");
        hashtable.put("91","国外");
        return hashtable;
    }
    /**
     * 验证身份证是否有效
     * @param IDStr
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("unused")
    public boolean IDCardValidate(String IDStr) throws ParseException{
        String errorInfo = "";//记录错误信息
        String[] ValCodeArr = {"1","0","x","9","8","7","6","5","4","3","2"};
        String[] Wi = {"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"};
        //String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
        String Ai="";

        //================ 号码的长度 15位或18位 ================
        if(IDStr.length()!=15 && IDStr.length()!=18){
//            errorInfo="号码长度应该为15位或18位。";
            //System.out.println(errorInfo);
            return false;
        }
        //=======================(end)========================


        //================ 数字 除最后以为都为数字 ================
        if(IDStr.length()==18){
            Ai=IDStr.substring(0,17);
        }
        else if(IDStr.length()==15){
            Ai=IDStr.substring(0,6)+"19"+IDStr.substring(6,15);
        }
        if(isNumber(Ai)==false){
//            errorInfo="15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            //System.out.println(errorInfo);
            return false;
        }
        //=======================(end)========================

        //================ 出生年月是否有效 ================
        String strYear =Ai.substring(6 ,10);//年份
        String strMonth=Ai.substring(10,12);//月份
        String strDay        =Ai.substring(12,14);//月份

        if(isDate(strYear+"-"+strMonth+"-"+strDay)==false){
//            errorInfo="生日无效。";
            //System.out.println(errorInfo);
            return false;
        }

        GregorianCalendar gc=new GregorianCalendar();
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        if((gc.get(Calendar.YEAR)-Integer.parseInt(strYear))>150 || (gc.getTime().getTime()-s.parse(strYear+"-"+strMonth+"-"+strDay).getTime())<0){
//            errorInfo="生日不在有效范围。";
            //System.out.println(errorInfo);
            return false;
        }
        if(Integer.parseInt(strMonth)>12 || Integer.parseInt(strMonth)==0){
//            errorInfo="月份无效";
            //System.out.println(errorInfo);
            return false;
        }
        if(Integer.parseInt(strDay)>31 || Integer.parseInt(strDay)==0){
//            errorInfo="日期无效";
            //System.out.println(errorInfo);
            return false;
        }
        //=====================(end)=====================


        //================ 地区码时候有效 ================
        Hashtable<String,String> h=getAreaCode();
        if(h.get(Ai.substring(0,2))==null){
            //errorInfo="地区编码错误。";
            //System.out.println(errorInfo);
            return false;
        }
        //==============================================


        //================ 判断最后一位的值 ================
        int TotalmulAiWi=0;
        for(int i=0 ; i<17 ; i++){
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue=TotalmulAiWi % 11;
        String strVerifyCode=ValCodeArr[modValue];
        Ai=Ai+strVerifyCode;

        if(IDStr.length()==18){
            if(Ai.equals(IDStr)==false){
//                errorInfo="身份证无效，最后一位字母错误";
                //System.out.println(errorInfo);
                //return false;
            }
        }else{
            //System.out.println("所在地区:"+h.get(Ai.substring(0,2).toString()));
            //System.out.println("新身份证号:"+Ai);
            return true;
        }
        //=====================(end)=====================
        //System.out.println("所在地区:"+h.get(Ai.substring(0,2).toString()));
        return true;
    }
    /**
     * 获取登录资源SQL
     * @return
     */
    public static final String getResourceSQL(boolean isAdmin,String role){
        String sql = "select distinct rs.code,rs.name,rs.parent_code,rs.path,rs.data,rs.icon,rs.res_path,rs.sort_order from pub_resource rs, pub_role_resource rr where rs.code=rr.res_code and rs.status='1'";
        if(isAdmin == false){
            String[] roles = role.split(",");
            StringBuffer sb = new StringBuffer();
            for(String re : roles){
                sb.append("'").append(re).append("',");
            }
            role = sb.substring(0,sb.length()-1);
            sql+=" and rr.role_code in("+role+")";
        }else{
            sql = "select rs.code,rs.name,rs.parent_code,rs.path,rs.data,rs.res_path from pub_resource rs where rs.status='1'";
        }
        sql +=" order by rs.parent_code,rs.sort_order";
        return sql;
    }
    /**
     * 生成ID
     * @return
     */
    public static synchronized String getNewID()
    {
        if (stringCount >= 9999) {
            stringCount = 1000;
        }
        StringBuffer sb = new StringBuffer(20);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(format.format(new Date()));
        sb.append(stringCount++);
        sb.append("00");
        return sb.toString();
    }

    /**
     * 生成主键
     * @param key
     * @return
     */
    public static synchronized String getNewID(String key){
        if (stringCount >= 9999) {
            stringCount = 1000;
        }
        StringBuffer sb = new StringBuffer(20);
        if(StringUtils.isNotBlank(key)){
            sb.append(key);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(format.format(new Date()));
        sb.append(stringCount++);
        sb.append("00");
        return sb.toString();
    }
    /**
     * 获取主键
     * @return
     */
    public static String getUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        s = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
        return s.toUpperCase();
    }
    /**
     * 获取主键
     * @return
     */
    public static String getID(){
        String s = UUID.randomUUID().toString();
        return s;
    }
    /**
     * 生成cookie值
     * @return
     */
    public static String getCookieKey(){
        return "COOKIE"+CTools.getUUID()+"$";
    }
    /**
     * 去除List里相同的object
     *
     * @param listIn
     *          ArrayList
     * @return ArrayList
     */
    public static ArrayList<Object> filterList(ArrayList<Object> listIn) {
        ArrayList<Object> listOut = new ArrayList<Object>();
        for (int i = 0; i < listIn.size(); i++) {
            if (!listOut.contains(listIn.get(i))) {
                listOut.add(listIn.get(i));
            }
        }
        return listOut;
    }
    /**
     * Clob类型转String`
     * @param baseValue
     * @return
     */
    public static String clob2string(Object baseValue) {
        String strBaseValue = null;
        if (baseValue instanceof Clob) {
            Clob c = (Clob) baseValue;
            try {
                if (c.length()>0) {
                    strBaseValue = c.getSubString(1, (int) c.length());
                }
            } catch (SQLException e) {
                log.error("CTools method clob2string error: ");
            }
        }
        return strBaseValue;
    }
    /**
     * 将 List 集合中的字符转换为 'value1','value2','value3'...，用于 SQL 查询
     *
     * @param list
     *          AbstractList
     * @return String
     */
    public static String listToString(AbstractList<String> list) {
        StringBuffer sReturn = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sReturn.append("'").append(String.valueOf(list.get(i))).append("',");
        }
        if (sReturn.length() > 0) {
            return sReturn.substring(0, sReturn.length() - 1);
        } else {
            return "''";
        }
    }

    /**
     * 将字符数组中的字符转换为 'value1','value2','value3'...，用于 SQL 查询
     *
     * @param args
     * @return
     */
    public static String arrayToString(String[] args) {
        StringBuffer sReturn = new StringBuffer();
        sReturn.append("(");
        for (int i = 0; i < args.length; i++) {
            sReturn.append("'").append(args[i]).append("',");
        }
        return sReturn.substring(0, sReturn.length() - 1) + ")";

    }
    /**
     * 完整的判断中文汉字和符号
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        //服务器端生成随机字符串原文并放入到session中
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 写cookie
     *
     * @param name
     * @param value
     * @param maxAge
     *            指定cookie多长时间后过期，单位秒（正数表示多长时间后过期，0表示要删除该cookie，负数表示该cookie不会持久化
     *            ，浏览器关闭时，则删除该cookie）
     * @param path
     *            设置cookie路径，在特定path下的Cookie只能被该path以及子目录下的读取，如果需要整个应用共享，
     *            可以设置path=/
     * @param domain
     *            设置cookie域名，如www.test.com,.test.com
     * @param response
     * @return
     */
    public static boolean setCookie(String name, String value, int maxAge,
                                    String path, String domain, HttpServletResponse response) {
        if (value != null) {
            try {
                value = URLEncoder.encode(value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error("CTools method setCookie error: ");
            }
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(false);// 设置为true，则仅有https请求时，cookie才会被发送。
        response.addCookie(cookie);
        return true;
    }
    /**
     * 获取指定位数的随机数（不保证随机数的唯一性）
     *
     * @param length
     *            随机数的位数
     * @return String
     */
    public static String getRandom(int length) {
        if (length <= 0) {
            length = 1;
        }
        StringBuilder num = new StringBuilder();
        int max = 10;
        SecureRandom secureRandom = new SecureRandom();
        int i = 0;
        while (i < length) {
            num.append(getSecureRandom(secureRandom, max));
            i++;
        }
        return num.toString();
    }

    private static int getSecureRandom(SecureRandom secureRandom, int max) {
        // 但是，我们可以使用SecureRandom生成字节并转换为整数
        byte[] randomByte = new byte[4]; // 4字节（32位）足以存储一个int
        secureRandom.nextBytes(randomByte);
        int randomIntFromBytes = ((randomByte[0] & 0xFF) << 24) |
                ((randomByte[1] & 0xFF) << 16) |
                ((randomByte[2] & 0xFF) << 8)  |
                (randomByte[3] & 0xFF);
        // 如果需要限制随机数的范围，可以进一步处理
        return Math.abs(randomIntFromBytes) % max;
    }

    /**
     * 主函数
     * @param args
     */
	/*public static void main(String[] args){
		System.out.println(getNewID());
		String phone = "15019239901";
		System.out.println("15019239901 encrypt password is "+encrypt(phone));
		System.out.println(decrypt(encrypt(phone)));
		String email = "15019239901@139.com";
		System.out.println("15019239901 encrypt password is "+encrypt(email));
		System.out.println(decrypt(encrypt(email)));
	}*/

    /**
     * 写cookie - 确保前端可访问版本
     *
     * @param name     Cookie名称
     * @param value    Cookie值
     * @param maxAge   Cookie有效期（秒），-1表示会话Cookie
     * @param path     Cookie路径，默认为"/"
     * @param response HttpServletResponse对象
     * @return boolean 设置是否成功
     */
    public static boolean setFrontendAccessibleCookie(String name, String value, int maxAge,
                                                      String path, HttpServletResponse response) {
        if (name == null || name.isEmpty()) {
            log.warn("Cookie name is null or empty");
            return false;
        }

        try {
            // URL编码处理
            if (value != null) {
                value = URLEncoder.encode(value, "UTF-8");
            }

            Cookie cookie = new Cookie(name, value);

            // 关键设置：允许前端JavaScript访问
            cookie.setHttpOnly(false);

            // 设置过期时间
            cookie.setMaxAge(maxAge);

            // 设置路径，默认为根路径
            cookie.setPath(path != null && !path.isEmpty() ? path : "/");

            // 根据请求协议设置Secure属性（需要HttpServletRequest参数支持）
            // cookie.setSecure(request.isSecure());
            cookie.setSecure(false); // 默认设为false以支持HTTP环境

            // 不显式设置Domain，让浏览器使用当前域名

            response.addCookie(cookie);
            return true;

        } catch (UnsupportedEncodingException e) {
            log.error("设置Cookie时URL编码失败: {}", name, e);
            return false;
        }
    }

}


