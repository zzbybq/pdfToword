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
     * 多个PDF合并为一个PDF
     * @param files
     * @return
     */
    @PostMapping("/mergeToPdf")
    public ResponseEntity<byte[]> mergeToPdf(@RequestParam("fileToUpload") MultipartFile[] files,
                                             @RequestParam("fileName") String fileName) throws IOException {
        // 获取上传文件
        if(files.length<1){
            return ResponseEntity.badRequest().body("一份文件不能合并".getBytes());
        }

        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        OutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream mergerUtilityDestinationStream = null;

        try {
            //合并文件不超过5个
            if(files.length>4) {
                return ResponseEntity.badRequest().body("一份文件不能合并".getBytes());
            }
            for (int i = 0; i < files.length; i++) {
                PDFmerger.addSource(files[i].getInputStream());
            }

            // 指定目标文件输出流
            PDFmerger.setDestinationFileName(fileName);
            PDFmerger.setDestinationStream(outputStream);
            //合并pdf
            PDFmerger.mergeDocuments(null);
            mergerUtilityDestinationStream = (ByteArrayOutputStream) PDFmerger.getDestinationStream();

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
        }finally {
            outputStream.close();
            if(mergerUtilityDestinationStream!=null){
                mergerUtilityDestinationStream.close();
            }
        }


    }
}
