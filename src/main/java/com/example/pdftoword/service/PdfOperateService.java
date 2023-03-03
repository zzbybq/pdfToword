package com.example.pdftoword.service;

public interface PdfOperateService {

    /**
     * 拆分pdf
     * @param path
     * @param bytes
     * @return 返回拆分后文件所在的目录
     */
    String splitPdf(String path, byte[] bytes);

    /**
     * pdf转doc
     * @param path
     * @param bytes
     * @return 输出转化后的byte[]
     */
    byte[] pdfToDoc(String path, byte[] bytes);

    /**
     * 将path路径下的pdf全转成doc,并输出doc文件目录（单页pdf转换）
     * @param splitDir
     * @return
     */
    String pdfToDocDir(String splitDir);
}
