<template>
  <div class="share-item">
    <div class="up-content">
      <div class="file-name">
        {{ fileInfo.fileName }}
      </div>
      <div>
        <img style="width: 50px;height: 50px;margin-top: 10px;" :src="`/file/${getFileCategoryByFileName(fileInfo.fileName,false)}.png`"/>
      </div>
    </div>
    <div v-if="fileInfo.error">
      <span style="color: red;" >上传失败</span>
    </div>
    <div v-else-if="fileInfo.fileSize" class="down-content">{{ getFormateFileSize() }}</div>
    <div v-else class="down-content">上传中...</div>
  </div>
</template>
<script setup>
import { formatFileSize,typeMap,getFileCategoryByFileName} from '@/util/Utils';

const {fileInfo} = defineProps(['fileInfo'])

const getFormateFileSize = () => {
  return formatFileSize(fileInfo.fileSize)
}




</script>

<style scoped>
.share-item {
  width: 200px;
  height: 80px;
  background-color: aliceblue;
  border: 1px solid rgb(232, 236, 238);
  border-radius: 4px;

}

.up-content {
  width: 200px;
  height: 60px;
  display: flex;
}

.down-content {
  color: rgb(146, 149, 150);
  margin-left: 15px;
  height: 16px;
  line-height: 16px;
  font-size: 12px;
}

.file-name {
  display: inline-block;
  padding-left: 10px;
  padding-top: 10px ;
  width: 135px;
  height: 40px;
  overflow: hidden;
  font-size: 14px;
}
</style>