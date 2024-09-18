<script setup>
import { message } from "@/util/Utils.js";
import { useRouter } from 'vue-router'
import { ref, watch,onMounted } from 'vue'
import { useUserStore } from "@/stores/useUserStore.js";
//useStore
const {getLastNickSideIndex,changeLastClickSideIndex} = useUserStore()
const option = ref("file")
//路由
const router = useRouter()
onMounted(()=>{
  let target = document.getElementsByClassName(getLastNickSideIndex())
  target[0].style.borderLeft = " 2px solid aqua"
  handleRouterChange(getLastNickSideIndex())
})
watch(option, (newVal, oldVal) => {
  //清除上一次点击的样式
  let old = document.getElementsByClassName(oldVal)[0]
  old.style.borderLeft = ""
  //设置当前点击的样式
  let target = document.getElementsByClassName(newVal)
  if(target)
    target[0].style.borderLeft = " 2px solid aqua"
  changeLastClickSideIndex(newVal)
  
})
const canNotUse = () => {
  message('功能还在开发中...', 'info')
}
const handleRouterChange = (value) => {
  option.value = value
  router.push(`/main/${value}`)
}

</script>

<template>

  <div class="main-aside">
    <div class="chat" @click="handleRouterChange('chat')"  id="option">
      <img src="/icon/chat.svg" />
    </div>

    <div class="file" @click="handleRouterChange('file')" id="option">
      <img src="/icon/Document.svg" />

    </div>

    <div class="user" @click="canNotUse" id="option">
      <svg t="1715049762101" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="15346" width="30" height="30">
        <path d="M943.6 1024H586V190l366.5 127.2C977.9 326.1 995 350 995 377v595.6c0 28.4-23 51.4-51.4 51.4z"
          fill="#B8EADA" p-id="15347"></path>
        <path
          d="M721.6 1024H117.5c-49.1 0-88.8-30.9-88.8-69.1V216.3c0-49.4 43.8-92.3 105.6-103.6l582.5-106c13.2-2.4 25.8 5.4 25.8 15.9v985.1c0 9-9.4 16.3-21 16.3z"
          fill="#24C891" p-id="15348"></path>
        <path
          d="M389.6 571h-8c-49.7 0-90-40.3-90-90v-8c0-49.7 40.3-90 90-90h8c49.7 0 90 40.3 90 90v8c0 49.7-40.3 90-90 90zM497.9 815H273.4c-22 0-38-20.7-32.6-42l24.9-97.4c10-39.2 45.3-66.6 85.8-66.6H420c40.5 0 75.8 27.4 85.8 66.6l24.9 97.4c5.2 21.3-10.9 42-32.8 42z"
          fill="#B8EADA" p-id="15349"></path>
        <path
          d="M912.5 498h-100c-16.8 0-30.5-13.7-30.5-30.5s13.7-30.5 30.5-30.5h100c16.8 0 30.5 13.7 30.5 30.5S929.3 498 912.5 498zM912.5 630h-37c-16.8 0-30.5-13.7-30.5-30.5s13.7-30.5 30.5-30.5h37c16.8 0 30.5 13.7 30.5 30.5S929.3 630 912.5 630z"
          fill="#24C891" p-id="15350"></path>
      </svg>

    </div>

    <div class="more" @click="canNotUse" id="option">
      <svg t="1715049834757" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="17246" width="30" height="30">
        <path
          d="M415.2 480.7h-339c-29.7 0-53.8-24.1-53.8-53.8v-339c0-29.7 24.1-53.8 53.8-53.8h339c29.7 0 53.8 24.1 53.8 53.8v339c0 29.7-24 53.8-53.8 53.8z"
          fill="#24C891" p-id="17247"></path>
        <path
          d="M946.1 480.7h-339c-29.7 0-53.8-24.1-53.8-53.8v-339c0-29.7 24.1-53.8 53.8-53.8h339c29.7 0 53.8 24.1 53.8 53.8v339c0 29.7-24.1 53.8-53.8 53.8zM416.9 993.3h-339c-29.7 0-53.8-24.1-53.8-53.8v-339c0-29.7 24.1-53.8 53.8-53.8h339c29.7 0 53.8 24.1 53.8 53.8v339c0 29.7-24.1 53.8-53.8 53.8zM946.7 993.7h-339c-29.7 0-53.8-24.1-53.8-53.8V601c0-29.7 24.1-53.8 53.8-53.8h339c29.7 0 53.8 24.1 53.8 53.8v339c0 29.6-24.1 53.7-53.8 53.7z"
          fill="#B8EADA" p-id="17248"></path>
      </svg>
    </div>

  </div>
</template>

<style scoped>
.main-aside {
  margin: 0;
  padding: 0;
  height: 570px;
  display: flex;
  flex-direction: column;
}

.active {
  border-left: 2px solid aqua;
}

.main-aside :first-child {
  margin-top: 100%;

}

.main-aside :last-child {
  margin: 0;
}

.main-aside>div {
  width: 90%;
  height: 15%;
  display: flex;
  align-items: center;
  justify-content: center;
}

img {
  width: 32px;
  height: 32px;
}
</style>