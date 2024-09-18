<template>
  <div class="emoji-container">
    <!-- 最近使用 -->
    <div class="use-wrap" v-if="historyEmojiList.length">
      <div class="emoji-title">最近使用</div>
      <div class="recently-use-emoji">
        <div class="emoji-item" v-for="(item, index) in historyEmojiList" :key="index">
          <img :src="`/gif/${item}.gif`" alt="">
        </div>
      </div>
    </div>
    <!-- 所有表情列表 -->
    <div class="emoji-title">小黄脸表情包</div>
    <div class="all-emoji">
      <div class="emoji-item" @click="addEmoji(item)" v-for="(item, index) in 203" :key="index">
        <img :src="`/gif/${item}.gif`" alt="">
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref ,watch} from 'vue'
const props = defineProps(['historyEmojiList'])
const historyEmojiList = ref([])
const emit = defineEmits(['selectEmoji'])
const addEmoji = (item) => {
  emit('selectEmoji', item)
}
watch(() => props.historyEmojiList, (val) => {
  historyEmojiList.value = val
}, { deep: true })

</script>

<style lang="scss" scoped>
.emoji-container {
  padding: 0;
  height: 350px;
  box-sizing: border-box;
  overflow: auto;

  &::-webkit-scrollbar {
    display: none;
  }
}

.emoji-title {
  font-size: 12px;
  margin: 10px 0 5px;
}

.recently-use-emoji,
.all-emoji {
  width: 100%;
  display: flex;
  flex-wrap: wrap;

  .emoji-item {
    width: 30px;
    height: 30px;
    margin: 5px 10px;
    cursor: pointer;

    img {
      width: 100%;
      height: 100%;
    }
  }
}</style>