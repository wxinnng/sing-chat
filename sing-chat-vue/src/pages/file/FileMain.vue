<template>
  <div class="container">
    <el-container>
      <el-header class="header">
        <div class="path" style="width: 400px;">
          <template v-for="node in currentPath" :key="node.id">
            >
            <el-button style="margin: 0;" link type="warning" size="default" @click="handleBackPath(node.id)">
              {{ node.path }}
            </el-button>
          </template>
        </div>
        <div class="reload-btn" @click="getFileList()" style="margin-left: 560px;">
          <svg t="1717920850822" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
            p-id="11827" data-spm-anchor-id="a313x.search_index.0.i3.3bb43a81rrBtza" width="18" height="18">
            <path
              d="M853.333333 768.064A426.112 426.112 0 0 1 512 938.666667C276.352 938.666667 85.333333 747.648 85.333333 512S276.352 85.333333 512 85.333333s426.666667 191.018667 426.666667 426.666667a21.333333 21.333333 0 0 1-42.666667 0c0-212.074667-171.925333-384-384-384S128 299.925333 128 512s171.925333 384 384 384a383.573333 383.573333 0 0 0 319.36-170.666667h-84.757333A21.248 21.248 0 0 1 725.333333 704c0-11.776 9.664-21.333333 21.269334-21.333333h128.128c5.866667 0 11.178667 2.346667 15.018666 6.186666l0.021334 0.106667c3.84 3.882667 6.229333 9.173333 6.229333 14.976v128.128c0 11.733333-9.472 21.269333-21.333333 21.269333-11.776 0-21.333333-9.664-21.333334-21.269333v-64z"
              fill="#ffffff" p-id="11828"></path>
          </svg>
        </div>
      </el-header>
      <el-main>
        <el-scrollbar class="file-box" height="360px">
          <div class="file-table" v-for="item in fileList" :key="item">

            <div class="file-date">{{ item[0]?.createTime }}</div>
            <br>
            <div class="file-icon-container">
              <FileIcon v-for="file in item" :key="file.fileId" @click="handleFilePreviewOrEnterFolder(file)"
                :file="file" />
            </div>
          </div>

          <template v-if="Object.keys(fileList).length === 0">
            <div class="file-empty">
              <el-empty description="快来上传文件吧~" image="/file_empty.png" />
            </div>
          </template>
        </el-scrollbar>
      </el-main>
    </el-container>

    <!--文件预览 -->
    <el-dialog v-model="isFilePreviewShow" title="文件预览" width="1200" destroy-on-close style="
        height: 540px;
        border-radius:10px ;
        border: 1px solid rgb(255, 255, 255);
      ">
      <div class="file-preview-header">
        <div style="font-size:large;width: 800px;">{{ targetFile.fileName }}</div>
        <el-button style="margin-left: 200px;" @click="handleShare" type="success">分享</el-button>
        <el-button style="margin-left: 10px;" @click="handleDownload" type="primary">下载</el-button>
        <el-button style="margin-left: 10px;" @click="handleRecycle" type="danger"> 回收</el-button>
      </div>
      <div class="file-preview-dialog">
        <FilePreview ref="filePreview" :file="targetFile" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import FileIcon from './FileIcon.vue'
import { ref, onMounted, onUnmounted } from 'vue'
import service from '../../util/request'
import FilePreview from './FilePreview.vue'
import { useUserStore } from '../../stores/useUserStore'
import { saveAs } from 'file-saver';
import { message } from '../../util/Utils'
import emitter from "@/mitt/index"
import {env_minio_production} from '@/env/index'

// 初始化
onMounted(async () => {
  await getFileList()
})
emitter.on("filterFileList", (type) => {

  //1 :全部  2:文档 3:视频 5:音频  6：其他
  let key = Object.keys(fileList2)[0]
  if (type === 1) {
    fileList.value[key] = fileList2[key]
  } else if (type === 2) {
    let document = [1, 2, 3, 5, 6, 7, 8, 10, 13, 15, 17, 18, 19, 21]
    fileList.value[key] = fileList2[key].filter(item => document.includes(item.fileCategory))
  } else if (type === 3) {
    fileList.value[key] = fileList2[key].filter(item => item.fileCategory === 12)
  } else if (type === 4) {
    fileList.value[key] = fileList2[key].filter(item => item.fileCategory === 9 || item.fileCategory === 14 )
  } else if (type === 5) {
    fileList.value[key] = fileList2[key].filter(item => item.fileCategory === 20)
  }
})
//组件卸载时，一定要解绑对应的事件
onUnmounted(() => {
  emitter.off('filterFileList')
})


// props from File.vue
const props = defineProps(['fileListChange'])
// store from useUserStore
const { getCurrentId, addNodeToCurrentPath, backCurrentPath, getAllPath } = useUserStore()
// 当前的完整路径
const currentPath = getAllPath()
// 文件列表
const fileList = ref({})
// 文件样本保存
let fileList2 = {}
// 获取文件列表
const getFileList = async () => {

  var res = await service.get('/file/list', {
    params: {
      pid: getCurrentId()
    }
  })
  if (res.data.code === 1) {
    fileList.value = res.data.data
    fileList2 = JSON.parse(JSON.stringify(res.data.data))
  }
}

//处理路径回退
const handleBackPath = async (id) => {
  backCurrentPath(id)
  await getFileList()
}
//文件预览
const isFilePreviewShow = ref(false)
//目标文件
const targetFile = ref({})
//处理进入文件夹目录 or 处理文件预览
const handleFilePreviewOrEnterFolder = async (file) => {
  if (file.fileCategory === 4) {
    //TODO:进入对应的目录，同时上面的路径也要改变
    addNodeToCurrentPath({ id: file.fileId, path: file.fileName })
    await getFileList()
    return
  }
  isFilePreviewShow.value = true
  targetFile.value = file


}
//下载文件
const handleDownload = () => {
  let url =  env_minio_production + targetFile.value.filePath;
  message("请稍等~", 'success')
  saveAs(url, targetFile.value.fileName)
}
//分享
const handleShare = () => {
  message("分享成功！", 'success')

}
//放到回收站
const handleRecycle = async () => {
  var res = await service.get('/file/recycle', {
    params: {
      fileId: targetFile.value.fileId
    }
  })
  if (res.data.code === 1)
    message("已放到回收站中", 'success')
  else
    message("删除失败", 'error')
  //重新加载文件列表
  await getFileList()
}

// 暴露给父组件
defineExpose({
  getFileList
})

</script>

<style scoped>
.file-box {
  display: flex;
  /* 使用Flex布局 */

}

.file-date {
  color: white;
}

.path {
  color: rgb(125, 94, 238);
}

span:active {
  transform: scale(0.4);
}

.file-icon-container {
  display: flex;
  flex-wrap: wrap;
  /* 允许 flex 项目换行 */
  justify-content: flex-start;
  /* 根据需要调整项目之间的间距 */
  align-items: flex-start;
}

.header {
  display: flex;
  height: 18px;
}

el-main {
  overflow: hidden
}

.container {
  width: auto;
  height: 450px;
  border-radius: 10px;
  padding: 10px;
  background-color: #272A37;
}

.file-empty {
  height: 200px;
  width: 900px;
}

.file-preview-header {
  height: 50px;
  display: flex;
  align-items: center;
}

.file-preview-dialog {
  margin-top: 10px;
  height: 450px;
  width: 1166px;
  overflow: hidden;
  border-top: 1px solid rgb(137, 127, 127);
}

.reload-btn:active {
  transform: scale(0.4);
}

.reload-btn {
  transition: transform 0.2s;
}
</style>
