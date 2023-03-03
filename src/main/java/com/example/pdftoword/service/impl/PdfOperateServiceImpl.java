package com.example.pdftoword.service.impl;

import com.example.pdftoword.service.DocOperateService;
import com.example.pdftoword.service.PdfOperateService;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.File;
import static com.example.pdftoword.domain.Constants.*;
import static com.example.pdftoword.domain.FileTypeEnum.DOC;

@Service
@Slf4j
public class PdfOperateServiceImpl implements PdfOperateService {

    @Resource
    private DocOperateService docOperateService;

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
        if (pdfDocument.getPages().getCount() <= 10) {
            pdfDocument.saveToStream(outputStream, FileFormat.DOCX);
            return outputStream.toByteArray();
        }
        //大于10页，需要拆分
        //拆分后文件所在目录
        String splitDir = splitPdf(path, bytes);
        if (!splitDir.endsWith(File.separator)) {
            splitDir = splitDir + File.separator;
        }
        //再转化doc
        String docFilePath = pdfToDocDir(splitDir);
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
        for (File childPdfFile : pdfFiles) {
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromFile(childPdfFile.getAbsolutePath());
            //将 PDF 转换为流动形态的Word
            pdf.getConvertOptions().setConvertToWordUsingFlow(true);
            String childDocFileName = docFilePath + FilenameUtils.getBaseName(childPdfFile.getName()) + DOC.getSuffix();
            pdf.saveToFile(childDocFileName, FileFormat.DOCX);
        }
        return docFilePath;
    }

}
