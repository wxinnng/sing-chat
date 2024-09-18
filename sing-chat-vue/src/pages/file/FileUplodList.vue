<template>
    <el-scrollbar height="200px">
        <div class="list-item" v-for="item in fileTable" :key="item.fileName">
            <div>{{ item.fileName }}</div>
            <el-progress :percentage="item.progress" />
            <div>
                <span v-if="item.state === 1" class="change-code">
                    转码中...
                </span>
                <div v-else-if="item.state === 2">
                    <span  class="upload">
                        上传中...
                    </span>
                    <!-- 1.0MB / 2.0MB -->
                    {{ formatFileSize(item.uploadSize) + "/" + formatFileSize(item.fileSize) }}
                    
                </div>
                <span v-else-if="item.state === 3" class="upload-success">
                    上传成功
                </span>
                <span v-else class="upload-fail">
                    上传失败
                </span>

            </div>
        </div>
    </el-scrollbar>
</template>

<script setup>
import { ref } from 'vue'
import { formatFileSize } from "../../util/Utils"
//文件列表
const fileTable = ref([])
//添加文件
const addFile = (fileItem) => {
    fileTable.value.push(fileItem)
}
//删除一个文件(上传完成就去除了)
const removeFile = (fileMd5) => {
    fileTable.value.splice(fileTable.value.findIndex(item => item.fileMd5 === fileMd5), 1)
}
//修改文件状态
const changeFileState = (fileName, state) => {
    fileTable.value.find(item => item.fileName == fileName).state = state
}
//add md5
const addMd5 = (fileName,fileMd5) => {
    // fileTable.value.find(item => item.fileName == fileName).fileMd5 = fileMd5
    for(let i = 0;i<fileTable.value.length;i++){
        console.log(fileName,"--",fileTable.value[i].fileName)
        if(fileTable.value[i].fileName == fileName){
            fileTable.value[i].fileMd5 = fileMd5
            break
        }
    }
}
//change progress
const changeProgress = (fileName, progress) => {
    fileTable.value.find(item => item.fileName == fileName).progress = progress
}
//修改上传的尺寸大小
const changeUploadSize = (fileName,size) => {
    fileTable.value.find(item => item.fileName == fileName).uploadSize = size
}
//暴露方法
defineExpose({
    addFile,
    removeFile,
    changeFileState,
    addMd5,
    changeProgress,
    changeUploadSize
})


</script>

<style lang="scss" scoped>
.list-item {
    height: 50px;
    margin-top: 12px;
}

.change-code {
    color: #b88230;
}

.upload {
    color: #79bbff;
    margin-right: 10px;
}

.upload-success {
    color: #95d475;
}

.upload-fail {
    color: rgb(255, 0, 0);
}
</style>
