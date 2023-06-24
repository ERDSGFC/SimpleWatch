package com.qin.utils;

import java.util.regex.Pattern;

public class ConstVariable {

    private ConstVariable() {};

    // 正则 表达式
    public static final String REGEXP_NUMBER_GT_ZERO = "^[1-9]\\d*$"; // 普通数字大于零的整数

    public static final String REGEXP_NUMBER_GTE_ZERO = "^(\\d+)$"; // 普通数字大于等于零的整数

    public static final String REGEXP_RGB = "^#+(([0-9]|[a-f]|[A-F]){2}){3}$"; // RGB

    // 大于等于零的 两位小数
    public final static String REGEXP_MONEY = "^((0{1}\\.\\d{1,2})|([1-9]\\d*\\.{1}\\d{1,2})|([1-9]+\\d*)|0)$";
    // 时间正则 yyyy-MM-dd
    public static final String REGEXP_DATE = "^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])$";
    // 时间正则 HH:mm:ss
    public static final String REGEXP_TIME = "^(20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
    // 日期格式正则 yyyy-MM-dd HH:mm:ss
    public static final String REGEXP_DATE_TIME = "^\\d{4}-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
    // 十六进制
    public static final String REGEXP_0X = "^([0-9]|[a-f]|[A-F])*$";


    // ipv4 地址
    public static final String REGEXP_IPV4 = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";
    public static final String REGEXP_CRON = "^\\s*($|#|\\w+\\s*=|(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?(?:,(?:[0-5]?\\d)(?:(?:-|\\/|\\,)(?:[0-5]?\\d))?)*)\\s+(\\?|\\*|(?:[01]?\\d|2[0-3])(?:(?:-|\\/|\\,)(?:[01]?\\d|2[0-3]))?(?:,(?:[01]?\\d|2[0-3])(?:(?:-|\\/|\\,)(?:[01]?\\d|2[0-3]))?)*)\\s+(\\?|\\*|(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|\\/|\\,)(?:0?[1-9]|[12]\\d|3[01]))?(?:,(?:0?[1-9]|[12]\\d|3[01])(?:(?:-|\\/|\\,)(?:0?[1-9]|[12]\\d|3[01]))?)*)\\s+(\\?|\\*|(?:[1-9]|1[012])(?:(?:-|\\/|\\,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|\\/|\\,)(?:[1-9]|1[012]))?(?:L|W)?)*|\\?|\\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\\s+(\\?|\\*|(?:[0-6])(?:(?:-|\\/|\\,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|\\/|\\,|#)(?:[0-6]))?(?:L)?)*|\\?|\\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\\s)+(\\?|\\*|(?:|\\d{4})(?:(?:-|\\/|\\,)(?:|\\d{4}))?(?:,(?:|\\d{4})(?:(?:-|\\/|\\,)(?:|\\d{4}))?)*))$";

    public static final Pattern NUMBER_RGB_PATTERN = Pattern.compile(ConstVariable.REGEXP_RGB);
    public static final Pattern RE_0X_PATTERN = Pattern.compile(ConstVariable.REGEXP_0X);
    public static final Pattern NUMBER_GTE_ZERO_PATTERN = Pattern.compile(ConstVariable.REGEXP_NUMBER_GTE_ZERO);
}
