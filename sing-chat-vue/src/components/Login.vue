<script setup>
import {ref} from "vue";
import {message} from '../util/Utils.js'
import {getRandom} from "../util/Utils.js";
import {
  Right
} from '@element-plus/icons-vue'
import service from "@/util/request.js";
import {useUserStore} from "@/stores/useUserStore.js";
import { useRouter } from 'vue-router'




//路由
const router = useRouter()
//展示的标题
const label = ref("起个好听的名字吧~");
//input content
const inputContent = ref(null)
//useStore
const userStore = useUserStore()
//通过点击次数，进行不同的渲染
let clickNum = 0
//注册用户的信息
const registerUser = ref({})
//注册成功框是否显示
const registerSuccessDialogIsShow = ref(false)
//注册成功的唯一ID
const registerId = ref(1001)
//按钮上的内容
const buttonContent = ref("我想好了，下一步")
//处理每次点击事件
const handleEverClick = async () => {
  if (inputContent.value == null || inputContent.value === "") {
    message("你还没输入呢","error")
    return
  }
  if (clickNum === 0) {
    //第一次点击,下一页应该显示密码
    label.value = "想一个难点的密码哦"
    buttonContent.value = "注册"

    //把输入的内容放到user中
    registerUser.value.nickName = inputContent.value

    //清空输入的内容
    inputContent.value = ""

    //点击次数加一
    clickNum += 1;
  }else{


    //把密码放到user对象中
    registerUser.value.password = inputContent.value

    //进行注册操作
    await register()

  }
}
//注册
const register = async () => {
  var result = await service.post("/user/register",{},{
    params:{
      nickName:registerUser.value.nickName,
      password:registerUser.value.password
    }
  })
  console.log(result.data.data)
  //进行对应的操作
  if(result.data.code === 1){
    //注册成功！:弄个弹窗
    registerSuccessDialogIsShow.value = true
    registerId.value = result.data.data
  }else{
    //注册失败!
    message("注册失败！","error")
  }
}
//登录弹出框是否显示
const loginDialogIsShow = ref(false)
//登录方法
const goToLogin =async ()=>{
  if(loginID.value == null  || loginID.value === ""){

    //内容填写不完整
    message("亲，内容填完整","error")
    return;
  }
  var res = await service.post("/user/login",{},{
    params:{
      userId:loginID.value,
      password:loginPassword.value
    }
  })
  //用户信息
  var data = res.data.data

  if(res.data.code === 1){
    message("登录成功！","success")
    //用户信息放到pinia中去
    userStore.changeUser(data.id,data.nickName,data.avatar,data.token,data.email,data.phone,data.age)
    //路由跳转
    await router.push("/main/chat")
  }else{
    message("账号或密码错误","error")
  }
}
const changeDialog = async () => {
  //去掉信息弹出框弹出框
  registerSuccessDialogIsShow.value = false
  //打开登录弹出框
  loginDialogIsShow.value = true
}
//用户登录信息
const loginID = ref()
const loginPassword = ref()
const show = ref(true)

