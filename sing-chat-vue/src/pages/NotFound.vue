<script setup>

</script>

<template>
  <div :v-html="`
  <div @click='test'>点我</div>
`"  class="container">
    <!-- <div class="image">
      <img src="../assets/404.png" style="width: 520px;height: 320px" alt="">
    </div>
    <div class="content">
      <p style="font-size: 70px">404</p>
    </div>
    <div class="desc">
      <p style="color: #72767b">你是想去外太空吗！</p>
    </div> -->

  </div>
</template>
<script setup>
import { ref} from "vue"
const aa = ref(`
  <button @click='test'>点我</button>
`)

const test = ()=>{
  console.log("test================================")
}
</script>
<style scoped>
.container {
  display: flex;
  flex-direction: column; /* 设置为列方向 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 如果你也想在垂直方向上居中 */
  /* 其他样式 */
}
.container:first-child{
  margin-top: 80px;
}
</style>