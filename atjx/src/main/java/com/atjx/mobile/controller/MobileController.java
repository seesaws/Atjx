package com.atjx.mobile.controller;

import com.atjx.mapper.ItemMapper;
import com.atjx.mapper.PicMapper;
import com.atjx.mapper.SpecificationMapper;
import com.atjx.mobile.model.AccessToken;
import com.atjx.mobile.util.RedisTokenHelper;
import com.atjx.model.Item;
import com.atjx.model.Item_Pic;
import com.atjx.model.Specification;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;


@Controller

public class MobileController {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private PicMapper picMapper;
    @Resource
    private SpecificationMapper specificationMapper;

    @Resource
    private RedisTokenHelper redisTokenHelper;

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
    public String goods(Item item, Model model, HttpServletRequest request){
        try{
            Item item1 = itemMapper.findAllInfo(item.getId());
            String html=item1.getSell_Point();
            String unhtml= HtmlUtils.htmlUnescape(html);
            item.setSell_Point(unhtml);
            List<Item_Pic> pic=picMapper.selectAll(item1.getId());
            model.addAttribute("picList",pic);
            model.addAttribute("item", item1);
            JSONObject json = JSONObject.fromObject(item1);
            model.addAttribute("itemJson",json);
            AccessToken AccessToken= (AccessToken) redisTokenHelper.getObject("global_token");
            System.out.println(AccessToken.getToken());
        }catch (Exception e){
            e.printStackTrace();
        }

        return "mobile/goods";
    }

    @RequestMapping(value = "/mobile/placeOrder")
    public String  PlaceOrder(Item item, Model model, HttpServletRequest request, Specification specification) {
        Integer id=item.getId();
        String spe_id=request.getParameter("state");
        specification.setSpe_id(Integer.parseInt(spe_id));
        Specification specification1=specificationMapper.find(specification);
        Item item1 = itemMapper.findAllInfo(id);
        model.addAttribute("item",item1);
//        List<Item_Pic> pic=picMapper.selectAll(item1.getId());
//        model.addAttribute("picList",pic);
        model.addAttribute("specification",specification1);

        return "mobile/placeOrder";
    }

    @RequestMapping(value = "/mobile/jingku")
    public String  jingku() {

        return "mobile/jingku";
    }


    @RequestMapping(value = "/mobile/rechargeSuccess")
    public String  rechargeSuccess() {

        return "mobile/rechargeSuccess";
    }
}
