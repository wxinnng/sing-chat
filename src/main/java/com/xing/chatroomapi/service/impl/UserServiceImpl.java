package com.xing.chatroomapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xing.chatroomapi.constant.MessageConstant;
import com.xing.chatroomapi.exception.BusinessException;
import com.xing.chatroomapi.mapper.*;
import com.xing.chatroomapi.pojo.ChatSession;
import com.xing.chatroomapi.pojo.Relation;
import com.xing.chatroomapi.pojo.dto.CreateGroupUserDTO;
import com.xing.chatroomapi.pojo.entity.*;
import com.xing.chatroomapi.pojo.vo.ApplicationResultVO;
import com.xing.chatroomapi.pojo.vo.ApplicationVO;
import com.xing.chatroomapi.pojo.vo.UserVO;
import com.xing.chatroomapi.service.GroupService;
import com.xing.chatroomapi.service.UserService;
import com.xing.chatroomapi.util.BaseContext;
import com.xing.chatroomapi.util.JwtUtil;
import com.xing.chatroomapi.util.RedisUtil;
import com.xing.chatroomapi.websocket.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:WangXing
 * @DATE:2024/4/26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Autowired
    private GroupService groupService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private ChatServer chatServer;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private UserRelationMapper userRelationMapper;

    @Override
    public List<ChatSession> loadRelationList() throws BusinessException {

        Integer currentUser = BaseContext.getCurrentUser();

        if(currentUser == null){
            throw new BusinessException("身份认证失败！");
        }

        //放在左边的数据有两种，1：group 2：聊天
        List<ChatSession> result = new ArrayList<>();

        //1. 获取聊天内容
        List<User> userRelationList = userRelationMapper.getUserRelationList(currentUser);

        //放到result中去
        result.addAll(userRelationList);

        //2.group 内容
        List<Group> groupRelation = groupMapper.getGroupRelation(currentUser);

        //放到result中去
        result.addAll(groupRelation);

        return result;
    }

    @Override
    public List<CreateGroupUserDTO> loadCreateGroupUserList() {
        return userRelationMapper.getCreateGroupUserDTOListByUserId(BaseContext.getCurrentUser());
    }

    @Override
    public void removeUser(Integer id) {
        //删除好友就是删除userRelation表中对应的信息
        userRelationMapper.removeRelation(id,BaseContext.getCurrentUser());
    }

    @Override
    public Long getUserSpace() {
        return fileInfoMapper.getUserSpace(BaseContext.getCurrentUser());
    }

    @Override
    public HashMap<Integer, CreateGroupUserDTO> loadIdUserInfoHash(Integer id, Integer type) {

        HashMap<Integer,CreateGroupUserDTO> hash = new HashMap<>();

        if(type == 1) {
            //user
            CreateGroupUserDTO userBaseInfo = userMapper.getUserBaseInfo(id);
            hash.put(id,userBaseInfo);

        }else{
            //group

            //先找到对应群中的所有成员信息
            List<Integer> ids = memberMapper.getMemberIdsByGroupId(id);

            //对应的用户信息
            List<CreateGroupUserDTO> userBaseInfoBatch = userMapper.getUserBaseInfoBatch(ids);

            //放到hash结构中
            for (CreateGroupUserDTO baseInfoBatch : userBaseInfoBatch) {
                hash.put(baseInfoBatch.getId(),baseInfoBatch);
            }

        }
        return hash;
    }

    @Override
    public Integer getUserStatusById(Integer userId) {
        return redisUtil.exists(MessageConstant.USER_ONLINE_REDIS_KEY + userId) ? 1 : 0;
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateUserInfo(user);
    }

    @Override
    public Relation loadRelationDetail(Integer id, Integer type) {
        if(type == 1){
            return userMapper.getUserRelationDetail(id,BaseContext.getCurrentUser());
        }else{
            return groupService.getGroupRelationDetail(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateApplication(Integer applicationId, Integer type,Integer groupType) throws BusinessException {

        //先从数据库中查询
        Application update = applicationMapper.selectById(applicationId);

        if(update == null){
            throw new BusinessException("参数异常");
        }

        //socket信息提醒
        ApplicationResultVO applicationResultVO = new ApplicationResultVO();
        applicationResultVO.setUserId(update.getFromUser());
        applicationResultVO.setResult(type);  //1 同意 2 拒绝
        applicationResultVO.setPushType(1);


        //更新对应的状态
        update.setStatus(type);
        update.setId(applicationId);
        applicationMapper.updateById(update);

        //如果拒绝的话，直接跳出
        if(type == 2) {
            applicationResultVO.setMsg("你的请求被拒绝了... :(");
            chatServer.sendMessageToClient(applicationResultVO);
            return;
        }

        //同意申请，修改对应的group信息
        if(groupType == 0){
            //在user_relation表中，插入对应的两条信息
            UserRelation relation = new UserRelation();

            //封装信息
            relation.setUserOne(update.getToUser());
            relation.setCreateTime(LocalDateTime.now());
            relation.setUserTwo(update.getFromUser());

            //插入第一条信息
            userRelationMapper.insert(relation);

            //封装信息
            relation.setId(null);
            relation.setUserOne(update.getFromUser());
            relation.setUserTwo(update.getToUser());

            //插入第二条信息
            userRelationMapper.insert(relation);

        }else{
            //群聊:group信息已经创建
            //update group message
            groupMapper.incrCurNum(update.getGroupId());

            //插入member信息
            Member member = new Member();
            member.setUserId(update.getFromUser());
            member.setGroupId(update.getGroupId());
            member.setJoinTime(LocalDateTime.now());
            member.setIsOwner(0);
            memberMapper.insert(member);

        }

        //申请同意成功！
        applicationResultVO.setMsg("你的申请被同意了，快去聊天吧~");
        chatServer.sendMessageToClient(applicationResultVO);
    }

    @Override
    public List<ApplicationVO> loadMessage() {
        Integer userId = BaseContext.getCurrentUser();
        //加载所有的申请信息 status = 0 待同意
        return applicationMapper.getApplication(userId);
    }

    @Override
    public void postApplication(Integer toTarget, Integer type) throws BusinessException {
        if(BaseContext.getCurrentUser().equals(toTarget)){
            throw new BusinessException("你是你自己最好的朋友，不用申请~",MessageConstant.CANNOT_BE_YOURSELF_FRIEND);
        }

        if(userRelationMapper.selectByUsers(BaseContext.getCurrentUser(),toTarget)!=null){
            throw new BusinessException("ta都是你的好友了，你还想怎么样！");
        }


        //如果是加入群聊的话，发送的请求是向群主的
        User user = userMapper.selectById(toTarget);

        //群聊ID
        Integer groupId = null;

        if (user == null){
            //可能是申请入群
            QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
            memberQueryWrapper.eq("group_id",toTarget).eq("user_id",BaseContext.getCurrentUser());

            if(memberMapper.selectOne(memberQueryWrapper) != null)
                throw new BusinessException("你已经在群里啦！");

            groupId = toTarget;

            // 不为null，toTarget就是群主的id，向他发送请求即可
            toTarget = memberMapper.getGroupOwnerByGroupId(toTarget);

            //
            if(toTarget == null)
                throw new BusinessException("操作错误！",MessageConstant.PARAMS_ERROR);
        }

        //发送过请求，就不能再发了
        QueryWrapper<Application> applicationQueryWrapper = new QueryWrapper<>();
        applicationQueryWrapper.eq("to_user",toTarget);
        applicationQueryWrapper.eq("from_user",BaseContext.getCurrentUser());
        applicationQueryWrapper.eq("type",type);
        applicationQueryWrapper.eq("status",MessageConstant.PENDING_APPROVAL);

        //数据库中查询
        Application application1 = applicationMapper.selectOne(applicationQueryWrapper);
        if(application1 != null){
            throw new BusinessException("你提交过申请了，在等等吧！",MessageConstant.ALREADY_POST_REQUEST);
        }

        //插入一条请求
        Application application = new Application();
        application.setFromUser(BaseContext.getCurrentUser());
        application.setToUser(toTarget);
        application.setStatus(MessageConstant.PENDING_APPROVAL);
        application.setCreateTime(LocalDateTime.now());
        application.setType(type);
        application.setGroupId(groupId);  //如果不是加入群聊申请的话，groupId就是NUll


        //插入一条数据
        applicationMapper.insert(application);

        //如果用户在线的话，把信息推送给对应的用户，没在线就不用操作
        ApplicationResultVO applicationResultVO = new ApplicationResultVO();
        applicationResultVO.setUserId(toTarget);
        applicationResultVO.setPushType(2);
        chatServer.sendMessageToClient(applicationResultVO);

    }

    @Override
    public ChatSession getUserInfoById(Integer id) {
        User targetUser = userMapper.selectById(id);

        //就直接通过ID查询用户
        if(targetUser != null){
            targetUser.setPassword(null);
            return targetUser;
        }

        return groupMapper.selectById(id);
    }

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
        result.setPhone(targetUser.getPhone());
        result.setAge(targetUser.getAge());
        result.setEmail(targetUser.getEmail());

        //生成token
        HashMap<String, Object> params = new HashMap<>();
        params.put("user",targetUser.getId());
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
