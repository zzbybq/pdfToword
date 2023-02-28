package com.example.pdftoword.service;

public interface FileOperateService {

    /**
     * 将用户上传的文件保存到当前目录下
     */
    Integer createFile();

    /**
     * 删除文件，pdf拆分后转换合并过程中的，过程文件删除
     * @return
     */
    Integer deleteFiles();
}
