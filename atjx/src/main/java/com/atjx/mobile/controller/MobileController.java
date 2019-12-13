package com.atjx.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atjx.mapper.*;
import com.atjx.mobile.pojo.WeixinUserInfo;
import com.atjx.mobile.util.RedisTokenHelper;
import com.atjx.model.Item;
import com.atjx.model.Item_Pic;
import com.atjx.model.Specification;
import com.atjx.model.Tixian;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
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
    private WxUserMapper wxUserMapper;
    @Resource
    private TixianMapper tixianMapper;
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
    //基本属性
//    private int currentPage;//当前页数，由用户指定
//    private int pageSize = 5 ;//每页显示的条数，固定的
//    private int totalRecords;//总记录条数，数据库查出来的
//    private int startIndex;//每页开始记录的索引，计算出来的
//    private int prePage;//上一页
//    private int nextPage;//下一页
//    private int startPage;//开始页码
//    private int endPage;//结束页码
    @RequestMapping(value = "/mobile/getItemPage",method = RequestMethod.GET)
    @ResponseBody
    public int page(HttpServletRequest req, HttpServletResponse resp) {
        //获取当前页参数，第一次访问为空
        String currPage = req.getParameter("num");
        System.out.println(currPage);
        //页长
        int size = Integer.parseInt(req.getParameter("size"));
        //总页数，计算出来的
        System.out.println(size);
        int totalPage = itemMapper.getItemPage();
        return totalPage%size==0?(totalPage/size):(totalPage/size+1);
    }



    @RequestMapping(value = "/mobile/goods")
    public String goods(Item item, Model model){
        try{
            String itemStr=null;
            Item item1 = itemMapper.findAllInfo(item.getId());
            String html=item1.getSell_Point();
            String unhtml= HtmlUtils.htmlUnescape(html);
            item.setSell_Point(unhtml);
            itemStr=JSON.toJSONString(item1);
            List<Item_Pic> pic=picMapper.selectAll(item1.getId());
            model.addAttribute("picList",pic);
            model.addAttribute("item", item1);
            JSONObject json = JSON.parseObject(itemStr);
            model.addAttribute("itemJson",json);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "mobile/goods";
    }

    @RequestMapping(value = "/mobile/placeOrder")
    public String  PlaceOrder(Item item, Model model, HttpServletRequest request, Specification specification) {
        Integer id=item.getId();
        specification.setSpe_id(Integer.parseInt(request.getParameter("state")));
        Specification specification1=specificationMapper.find(specification);
        Item item1 = itemMapper.findAllInfo(id);
        model.addAttribute("item",item1);
//        List<Item_Pic> pic=picMapper.selectAll(item1.getId());
//        model.addAttribute("picList",pic);
        model.addAttribute("specification",specification1);

        return "mobile/placeOrder";
    }

    @RequestMapping(value = "/mobile/jingku")
    public String  jingku(HttpServletRequest request,Model model) {
        WeixinUserInfo weixinUserInfo1= (WeixinUserInfo) request.getSession().getAttribute("weixinUserInfo");
        try{
            weixinUserInfo1=wxUserMapper.select(weixinUserInfo1.getOpenId());
            model.addAttribute("wxuser", weixinUserInfo1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "mobile/jingku";
    }


    @RequestMapping(value = "/mobile/tixian")
    @ResponseBody
    public JSONObject rechargeSuccess(HttpServletRequest request) {
        String openid=request.getParameter("openid");
        String t_money=request.getParameter("t_money");
        BigDecimal tb_money=new BigDecimal(t_money);
        System.out.println("转换="+tb_money);
        JSONObject jsonObject=new JSONObject();
        if(Integer.parseInt(t_money)<50){
            jsonObject.put("tips","提现金额小于50元!");
            return jsonObject;
        }else{
            try{
                Tixian tixian=new Tixian();
                //查询用户余额

                WeixinUserInfo weixinUserInfo = wxUserMapper.select(openid);
                tixian.setT_openid(weixinUserInfo.getOpenId());
                tixian.setT_nickname(weixinUserInfo.getNickname());
                tixian.setT_money(tb_money);
                tixian.setT_status("提现中");
                tixianMapper.insert(tixian);
                //减用户余额
                BigDecimal sub= weixinUserInfo.getMoney().subtract(tb_money);

                weixinUserInfo.setMoney(sub);
                //增加已提现额度
                BigDecimal add= weixinUserInfo.getUsed_money().add(tb_money);
                weixinUserInfo.setUsed_money(add);
                //更新用户信息
                wxUserMapper.update(weixinUserInfo);
                jsonObject.put("tips","提现申请提交成功!");
            }catch (Exception e){
                e.printStackTrace();
                jsonObject.put("tips","提现异常!");
            }
        }

        return jsonObject;


    }

    @RequestMapping(value = "/mobile/test")
    public String test(Item item, Model model) {
        try{
            String itemStr=null;
            Item item1 = itemMapper.findAllInfo(item.getId());
            String html=item1.getSell_Point();
            String unhtml= HtmlUtils.htmlUnescape(html);
            item.setSell_Point(unhtml);
            itemStr=JSON.toJSONString(item1);
            List<Item_Pic> pic=picMapper.selectAll(item1.getId());
            model.addAttribute("picList",pic);
            model.addAttribute("item", item1);
            JSONObject json = JSON.parseObject(itemStr);
            model.addAttribute("itemJson",json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "mobile/index_test";
    }
}
