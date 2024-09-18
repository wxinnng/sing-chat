<template>
  <div>
    <RouterView></RouterView>
  </div>
</template>

<script setup>
import {RouterView} from "vue-router";


</script>


<style >
  *::-webkit-scrollbar
  {
      width: 8px;
      height: 8px;
      background-color: #ffffff;
  }
 
  /*定义滚动条轨道 内阴影+圆角*/
  *::-webkit-scrollbar-track
  {
      -webkit-box-shadow: inset 0 0 9px rgba(0,0,0,0.3);
     border-radius: 10px;
     background-color: #F5F5F5;
  }
 
  /*定义滑块 内阴影+圆角*/
  *::-webkit-scrollbar-thumb
  {
      border-radius: 10px;
     -webkit-box-shadow: inset 0 0 9px  rgb(125, 94, 238);
      background-color: #bfbfbf;
  }
</style>
