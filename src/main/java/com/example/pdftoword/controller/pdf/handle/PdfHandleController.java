package com.example.pdftoword.controller.pdf.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * PDF处理模块 合并 分割 加密 3/1
 */
@Controller
@Slf4j
public class PdfHandleController {

    /**
     * pdf文件流合并，返回byte文件流
     * @param bytes
     * @param appointmentListByte
     * @return
     */
    public static byte[] mergePdf(byte[] bytes, byte[] appointmentListByte) {
        try {
            //pdf合并工具类
            PDFMergerUtility mergePdf = new PDFMergerUtility();
            // 添加 pdf 数据源
            mergePdf.addSource(new ByteArrayInputStream(bytes));
            mergePdf.addSource(new ByteArrayInputStream(appointmentListByte));
            OutputStream outputStream = new ByteArrayOutputStream();
            // 指定目标文件输出流
            mergePdf.setDestinationStream(outputStream);
            //合并pdf
            mergePdf.mergeDocuments(null);
            ByteArrayOutputStream mergerUtilityDestinationStream = (ByteArrayOutputStream) mergePdf.getDestinationStream();
            return mergerUtilityDestinationStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 多个PDF合并为一个PDF
     * @param files
     * @return
     */
    @PostMapping("/mergeToPdf")
    public ResponseEntity<byte[]> mergeToPdf(@RequestParam("fileToUpload") MultipartFile[] files) {

        String pdf1="/Users/nealxie/Downloads/050510/P1.pdf";
        String pdf2="/Users/nealxie/Downloads/050510/P2.pdf";

        // 获取上传文件
        if(files.length<1){
            return ResponseEntity.badRequest().body("一份文件不能合并".getBytes());
        }


        PDFMergerUtility PDFmerger = new PDFMergerUtility();

        try {
            PDFmerger.addSource(files[0].getInputStream());
            PDFmerger.addSource(files[1].getInputStream());

            OutputStream outputStream = new ByteArrayOutputStream();

            // 指定目标文件输出流
            PDFmerger.setDestinationFileName("demo.pdf");
            PDFmerger.setDestinationStream(outputStream);
            //合并pdf
            PDFmerger.mergeDocuments(null);
            ByteArrayOutputStream mergerUtilityDestinationStream = (ByteArrayOutputStream) PDFmerger.getDestinationStream();



            // 将输出流转换为字节数组并设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(PDFmerger.getDestinationFileName()).build());
            headers.setContentLength(mergerUtilityDestinationStream.size());
            // 构造响应实体并返回
            return new ResponseEntity<>(mergerUtilityDestinationStream.toByteArray(), headers, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
}
