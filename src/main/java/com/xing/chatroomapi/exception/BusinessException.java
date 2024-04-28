package com.xing.chatroomapi.exception;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 * @DESCRIPTION: 业务异常类
 */
public class BusinessException extends Exception{
    public BusinessException(){}
    public BusinessException(String msg){
        super(msg);
    }
}
