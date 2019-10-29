package com.atjx.mobile.controller;

import com.atjx.mapper.ItemMapper;
import com.atjx.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;


@Controller

public class MobileController {

    @Autowired
    private ItemMapper itemMapper;

    //初始页
    @RequestMapping(value = "/mobile/")
    public String index0( Model model) {
        List<Item> list = itemMapper.selectAll();
        Collections.reverse(list);
        model.addAttribute("itemlist",list);
        return "mobile/index";
    }

    @RequestMapping(value = "/mobile/index")
    public String index( Model model) {

        List<Item> list = itemMapper.selectAll();
        Collections.reverse(list);
        model.addAttribute("itemlist",list);

        return "mobile/index";
    }

//    @RequestMapping(value = "/mobile/user")
//    public String  user(Model model,Item item) {
//        // 获取接口访问凭证
////        String accessToken = CommonUtil.getToken("wxb8ef126b92b8c857", "761a8370a5dd929f19186beacb9545e1").getToken();
//        /**
//         * 获取用户信息
//         */
////        WeixinUserInfo user = CommonUtil.getUserInfo(accessToken, "ocUlj5jz8hHcGur0EmTlyOvfqzKk");
////            model.addAttribute("user",user);
//
//        return "mobile/my";
//    }

    @RequestMapping(value = "/mobile/goods")
    public String  goods(Item item,Model model) {

            Item item1 = itemMapper.findById(item);
//            String html=item1.getSellPoint();
//            String unhtml= HtmlUtils.htmlUnescape(html);
//            item.setSellPoint(unhtml);
//            System.out.println(unhtml);
            model.addAttribute("item", item1);

             return "mobile/goods";
    }

    @RequestMapping(value = "/mobile/placeOrder")
    public String  PlaceOrder(Item item,Model model) {

        Item item1=itemMapper.findById(item);
        model.addAttribute("item",item1);
        return "mobile/placeOrder";
    }


    @RequestMapping(value = "/mobile/rechargeSuccess")
    public String  rechargeSuccess() {

        return "mobile/rechargeSuccess";
    }
}
