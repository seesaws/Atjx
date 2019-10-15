package com.atjx.controller;

import com.alibaba.fastjson.JSONObject;
import com.atjx.mapper.ItemCategoryMapper;
import com.atjx.mapper.ItemMapper;
import com.atjx.mapper.ReItemMapper;
import com.atjx.model.Item;
import com.atjx.model.ItemCategory;
import com.atjx.model.ReItem;
import com.atjx.model.ResObject;
import com.atjx.util.*;
//import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 商品管理
 */
@Controller
public class ItemController {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Autowired
    private ReItemMapper reItemMapper;

    public static final String ROOT = "src/main/resources/static/img/item/";

//    MongoUtil mongoUtil = new MongoUtil();

    private final ResourceLoader resourceLoader;

    @Autowired
    public ItemController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    List<Item> itemList;

    File getFile = null;

    @RequestMapping("/user/itemManage_{pageCurrent}_{pageSize}_{pageCount}")
    public String itemManage(Item item, @PathVariable Integer pageCurrent,
                             @PathVariable Integer pageSize,
                             @PathVariable Integer pageCount,
                             Model model) {
        if (pageSize == 0) pageSize = 50;
        if (pageCurrent == 0) pageCurrent = 1;

        int rows = itemMapper.count(item);
        if (pageCount == 0) pageCount = rows % pageSize == 0 ? (rows / pageSize) : (rows / pageSize) + 1;
        item.setStart((pageCurrent - 1) * pageSize);
        item.setEnd(pageSize);
        itemList = itemMapper.list(item);
        for (Item i : itemList) {
            i.setCreatedStr(DateUtil.getDateStr(i.getCreated()));
            i.setUpdatedStr(DateUtil.getDateStr(i.getUpdated()));
        }
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setStart(0);
        itemCategory.setEnd(Integer.MAX_VALUE);
        List<ItemCategory> itemCategoryList = itemCategoryMapper.list(itemCategory);
        Integer minPrice = item.getMinPrice();
        Integer maxPrice = item.getMaxPrice();
        model.addAttribute("itemCategoryList", itemCategoryList);
        model.addAttribute("itemList", itemList);
        String pageHTML = PageUtil.getPageContent("itemManage_{pageCurrent}_{pageSize}_{pageCount}?title=" + item.getTitle() + "&cid=" + item.getCid() + "&minPrice" + minPrice + "&maxPrice" + maxPrice, pageCurrent, pageSize, pageCount);
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("item", item);
        return "item/itemManage";
    }

