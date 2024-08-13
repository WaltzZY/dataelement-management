package com.inspur.dsp.direct.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public final static String YYYY = "yyyy";

    public final static String YYYY_MM = "yyyy-MM";

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private final static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private final static long MONTH = 12;

    public final static DateTimeFormatter defaultFormatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final static DateTimeFormatter formatterWithTime = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);


    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String dateMonth(final Date date) {
        return parseDateToStr(YYYY_MM, date);
    }
    public static final String DateToYearMonth (final Date date) {
        return parseDateToStr("yyyy年MM月", date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 获取年
     */
    public static final String getYear(Date date) {
        return DateFormatUtils.format(date, "yyyy");
    }

    /**
     * 获取当前年
     */
    public static final String getNowYear() {
        return DateFormatUtils.format(new Date(), "yyyy");
    }

    /**
     * 获取月
     */
    public static final String getMonth(Date date) {
        return DateFormatUtils.format(date, "MM");
    }

    /**
     * 获取当前月
     */
    public static final String getNowMonth() {
        return DateFormatUtils.format(new Date(), "MM");
    }

    /**
     * 获取日
     */
    public static final String getDay(Date date) {
        return DateFormatUtils.format(date, "dd");
    }

    /**
     * 获取指定年月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        //获取某月最小天数
        int lastDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中的月份，当月+1月-1天=当月最后一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    /**
     * 获取指定年月的第一天
     *
     * @param yearMonth
     * @return
     */
    public static Date getFirstDayOfMonthDate(String yearMonth) {
        String[] split = yearMonth.split("-");
        Integer year = Integer.valueOf(split[0]);
        Integer month = Integer.valueOf(split[1]);
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        return cal.getTime();
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param yearMonth
     * @return
     */
    public static Date getLastDayOfMonthDate(String yearMonth) {
        String[] split = yearMonth.split("-");
        Integer year = Integer.valueOf(split[0]);
        Integer month = Integer.valueOf(split[1]);
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        //获取某月最小天数
        int lastDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中的月份，当月+1月-1天=当月最后一天
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1);
        //格式化日期
        return cal.getTime();
    }

    public static String getFirstDayOfMonth(String yearMonth) {
        String[] split = yearMonth.split("-");
        Integer year = Integer.valueOf(split[0]);
        Integer month = Integer.valueOf(split[1]);
        return getFirstDayOfMonth(year, month);
    }

    public static String getLastDayOfMonth(String yearMonth) {
        String[] split = yearMonth.split("-");
        Integer year = Integer.valueOf(split[0]);
        Integer month = Integer.valueOf(split[1]);
        return getLastDayOfMonth(year, month);
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String getYyyyMmDd(Date now) {
        if (Objects.isNull(now)) {
            now = new Date();
        }
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String getYyyyMmDd(Date now, String format) {
        if (Objects.isNull(now)) {
            now = new Date();
        }
        return DateFormatUtils.format(now, format);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }
    /**
     * 计算相差天数 date2 - date1
     */
    public static Long getDays(Date date1, Date date2) {
        LocalDate startDate = LocalDate.parse(DateFormatUtils.format(date1, YYYY_MM_DD), defaultFormatterWithTime);
        LocalDate endDate = LocalDate.parse(DateFormatUtils.format(date2, YYYY_MM_DD), defaultFormatterWithTime);
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return days;
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 时间计算相差 多少分钟
     *
     * @return
     */
    public static long dateDiff(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime()) / 1000 / 60;
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 月份根据传入的 num进行加减
     *
     * @param str
     * @param num
     * @return
     */
    public static Date getPreMonth(Object str, Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(str));
        cal.add(cal.MONTH, num);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        String preMonth = dft.format(cal.getTime());
        return parseDate(preMonth);
    }

    /**
     * 月份根据传入的 num进行加减
     *
     * @param date
     * @param num
     * @return
     */
    public static Date datePreMonth(Date date, Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.MONTH, num);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String preMonth = dft.format(cal.getTime());
        return parseDate(preMonth);
    }

    /**
     * 判断日期是否大于本月15号
     *
     * @param date
     * @return
     */
    public static boolean isMoreThanThisMonth(Date date, String obj) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(obj));
        //设置为1号,当前日期既为本月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        cal.set(Calendar.MINUTE, 0);
        //将秒至0
        cal.set(Calendar.SECOND, 0);
        //将毫秒至0
        cal.set(Calendar.MILLISECOND, 0);
        return date.after(cal.getTime());
    }



    /**
     * 计算月数
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String monthCompare(String fromDate, String toDate) {
        if (StringUtils.isEmpty(fromDate) || StringUtils.isEmpty(toDate)) {
            return null;
        }
        LocalDate startDate = LocalDate.parse(fromDate, defaultFormatterWithTime);
        LocalDate endDate = LocalDate.parse(toDate, defaultFormatterWithTime);
        return String.valueOf(ChronoUnit.MONTHS.between(endDate, startDate));
    }

    /**
     * 获取当前日期的前一天
     * @author szf
     * @date  13:47
     * @param: date
     * @return: java.util.Date
     **/
    public static Date getThePreviousDay(Date date) {
        Date dBefore ;
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        return dBefore;
    }

    public static Date getFirstDay(Date date) {
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        dBefore = calendar.getTime();
        return dBefore;
    }

    public static Date getMaxDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime maxDateTime = localDateTime.with(LocalTime.MAX);
        return Date.from(maxDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean checkTaxFilingDate(Date salaryDate,Date taxFilingDate){
        //判断是否同一年
        if(!DateUtils.getYear(taxFilingDate).equals(DateUtils.getYear(salaryDate))){
            return false;
        }
        //判断历史报税年月小于等于当前报税年月
        if(salaryDate.compareTo(taxFilingDate) <= 0){
            return true;
        }else {
            return false;
        }
    }
}
