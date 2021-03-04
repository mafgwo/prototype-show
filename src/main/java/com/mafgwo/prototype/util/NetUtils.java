package com.mafgwo.prototype.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 网络工具类
 * @author chenxiaoqi
 */
public class NetUtils {

    private static final String SCHEME_SEPARATOR = "://";
    private static final String QUERY_STRING_SEPARATOR = "?";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_REFERER = "Referer";


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取Url
     * @param request
     * @return
     */
    public static String getURL(HttpServletRequest request){
        String url = request.getScheme() + SCHEME_SEPARATOR + request.getServerName()
                + request.getRequestURI();
//                + request.getServletPath();
        if (request.getQueryString() != null){
            url += QUERY_STRING_SEPARATOR + request.getQueryString();
        }
        return url;
    }

    /**
     * 获取请求浏览器类型
     * @param request
     * @return
     */
    public static String getBrowserType(HttpServletRequest request){
        return request.getHeader(HEADER_USER_AGENT);
    }

    /**
     * 获取Referer
     * @param request
     * @return
     */
    public static String getReferer(HttpServletRequest request){
        return request.getHeader(HEADER_REFERER);
    }

}