//插图
const loginImage = ref(`/loginImg/login_${getRandom(1,11)}.jpg`)
</script>
<template>
 <el-dialog
     v-model="show"
     style="width: 950px; border: 2px solid wheat;padding: 0;border-radius: 50px"
     :show-close="false"

 >
   <div class="login-container">
     <el-container >
       <el-header height="100px" class="login-header">
         <div class="logo">
           <img src="../assets/star.ico" style="height: 50px;width: 50px"/>
         </div>
         <div class="logo-font">
           SingingChat
         </div>
       </el-header>
       <el-main style="height: 350px" class="main-body">
         <el-container class="main-container">
           <el-aside class="main-container-left">
             <div class="login_main_image">
               <img :src="loginImage" style=" height:345px; width:268px"/>
             </div>
           </el-aside>
           <el-main  class="main-container-right">
             <div class="register-main">
               <div class="register-font">
                 <p style="font-size:20px;color: #238E68">
                   {{label}}
                 </p>
               </div>
               <div class="register-input">
                 <el-input type="text" v-model="inputContent"></el-input>
                 <br>
                 <br>
                 <el-button  @click="handleEverClick"><p style="color:  #FF7F00">{{buttonContent}}</p> <el-icon style="color: #FF7F00"><Right /></el-icon></el-button>
               </div>
             </div>
           </el-main>
         </el-container>
       </el-main>
       <el-footer height="50px" class="login-footer">
         <div class="login-link">
           <el-button
               @click="changeDialog"
               type="primary"
               link
           >
             我有账号，去登录
           </el-button>
         </div>
       </el-footer>
     </el-container>


     <!--    注册成功框-->
     <el-dialog
         v-model="registerSuccessDialogIsShow"
         width="420"
         style="border: 1px solid wheat;border-radius: 20px"
         align-center
         center
     >
       <div class="dialog-message">
         <div style="color: #238E68;font-size: 30px">
           注册成功
         </div>
         <br>
         <div style="font-size: 17px; color: #ED723F;font-family: '幼圆', Times, serif">记住你的唯一ID哦，它是独一无二的！！！</div>
         <br>
         <div style="font-size: 25px; color: #ED723F;font-family: 'Verdana', Times, serif">
           <el-tag type="success" size="large">
             {{registerId }}
           </el-tag>
         </div>
         <br>
       </div>
       <template #footer>
         <div class="dialog-footer">
           <el-button type="primary" @click="changeDialog" >
             我记住了,去登录
           </el-button>
         </div>
       </template>
     </el-dialog>

     <!--    登录弹出框-->
     <el-dialog
         v-model="loginDialogIsShow"
         width="400"
         align-center
         center
         :show-close="false"
         style="border-radius: 20px;border: 2px solid antiquewhite"

     >
       <el-container>
         <el-header class="login-dialog-header">
           <div class="logo-font">
             <img src="../assets/star.ico" style="height: 40px;width: 40px"/>
             SingingChat
           </div>
         </el-header >
         <el-main class="login-dialog-main">
           <div>
             账号：
             <el-input style="width: 200px"
                       v-model="loginID"
                       placeholder="请输入唯一ID"/>
             <br>
             <br>
             密码：
             <el-input style="width: 200px"
                       v-model="loginPassword"
                       placeholder="请输入你的密码" type="password"/>
           </div>
         </el-main>
         <el-footer class="login-dialog-footer">
           <div>
             <el-button @click="goToLogin" type="success"> 登录</el-button>
             <el-button @click="loginDialogIsShow = false">取消</el-button>
           </div>
         </el-footer>
       </el-container>
     </el-dialog>

   </div>
 </el-dialog>
</template>

<style scoped>
.login-dialog-header {
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-dialog-main{
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-dialog-footer{
  display: flex;
  justify-content: center;
  align-items: center;
}
.login-container {
  width: 100%;
}
.login-header{
  display: flex;
  align-items: center;
}
.logo{
  display:inline;
  margin-left: 8%;
}
.logo-font{
  display:inline;
  font-family: "Comic Sans MS", Times, serif;
  font-size: 34px;
  color: #000000;
}
.login-footer{
  position: relative;
  align-items: center;
}
.login-link{
  position: absolute;
  top: 30%;
  right: 10%;
  display: inline-flex;
}

.main-body{
  margin: 0;
  padding: 0;
}
.register-main{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.register-font{
  margin-top: 10%;
  font-family: "幼圆", Times, serif;
}
.register-input{
  margin-top: 20px;
  color:  #FF7F00;
}
.main-container{
  width: 100%;
  height: 100%;
}
.main-container-left{
  width: 40%;
}
.login_main_image{
  margin-left: 20%;
}
.main-container-right{
  padding: 0;
}
.dialog-message{
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中，如果需要的话 */
}
</style>