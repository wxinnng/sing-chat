<template>
  <div class="preview-body">

    <!-- word -->
    <vue-office-docx v-if="getFileType() == 1" :src="getFileUrl()" style="height: 400px;" @rendered="renderedHandler"
      @error="errorHandler" />

    <!-- pdf -->
    <vue-office-pdf v-else-if="getFileType() == 2" :src="getFileUrl()" style="height: 400px;"
      @rendered="renderedHandler" @error="errorHandler" />

    <!-- iamge -->
    <div v-else-if="getFileType() == 3">
      <el-image :src="getFileUrl()" style="height: 100px; width: 100px;" :zoom-rate="1.2" :max-scale="7"
        :min-scale="0.2" :preview-src-list="imageList" :initial-index="4" />
      <br>
      <el-text style="margin-left: 0px;" link type="primary">点击图片查看详情</el-text>
    </div>

    <!-- 不支持显示 -->
    <div v-else-if="getFileType() == 4">
      <br>
      该文件不支持在线浏览，请下载后查看!
    </div>

    <!-- 视频 -->
    <div v-else-if="getFileType() == 5">
        <video autoplay width="1200px" height="400px" controls 
          :src="getFileUrl()"
          id="myVideo"
          >
        </video>
    </div>

    <!-- 文本显示 -->
    <div v-else>
      <el-scrollbar height="400px" class="document-preview">
        <pre>{{ documentContent }}</pre>
      </el-scrollbar>
    </div>
  </div>
</template>

<script setup>
//引入相关样式
import VueOfficeDocx from '@vue-office/docx'
import VueOfficePdf from '@vue-office/pdf'
import '@vue-office/docx/lib/index.css'
import { ref } from 'vue'
import axios from 'axios';
import {env_minio_production} from '@/env'


const props = defineProps(['file'])
const video = document.getElementById("myVideo")


const getFileUrl = () => {
  return env_minio_production + props.file.filePath;
}
const getFileType = () => {
  let category = props.file.fileCategory

  if (category == 18 || category == 19) {
    return 1
  }
  else if (category == 13)
    return 2

  else if (category == 9 || category == 14 || category == 5) {
    imageList.value.push(getFileUrl())
    return 3
  }
  else if (category == 20 || category == 11 || category == 15)
    return 4

  else if (category == 12) {
    //视频
    return 5
  } else {
    //文本
    readDocumentContent();
  }

}

const readDocumentContent = async () => {
  var res = await axios.get(getFileUrl(), {
    responseType: 'text',
  })
  documentContent.value = `\n${res.data}\n`
}
//文件中的内容
const documentContent = ref('')
//图片列表
const imageList = ref([])

const renderedHandler = () => {
  console.log("渲染成功")
}
const errorHandler = () => {
  console.log("渲染失败")
}

</script>

<style lang="scss" scoped>
.document-preview {
  margin-right: 100px;
  background-color: #ccc;

  width: 1164px;
  border: 2px solid #ccc;
  height: 400px;
  border-radius: 0 0 10px 10px;
  text-align: left;
}
pre {
  font-family: 'Microsoft YaHei';
}
</style>
