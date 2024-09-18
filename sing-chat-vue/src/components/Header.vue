<template>
  <div class="header">
    <div class="radius">

      <el-dropdown style="margin-left: 5%">
        <div class="avatar" id="ch">
          <el-avatar
              :size="'large'"
              shape="square"
              style="width: 50px;"
              :src="user.avatar"
          />
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <!-- 以后可以弄更多的选项 -->
            <el-dropdown-item @click="loadUserInfo">查看我的信息</el-dropdown-item>
            <el-dropdown-item @click="handleExit">退出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <div class="nick" id="ch">
        <div class="chinese">{{user.nickName}} </div>
      </div>
      <div class="search-input" id="ch">
        <el-input
            v-model="search"
            style="width: 240px"
            size="large"
            placeholder="快来这寻找你的好友吧~"
            :suffix-icon="Search"
        />

      </div>
      <div class="search-button" id="ch">
        <el-button :icon="Search" @click="handleSearch" circle />
      </div>
      <div class="message-icon" >
<!--        气泡信息框-->
        <el-popover
          :visible="popoverIsShow"
          placement="bottom"
          width="400"
          trigger="click"
        >
          <template #reference>
            <el-button @click="handleShowApplication" style="width: 20px; height: 22px">
              <el-badge
                  :show-zero="false"
                  :value="messageList.length">
                <el-icon size="25"><Message/></el-icon>
              </el-badge>
            </el-button>
          </template>
          <el-scrollbar height="280px" class="message-scrollbar">
            <el-alert
                      :closable="false"
                      type="info"
                      style="margin-top: 10px"
                      v-for="item in messageList" :key="item.id">
              <div class="message-content">
                <div class="content-avatar">
                  <el-avatar :src="item.avatar"/>
                </div>
                <div class="content-font">
                  <p style="line-height: 17px;font-size: 17px ;text-decoration: underline">
                    {{item.nickName}}
                  </p>
                  <p v-if="item.type ===0 ">
                    想成为您的好友
                  </p>
                  <p v-else>
                    想加入您的群聊
                  </p>
                </div>
                <div class="content-button">
                  <el-button @click="handleApprove(item.id,item.type)" type="success" size="small">同意</el-button>
                  <el-button @click="handleReject(item.id,item.type)" type="danger" size="small">拒绝</el-button>
                </div>
              </div>

            </el-alert>
          </el-scrollbar>
          <el-button type="info" @click="popoverIsShow = false">关闭</el-button>
        </el-popover>
      </div>
      <div class="more" @click="handleCreateGroupDialogShow">
        <svg t="1715059389702" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2561" width="32" height="32"><path d="M694.445 406.059H547.641V260.478c0-19.489-15.991-35.346-35.641-35.346-19.654 0-35.641 15.858-35.641 35.346v145.581H329.552c-19.651 0-35.641 15.858-35.641 35.344 0 19.489 15.991 35.346 35.641 35.346h146.807v145.584c0 19.486 15.988 35.344 35.641 35.344 19.651 0 35.641-15.858 35.641-35.344V476.749h146.804c19.654 0 35.641-15.858 35.641-35.346 0.001-19.487-15.987-35.344-35.641-35.344z" p-id="2562" fill="#ffffff"></path><path d="M744.143 63.944H279.857c-111.142 0-201.567 89.578-201.567 199.679v365.165c0 110.101 90.309 199.679 201.31 199.679h85.221l98.702 114.808c1.891 2.2 3.995 4.085 6.201 5.771 0.361 0.376 0.636 0.812 1.015 1.174 6.676 6.34 15.436 9.836 24.681 9.836 9.801 0 18.931-3.886 25.699-10.868l116.82-120.72h106.25c111.119 0 201.521-89.578 201.521-199.679V263.623c0-110.101-90.425-199.679-201.567-199.679z m130.281 564.845c0 71.104-58.42 128.969-130.235 128.969H622.758c-9.662 0-19.026 3.975-25.725 10.87l-101.602 104.99-107.667-116.835-61.115 0.974H279.6c-71.696 0-130.027-57.865-130.027-128.969V263.623c0-71.101 58.443-128.966 130.284-128.966h464.286c71.838 0 130.281 57.865 130.281 128.966v365.166z" p-id="2563" fill="#ffffff"></path></svg>      </div>

    </div>

    <!--  搜索的dialog-->
    <el-dialog
              v-model="searchIsShow"
               class="search-dialog"
               width="400"
               :show-close="false"
               style="height: 390px; border-radius: 20px; padding: 0;width: 20%"
               center>

