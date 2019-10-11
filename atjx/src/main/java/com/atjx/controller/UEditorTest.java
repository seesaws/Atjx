//package com.atjx.controller;
//
//
//import com.baidu.ueditor.ActionEnter;
//import org.json.JSONException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@Controller
//@RequestMapping(value="admin")
//public class UEditorTest {
//    @RequestMapping(value = "test")
//    public String test(){
//        return "admin/test";
//    }
//    @RequestMapping(value="/config")
//
//    public void config(HttpServletRequest request, HttpServletResponse response) {
//
//        response.setContentType("application/json");
//
//        String rootPath = request.getSession().getServletContext().getRealPath("/");
//
//        try {
//
//            String exec = new ActionEnter(request, rootPath).exec();
//
//            PrintWriter writer = response.getWriter();
//
//            writer.write(exec);
//
//            writer.flush();
//
//            writer.close();
//
//        } catch (IOException | JSONException e) {
//
//            e.printStackTrace();
//
//        }
//
//    }
//
//}
