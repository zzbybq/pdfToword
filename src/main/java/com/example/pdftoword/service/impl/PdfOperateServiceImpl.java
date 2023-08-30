package com.example.pdftoword.service.impl;

import com.example.pdftoword.service.DocOperateService;
import com.example.pdftoword.service.PdfOperateService;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.example.pdftoword.domain.Constants.*;
import static com.example.pdftoword.domain.FileTypeEnum.DOC;

@Service
@Slf4j
public class PdfOperateServiceImpl implements PdfOperateService {

    @Resource
    private DocOperateService docOperateService;

    public ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public String splitPdf(String path, byte[] bytes) {
        //调用pdf的拆分方法拆分单个pdf
        PdfDocument pdfDocument = new PdfDocument();
        //加载PDF文件
        pdfDocument.loadFromBytes(bytes);
        //将 PDF 拆分为单页 PDF
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        pdfDocument.split(path + "split{0}.pdf", 1);
        return path;
    }

    @Override
    public byte[] pdfToDoc(String path, byte[] bytes) {
        //输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument();
        //加载PDF文件
        pdfDocument.loadFromBytes(bytes);
        //页数少于10页，直接转化
        if (pdfDocument.getPages().getCount() <= 3) {
            pdfDocument.saveToStream(outputStream, FileFormat.DOCX);
            return outputStream.toByteArray();
        }
        long startTime = System.currentTimeMillis();
        log.info("拆分pdf开始,startTime={}", startTime);
        //大于10页，需要拆分
        //拆分后文件所在目录
        String splitDir = splitPdf(path, bytes);
        log.info("拆分pdf结束,耗时={}s", (System.currentTimeMillis() - startTime) / 1000);
        if (!splitDir.endsWith(File.separator)) {
            splitDir = splitDir + File.separator;
        }
        //再转化doc
        startTime = System.currentTimeMillis();
        log.info("转化pdf开始,startTime={}", startTime);
        String docFilePath = pdfToDocDir(splitDir);
        log.info("转化pdf结束,耗时={}s", (System.currentTimeMillis() - startTime) / 1000);
        //将转换的doc进行合并
        return docOperateService.mergeDocs(docFilePath, outputStream);
    }

    @Override
    public String pdfToDocDir(String splitDir) {
        File[] pdfFiles = new File(splitDir).listFiles();
        if (pdfFiles == null) {
            return null;
        }
        //doc存储路径
        String docFilePath = splitDir + DOC_CHILD_PATH;

        List<Future<String>> futures = new ArrayList<>();

        for (File childPdfFile : pdfFiles) {

            Future<String> future = executorService.submit(() -> {
                PdfDocument pdf = new PdfDocument();
                pdf.loadFromFile(childPdfFile.getAbsolutePath());
                //将 PDF 转换为流动形态的Word
                pdf.getConvertOptions().setConvertToWordUsingFlow(true);
                String childDocFileName = docFilePath + FilenameUtils.getBaseName(childPdfFile.getName()) + DOC.getSuffix();
                pdf.saveToFile(childDocFileName, FileFormat.DOCX);
                return null;
            });

            futures.add(future);
        }

        while (futures.stream().anyMatch(o -> !o.isDone())) {
            try {
                TimeUnit.MILLISECONDS.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return docFilePath;
    }
}
