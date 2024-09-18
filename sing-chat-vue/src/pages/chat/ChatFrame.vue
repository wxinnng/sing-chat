<script setup>
import { ref, nextTick, onMounted, reactive, watch, h, defineAsyncComponent } from "vue";
import { useUserStore } from "@/stores/useUserStore.js";
import { postMessageToServer } from "@/webSocket/index.js";
import service from "../../util/request.js";
import { message, timeArrayToFormattedDateTime } from "@/util/Utils.js";
import Emoji from "@/components/Emoji.vue";
import { ElNotification } from 'element-plus';
import { generateMD5OfFile, uploadFileToServer } from '@/hooks/UploadFile'
import ChatFile from './ChatFile.vue'
import {env_ws_production} from '@/env/index'

/*
  chat list
 */
//挂载组件的时候生效
onMounted(async () => {
  await loadRelationList()
  await focusClick(userStore.lastNickIndex)
  historyEmojiList.value = localStorage.getItem('emojiHistory') ? JSON.parse(localStorage.getItem('emojiHistory')) : []
})
//user store
const userStore = useUserStore()
//当前用户ID
const currentUser = ref(userStore.user.id)
//Relation Cards dom
const userCards = ref([])
//侧边List
const relationList = ref([])
//当前选中的对话关系
const currentRelation = reactive({
  id: null,
  name: null,
  avatar: null,
  type: null,
})
//最后一次点击的索引
const lastClickIndex = ref(null)
//当前用户是否在线
const currentUserIsOnline = ref(0)
//点击样式
const focusClick = async (index) => {

  if (lastClickIndex.value != null) {
    userCards.value[lastClickIndex.value].style.backgroundColor = ""
    userCards.value[lastClickIndex.value].style.border = ""
  }
  userCards.value[index].style.backgroundColor = "lightsteelblue"
  userCards.value[index].style.border = "2px solid aqua"
  userCards.value[index].style.transition = "transform 0.2s"
  lastClickIndex.value = index
  userStore.lastNickIndex = index


  //将点击到的组件的信息传给父亲
  //判断是group还是user
  var targetRelation = relationList.value[lastClickIndex.value]
  if (targetRelation.nickName != null) {
    //好友
    currentRelation.id = targetRelation.id
    currentRelation.name = targetRelation.nickName
    currentRelation.type = 1
  } else {
    currentRelation.id = targetRelation.id
    currentRelation.name = targetRelation.groupName
    currentRelation.type = 2
  }

  //加载聊天记录
  chatHistory.value[lastClickIndex.value] = await loadChatMessageList()

}
//加载聊天记录
const loadChatMessageList = async () => {

  if (currentRelation.type === 1) {
    await loadCurrentUserStatus()
  }

  //后端发送请求，获取对应的信息
  var res = await service.post("/chat/messages", {}, {
    params: {
      user1: currentUser.value,
      user2: currentRelation.id,
      type: currentRelation.type
    }
  })

  var res1 = await service.get("/user/loadUserInfoHash", {
    params: {
      id: currentRelation.id,
      type: currentRelation.type
    }
  })

  if (res.data.code !== 1) {
    message("网络异常!", 'error')
    return;
  }

  //userInfo hash
  currentChattingPeoples.value = res1.data.data

  //返回聊天记录的信息
  return res.data.data
}
//加载用户关系列表
const loadRelationList = async () => {
  var res = await service.get('/user/loadRelations')
  relationList.value = res.data.data
}
//加载当前用户状态
const loadCurrentUserStatus = async () => {
  var res = await service.get('/user/status', {
    params: {
      userId: currentRelation.id
    }
  })

  if (res.data.code !== 1) {
    return;
  }

  currentUserIsOnline.value = res.data.data

}
/*
  chat frame
 */