<!--      数据为空-->
     <template v-if="searchContent==null">
       <el-empty description="你的好友是空气吗~"/>
     </template>
<!--      用户信息-->
      <template v-else-if="searchContent.nickName != null">
        <div class="header-search-dialog-avatar-father">
          <div class="header-search-dialog-avatar">
            <img :src="searchContent.avatar" style="height: 90px;width: 90px"/>
          </div>
        </div>
        <div class="header-search-dialog-content-1">
          <div>
            <el-tag type="primary" style=" height: 27px;width: 150px;">
              <p style="font-size: 20px">
                {{searchContent.id}}
              </p>
            </el-tag>
          </div>
        </div>
        <div class="header-search-dialog-content">
          <!---->
          <p style="font-size: 20px">
            {{searchContent.nickName}}
          </p>
        </div>
        <div class="header-search-dialog-button">
          <div>
            <el-button type="success" @click="handleApplication" size="large">添加好友</el-button>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="header-search-dialog-avatar-father">
          <div class="header-search-dialog-avatar">
            <img src="https://tse4-mm.cn.bing.net/th/id/OIP-C.1K4QXQUQJz6yBZ0jEODdGQAAAA?w=198&h=198&c=7&r=0&o=5&dpr=2&pid=1.7" style="height: 90px;width: 90px"/>
          </div>
        </div>
        <div class="header-search-dialog-content-2">
          <div>
            <el-tag type="primary" style=" height: 27px;width: 150px;">
              <p style="font-size: 20px">
                {{searchContent.id}}
              </p>
            </el-tag>
          </div>
        </div>
        <div class="header-search-dialog-content-s">
          <div>
            <el-tag type="primary" style=" height: 27px;width: 150px;">
              <p style="font-size: 20px">
                人数：{{searchContent.curNum}}
              </p>
            </el-tag>
          </div>
        </div>
        <div class="header-search-dialog-content">
          <!---->
          <p style="font-size: 20px">
            {{searchContent.groupName}}
          </p>
        </div>
        <div class="header-search-dialog-button-2">
          <div>
            <el-button type="success" @click="handleApplication" size="large">申请加入</el-button>
          </div>
        </div>
      </template>
    </el-dialog>
    
<!--    创建群聊-->
    <el-dialog
      v-model="createGroupIsShow"
      :show-close="false"
      center
      style="background-color: #f8f7f5;border-radius: 20px; border: 1px solid wheat;width: 400px;height: 500px;"
      class="group_create"
    >
      <div class="create-group-name">
        <el-tag type="success" size="large">名称</el-tag>
        <br>
        <hr />
        <br>
        <el-input style="width: 200px;"
          maxlength="10"
          placeholder="写一个心仪的名字吧"
          show-word-limit
          v-model="createInputValue.groupName"
        ></el-input>
      </div>
      <div class="create-group-info">
        <el-tag type="primary" size="large">简介</el-tag>
        <br>
        <hr />
        <br>
        <el-input style="width: 250px;"
                  maxlength="20"
                  placeholder="简单介绍下你的群聊吧"
                  show-word-limit
                  v-model="createInputValue.groupInfo"
        ></el-input>
      </div>
      <div class="create-group-member">
        <el-tag type="danger" size="large">添加成员</el-tag>
        <br>
        <hr />
        <br>
        <div class="user-tip" style="height: 50px ;background-color: white">
          <el-scrollbar>
            <div class="scrollbar-flex-content">
              <p v-for="(item,index) in createInputValue.members" :key="item">
                <el-alert
                    @close="handleRemoveMember(index)"
                    style="margin-right:20px;width: 150px;height: 50px"
                    type="success">{{ item.nickName }}
                </el-alert>
              </p>
            </div>
          </el-scrollbar>
        </div>
        <br>
        <div class="user-list">
          <el-dropdown
              placement="bottom"
              @command="handleAddMember"
          >
            <el-link type="primary">
              选择好友
            </el-link>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="(item,index) in userList" :key="item.id" :command="index">
                  <div class="userList-item">
                    <img :src="item.avatar" style="width: 25px;height: 25px" alt=""/>
                    {{item.nickName}}
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <br>
      <div class="create-group-button">
        <el-button type="primary" @click="HandleCreateGroup" size="default">确认</el-button>
        <el-button type="info" @click="createGroupIsShow = false" size="default">取消</el-button>
      </div>
    </el-dialog>

