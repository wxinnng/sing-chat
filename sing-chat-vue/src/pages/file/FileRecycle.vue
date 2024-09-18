<template>
  <div>
    <el-table :data="tableData" stripe style="width: 600px">
    <el-table-column prop="fileName" label="文件名" width="180" />
    <el-table-column prop="fileSize" label="文件大小" width="120" />
    <el-table-column prop="createTime" label="上传时间"  width="140" />
    <el-table-column label="操作" width="140">
        <template #default="scope">
            <el-button type="success" size="small" @click="handleRecover(scope.row)">复原</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
    </el-table-column>
  </el-table>
  </div>
</template>

<script setup>
import service from '@/util/request';
import {formatFileSize, message} from '@/util/Utils'

import {ref ,onMounted} from 'vue'

onMounted(async()=>{
    await getRecycleList()
})
const getRecycleList = async()=>{
    const res = await service.get('/file/recycle_list')
    tableData.value = res.data.data
    for(let i = 0;i<tableData.value.length;i++){
        tableData.value[i].fileSize = formatFileSize(tableData.value[i].fileSize)
    }
}
const tableData = ref([])
const handleRecover = async ({fileId}) => {
    var res = await service.get('/file/restore',{
       params:{
            fileId:fileId
       }
    })
    if(res.data.code == 1)
        await getRecycleList()
    else
        message('网络出现了点异常！','error')
}
const handleDelete = async ({fileId}) => {
    var res = await service.get('/file/delete',{
       params:{
            fileId:fileId
       }
    })
    if(res.data.code == 1)
        await getRecycleList()
    else
        message('网络出现了点异常！','error')
}
</script>

<style lang="scss" scoped>
</style>