// 用于判断，当前的聊天的对象  ， 如果是null，就显示el-empty
const nowFrame = ref(1)
//聊天时输入的内容
const inputContent = ref("")
//输入框@input的方法
const messageInput = (e) => {
  inputContent.value = e.target.innerHTML
}
//inner 组件
const innerRef = ref(null)
//scroll 组件
const scrollbarRef = ref(null)
//id -> image
const getUserIndexByUserId = (id) => {
  return currentChattingPeoples.value[id]
}
//socket
const socket = ref(new WebSocket(`${env_ws_production}/chat/${currentUser.value}`))
//发信息
const putMessage = async () => {

  if (inputContent.value === "") {
    return;
  }

  //把信息放到后端先,返回对应的信息{}
  var message = {
    chatId: null,
    userId: currentUser.value,
    targetId: currentRelation.id,
    type: currentRelation.type,
    messageType: 1,
    body: inputContent.value,
  }

  //把信息放到后端
  postMessageToServer(message, socket)
  //等待dom加载完成
  await nextTick()
  //把信息放到push到数组中
  chatHistory.value[lastClickIndex.value].push(message)

  //等待dom加载完成
  await nextTick()

  //输入框中的信息清空
  messageInputDom.value.innerHTML = ''
  inputContent.value = ""

  //滑到最底端
  let max = chatHistory.value[lastClickIndex.value].length * 100
  scrollbarRef.value.setScrollTop(max)

}
//接收信息
socket.value.onmessage = async (messages) => {
  //把字符串解析成对象
  var chatMessage = JSON.parse(messages.data)
  //不是聊天信息，而是推送的信息
  if (chatMessage.pushType !== undefined && chatMessage.pushType !== null) {
    //申请信息
    if (chatMessage.pushType === 1) {
      message(chatMessage.msg, chatMessage.result === 1 ? 'success' : 'info')
      await loadRelationList()
    } else if (chatMessage.pushType === 2) {
      //userStore中的值改变，header组件中监视对应的属性，进行相应的操作
      userStore.reloadMessage++
    }
  }
  //如果不是看的当前的Relation，就发送个提醒
  if (chatMessage.userId !== currentRelation.id && chatMessage.targetId !== currentRelation.id) {
    ElNotification({
      title: '信息提示',
      message: h('i', { style: 'color: teal' }, chatMessage.userId + " 给您发来信息"),
    })
  }
  chatHistory.value[lastClickIndex.value].push(chatMessage)

  //等待dom加载完成
  await nextTick()

  //滑到最底端
  let max = chatHistory.value[lastClickIndex.value].length * 100
  scrollbarRef.value.setScrollTop(max)

}
// 聊天的用户单独放，避免信冗余。
const currentChattingPeoples = ref(null)
//聊天记录
const chatHistory = ref([])
//relation detail is show
const relationDetailDrawerIsShow = ref(false)
//relation detail
const relationDetail = ref({
  id: 10001,
  nickName: null,
  age: null,
  email: null,
  phone: null,
  groupName: null,
  join_time: null,
  avatar: null,
  introduce: null,
  curNum: null,
  createTime: null,
  ownerId: null,
  memberList: [],
})
//在群聊的成员列表里，添加好友
const handleAddFriendByGroup = async (id) => {
  var res = await service.post('/user/postApplication', {}, {
    params: {
      toTarget: id,
      type: 0
    }
  })
  if (res.data.code === 1) {
    message("请求发送成功!", "success")
  } else {
    message(res.data.msg, "error")
  }
}
// 展示关系详情面板
const showRelationDetailDrawer = async () => {
  await loadRelationDetail()
  relationDetailDrawerIsShow.value = true
}
//加载关系详细数据
const loadRelationDetail = async () => {
  let id = currentRelation.id
  let type = currentRelation.type
  var res = await service.get(`/user/relationDetail/${id}/${type}`)
  if (res.data.code !== 1) {
    message("网络异常！", 'error')
  }

  //属性赋值
  relationDetail.value = res.data.data
}
//对store中的属性进行监听，当发生改变的时候，就要刷新对应的userList
watch(() => userStore.reloadUserList, async (newVal, oldVal) => {
  //有了新的用户加入,所以要进行局部userList的刷新
  await loadRelationList()
})
//删除用户
const handleRemoveUser = async (id) => {
  var res = await service.get(`/user/remove/${id}`, {})
  if (res.data.code === 1) {

    message('删除成功！', 'success')

    //关闭信息页面
    relationDetailDrawerIsShow.value = false

    //最后点击的为空
    lastClickIndex.value = null

    //重新加载userList
    await loadRelationList()

  } else {
    message('操作异常！', 'error')
  }
}
//退出群群聊
const handleOutGroup = async (id) => {

  var res = await service.get("/group/outGroup/" + id)

  if (res.data.code === 1) {
    message("操作成功！", 'success')
    lastClickIndex.value = null
  } else {
    message("操作异常！", 'error')
  }

  await loadRelationList()

}
//功能开发中
const canNotUse = () => {
  message('功能还在开发中...', 'info')
}
// 自定义输入框dom 绑定ref
const messageInputDom = ref()
// 选择的emoji
const selectEmoji = (index) => {
  // 没有焦点就获取输入框焦点
  if (document.activeElement !== messageInputDom.value) {
    messageInputDom.value.focus()
  }
  let emojiImg = `<img src="/gif/${index}.gif" width="25" height="25" style="vertical-align: middle;">`
  document.execCommand('insertHTML', false, emojiImg)
  // 保存最近使用的emoji
  recentlyUseEmoji(index)
}
// 最近使用的emoji列表
const historyEmojiList = ref([])
// 保存最近使用的emoji
const recentlyUseEmoji = (index) => {
  let idx = historyEmojiList.value.indexOf(index)
  if (idx < 0) {
    historyEmojiList.value.unshift(index)
  } else {
    historyEmojiList.value.unshift(historyEmojiList.value.splice(idx, 1)[0])
  }
  // 只要两行emoji(16个)
  historyEmojiList.value = historyEmojiList.value.splice(0, 16)
  // 保存记录
  localStorage.setItem('emojiHistory', JSON.stringify(historyEmojiList.value))
}
const fileComponent = defineAsyncComponent(() => import("@/pages/share/ShareItem.vue"))
const currentComponentRef = ref(fileComponent);
const chunkSize = 1024 * 1024 * 5
const showFileDialog = ref(false)
const currentFile = ref({})
const fileDetail = (file) => {
  showFileDialog.value = true
  currentFile.value = file
}
const postFile = async (uploadFile) => {
  //文件名字
  let fileName = uploadFile.name
  //文件的大小
  const fileSize = uploadFile.size || 0
  //dd
  let fileItem = {}
  fileItem.fileName = fileName
  fileItem.fileSize = fileSize
  fileItem.filePid = null
  fileItem.fileMd5 = ""
  fileItem.uploadSize = 0

  //把信息放到后端先,返回对应的信息{}
  let message = {
    chatId: null,
    userId: currentUser.value,
    targetId: currentRelation.id,
    type: currentRelation.type,
    isFile: true,
    messageType: 2,
    body: null
  }
  message.body = JSON.stringify({
    fileName: fileName,
    fileSize: fileSize,
  })
  //把信息放到push到数组中
  if (fileSize > 52428800)
    ElNotification({
      title: '文件上传',
      message: h('i', { style: 'color: teal' }, '文件有点大，请稍等 ...'),
    })
  //获得文件的md5
  if (uploadFile.raw) {
    await generateMD5OfFile(uploadFile.raw).then(
      res => {

        fileItem.fileMd5 = res
      }
    )
  }

  //分片上传
  let chunkTotals = Math.ceil(fileSize / chunkSize);

  //分片上传
  if (chunkTotals > 0) {
    chatHistory.value[lastClickIndex.value].push(message)



    for (let chunkNumber = 0, start = 0; chunkNumber < chunkTotals; chunkNumber++, start += chunkSize) {
      //文件最后的end
      let end = Math.min(fileSize, start + chunkSize);
      // el-mement - plus中，上传的文件就在raw里面
      const files = uploadFile.raw?.slice(start, end)
      //上传的结果
      const result = await uploadFileToServer(files, chunkNumber + 1, chunkTotals, fileName, 'root', fileItem.fileMd5, currentUser.value)
      if (result.data.data.status === 1) {

      } else if (result.data.data.status === 3) {

        //先拿filePid
        let fileId = result.data.data.fileId

        //消息体
        message.body = JSON.stringify({
          fileId: fileId,
          fileName: fileName,
          fileSize: fileSize,
        })

        //把信息放到后端
        postMessageToServer(message, socket)
        //滑到最底端
        let max = chatHistory.value[lastClickIndex.value].length * 100 + 100
        scrollbarRef.value.setScrollTop(max)
        return; //结束
      } else {
        ElNotification({
          title: '文件上传',
          message: h('i', { style: 'color: teal' }, '文件上传失败 :('),
        })
        return;  //结束
      }
    }
  }
}

