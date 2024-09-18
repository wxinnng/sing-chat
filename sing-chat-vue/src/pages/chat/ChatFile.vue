<template>
    <div class="chat-file-main">
        <!-- 图标 -->
        <div class="file-icon">
            <img :src="`/file/${getFileCategoryByFileName(fileInfo.fileName)}.png`">
        </div>

        <!-- 文件名称 -->
        <div class="file-name">
            <div style="font-size: 16px;"> {{ fileInfo.fileName }}</div>
            <br>
            <span style="font-size: 11px;"> {{ formatFileSize(fileInfo.fileSize) }}</span>
        </div>

        <!-- 按钮 -->
        <div class="file-button">
            <el-button type="success" size="default" @click="handleSave">保存文件</el-button>
        </div>
    </div>

</template>

<script setup>
import { getFileCategoryByFileName, formatFileSize ,message} from "@/util/Utils"
import service from "@/util/request"
const { fileInfo } = defineProps(["fileInfo"])
const handleSave = async() => {
    var res = await service.get(`/file/save/${fileInfo.fileId}`)
    if (res.data.code === 1) {
        message("已保存到root目录下", "success")
    }else{
        message("保存失败", "error")
    }
}

</script>

<style lang="scss" scoped>

.chat-file-main{
    display: grid;
    place-items: center;
}
.file-icon{
    margin-top: 20px;
    margin-bottom: 30px;
}
.file-name{
    display: grid;
    place-items: center;
    overflow: hidden;
    margin-bottom: 50px;
}
.file-name>div:first-child{
    display: grid;
    place-items: center;
    width:120px;
    text-overflow: ellipsis;
    overflow: hidden;
}
.file-button{
    margin-bottom: 20px;
}
</style>
