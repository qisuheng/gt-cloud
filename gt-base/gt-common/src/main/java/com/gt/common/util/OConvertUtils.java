package com.gt.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * 功能描述：
 *
 * @Author: qisuheng
 * @Date: 2022/12/26 15:57
 */
public class OConvertUtils {
    private static final Logger log = LoggerFactory.getLogger(OConvertUtils.class);

    public OConvertUtils() {
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if ("".equals(object)) {
            return true;
        } else {
            return "null".equals(object);
        }
    }

    public static boolean isNotEmpty(Object object) {
        return object != null && !"".equals(object) && !object.equals("null");
    }

    public static String decode(String strIn, String sourceCode, String targetCode) {
        String temp = code2code(strIn, sourceCode, targetCode);
        return temp;
    }

    public static String StrToUTF(String strIn, String sourceCode, String targetCode) {
        strIn = "";

        try {
            strIn = new String(strIn.getBytes("ISO-8859-1"), "GBK");
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

        return strIn;
    }

    private static String code2code(String strIn, String sourceCode, String targetCode) {
        String strOut = null;
        if (strIn != null && !"".equals(strIn.trim())) {
            try {
                byte[] b = strIn.getBytes(sourceCode);

                for(int i = 0; i < b.length; ++i) {
                    System.out.print(b[i] + "  ");
                }

                strOut = new String(b, targetCode);
                return strOut;
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        } else {
            return strIn;
        }
    }

    public static int getInt(String s, int defval) {
        if (s != null && s != "") {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var3) {
                return defval;
            }
        } else {
            return defval;
        }
    }

    public static int getInt(String s) {
        if (s != null && s != "") {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var2) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static int getInt(String s, Integer df) {
        if (s != null && s != "") {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException var3) {
                return 0;
            }
        } else {
            return df;
        }
    }

    public static Integer[] getInts(String[] s) {
        if (s == null) {
            return null;
        } else {
            Integer[] integer = new Integer[s.length];

            for(int i = 0; i < s.length; ++i) {
                integer[i] = Integer.parseInt(s[i]);
            }

            return integer;
        }
    }

    public static double getDouble(String s, double defval) {
        if (s != null && s != "") {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException var4) {
                return defval;
            }
        } else {
            return defval;
        }
    }

    public static double getDou(Double s, double defval) {
        return s == null ? defval : s;
    }

    public static int getInt(Object object, int defval) {
        if (isEmpty(object)) {
            return defval;
        } else {
            try {
                return Integer.parseInt(object.toString());
            } catch (NumberFormatException var3) {
                return defval;
            }
        }
    }

    public static Integer getInt(Object object) {
        if (isEmpty(object)) {
            return null;
        } else {
            try {
                return Integer.parseInt(object.toString());
            } catch (NumberFormatException var2) {
                return null;
            }
        }
    }

    public static int getInt(BigDecimal s, int defval) {
        return s == null ? defval : s.intValue();
    }

    public static Integer[] getIntegerArry(String[] object) {
        int len = object.length;
        Integer[] result = new Integer[len];

        try {
            for(int i = 0; i < len; ++i) {
                result[i] = new Integer(object[i].trim());
            }

            return result;
        } catch (NumberFormatException var4) {
            return null;
        }
    }

    public static String getString(String s) {
        return getString(s, "");
    }

    public static String getString(Object object) {
        return isEmpty(object) ? "" : object.toString().trim();
    }

    public static String getString(int i) {
        return String.valueOf(i);
    }

    public static String getString(float i) {
        return String.valueOf(i);
    }

    public static String getString(String s, String defval) {
        return isEmpty(s) ? defval : s.trim();
    }

    public static String getString(Object s, String defval) {
        return isEmpty(s) ? defval : s.toString().trim();
    }

    public static long stringToLong(String str) {
        Long test = new Long(0L);

        try {
            test = Long.valueOf(str);
        } catch (Exception var3) {
        }

        return test;
    }

    public static String getIp() {
        String ip = null;

        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (UnknownHostException var2) {
            var2.printStackTrace();
        }

        return ip;
    }

    private static boolean isBaseDataType(Class clazz) throws Exception {
        return clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.isPrimitive();
    }