<!--    userInfo dialog-->
    <el-dialog
        v-model="userInfoIsShow"
        :show-close="false"
        center
        style="background-color: #f8f7f5;border-radius: 20px; border: 1px solid wheat;width: 400px;height: 500px;"
        class="user-info">
      <div v-if="currentUserInfoShow === 1">
        <div class="user-info-image" style="margin-top:10px ; height: 80px">
          <p style="line-height: 80px;">
            头像
          </p>
          <div class="user-info-image-img">
            <el-upload
                action="http://60.205.141.200:8080/user/upAvatar"
                :show-file-list="false"
                :headers="headers"
                :on-success="handleAvatarSuccess"
            >
              <div
                  class="avatar-uploader"
                  style="width: 50px;height: 50px ;border-radius: 15px;
              border: 1px solid black;
              overflow: hidden;
              "
              >
                <img :src="user.avatar" style="height: 50px;width: 50px;" />
              </div>
            </el-upload>
          </div>
        </div>
        <div class="user-info-nick" style="height: 80px">
          <p style="line-height: 80px">
            昵称
          </p>
          <p style="line-height: 80px;margin-left: 220px">{{user.nickName}}</p>
          <div class="user-info-change-button"
          >
            <el-button @click="handleChangeUserInfo('nickName')" link type="info"><el-icon><Right /></el-icon></el-button>
          </div>
        </div>
        <div class="user-info-age" style="height: 80px">
          <p style="line-height: 80px;width: 220px">
            年龄
          </p>
          <p style="line-height: 80px;margin-left: 100px">
            {{user.age}}
          </p>
          <div >
            <el-button @click="handleChangeUserInfo('age')" style="margin-left: 30px" class="user-info-change-button" link type="info"><el-icon><Right /></el-icon></el-button>
          </div>
        </div>
        <div class="user-info-email" style="height: 80px">
          <p style="line-height: 80px;width: 50px">
            邮箱
          </p>
          <p style="line-height: 80px;margin-left: 100px;width: 180px">
            {{user.email}}
          </p>
          <div style="margin-left: 20px">
            <el-button @click="handleChangeUserInfo('email')" class="user-info-change-button"link type="info"><el-icon><Right /></el-icon></el-button>
          </div>
        </div>
        <div class="user-info-phone" style="height: 80px">
          <p style="line-height: 80px;width: 200px">
            手机号
          </p>
          <p style="line-height: 80px;width: 130px">
            {{user.phone}}
          </p>
          <div style="margin-left: 19px">
            <el-button  @click="handleChangeUserInfo('phone')" link class="user-info-change-button-phone" type="info"><el-icon><Right /></el-icon></el-button>
          </div>
        </div>
      </div>
      <div v-else>
        <el-button  @click="currentUserInfoShow = 1" link  type="info" size="large">
          <el-icon><Back /></el-icon>
          <el-button @click="postUpdateUserInfo" :disabled="saveButtonIsDisabled" style="margin-left: 300px" size="default" type="success">保存</el-button>
        </el-button>
        <br>
        <br>
        <br>
        <el-input  v-model="currentPropertyValue" style="margin-left: 20px;width: 300px"/>

      </div>
    </el-dialog>

  </div>

</template>

