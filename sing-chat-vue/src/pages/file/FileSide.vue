<template>
  <div class="aside">
    <el-container>
      <el-header class="side-header">
        <img src="/star.ico" style="margin-left:5px;height:  40px; width: 40px;" />
        Singing
      </el-header>
      <el-main class="side-main">
        <br>
        <br>
        <el-button type="success" @click="handleFilterFileList(1)" link>全部</el-button>
        <br>
        <br>
        <br>
        <el-button type="primary" @click="handleFilterFileList(2)" link>文档</el-button>
        <br>
        <br>
        <br>
        <el-button type="danger" @click="handleFilterFileList(3)" link>视频</el-button>
        <br>
        <br>
        <br>
        <el-button type="info" @click="handleFilterFileList(4)" link>图片</el-button>
        <br>
        <br>
        <br>
        <el-button type="warning" @click="handleFilterFileList(5)" link>其他</el-button>
      </el-main>
      <el-footer class="side-footer">
        <el-progress type="dashboard" :percentage="rate" :color="colors" width="60" />
        <svg @click="getRate" t="1717912099819" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4263" width="10" height="10"><path d="M927.999436 531.028522a31.998984 31.998984 0 0 0-31.998984 31.998984c0 51.852948-10.147341 102.138098-30.163865 149.461048a385.47252 385.47252 0 0 1-204.377345 204.377345c-47.32295 20.016524-97.6081 30.163865-149.461048 30.163865s-102.138098-10.147341-149.461048-30.163865a385.47252 385.47252 0 0 1-204.377345-204.377345c-20.016524-47.32295-30.163865-97.6081-30.163865-149.461048s10.147341-102.138098 30.163865-149.461048a385.47252 385.47252 0 0 1 204.377345-204.377345c47.32295-20.016524 97.6081-30.163865 149.461048-30.163865a387.379888 387.379888 0 0 1 59.193424 4.533611l-56.538282 22.035878A31.998984 31.998984 0 1 0 537.892156 265.232491l137.041483-53.402685a31.998984 31.998984 0 0 0 18.195855-41.434674L639.723197 33.357261a31.998984 31.998984 0 1 0-59.630529 23.23882l26.695923 68.502679a449.969005 449.969005 0 0 0-94.786785-10.060642c-60.465003 0-119.138236 11.8488-174.390489 35.217667a449.214005 449.214005 0 0 0-238.388457 238.388457c-23.361643 55.252253-35.22128 113.925486-35.22128 174.390489s11.8488 119.138236 35.217668 174.390489a449.214005 449.214005 0 0 0 238.388457 238.388457c55.252253 23.368867 113.925486 35.217667 174.390489 35.217667s119.138236-11.8488 174.390489-35.217667A449.210393 449.210393 0 0 0 924.784365 737.42522c23.368867-55.270316 35.217667-113.925486 35.217667-174.390489a31.998984 31.998984 0 0 0-32.002596-32.006209z" fill="#e6e6e6" p-id="4264"></path></svg>
        <span style=" font-size: 10px;" >{{ "共10GB" }}</span>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import service from '@/util/request'
import emitter from '../../mitt/index'

//默认的用户空间
const allSpace = 10737418240
const rate = ref(0)
const getRate = async () => {
  var res = await service.get('/user/space')
  rate.value = (res.data.data / allSpace).toFixed(2) * 100
}
const handleFilterFileList = (type) => {
  emitter.emit('filterFileList',type)
}
onMounted(async() => {
  await getRate()
})
const colors = [
  { color: '#6f7ad3', percentage: 20 },
  { color: '#5cb87a', percentage: 40 },
  { color: '#e6a23c', percentage: 60 },
  { color: '#1989fa', percentage: 80 },
  { color: '#f56c6c', percentage: 100 },
]



</script>

<style scoped>

.side-header {
  padding: 0;
  align-items: center;

}
.side-footer{
  padding: 0;
  margin-top:30px;
  color: white;
}

.side-main {
  height: 350px;
  padding: 0;
  margin-left: 10px;
}
.icon:active{
  transform: scale(0.4);
}
.icon{
  margin-right: 2px;
  transition: transform 0.2s; /* 添加过渡效果 */
}
</style>
