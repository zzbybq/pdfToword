package com.example.pdftoword.controller;

import java.io.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.pdftoword.domain.FileTypeEnum.PDF;

@Controller
@Slf4j
public class CovertController {

    @PostMapping("/covertToWord")
    public ResponseEntity<byte[]> covertToWord(@RequestParam("fileToUpload") MultipartFile file) {

        //文件非空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("上传的文件为空".getBytes());
        }

        //文件类型为pdf
        String fileType = file.getContentType();
        if (!PDF.getType().equals(fileType)) {
            return ResponseEntity.badRequest().body("上传的文件不是pdf文件".getBytes());
        }

        try {
            String fileName = file.getOriginalFilename();
            String convertedFileName = FilenameUtils.getBaseName(fileName);
            // 加载PDF文档
            PDDocument pdf = PDDocument.load(file.getInputStream());
            // 创建一个新的Word文档
            XWPFDocument word = new XWPFDocument();
            // 获取PDF中的所有页面并迭代
            for (int i = 0; i < pdf.getNumberOfPages(); i++) {
                PDPage page = pdf.getPage(i);
                // 将PDF页面转换为可编辑文本
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String text = stripper.getText(pdf);
                // 创建Word段落并将文本写入段落
                XWPFParagraph para = word.createParagraph();
                XWPFRun run = para.createRun();
                run.setText(text);
            }

            // 将Word文档写入输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            word.write(out);
            pdf.close();
            // 将输出流转换为字节数组并设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(convertedFileName +".docx").build());
            headers.setContentLength(out.size());
            // 构造响应实体并返回
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
