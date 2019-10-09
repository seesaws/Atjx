package com.atjx.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.atjx.vo.KindImage;

@Controller
public class KindController {

    @RequestMapping("/kindeditor/upload")
    @ResponseBody
    public KindImage uploadFile(MultipartFile uploadFile) {
        //创建一个KindImage对象
        KindImage kingImage = new KindImage();
        //获取源文件名并转换为字母小写
        String realName = uploadFile.getOriginalFilename().toLowerCase();

        //1. 正则表达式判断文件名
        if (!realName.matches("^.+\\.(jpg|png|gif)$")) {
            kingImage.setError(1);
            return kingImage;//不是正确的图片后缀名，返回错误
        }
        //2. 获取图片的宽度和高度，用于判断是否为真实的图片文件
        try {
            BufferedImage bufferredImage = ImageIO.read(uploadFile.getInputStream());
            //获取图片的宽和高
            int height = bufferredImage.getHeight();
            int width = bufferredImage.getWidth();
            if (height == 0 || width == 0) {
                kingImage.setError(1);
                return kingImage;//不是真实的图片文件，返回错误
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            kingImage.setError(1);//图片读取出错，返回错误
        }

        //3. 将图片分目录存储（以时间格式：yyyy/MM/dd）
        String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//获取当前年月日
        //String localFileDir = "D:/upload_demo/image/";//本地路径
        String localFileDir = "D:/demo/";//本地路径
        String imagePath = localFileDir + dateDir;//拼接路径
        File file = new File(imagePath);
        if (!file.exists()) file.mkdirs();//若文件路径不存在，创建文件夹（包括父级文件夹）

        //4.重新生成图片名字
        String uuid = UUID.randomUUID()//获取uuid
                .toString()//转换成字符串格式
                .replace("-", "");//去掉其中的“-”
        String FileName = uuid + realName.substring(realName.lastIndexOf("."));//拼接后缀名
        File filePath = new File(imagePath + "/" + FileName);//拼接路径

        //5.上传图片
        try {
            uploadFile.transferTo(filePath);//上传图片
            kingImage.setUrl("image/" + dateDir + "/" + FileName);//图片上传成功，设置回显路径
            System.out.println("文件上传成功!!!!");
        } catch (Exception e) {
            e.printStackTrace();
            kingImage.setError(1);//图片上传失败，返回错误信息
        }
        return kingImage;
    }
}