<script setup>
import { ref ,onMounted ,reactive,watch} from 'vue'
import service from '@/util/request.js'
import {message,notification} from '@/util/Utils.js'
import {useUserStore} from "@/stores/useUserStore.js";
import {
  Search,
  Message,
  Right,
  Back
} from '@element-plus/icons-vue'
//用户信息
const userStore = useUserStore()
const user = userStore.user
//展示用户信息
const userInfoIsShow = ref(false)
onMounted(async () => {
  //加载信息
  await loadMessage()
})
//加载信息
const loadMessage = async ()=> {
  var res = await service.get('/user/message')

  if(res.data.code === 0 ){
    message("网络异常","error")
    return
  }
  messageList.value = res.data.data
}
//search dialog
const searchIsShow = ref(false)
// create group dialog
const createGroupIsShow = ref(false)
//搜索框内容
const search = ref('')
//查找出的内容
const searchContent = ref({
  id:null,
  groupName:null, // 通过groupName来判断，
  ownerId:null,
  curNum:null,
  createTime:null,
  nickName:null,
  avatar:null,

})
//搜索用户
const handleSearch = async () => {

  if(search.value === '') {
    message("没有人的ID是空白哦",'error')
    return
  }

  var isNotNum = isNaN(parseInt(search.value))

  if(isNotNum){
    message("亲，我们只支持用户ID和群号查询",'error')
    return
  }

  var res = await service.get(`/user/search?id=${search.value}`)

  if(res.data.code === 0){
    message("网络异常 :(","error")
    return
  }

  searchContent.value = res.data.data

  searchIsShow.value = true
}
//发送请求
const handleApplication = async () => {
  console.log(searchContent.value)
  console.log(searchContent.value.nickName )
  var res  = await service.post('/user/postApplication',{},{
    params:{
      toTarget:searchContent.value.id,
      type: searchContent.value.nickName === undefined ? 1 : 0
    }
  })
  if(res.data.code === 1){
    message("请求发送成功!","success")
  }else{
    message(res.data.msg,"error")
  }
  searchIsShow.value = false
}
//信息数量
const messageList  = ref([])
//加载申请信息
const handleShowApplication = async () => {
  popoverIsShow.value = true
}
//气泡框
const popoverIsShow = ref(false)
//处理同意申请
const handleApprove = async (id,groupType)=> {
  var res = await service.get(`/user/operateApplication?applicationId=${id}&type=1&groupType=${groupType}`,{})

  if(res.data.code === 1){
    userStore.changeReloadUserList()
    notification("操作成功！","快去和ta聊天吧！！！","success")
  }else{
    notification("操作失败","网络出了点异常。。。","error")
  }

  //重新加载数据
  await loadMessage()

}
//处理拒绝申请
const handleReject = async (id,groupType) => {
  console.log(id)
  var res = await service.get(`/user/operateApplication?applicationId=${id}&type=2&groupType=${groupType}`,{})
  if(res.data.code === 1){
    notification("操作成功","你成功拒绝了ta！ ","success")
  }else{
    notification("操作失败","网络都让你拒绝ta。。。","error")
  }

  //重新加载数据
  await loadMessage()

}
//显示创建group的dialog，并且加载相应的信息
const handleCreateGroupDialogShow = async () => {
  //显示dialog
  createGroupIsShow.value = true

  var res = await service.get("/user/createGroupUserList")
  if(res.data.code !== 1){
    message("网络异常！",'error')
  }

  userList.value = res.data.data

}
//创建群聊时，输入的内容
const createInputValue = reactive({
  groupName:"",
  groupInfo:"",
  members:[]
})
//好友信息列表
const userList = ref([])
//创建group时，把好友添加为群友
const handleAddMember = (index) => {
  //将选中的用户放到数组中去
  createInputValue.members.push(userList.value[index])
  console.log(userList.value[index])
  userList.value.splice(index,1)  //删除对应用户信息
}
//删除members中的member
const handleRemoveMember = (index) => {
  userList.value.push(createInputValue.members[index])
  createInputValue.members.splice(index,1)
}
//创建群聊
const HandleCreateGroup = async () =>{
  let groupName = createInputValue.groupName
  let groupInfo = createInputValue.groupInfo
  if(groupName === "" || groupInfo === ""){
    message("信息不完整","error")
    return ;
  }


  let memberIds = []  //成员id信息

  for (var item in createInputValue.members){
    memberIds.push(createInputValue.members[item].id)
  }

  var res = await service.post('/group/create',{
    groupName:groupName,
    groupInfo:groupInfo,
    memberIds:memberIds
  })

  if(res.data.code !== 1){
    message("操作异常",'error')
    return ;
  }


  message("创建成功！",'success')
  createGroupIsShow.value = false

}
//退出登录
const handleExit = async () => {

  //对pinia进行操作
  userStore.logout()
  await loadMessage()

}
//查看用户信息
const loadUserInfo = async () => {
  userInfoIsShow.value = true
}
//监视reloadMessage
watch(() => userStore.reloadMessage,async (newVal,oldValue)=>{
  //数值发送变化，message刷新
  await loadMessage()
})
//当前用户信息面板展示的内容 :
const currentUserInfoShow = ref(1)
//当前点击的属性
const currentProperty = ref("")
//当前修改的属性内容
const currentPropertyValue = ref("")
//显示对应属性的用户信息
const handleChangeUserInfo = (property) => {
  currentProperty.value = property
  currentUserInfoShow.value = 0  //修改信息的页面
  //监听修改的内容
  currentPropertyValue.value = user[property]
}
//修改用户信息
const postUpdateUserInfo = async () => {

  //修改userStore中的信息
  user[currentProperty.value] = currentPropertyValue.value


  var res = await service.post('/user/updateUserInfo',user)
  if(res.data.code !== 1){
    message('网络异常！','error')
  }

  //跳转页面
  currentUserInfoShow.value = 1

}
//决定button能不能使用
const saveButtonIsDisabled = ref(true)
//监听currentPropertyValue的变化，只有与原来的值不同的时候才可以改变
watch(() => currentPropertyValue.value, (newVal, oldValue)=>{

  if(newVal !== user[currentProperty.value]){
    saveButtonIsDisabled.value = false
  }else{
    saveButtonIsDisabled.value = true
  }
})
//上传图片是携带的请求头
const headers = ref({
  token:user.token
})
//图片上传成功！
const handleAvatarSuccess = (response,file,fileList) => {
  user.avatar = response.data
}

