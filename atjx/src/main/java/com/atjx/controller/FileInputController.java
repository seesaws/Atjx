package com.atjx.controller;

import com.atjx.model.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class FileInputController {

    @RequestMapping("/user/fileinput")
    public String FileInput(){

        return "fileinput/examples/index";
    }
}
