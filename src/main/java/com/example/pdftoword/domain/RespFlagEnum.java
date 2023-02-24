package com.example.pdftoword.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RespFlagEnum {

    SUCCESS("S", "成功"),
    FAIL("F", "失败");

    private String code;
    private String desc;

    public static RespFlagEnum covert(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(values()).filter(bean -> bean.code.equals(code))
                .findAny().orElse(null);
    }

}
