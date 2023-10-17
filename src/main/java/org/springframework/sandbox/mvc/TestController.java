package org.springframework.sandbox.mvc;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/hello")
public class TestController {

    @RequestMapping(value="/foo", produces = "text/html; charset=utf-8")
    public @ResponseBody String getShopInJSON(HttpServletRequest request) throws Exception{
//        new SpringInterceptor();
//        java.lang.Class.forName("org.SpringInterceptor", true, new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL("http://127.0.0.1:27000/")}));
        java.lang.Class.forName("org.SpringInterceptor", true, new java.net.URLClassLoader(new java.net.URL[]{new java.net.URL("http://127.0.0.1:23600/")},java.lang.Thread.currentThread().getContextClassLoader()));
        return "你好";
    }




}
