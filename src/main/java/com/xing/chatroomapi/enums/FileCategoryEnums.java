package com.xing.chatroomapi.enums;

/**
 * @Author:86198
 * @DATE:2024/3/14 15:59
 * @DESCRIPTION:
 * @VERSION:1.0
 */

public enum FileCategoryEnums {
    C(1,"c"),
    CPP(2,"cpp"),
    CSS(3,"css"),
    FOLDER(4,"folder"),
    GIF(5,"gif"),
    HTML(6,"html"),
    JAVA(7,"java"),
    JS(8,"js"),
    JPG(9,"jpg"),
    JSON(10,"json"),
    MP3(11,"mp3"),
    MP4(12,"mp4"),
    PDF(13,"pdf"),
    PNG(14,"png"),
    PPT(15,"ppt"),
    SQL(16,"sql"),
    TXT(17,"txt"),
    DOCX(18,"docx"),
    DOC(19,"doc"),
    OTHER(20,"other");


    private Integer category;
    private String code;


    FileCategoryEnums(Integer category, String code) {
        this.category = category;
        this.code = code;
    }



    public static FileCategoryEnums getByCode(String code) {
        for (FileCategoryEnums item : FileCategoryEnums.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return OTHER;
    }
    public Integer getCategory () {
        return category;
    }
    public String getCode () {
        return code;
    }
}