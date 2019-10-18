package com.atjx.controller;

import com.baidu.ueditor.ActionEnter;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Controller
public class UEditorController {

    @RequestMapping(value="/admin/ueditor/config")
    public void config(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type" , "text/html");
        response.setContentType("application/json");
//        InputStream rootPath = this.getClass().getResourceAsStream("/templates/exportQuestionTemplate.xls");
        File file=new File("/");
        String rootPath = file.getAbsolutePath();
//        String rootPath = request.getSession().getServletContext().getRealPath("/");
//        String rootPath = ("http://49.235.1.217/static/config.json");
//        File file = ResourceUtils.getFile("classpath:config.json");
//        ClassPathResource classPathResource = new ClassPathResource("config.json");
//        InputStreamReader rootPath=new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8);


        String hostname="/";
//        Properties pro=new Properties();
//        String realpath="c:";

        try {

            String exec = new ActionEnter(request,hostname, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