</script>

<template>
  <div class="chat-frame">
    <el-empty v-if="nowFrame == null" description="快去找你的好友聊天吧~" />
    <div v-else class="message">

      <el-container>
        <!--      chat list-->
        <el-aside width="150px" style="height: 545px">
          <el-scrollbar height="100%" class="user-list">
            <template v-if="relationList.length > 0" v-for="(item, index) in relationList" :key="item.id">
              <div class="user-card" v-if="item.nickName != null" @click="focusClick(index)" ref="userCards">
                <div class="user_avatar">
                  <el-avatar :src="item.avatar" />
                </div>

                <div class="nick-chinese" style="width: 300px;margin-right:13px;text-align:right">
                  <p>{{ item.nickName }}</p>
                </div>
              </div>

              <div class="user-card" v-else @click="focusClick(index)" ref="userCards">
                <div class="user_avatar">
                  <el-avatar
                    src="https://tse4-mm.cn.bing.net/th/id/OIP-C.1K4QXQUQJz6yBZ0jEODdGQAAAA?w=198&h=198&c=7&r=0&o=5&dpr=2&pid=1.7" />
                </div>
                <div class="nick chinese" style="width: 300px;margin-right:13px;text-align:right">
                  <p>{{ item.groupName }}</p>
                </div>
              </div>
            </template>
            <template v-else>
              <el-empty image-size="100" description="快找你的好友聊天去吧~" />
            </template>
          </el-scrollbar>
        </el-aside>

        <!--      chat main-->
        <el-main v-if="currentRelation.id != null">
          <!--      一个header - main -footer 的布局-->
          <el-container>
            <!--        header-->
            <el-header class="message-header" height="200px">
              <div class="header-font-user-name">
                <p style="font-family: Consolas,serif; line-height: 55px;font-size: 20px;color: white">
                  {{ currentRelation.name }}
                </p>
              </div>
              <div v-if="currentRelation.type === 1" class="header-font">
                <el-tag v-if="currentUserIsOnline === 1" type="success">在线</el-tag>
                <el-tag v-else type="danger">离线</el-tag>
              </div>
              <div class="header-more">
                <el-button type="primary" size="large" @click="showRelationDetailDrawer" link>
                  ***
                </el-button>
              </div>

              <!--              relation detail -->
              <el-drawer v-model="relationDetailDrawerIsShow" :with-header="false" style="border-radius: 20px 0 0 20px;
                              width: 50%;
                              height: 100%;
                          ">
                <div class="relation-drawer-user" v-if="relationDetail.nickName != null">

                  <div class="drawer-head-font">
                    <p style="font-size: 40px;color: #215A59;
                          margin-top: 8%;
                          margin-left: 5%;
                          display: flex;  align-items: center;

                      ">
                      {{ relationDetail.nickName }}
                    </p>
                    <br>
                    <div style="
                        padding: 0;margin-left:5%;
                        width: 70px;height: 70px;
                        border: 2px solid aqua;
                        border-radius: 20px;
                        overflow: hidden;
                        ">
                      <img :src="relationDetail.avatar" style="
                            width: 70px;
                            height: 70px;

                          " />
                    </div>
                    <div style="height: 70px;">
                      <p style="font-size: 20px;line-height: 70px;
                        color: #ED723F;

                        ">
                        {{ timeArrayToFormattedDateTime(relationDetail.time) }} 你们成为好友！！！
                      </p>
                    </div>
                  </div>
                  <hr />
                  <br>
                  <div class="drawer-user-info">
                    <el-tag type="success" size="large">用户信息</el-tag>
                    <br>
                    <br>
                    <div class="user-info-age">
                      <div>
                        <p style="font-size: 20px">
                          年龄:
                        </p>
                      </div>
                      <div style="margin-left: 250px">
                        <p style="font-size: 20px">
                          {{ relationDetail.age }}
                        </p>
                      </div>
                    </div>
                    <br>
                    <div class="user-info-email">
                      <div>
                        <p style="font-size: 20px">邮箱:</p>
                      </div>
                      <div>
                        <p style="font-size: 20px;margin-left: 100px">
                          {{ relationDetail.email }}
                        </p>
                      </div>
                    </div>
                    <br>
                    <div class="user-info-phone">
                      <div>
                        <p style="font-size: 20px">手机号:</p>
                      </div>

                      <div>
                        <p style="font-size: 20px;margin-left: 148px">
                          {{ relationDetail.phone }}
                        </p>
                      </div>
                    </div>
                  </div>
                  <br>
                  <br>
                  <div class="drawer-delete-button">
                    <el-button @click="handleRemoveUser(relationDetail.id)" type="danger" size="large">删除用户</el-button>
                  </div>
                </div>

                <!--                Group的时候-->
                <div class="relation-drawer-group" v-else>
                  <div class="drawer-head-font">
                    <p style="font-size: 40px;color: #215A59;
                          margin-top: 8%;
                          margin-left: 5%;
                          display: flex;  align-items: center;
                      ">
                      {{ relationDetail.groupName }}
                    </p>
                    <br>
                    <div style="
                        padding: 0;margin-left:5%;
                        width: 70px;height: 70px;
                        border: 2px solid aqua;
                        border-radius: 20px;
                        overflow: hidden;
                        ">
                      <img
                        src="https://tse4-mm.cn.bing.net/th/id/OIP-C.1K4QXQUQJz6yBZ0jEODdGQAAAA?w=198&h=198&c=7&r=0&o=5&dpr=2&pid=1.7"
                        style="
                            width: 70px;
                            height: 70px;

                          " />
                    </div>
                    <div style="height: 70px;">
                      <p style="font-size: 20px;line-height: 70px;
                        color: #ED723F;

                        ">
                        {{ timeArrayToFormattedDateTime(relationDetail.time) }} 你加入此群！！！
                      </p>
                    </div>
                  </div>
                  <hr>
                  <el-tag type="primary" size="large">群聊信息</el-tag>
                  <div class="drawer-group-info" style="margin-bottom: 20px">
                    <div>
                      群聊ID : {{ relationDetail.id }}
                    </div>
                    <div>
                      群主ID: {{ relationDetail.ownerId }}
                    </div>
                    <div>
                      简介 : {{ relationDetail.introduce }}
                    </div>
                    <div>
                      创建时间 : {{ timeArrayToFormattedDateTime(relationDetail.createTime) }}
                    </div>
                    <div>
                      人数: {{ relationDetail.curNum }}
                    </div>
                  </div>
                  <hr>
                  <el-tag type="success" size="large">成员列表</el-tag>
                  <div class="drawer-group-member" style="margin-bottom: 10%">

                    <el-scrollbar height="130px">
                      <el-alert :closable="false" type="info" style="margin-top: 10px"
                        v-for="item in relationDetail.memberList" :key="item.id">
                        <div class="message-content">
                          <div class="content-avatar">
                            <el-avatar :src="item.avatar" />
                          </div>
                          <div class="content-font">
                            <p style="line-height: 17px;font-size: 17px ;text-decoration: underline">
                              {{ item.nickName }}
                            </p>
                          </div>
                          <div class="content-button">
                            <el-button @click="handleAddFriendByGroup(item.id)" style="margin-left: 30%" type="primary"
                              size="default">加ta好友</el-button>
                          </div>
                        </div>
                      </el-alert>
                    </el-scrollbar>
                  </div>

                  <div class="drawer-out-button">
                    <el-button v-if="userStore.user.id !== relationDetail.ownerId"
                      @click="handleOutGroup(relationDetail.id)" type="danger" size="large">
                      退出群聊
                    </el-button>
                    <el-button v-else @click="handleOutGroup(relationDetail.id)" type="danger" size="large">
                      解散群聊
                    </el-button>
                  </div>
                </div>
              </el-drawer>

            </el-header>

            <!--        main-->
            <el-main class="message-main">
              <div class="message-body">
                <!--            聊天内容-->
                <el-scrollbar height="400px" ref="scrollbarRef">
                  <div v-for="item in chatHistory[lastClickIndex]" :key="item.id" ref="innerRef">
                    <!--                别人的消息-->
                    <template v-if="item.userId !== currentUser">
                      <div class="others-content">
                        <div>
                          <el-avatar :src=getUserIndexByUserId(item.userId).avatar shape="square" />
                        </div>
                        <div class="others-content-body" v-if="item.messageType == 1" style="background-color: white"
                          v-html="item.body"></div>
                        <component v-else class="other-file-content" @click="fileDetail(JSON.parse(item.body))"
                          :fileInfo="JSON.parse(item.body)" :is="currentComponentRef"></component>
                      </div>
                    </template>
                    <!--                自己的消息-->
                    <template v-else>
                      <div class="my-content">
                        <div class="my-content-body" v-if="item.messageType == 1" v-html="item.body"></div>
                        <component v-else class="file-content" @click="fileDetail(JSON.parse(item.body))"
                          :fileInfo="JSON.parse(item.body)" :is="currentComponentRef">
                        </component>
                        <div>
                          <el-avatar :src=userStore.user.avatar shape="square" />
                        </div>
                      </div>
                    </template>
                  </div>
                  <div style="height: 100px;"></div>
                </el-scrollbar>
              </div>
            </el-main>

            <el-dialog v-model="showFileDialog" style="width: 250px; border-radius: 6px;">
              <ChatFile :fileInfo="currentFile" />
            </el-dialog>

            <!--        footer-->
            <el-footer class="message-footer">
              <div class="footer-all">

                <div class="container">
                  <div class="top-icon">
                    <!-- emoji图标 -->
                    <el-popover placement="top-end" trigger="click" width="450">
                      <template #reference>
                        <div class="icon-box">
                          <svg t="1715310594374" class="icon" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="17753" width="19" height="19">
                            <path
                              d="M945.424922 509.059018c0-240.652965-195.076954-435.727873-435.726849-435.727873-240.651942 0-435.727873 195.074907-435.727873 435.727873 0 240.662175 195.075931 435.737082 435.727873 435.737082 98.792027 0 189.248202-33.611515 262.339894-88.998987 3.003404-3.40761 4.975315-7.753591 4.975315-12.637832 0-10.666944-8.651031-19.315928-19.315928-19.315928-5.53404 0-10.464329 2.194992-13.981433 5.91471l-0.246617 0c-65.407686 48.12916-145.915277 76.945463-233.346559 76.945463-217.908962 0-394.587893-176.701444-394.587893-394.598126 0-217.931474 176.678932-394.588917 394.587893-394.588917 217.930451 0 394.58687 176.657442 394.58687 394.588917 0 66.571185-15.617699 129.220037-44.74611 184.229908l0 0.323365c-0.536212 1.838881-1.122567 3.631714-1.122567 5.602601 0 10.666944 8.650007 19.315928 19.314904 19.315928 8.312316 0 15.259542-5.335519 17.971303-12.68388l0 0.1361C927.164023 649.080052 945.424922 581.210291 945.424922 509.059018zM395.534963 391.95902c0-24.334221-19.71911-44.052309-44.053332-44.052309-24.334221 0-44.052309 19.718087-44.052309 44.052309 0 24.334221 19.718087 44.029796 44.052309 44.029796C375.815853 435.989839 395.534963 416.294264 395.534963 391.95902zM669.125085 347.974249c-24.314779 0-44.030819 19.696598-44.030819 44.029796s19.717064 44.052309 44.030819 44.052309c24.332175 0 44.052309-19.71911 44.052309-44.052309C713.17637 367.669824 693.456237 347.974249 669.125085 347.974249zM680.214654 642.001843c-2.62171 0-5.063319 0.647753-7.370875 1.477654l-0.201591 0c-49.497321 22.382776-104.283088 34.728966-162.137751 34.728966-58.752102 0-114.322745-12.097526-164.423817-35.111682l-0.448208 0c-1.74576-0.468674-3.473101-1.095961-5.377474-1.095961-11.965519 0-21.621437 9.655917-21.621437 21.597901 0 7.912204 5.685489 15.400758 12.027941 19.173688-0.169869-0.075725-0.297782-0.132006 0.048095 0.028653 54.629201 24.670889 116.382661 39.593763 180.219571 39.593763 64.24214 0 124.875079-14.564717 179.772386-39.526225 6.721075-3.697205 11.293207-10.891048 11.293207-19.114336C701.994704 651.726322 692.224176 642.001843 680.214654 642.001843z"
                              fill="#ffffff" p-id="17754"></path>
                          </svg>
                        </div>
                      </template>
                      <Emoji  @selectEmoji="selectEmoji" :historyEmojiList="historyEmojiList" />
                    </el-popover>


                    <el-upload action="#" :multiple="true" :auto-upload="false" :on-change="postFile"
                      :show-file-list="false">
                      <!--      文件-->
                      <div class="click-icon" @click="postFile">
                        <svg t="1715311316995" class="icon" viewBox="0 0 1024 1024" version="1.1"
                          xmlns="http://www.w3.org/2000/svg" p-id="25286" width="19" height="19">
                          <path
                            d="M912 208H427.872l-50.368-94.176A63.936 63.936 0 0 0 321.056 80H112c-35.296 0-64 28.704-64 64v736c0 35.296 28.704 64 64 64h800c35.296 0 64-28.704 64-64v-608c0-35.296-28.704-64-64-64z m-800-64h209.056l68.448 128H912v97.984c-0.416 0-0.8-0.128-1.216-0.128H113.248c-0.416 0-0.8 0.128-1.248 0.128V144z m0 736v-96l1.248-350.144 798.752 1.216V784h0.064v96H112z"
                            fill="#ffffff" p-id="25287"></path>
                        </svg>
                      </div>
                    </el-upload>

                    <!--    麦克风-->
                    <div class="click-icon" @click="canNotUse" style="margin-left: 10px">
                      <svg t="1715311363062" class="icon" viewBox="0 0 1024 1024" version="1.1"
                        xmlns="http://www.w3.org/2000/svg" p-id="27341" width="19" height="19">
                        <path
                          d="M512 128c35.2 0 64 28.8 64 64v320c0 35.2-28.8 64-64 64s-64-28.8-64-64V192c0-35.2 28.8-64 64-64m0-64c-70.4 0-128 57.6-128 128v320c0 70.4 57.6 128 128 128s128-57.6 128-128V192c0-70.4-57.6-128-128-128z m320 448h-64c0 140.8-115.2 256-256 256S256 652.8 256 512h-64c0 165.6 126.4 302.4 288 318.4V960h64v-129.6c161.6-16 288-152.8 288-318.4z"
                          p-id="27342" fill="#ffffff"></path>
                      </svg>
                    </div>
                  </div>

                  <!-- 自定义输入框 -->
                  <div class="message-input" @input="messageInput" ref="messageInputDom" contenteditable="true"
                    spellcheck="false" autofocus></div>
                </div>


                <el-button @click="putMessage" type="primary">发送</el-button>
              </div>

            </el-footer>
          </el-container>
        </el-main>
      </el-container>
    </div>

  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 80%;
  flex: 1;
  padding: 10px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.send-message-btn {
  position: absolute;
  right: 5px;
  bottom: 5px;
  color: #323644;
}

