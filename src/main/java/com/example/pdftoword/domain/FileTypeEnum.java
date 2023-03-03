package com.example.pdftoword.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    PDF("application/pdf", ".pdf", "pdf文件类型"),
    DOC("", ".docx", "doc文件类型"),
    ;

    private String type;

    private String suffix;

    private String desc;

    public static FileTypeEnum covert(String type) {
        if (type == null) {
            return null;
        }
        return Stream.of(values()).filter(bean -> bean.type.equals(type))
                .findAny().orElse(null);
    }

}
