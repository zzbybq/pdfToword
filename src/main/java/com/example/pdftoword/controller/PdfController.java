package com.example.pdftoword.controller;

import com.example.pdftoword.service.FileOperateService;
import com.example.pdftoword.service.PdfOperateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
public class PdfController {

    @Resource
    private FileOperateService fileOperateService;
    @Resource
    private PdfOperateService pdfOperateService;

    @PostMapping("/convertToDoc")
    public ResponseEntity<byte[]> convertToDoc(@RequestParam("uploadFile") MultipartFile uploadFile)  {

        String uploadDir = null;
        try {
            //创建上传目录
            uploadDir = fileOperateService.createUploadDir();
            if (StringUtils.isEmpty(uploadDir)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            String fileName = uploadFile.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            //文件名
            String nameWithoutSuffix = FilenameUtils.removeExtension(fileName);
            String encodedNameWithSuffix = URLEncoder.encode(nameWithoutSuffix, StandardCharsets.UTF_8.name()) + ".docx";
            //pdf转doc
            byte[] bytes = pdfOperateService.pdfToDoc(uploadDir, uploadFile.getBytes());
            //传输字节流必须设置响应头信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set("Content-Disposition", String.format("attachment; filename*=UTF-8''%s", encodedNameWithSuffix));
            headers.setContentLength(bytes.length);
            // 构造响应实体并返回
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("pdf convert doc error,fileName={}", uploadFile.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }finally {
            //删除过程文件
            if (StringUtils.isNotBlank(uploadDir)) {
                fileOperateService.deleteFiles(uploadDir);
            }
        }
    }
}
