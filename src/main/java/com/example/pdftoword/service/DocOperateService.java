package com.example.pdftoword.service;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

public interface DocOperateService {

    /**
     * 合并doc文件
     * @return
     */
    byte[] mergeDocs(String docFilePath, ByteArrayOutputStream outputStream);
}
