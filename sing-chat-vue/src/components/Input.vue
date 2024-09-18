<template>

</template>

<script setup >
import {ref} from 'vue';

import Emoji from "@/components/Emoji.vue";
// 自定义输入框dom 绑定ref
const messageInputDom = ref()
// 选择的emoji
const selectEmoji = (index) => {
  // 没有焦点就获取输入框焦点
  if (document.activeElement !== messageInputDom.value) {
    messageInputDom.value.focus()
  }
  let emojiImg = `<img src="/gif/${index}.gif" width="25" height="25" style="vertical-align: middle;">`
  document.execCommand('insertHTML', false, emojiImg)
  // 保存最近使用的emoji
  recentlyUseEmoji(index)
}
</script>

<style scoped lang="scss">
.container {
  width: 100%;
  height: 80%;
  flex: 1;
  padding: 10px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.send-message-btn {
  position: absolute;
  right: 5px;
  bottom: 5px;
  color: #323644;
}

.top-icon {
  width: 12%;
  display: flex;
  .icon-box {
    margin-right: 10px;
    cursor: pointer;
  }
}

#message-input {
  width: 60%;
  height: 100%;
  flex: 1;
  margin-top: 0;
  margin-left: 0;
  box-sizing: border-box;
  border: #323644 solid 1px;
  resize: none;
  overflow: auto;
  color: white;
  background-color: #424349;
  line-height: 100%;
  &::placeholder {
    font-size: 15px;
  }
}
</style>