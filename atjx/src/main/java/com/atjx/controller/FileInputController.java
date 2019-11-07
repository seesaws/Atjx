package com.atjx.controller;

import com.atjx.mapper.PicMapper;
import com.atjx.model.Item_Pic;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


@Controller
public class FileInputController {


    @Autowired
    private PicMapper picMapper;

    @RequestMapping("/user/fileinput")
    public String FileInput(){

        return "fileinput/fileinput";
    }

    @RequestMapping("/uploadPicFile")
    @ResponseBody
    public String saveFile(@RequestParam(value="file") MultipartFile file,
                           HttpServletRequest request, HttpServletResponse response, Item_Pic pic){
        OutputStream os = null;
        InputStream is = null;
        boolean returnBoolean = false;

        String fileName = file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        String bug_id = request.getParameter("id");   // 传过来的额外参数
        pic.setItem_id(Integer.parseInt(bug_id));
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName ="IMG"+bug_id+"_"+ dt.format(new Date()) + "_" + new Random().nextInt(1000) ;
        try {
            File root=new File("/static/upload/DCIM");
            File path = new File(root.getAbsolutePath());
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
            String currentDate = df.format(System.currentTimeMillis());
            String pathTemp = path + File.separator + currentDate + File.separator + bug_id;
            File fileTemp = new File(pathTemp);
            if(!fileTemp.exists()) {
                fileTemp.mkdirs();
            }
            String fullFilePath = pathTemp + File.separator + newFileName+"."+fileExt;
            File fullFile = new File(fullFilePath);

            os = new FileOutputStream(fullFile);
            is =file.getInputStream();
            byte[] buf = new byte[1024];//可以修改 1024 以提高读取速度
            int length = 0;
            while( (length = is.read(buf)) > 0 ){
                os.write(buf, 0, length);
            }
            returnBoolean = true;
            pic.setPic_url(fullFilePath);
            picMapper.insert(pic);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is !=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os !=null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSONObject.fromObject("{\"success\":"+ returnBoolean +"}").toString();
    }
}