</script>

<style>
.user-info-change-button{
  margin-left: auto;
  flex-shrink: 0;
}
.avatar-uploader:hover{
  border: 1px solid aqua;
}
.user-info-change-button-phone{
  margin-left: auto;
  flex-shrink: 0;
}
.user-info-image-img{
  margin-left: 250px;
}
.user-info>div{
  font-size: 16px;
}
.user-info-image{
  border-bottom: 1px solid black;
  display: flex;
  align-items: center;
}
.user-info-email{
  border-bottom: 1px solid black;
  display: flex;
  align-items: center;
}
.user-info-nick{
  border-bottom: 1px solid black;
  display: flex;
  align-items: center;
}
.user-info-phone{
  border-bottom: 1px solid black;
  display: flex;
  align-items: center;
}
.user-info-age{
  border-bottom: 1px solid black;
  display: flex;
  align-items: center;
}


.scrollbar-flex-content{
  display: flex;
}
.create-group-name{
  margin-bottom: 10%;
}
.create-group-info{
  margin-bottom: 10%;
}
.create-group-button{
  display: flex;
  justify-content: center;
  align-items: center;
}
.header{
  padding: 0;
  margin: 0;
  height: 70px;
}
.message-content{
  display: flex;
  justify-content: center;
  align-items: center;
}
.message-content>div{
  display: inline;
}
.content-font{
  font-size: 15px;
  width: 150px;
  margin-left: 10%;
}
.content-button{

  width: 190px;
}

.header-search-dialog-avatar-father{
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top :0;
}
.header-search-dialog-avatar{
  width: 90px;
  height: 90px;
  border: 1.5px solid black;
  border-radius: 25%;
  overflow: hidden;

}
.header-search-dialog-content-1{
  margin-top: 55px;
}
.header-search-dialog-content-2{
  margin-top: 45px;
}
.header-search-dialog-content{
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-search-dialog-button{
  margin-top: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.header-search-dialog-button-2{
   margin-top: 30px;
   display: flex;
   justify-content: center;
   align-items: center;
 }

.search-dialog{
  border: 1px solid black;
  height: 450px;
  width: 300px;
  display: grid;
  place-items: center;
}

.radius {
  background-color: #272A37;
  height: 90%;
  margin-left: 1%;
  width: 98%;
  border: 2px solid var(--el-border-color);

  border-radius: 20px;
  border-color: black;
  display: flex;
  align-items: center;
}
#ch{
  display: inline;
}
.avatar{
  margin-left: 3%;
  border: 2px solid white;
  border-radius: 15px;
  width: 50px;
  height: 50px;
  overflow: hidden;
}
.nick{
  margin-left: 2%;
  margin-right: 34%;
  width: 200px;
}
.search-input{
  margin-right: 10px;
}
.more{
  margin-left: 20px;
}
.more:active{
  border: 1px solid var(--el-border-color);
}
p{
  font-family: Consolas,cursive;
}
.message-icon{
  display: inline;
  width: 25px;
  height: 25px;
  margin-left: 2%;
}
.message-icon>el-icon:active{
  background-color: black;
}

</style>
