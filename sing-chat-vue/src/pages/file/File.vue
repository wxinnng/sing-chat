<template>
  <div class="file-main"> 
    <el-container>
      <el-aside width="60px">
        <FileSide/>
      </el-aside>
      <el-container>
        <el-header>
          <FileHeader  @addChangeNum="addChangeNum" />
        </el-header>
        <el-main class="el-main">
          <FileMain ref="fileMain"  @changeCurrentPath="changeCurrentPath" :fileListChange="fileListChange"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import FileHeader from './FileHeader.vue'
import FileMain from './FileMain.vue'
import FileSide from './FileSide.vue'
import { ref ,provide} from 'vue'

//搜索内容
const searchContent = ref('')
//main组件
const fileMain = ref(null)
//搜索处理方法
const handleSearch =  () => {
  console.log(searchContent.value)

}
const changeCurrentPath = (path)=>{
  folderPath.value = path  
}
//
const fileListChange = ref(0)

const addChangeNum = async () => {
  if(fileMain.value)
    await fileMain.value.getFileList()
}

//提供给孙组件访问
provide('searchContent', {searchContent,handleSearch})

</script>

<style lang="scss" scoped>
.file-main{
    width: 1070px;
    height: 550px;
    margin: 0 auto;
    background-color: #323644;
    border-radius: 20px;
    padding: 10px;
    
}
.el-main{
  padding: 0 10px;
}
</style>