.top-icon {
  width: 12%;
  display: flex;

  .icon-box {
    margin-right: 10px;
    cursor: pointer;
  }
}

.message-input {
  width: 60%;
  height: 110%;
  flex: 1;
  margin-top: 0;
  margin-left: 0;
  padding: 9px;
  box-sizing: border-box;
  border-radius: 10px;
  resize: none;
  overflow: auto;
  color: black;
  background-color: white;
  display: flex;
  align-items: center;
  /* 垂直居中 */
  line-height: 100%;

  &::placeholder {
    font-size: 20px;
  }
}

.drawer-group-info {
  align-items: center;
  justify-content: center;
  text-align: center;
}

.drawer-group-info>div {
  font-family: "幼圆", Times, serif;
  font-size: 20px;
  margin-top: 10px;
  color: #215A59;
}

.drawer-head-font>div {
  display: inline-block;
}

.drawer-delete-button {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.drawer-out-button {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.chat-frame {
  height: 95%;
  width: 99%;
  padding-top: 10px;
}

.message {
  height: 102%;
  width: 100%;
  border: 3px solid black;
  border-radius: 20px;
  padding: 0;
}

.message-header {
  width: 100%;
  height: 55px;
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
  background-color: #323644;

  display: flex;
  align-items: center;
}

.header-font {
  display: inline;
}

.header-font-user-name {
  margin-left: 370px;
  margin-right: 10px;

}

.header-more {
  position: absolute;
  margin-left: 830px;
}

.message-main {
  width: 100%;
  height: 400px;
  margin: 0;
  padding: 0;

}

.message-footer {
  width: 100%;
  height: 65px;
  margin: 0;
  padding: 0;
  display: flex;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  position: relative;
}

.footer-all {
  width: 100%;
  display: flex;
  align-items: center;
  position: relative;
  top: -10px;
  background-color: #323644;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
}

.other el-button {
  display: inline-block;
  margin-left: 0;
  padding: 0;

}

.others-content {
  width: 100%;
  height: 85px;
  display: flex;
  position: relative;
  align-items: center;
  margin-bottom: 1px;
}

.others-content>div {
  display: inline-block;
}

.others-content>div:first-child {
  margin-left: 13px;
}

.others-content-body {
  margin-left: 10px;
  border: 2px solid #f1e8e8;
  border-radius: 0 10px 10px 10px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 10px;
}

.my-content {
  width: 100%;
  height: 85px;
  display: flex;
  position: relative;
  align-items: center;
  margin-bottom: 1px;
}

.my-content-body {
  padding: 10px;
  border-radius: 10px 0 10px 10px;
  
  position: absolute;
  right: 7.5%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  background-color: #85f883;
}

.other-file-content {
  margin-left: 20px;
  margin-bottom: 1px;
  
}

.file-content {
  margin-left: 640px;
  margin-bottom: 1px;
}

.my-content>div:last-child {
  position: absolute;
  right: 20px;
}

.user-card {
  border-radius: 50px;
  display: flex;
  align-items: center;

  border: 1px solid #131111;
}

.user_avatar {
  margin-left: 2px;
  display: inline;
}

.user-list {
  border: 2px;
  background-color: #323644;
  border-radius: 20px;
  margin-left: 0;
  overflow: auto;
}

.nick {
  margin-left: 10px;
}

p {
  font-family: Consolas, cursive;
}

.click-icon:active {
  transform: scale(0.4);
}

.click-icon {
  transition: transform 0.2s;
}
</style>