    public static String getIpAddrByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getRealIp() throws SocketException {
        String localip = null;
        String netip = null;
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;

        while(netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
            Enumeration address = ni.getInetAddresses();

            while(address.hasMoreElements()) {
                ip = (InetAddress)address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                }

                if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    localip = ip.getHostAddress();
                }
            }
        }

        return netip != null && !"".equals(netip) ? netip : localip;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            String reg = "\\s*|\t|\r|\n";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }

    public static boolean isIn(String substring, String[] source) {
        if (source != null && source.length != 0) {
            for(int i = 0; i < source.length; ++i) {
                String aSource = source[i];
                if (aSource.equals(substring)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static Map<Object, Object> getHashMap() {
        return new HashMap(5);
    }

    public static Map<Object, Object> setToMap(Set<Object> setobj) {
        Map<Object, Object> map = getHashMap();
        Iterator iterator = setobj.iterator();

        while(iterator.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry)iterator.next();
            map.put(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue().toString().trim());
        }

        return map;
    }

    public static boolean isInnerIp(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        String localIp = "127.0.0.1";
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || localIp.equals(ipAddress);
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = (long)Integer.parseInt(ip[0]);
        long b = (long)Integer.parseInt(ip[1]);
        long c = (long)Integer.parseInt(ip[2]);
        long d = (long)Integer.parseInt(ip[3]);
        long ipNum = a * 256L * 256L * 256L + b * 256L * 256L + c * 256L + d;
        return ipNum;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return userIp >= begin && userIp <= end;
    }

    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            if (!name.contains("_")) {
                return name.substring(0, 1).toLowerCase() + name.substring(1).toLowerCase();
            } else {
                String[] camels = name.split("_");
                String[] var3 = camels;
                int var4 = camels.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String camel = var3[var5];
                    if (!camel.isEmpty()) {
                        if (result.length() == 0) {
                            result.append(camel.toLowerCase());
                        } else {
                            result.append(camel.substring(0, 1).toUpperCase());
                            result.append(camel.substring(1).toLowerCase());
                        }
                    }
                }

                return result.toString();
            }
        } else {
            return "";
        }
    }

    public static String camelNames(String names) {
        if (names != null && !"".equals(names)) {
            StringBuffer sf = new StringBuffer();
            String[] fs = names.split(",");
            String[] var3 = fs;
            int var4 = fs.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String field = var3[var5];
                field = camelName(field);
                sf.append(field + ",");
            }

            String result = sf.toString();
            return result.substring(0, result.length() - 1);
        } else {
            return null;
        }
    }

    public static String camelNameCapFirst(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            if (!name.contains("_")) {
                return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            } else {
                String[] camels = name.split("_");
                String[] var3 = camels;
                int var4 = camels.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String camel = var3[var5];
                    if (!camel.isEmpty()) {
                        result.append(camel.substring(0, 1).toUpperCase());
                        result.append(camel.substring(1).toLowerCase());
                    }
                }

                return result.toString();
            }
        } else {
            return "";
        }
    }

    public static String camelToUnderline(String para) {
        int length = 3;
        if (para.length() < length) {
            return para.toLowerCase();
        } else {
            StringBuilder sb = new StringBuilder(para);
            int temp = 0;

            for(int i = 2; i < para.length(); ++i) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    ++temp;
                }
            }

            return sb.toString().toLowerCase();
        }
    }

    public static String randomGen(int place) {
        String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();

        for(int i = 0; i < place; ++i) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }

        return sb.toString();
    }

    public static Field[] getAllFields(Object object) {
        Class<?> clazz = object.getClass();

        ArrayList fieldList;
        for(fieldList = new ArrayList(); clazz != null; clazz = clazz.getSuperclass()) {
            fieldList.addAll(new ArrayList(Arrays.asList(clazz.getDeclaredFields())));
        }

        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    public static List<Map<String, Object>> toLowerCasePageList(List<Map<String, Object>> list) {
        List<Map<String, Object>> select = new ArrayList();
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            Map<String, Object> row = (Map)var2.next();
            Map<String, Object> resultMap = new HashMap(5);
            Set<String> keySet = row.keySet();
            Iterator var6 = keySet.iterator();

            while(var6.hasNext()) {
                String key = (String)var6.next();
                String newKey = key.toLowerCase();
                resultMap.put(newKey, row.get(key));
            }

            select.add(resultMap);
        }

        return select;
    }

    public static <F, T> List<T> entityListToModelList(List<F> fromList, Class<T> tClass) {
        if (fromList != null && !fromList.isEmpty()) {
            List<T> tList = new ArrayList();
            Iterator var3 = fromList.iterator();

            while(var3.hasNext()) {
                F f = (F) var3.next();
                T t = entityToModel(f, tClass);
                tList.add(t);
            }

            return tList;
        } else {
            return null;
        }
    }

    public static <F, T> T entityToModel(F entity, Class<T> modelClass) {
        log.debug("entityToModel : Entity属性的值赋值到Model");
        Object model = null;
        if (entity != null && modelClass != null) {
            try {
                model = modelClass.newInstance();
            } catch (InstantiationException var4) {
                log.error("entityToModel : 实例化异常", var4);
            } catch (IllegalAccessException var5) {
                log.error("entityToModel : 安全权限异常", var5);
            }

            BeanUtils.copyProperties(entity, model);
            return (T) model;
        } else {
            return null;
        }
    }

    public static boolean listIsEmpty(Collection list) {
        return list == null || list.size() == 0;
    }

    public static boolean listIsNotEmpty(Collection list) {
        return !listIsEmpty(list);
    }

    public static String readStatic(String url) {
        String json = "";

        try {
            InputStream stream = OConvertUtils.class.getClassLoader().getResourceAsStream(url.replace("classpath:", ""));
            json = IOUtils.toString(stream, "UTF-8");
        } catch (IOException var3) {
            log.error(var3.getMessage(), var3);
        }

        return json;
    }
}
