//package com.atjx.service;
//
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class PicUploadResult {
//
//    public PicUploadResult uploadFile(MultipartFile uploadFile) {
//        PicUploadResult picUploadResult= new PicUploadResult();
//        //1.判断是否为图片
//        String fileName=uploadFile.getOriginalFilename();
//        //不是图片
//        if(!fileName.matches("^.*(png|jpg|gif|jpge)$")){
//            //不是图片类型
//            picUploadResult.setError(1);
//            return picUploadResult;
//        }
//        //2.判断是否为恶意程序
//        try {
//            BufferedImage bufferedImage=
//                    ImageIO.read(uploadFile.getInputStream());
//            //2.1获取宽高
//            int height=bufferedImage.getHeight();
//            int width=bufferedImage.getWidth();
//
//            if(height==0||width==0){
//                //表示不是图片
//                picUploadResult.setError(1);
//                return picUploadResult;
//            }
//
//            //3.由于文件个数多,采用分文件存储
//            String dateDir=
//                    new SimpleDateFormat("yyyy/MM/dd")
//                            .format(new Date());
//
//            //生成对应的文件夹
//            String dirPath=filePath+dateDir;
//            //判断是否存在
//            File file=new File(dirPath);
//            if(!file.exists()){
//                //生成文件夹
//                file.mkdirs();
//            }
//            //防止图片上传量过大引起的重名问题
//            String  uuidName=
//                    UUID.randomUUID()
//                            .toString().replace("-", "");
//            String  randomNum=((int)(Math.random()*99999))+"";
//            //获取文件后缀名
//            String fileType=
//                    fileName.substring(
//                            fileName.lastIndexOf("."));
//            String prefix=fileName.substring(0, fileName.lastIndexOf("."));
//            //路径拼接(文件真实的存储路径)
//            String  fileDirPath=
//                    dirPath+"/"+prefix+uuidName+randomNum+fileType;
//
//            //文件上传
//            uploadFile.transferTo(new File(fileDirPath));
//
//            //生成正确的页面回显信息
//            picUploadResult.setHeight(height+"");
//            picUploadResult.setWidth(width+"");
//            /**1.本地磁盘路径
//             * 2.网络虚拟路径
//             */
//            //String  urlDir="http://image.jt.com/";
//            String urlPath=urlDir+dateDir+"/"+prefix+uuidName+randomNum+fileType;
//            picUploadResult.setUrl(urlPath);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return picUploadResult;
//    }
//
//
//
//    /**
//     * 文件上传的类
//     *
//     *  {"error":0,
//     *  "url":"图片的保存路径",
//     *  "width":图片的宽度,
//     *  "height":图片的高度}
//     * @author Administrator
//     *
//     */
//    public  class PicUploadResult {
//
//        private Integer error=0;		//0表示无异常，1代表异常
//        private String url;
//        private String width;
//        private String height;
//
//        public Integer getError() {
//            return error;
//        }
//
//        public void setError(Integer error) {
//            this.error = error;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getWidth() {
//            return width;
//        }
//
//        public void setWidth(String width) {
//            this.width = width;
//        }
//
//        public String getHeight() {
//            return height;
//        }
//
//        public void setHeight(String height) {
//            this.height = height;
//        }
//
//
//
//    }
//}
