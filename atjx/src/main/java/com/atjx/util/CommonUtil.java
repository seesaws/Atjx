package com.atjx.util;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }



    public static class DateUtil {
        public static final String HH = "HH";
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final DateTimeFormatter FORMATTER_HH = DateTimeFormatter.ofPattern(HH);
        public static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern(YYYY_MM_DD);
        public static String getHH() {
            return FORMATTER_HH.format(LocalDateTime.now());
        }
        public static String getYYYYMMDD() {
            return FORMATTER_YYYY_MM_DD.format(LocalDateTime.now());
        }
    }



    public static class FileUtil {
        final public static File makeEmptyFile(String URIPath) {
            File newFile = new File(URIPath);
            if (!newFile.getParentFile().exists()) {
                newFile.getParentFile().mkdirs();
            }
            return newFile;
        }

        final public static StringBuilder uploadFiles(String rootPath, String fileURL, String folderType, MultipartFile files[]) throws IOException {
            final StringBuilder filePaths = new StringBuilder();
            for (MultipartFile file : files) {
                if (file != null) {
                    String originalFilename = file.getOriginalFilename();
                    if (!file.isEmpty()) {
                        /**
                         * 文件相对路径
                         * */
                        String URIDir = File.separator + folderType + File.separator + DateUtil.getYYYYMMDD() + File.separator + DateUtil.getHH();
                        /**
                         * 设置文件名称
                         * */
                        String newFileName = URIDir + File.separator + CommonUtil.getUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
                        /**
                         * 文件绝对路径
                         * */
                        String realFilePath = rootPath + newFileName;
                        /**
                         * 创建空文件
                         * */
                        File newFile = FileUtil.makeEmptyFile(realFilePath);
                        /**
                         * 将文件数据写入磁盘
                         * */
                        file.transferTo(newFile);
                        if (filePaths.length() > 0) {
                            filePaths.append(",");
                        }
                        filePaths.append(fileURL + newFileName);
                    }
                }
            }

            return filePaths;
        }

    }
}

