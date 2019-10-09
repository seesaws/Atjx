/*
package com.atjx.controller;

import com.sun.javafx.PlatformUtil;
import com.sun.media.jfxmediaimpl.HostUtils;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController

  */
/*富文本编辑器的上传文件请求和打开文件管理器的请求*//*



public class FileController4RichTextEdit {
    public static final HashMap<String, List<String>> EXT_MAP = new HashMap<>();//支持的文件类型，目前不限制
    public static final List<String> IMGE_TYPES = Arrays.asList("gif","jpg","jpeg","png","bmp");
    static{
        EXT_MAP.put("image", IMGE_TYPES);
        EXT_MAP.put("flash", Arrays.asList("swf","flv"));
        EXT_MAP.put("media", Arrays.asList("swf","flv","mp3","wav","wma","wmv","mid","avi","mpg","asf","rm","rmvb"));
        EXT_MAP.put("file", Arrays.asList("doc","docx","xls","xlsx","ppt","htm","html","txt","zip","rar","gz","bz2"));
    }

    public static Comparator<HashMap<String, Object>> DIR_COMPARAOR_FUNC =(hashA, hashB)-> {
        return ((Boolean)hashB.get("is_dir")).compareTo((Boolean)hashA.get("is_dir"));
    };
    public static Comparator<HashMap<String, Object>> TYPE_COMPARAOR_FUNC =(hashA, hashB)-> {
        return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
    };

    public static Comparator<HashMap<String, Object>> NAME_COMPARAOR_FUNC = (hashA, hashB)-> {
        return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
    };

    public static Comparator<HashMap<String, Object>> SIZE_COMPARAOR_FUNC=(hashA, hashB)-> {
        return ((Long)hashA.get("filesize")).compareTo((Long)hashB.get("filesize"));
    };



    @RequestMapping("/file_manager_json")
    public Object fileManager(HttpServletRequest req, HttpServletResponse rsp){
        String dirPath = "upload/";
        String rootPath = req.getServletContext().getRealPath("/");
        if(SystemManager.isLinux()){
            rootPath = SystemManager.inst().upload_path;
        }
        rootPath += dirPath;

        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl  = req.getContextPath() + "/" + dirPath;
        LogCore.BASE.info("rootPath={}", rootPath);
        LogCore.BASE.info("rootUrl={}", rootUrl);

        String dirName = req.getParameter("dir");
        if (dirName != null) {
            if(!EXT_MAP.containsKey(dirName)){
                return "Invalid Directory name.";
            }
            rootPath += dirName + "/";
            rootUrl += dirName + "/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
        //根据path参数，设置各路径和URL
        String path = Objects.toString(req.getParameter("path"), "");
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = Objects.toString(req.getParameter("order").toLowerCase(), "name");

        //不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            return "Access is not allowed.";
        }
        //最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            return "Parameter is not valid.";
        }
        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if(!currentPathFile.isDirectory()){
            return "Directory does not exist.";
        }

        //遍历目录取的文件信息
        List<HashMap<String, Object>> fileList = new ArrayList<>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                HashMap<String, Object> hash = new HashMap<>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", IMGE_TYPES.contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }

        if ("size".equals(order)) {
            Collections.sort(fileList, DIR_COMPARAOR_FUNC.thenComparing(SIZE_COMPARAOR_FUNC));
        } else if ("type".equals(order)) {
            Collections.sort(fileList, DIR_COMPARAOR_FUNC.thenComparing(TYPE_COMPARAOR_FUNC));
        } else {
            Collections.sort(fileList, DIR_COMPARAOR_FUNC.thenComparing(NAME_COMPARAOR_FUNC));
        }

        rsp.setContentType("application/json; charset=UTF-8");
        JSONObject json = JSONBuilder.create().append("moveup_dir_path", moveupDirPath)
                .append("current_dir_path", currentDirPath)
                .append("current_url", currentUrl)
                .append("total_count", fileList.size())
                .append("file_list", fileList).buildJSON();
        LogCore.BASE.info("filemanager json={}", json);
        return json;
    }

    @RequestMapping("/upload_file_json")
    Object upload_media(StandardMultipartHttpServletRequest req, HttpServletResponse rsp){
        try {
            LogCore.BASE.info("upload_file_json' invoked{}",HttpHeadTool.getParamsMap(req));
            String dirName = Objects.toString(req.getParameter("dir"), "image");

            //文件保存目录路径
            //rootPath/dirPath/dirName/ymdPath/fileName
            String ymdPath = TimeTool.formatTime(System.currentTimeMillis(), "yyyy_MM_dd")+"/";
            String dirPath = "upload/";
            String rootPath = req.getServletContext().getRealPath("/");
            if(SystemManager.isLinux()){
                rootPath = SystemManager.inst().upload_path;
            }
            String filePath = dirPath + dirName+ "/"+ ymdPath;
新文件名字的命名规则*

            Function<String ,String> nameFunc = fileName ->{
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();//检查后缀名
                String newFileName = TimeTool.formatTime(System.currentTimeMillis(), "HH_mm_ss_") + new Random().nextInt(1000);
                return newFileName+"."+fileExt;
            };

            rsp.setContentType("text/html; charset=UTF-8");

            if(!ServletFileUpload.isMultipartContent(req)){
                return getError("请选择文件。");
            }

            //检查目录
            File uploadDir = new File(rootPath);

            //检查目录写权限
            if(!uploadDir.canWrite()){
                return getError("上传目录没有写权限。");
            }

            testOther(req);//仅仅测试用

            String url = FileManager.inst().upload_multi_file(rootPath, filePath, nameFunc, req);
            LogCore.BASE.info("file request={}, requestStr={}, url={}", req, req.toString(), url);
            LogCore.BASE.info("file requestjson={}", req.toString());

            return JSONBuilder.creatJsonString("error", 0, "url", url);
        } catch (Exception e) {
            LogCore.BASE.error("file upload err!{}", e);
            return getError("未知错误");
        }
    }

    private String getError(String message) {
        return JSONBuilder.creatJsonString("error", 1, "message", message);
    }
    public String upload_multi_file(String saveRootPath, String filePath, Function<String, String> nameFunc, StandardMultipartHttpServletRequest freq) {
        StopWatch stopWatch = new StopWatch("upfile");
        stopWatch.start("save file");

        String saveUrl = freq.getContextPath() + "/" + filePath;
        String realpath = saveRootPath + filePath;
        File dir = new File(realpath);
        if(!dir.exists()) dir.mkdirs();

        String file_url[] = new String[1];
        try {
            freq.getMultiFileMap().values().stream().forEach(files -> {
                files.stream().forEach((MultipartFile file) -> {
                    try {
                        String fileName = file.getOriginalFilename();
                        if(Util.isEmpty(fileName)){
                            LogCore.BASE.info("upload failed because of nofiles fileName={}", fileName);
                            return;
                        }
                        String _new_file_original_name = newFileName;
                        if(null != nameFunc){
                            _new_file_original_name = nameFunc.apply(fileName);
                        }else{
                            _new_file_original_name = fileName;
                        }
                        File newFile = new File(dir, _new_file_original_name);
                        file.transferTo(newFile);
                        file_url[0] = saveUrl + newFile.getName();
                    } catch (IOException e) {
                        LogCore.BASE.error("read file err:{}", e);
                    }
                });
            });
        } catch (Exception e2) {
            LogCore.BASE.error("file upload err", e2);
            return file_url[0];
        }
        stopWatch.stop();
        LogCore.BASE.info("upfile used time:{},\n update nums={}", stopWatch.prettyPrint());
        return file_url[0];
    }
}
*/
