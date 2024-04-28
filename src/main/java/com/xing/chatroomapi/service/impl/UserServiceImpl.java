package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.mapper.UserMapper;
import com.xing.chatroomapi.pojo.entity.User;
import com.xing.chatroomapi.pojo.vo.UserVO;
import com.xing.chatroomapi.service.UserService;
import com.xing.chatroomapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO login(Integer userId, String password) throws BusinessException {
        //数据库中查询
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id",userId).eq("password",DigestUtils.md5DigestAsHex(password.getBytes()));
        User targetUser = userMapper.selectOne(query);

        //如果user是null，账号或密码错误！
        if(targetUser == null){
            throw new BusinessException("账号或密码错误！");
        }

        //用户密码弄成null
        targetUser.setPassword(null);

        //登录成功
        UserVO result = new UserVO();
        result.setAvatar(targetUser.getAvatar());
        result.setNickName(targetUser.getNickName());
        result.setId(targetUser.getId());

        //生成token
        HashMap<String, Object> params = new HashMap<>();
        params.put("user",targetUser);
        String token = JwtUtil.createJWT(MessageConstant.SECRETE_KEY, MessageConstant.EXPIRATION_TIME, params);
        result.setToken(token);

        //返回结果
        return result;
    }

    @Override
    public Integer register(String nickName, String password) throws BusinessException {

        //封装实体类entity
        User user = new User();
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setNickName(nickName);

        //结果
        int result = userMapper.insert(user);

        //插入事变，抛出异常
        if(result < 1){
            throw new BusinessException("注册失败！");
        }

        //插入成功，返回ID
        return user.getId();
    }
}