    @RequestMapping("/user/download1")
    public void postItemExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //导出excel
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
        fieldMap.put("id", "商品id");
        fieldMap.put("title", "商品标题");
        fieldMap.put("sellPoint", "商品卖点");
        fieldMap.put("price", "商品价格");
        fieldMap.put("num", "库存数量");
        fieldMap.put("image", "商品图片");
        fieldMap.put("cid", "所属类目，叶子类目");
        fieldMap.put("status", "商品状态，1-正常，2-下架，3-删除");
        fieldMap.put("created", "创建时间");
        fieldMap.put("updated", "更新时间");
        String sheetName = "商品管理报表";
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=ItemManage.xls");//默认Excel名称
        response.flushBuffer();
        OutputStream fos = response.getOutputStream();
        try {
            ExcelUtil.listToExcel(itemList, fieldMap, sheetName, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    String imageName = null;

    @RequestMapping("/user/NewItem")
    public String NewItem(){

        return "item/NewItemEdit";
    }
    @RequestMapping("/NewItem")
    public String NewItemEdit(Item item,HttpServletRequest request){
        String notes = request.getParameter("sell_point");

        String html= HtmlUtils.htmlEscape(notes);
        item.setSellPoint(html);
        itemMapper.insert(item);
        return "redirect:/user/itemManage_0_0_0";
    }

    @GetMapping("/user/itemEdit")
    public String itemEditGet(Model model, Item item) {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setStart(0);
        itemCategory.setEnd(Integer.MAX_VALUE);

        String html=item.getSellPoint();
        String unhtml=HtmlUtils.htmlUnescape(html);
        item.setSellPoint(unhtml);
        if (item.getId() != 0) {
            Item item1 = itemMapper.findById(item);
            itemMapper.update(item1);
            model.addAttribute("item", item1);
        }
        return "item/itemEdit";
    }

    @PostMapping("/user/itemEdit")
    public String itemEditPost(HttpServletRequest request, Item item) throws IOException {
        //生成日期
        Date date=new Date();
        //设置商品更新时间
        item.setUpdated(date);
        item.setBarcode("");
        //获取前端商品详情富文本
        String notes = request.getParameter("sell_point");
        String html= HtmlUtils.htmlEscape(notes);
        item.setSellPoint(html);
        if(item.getId() == 0){
            itemMapper.insert(item);
        }else{
            itemMapper.update(item);
        }

        return "redirect:itemManage_0_0_0";


    }
    @GetMapping(value = "/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile() {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, imageName).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @ResponseBody
    @PostMapping("/user/itemEditState")
    public ResObject<Object> itemEditState(Item item1) {
        Item item = itemMapper.findById(item1);
        ReItem reItem = new ReItem();
        reItem.setId(item.getId());
        reItem.setBarcode(item.getBarcode());
        reItem.setCid(item.getCid());
        reItem.setImage(item.getImage());
        reItem.setPrice(item.getPrice());
        reItem.setNum(item.getNum());
        reItem.setSellPoint(item.getSellPoint());
        reItem.setStatus(item.getStatus());
        reItem.setTitle(item.getTitle());
        reItem.setRecovered(new Date());
        reItem.setDel_price(item.getDel_price());
        reItemMapper.insert(reItem);
        itemMapper.delete(item1);
        ResObject<Object> object = new ResObject<Object>(Constant.Code01, Constant.Msg01, null, null);
        return object;
    }

    @ResponseBody
    @RequestMapping(value="/uploadImg",method={RequestMethod.POST})
    public  String uploadImg(HttpServletRequest request,@RequestParam(value="file")MultipartFile file,Item item,Model model){

        Date date = new Date();
        String id1=request.getParameter("id1");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(date) + "_" + new Random().nextInt(1000) ;
        String originalFileName=file.getOriginalFilename();

        String extension=originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());

        int id= Integer.parseInt(id1);
        item.setId(id);

        String name="P"+id1+"_"+newFileName;
        String dt=new SimpleDateFormat("/yyyyMM/").format(date);
        File path= new File("/");
        File upload = new File(path.getAbsolutePath(),"static/upload/images"+dt);

//        String path="/image"+dt+name+extension;
        //绝对路径
//        String url=request.getSession().getServletContext().getRealPath("")+path;
//        String url="/upload/image/"+dt;
        //网站路径
        String webUrl="../static/upload/images"+dt+name+extension;
        String url=webUrl;
        webUrl=webUrl.replaceAll("http://", "").replaceAll("/uploadImg/WEB-INF", "");
        webUrl="{\"url\":\""+webUrl+"\",\"path\":\"/upload"+dt+extension+"\"}";
        File dir=new File(String.valueOf(upload));
        //上传图片
        try {
            if(!dir.exists()){
                dir.mkdirs();
            }
            file.transferTo(new File(upload+"/"+name+extension));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
            item.setImage(url);
            itemMapper.update(item);

            return webUrl;
    }

    @ResponseBody
    @RequestMapping(value="/newImg",method={RequestMethod.POST})
    public  String NewImage(HttpServletRequest request,@RequestParam(value="file")MultipartFile file,Item item,Model model) throws FileNotFoundException {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(date) + "_" + new Random().nextInt(1000) ;
        String originalFileName=file.getOriginalFilename();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
        int id=itemMapper.findMaxId();
        int a=1;
        id+=a;
        String name="P"+id+"_"+newFileName;
        String dt=new SimpleDateFormat("/yyyyMM/").format(date);
//        String path=ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File path = null;
        File upload = new File(path.getAbsolutePath(),"static/images/upload/"+dt);

        String webUrl="/upload/images"+dt+name+extension;

        String url=webUrl;
        webUrl=webUrl.replaceAll("http://", "").replaceAll("/uploadImg/WEB-INF", "");
        webUrl="{\"url\":\""+webUrl+"\",\"path\":\"/upload/"+dt+extension+"\"}";
        File dir=new File(String.valueOf(upload));
        try {
            if(!dir.exists()){
                dir.mkdirs();
            }
            file.transferTo(new File(upload+"/"+name+extension));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return webUrl;
    }

    @RequestMapping("/user/addEdit")
    public String addEdit(){
        return "item/addManage";
    }

    @RequestMapping("/user/speEdit")
    public String speEdit(){
        return "item/speManage";
    }
}
