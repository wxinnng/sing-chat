package com.xing.chatroomapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.chatroomapi.pojo.entity.Application;
import com.xing.chatroomapi.pojo.vo.ApplicationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:WangXing
 * @DATE:2024/4/30
 */
@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
    List<ApplicationVO> getApplication(Integer userId);
}
