package com.xing.chatroomapi.enums;


public enum UploadStatus {
    UPLOAD_SECONDS(0,"秒传"),
    UPLOADING(1,"上传中"),
    UPLOAD_FINISH(3,"上传完成"),
    UPLOAD_FAIL(4,"上传失败");
    private Integer status;
    private String desc;

    UploadStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
