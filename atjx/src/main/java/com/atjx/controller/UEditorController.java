package com.atjx.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class UEditorController {


    @RequestMapping("/")
    private String showPage(){
        return "index";
    }


}
