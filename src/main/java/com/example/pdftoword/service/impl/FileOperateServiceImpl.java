package com.example.pdftoword.service.impl;

import com.example.pdftoword.service.FileOperateService;
import com.example.pdftoword.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static com.example.pdftoword.domain.Constants.UPLOAD_CHILD_PATH;

@Service
@Slf4j
public class FileOperateServiceImpl implements FileOperateService {

    @Override
    public boolean saveFile(String path, String fileName, InputStream inputStream) {
        if (StringUtils.isBlank(path) || inputStream == null) {
            log.error("path is null or file is null");
            return false;
        }
        //保存文件
        String pathName = path + File.separator + fileName;
        File file = new File(pathName);
        try {
            FileUtils.copyInputStreamToFile(inputStream,file);
            return true;
        } catch (IOException e) {
            log.error("save file error,path={},fileName={}", path, fileName, e);
            return false;
        }
    }

    @Override
    public boolean createDir(String path) {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        File dir = new File(path);
        if (dir.exists()) {
            log.info("dir is already exist,path={}", path);
            return true;
        }

        if (dir.mkdirs()) {
            return true;
        } else {
            log.error("dir create error,path={}", path);
            return false;
        }
    }

    @Override
    public String createUploadDir() {
        try {
            //项目根目录
            String path = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
            //创建目录
            String dir = path + UPLOAD_CHILD_PATH + CommonUtil.getUUID();
            boolean result = createDir(dir);
            if (!result) {
                return null;
            }
            return dir;
        } catch (FileNotFoundException e) {
            log.error("create upload dir error", e);
            return null;
        }
    }

    @Override
    public void deleteFiles(String path) {
        if (StringUtils.isBlank(path)) {
            log.error("delete files path is not empty");
            return;
        }
        deleteFile(new File(path));
    }

    /**
     * 递归删除
     * @param file
     */
    private void deleteFile(File file) {

        if (file == null || !file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            file.delete();
            return;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            file.delete();
            return;
        }
        for (File listFile : files) {
            deleteFile(listFile);
        }
    }
}
