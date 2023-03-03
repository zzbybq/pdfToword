package com.example.pdftoword.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileOperateService {

    /**
     * 将用户上传的文件保存到当前目录下
     *
     * @param path       文件路径
     * @param fileName 文件名
     * @param inputStream 文件
     */
    boolean saveFile(String path, String fileName, InputStream inputStream);

    /**
     * 创建目录
     * @param path
     * @return
     */
    boolean createDir(String path);

    /**
     * 创建上传文件目录
     * @return 返回文件全路径
     */
    String createUploadDir();

    /**
     * 删除文件，pdf拆分后转换合并过程中的，过程文件删除
     * @param path 删除路径下所有文件
     */
    void deleteFiles(String path);
}
