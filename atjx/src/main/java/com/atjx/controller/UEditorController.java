package com.atjx.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ldb on 2017/4/9.
 */
@Controller
public class UEditorController {


    @RequestMapping("/")
    private String showPage(){
        return "index";
    }


}
