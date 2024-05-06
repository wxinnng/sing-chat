package com.xing.chatroomapi.exception;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 * @DESCRIPTION: 业务异常类
 */
public class BusinessException extends Exception{
    public BusinessException(){}
    public Integer code;
    public BusinessException(String msg){
        super(msg);
    }
    public BusinessException(String msg,Integer code){
        super(msg);
    }
}
