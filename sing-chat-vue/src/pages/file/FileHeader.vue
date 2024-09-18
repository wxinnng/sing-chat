<template>
  <div class="header-main">
    <FileButton content="创建文件夹" type="default" class="file-button" @click="() => { dialogVisible = true }" />
    
    <FileButton content="回收站" type="danger" @click="() => {isRecycleVisible = true}" class="file-button" />
    
    <el-upload action="#" :multiple="true" :auto-upload="false" :on-change="handleChange" :show-file-list="false">
      <FileButton content="上传文件" type="primary" class="file-button" />
    </el-upload>
    
    <!-- 查询列表 -->
    <FileSearch class="search-box" />
    
    <!-- 显示正在传输的列表 -->
    <el-popover :visible="isVisible" class="load-list" placement="bottom" title="文件传输列表" :width="500" ref="popoverRef"
      height="300px" trigger="click">
      <template #reference>
        <el-button size="large" @click="onClickUploadList" v-click-outside="onClickOutside" link>
          <el-icon :size="23" color="white">
            <Promotion />
          </el-icon></el-button>
      </template>
      <FileUploadList ref="fileUploadList" />
    </el-popover>

    <!-- 创建一个用于创建群聊的弹出框 -->
    <el-dialog style="border-radius: 20px; border: 1px solid aqua;" v-model="dialogVisible" title="创建一个文件夹" width="350"
      draggable>
      <br>
      <el-tag type="success">名称</el-tag>-
      <el-input v-model="createFolderName" style="width: 240px" placeholder="输入文件夹名称" />
      <br>
      <br>
      <el-button style="margin-left: 40%;" @click="handleCreateFolder" type="primary">创建</el-button>
    </el-dialog>

    <!-- 用于删除文件的弹窗 -->
    <el-dialog v-model="isRecycleVisible" width="630" destroy-on-close title="回收站">
      <FileRecycle/>
    </el-dialog>

  </div>
</template>

<script setup>
import FileButton from './FileButton.vue'
import FileSearch from './FileSearch.vue'
import service from "../../util/request"
import { ref, unref } from 'vue'
import { ClickOutside as vClickOutside } from 'element-plus'
import { Promotion } from '@element-plus/icons-vue'
import FileUploadList from './FileUplodList.vue'
import { message, formatFileSize } from "../../util/Utils"
import { useUserStore } from '../../stores/useUserStore'
import FileRecycle from './FileRecycle.vue'
import {generateMD5OfFile,uploadFileToServer} from '@/hooks/UploadFile'

//路径
const props = defineProps(['filePath'])
//拿到父组件的函数
const $emit = defineEmits(['addChangeNum'])
//拿token
const { getUserInfo, getCurrentId } = useUserStore()
//当前用户的id，用于传输文件
const userId = getUserInfo().id
//子组件
const fileUploadList = ref(null)
//控制创建文件夹的显示
const dialogVisible = ref(false)
//创建的文件夹名称
const createFolderName = ref("")
//创建一个文件夹
const handleCreateFolder = async () => {

  if (createFolderName.value === "") {
    message("没输入文件夹名呢", "error")
    return
  }

  var res = await service.post("/file/c_folder", {}, {
    params: {
      folderName: createFolderName.value,
      pid: getCurrentId()
    }
  })

  if (res.data.code === 1) {
    message("创建成功~", "success")
    createFolderName.value = ""
    $emit('addChangeNum')
    dialogVisible.value = false

  } else {
    message("创建失败！", "error")
  }
}
//默认分片大小
const chunkSize = 5 * 1024 * 1024
const popoverRef = ref()
const isVisible = ref(false)
const onClickUploadList = () => {
  isVisible.value = true
}
//点击外部，关闭弹窗
const onClickOutside = () => {
  unref(popoverRef).popperRef?.delayHide?.()
  isVisible.value = false
}


//处理文件上传
const handleChange = async (uploadFile) => {

  //文件名字
  let fileName = uploadFile.name

  //文件的大小
  const fileSize = uploadFile.size || 0


  //当前的文件对象
  let fileItem = {}
  fileItem.fileName = fileName
  fileItem.fileSize = fileSize
  fileItem.state = 1  //解码中
  fileItem.progress = 0  //进度是0
  fileItem.filePid = 102903232
  fileItem.fileMd5 = ""
  fileItem.uploadSize = 0

  fileUploadList.value.addFile(fileItem)

  //弹框显示
  isVisible.value = true

  //获得文件的md5
  if (uploadFile.raw) {
    await generateMD5OfFile(uploadFile.raw).then(
      res => {
        fileItem.fileMd5 = res
      }
    )
  }



  fileUploadList.value.addMd5(fileItem.fileName, fileItem.fileMd5)

  fileUploadList.value.changeFileState(fileItem.fileName, 2)

  //分片上传
  let chunkTotals = Math.ceil(fileSize / chunkSize);

  //分片上传
  if (chunkTotals > 0) {

    for (let chunkNumber = 0, start = 0; chunkNumber < chunkTotals; chunkNumber++, start += chunkSize) {
      //文件最后的end
      let end = Math.min(fileSize, start + chunkSize);
      // el-mement - plus中，上传的文件就在raw里面
      const files = uploadFile.raw?.slice(start, end)

      //上传的结果
      const result = await uploadFileToServer(files, chunkNumber + 1, chunkTotals, fileName , getCurrentId(), fileItem.fileMd5,userId)
      console.log(result.data)
      console.log(result.data.data)
      if (result.data.data.status === 1) {
        // console.log("上传中")
        //上传的进度
        fileUploadList.value.changeProgress(fileItem.fileName, ((end / fileSize) * 100).toFixed(1))
        //修改已经上传完成的文件大小
        fileUploadList.value.changeUploadSize(fileItem.fileName, end)
      
      } else if (result.data.data.status === 3) {

        // console.log("上传成功！")
        fileUploadList.value.changeFileState(fileItem.fileName, 3)  //上传完成
        fileUploadList.value.changeProgress(fileItem.fileName, 100)  // 进度100%

        //通过main，进行刷新
        $emit("addChangeNum")

        return ; //结束
      } else {
        message("上传失败", 'error')

        return;  //结束
      }
    }
  }
}
//控制删除文件弹出框的显示
const isRecycleVisible = ref(false)
//删除文件
const handleDelete = (filePid) => {
  fileUploadList.value.deleteFile(fileName)
}

</script>

<style lang="scss" scoped>
.load-list {
  display: flex;
  justify-self: center;
  margin-left: auto;
  padding: 10px;
}

.header-main {
  display: flex;
}

img:active {
  transform: scale(0.4);
}

img {
  transition: transform 0.2s;
  /* 添加过渡效果 */
}

.file-button {
  margin-right: 10px;
}

.search-box {
  margin-left: auto;
}
</style>
