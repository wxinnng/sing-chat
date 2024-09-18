<template style="background-image: url('/bg2.jpg')">
  <el-dialog
      v-model="chatShow"
      :show-close="false"
      :close-on-press-escape="false" :close-on-click-modal="false"
      style="width: 1200px;border: 1px solid antiquewhite;padding: 0;background-color: #272A37;
        border-radius: 20px;
      "
  >
    <div class="common-layout">
      <!--    功能栏-->
      <el-container
      >
        <el-main class="chat-frame" width="800px">
          <el-container>

            <el-header height="90px" style="padding: 0">
              <Header/>
            </el-header>
            <el-main class="main-body">
              <el-container>
                <el-aside width="50px" class="chat-aside">
                  <Aside/>
                </el-aside>
                <el-main class="main-body-chat-main">
                  <RouterView/>
                </el-main>
              </el-container>
            </el-main>
          </el-container>
        </el-main>
      </el-container>
    </div>
  </el-dialog>
</template>

<script setup>
import Header from '../components/Header.vue'
import Aside from "@/components/Aside.vue";
import {ref} from "vue";

const chatShow = ref(true);

</script>


<style scoped>
.common-layout{
  width: 1200px;
  height: 670px;
}
.main-body{
  margin: 0;
  padding: 5px;
}
.main-body-chat-main{
  padding: 0 0 0 20px;
}
.chat-aside{
  align-items: center;
}
.chat-frame{
  padding: 0;
}


</style>