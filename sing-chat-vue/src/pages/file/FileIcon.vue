<template>
  <div class="file-icon">
    <img :src="`/file/${suffix}.png`" style="height: 40px;width: 40px;" alt=""/>
    <div class="font">{{ props.file.fileName}}</div>
  </div>
</template>

<script setup>
const typeMap = {
  '1':'c',
  '2':'cpp',
  '3':'css',
  '4':'folder',
  '5':'gif',
  '6':'html',
  '7':'java',
  '8':'js',
  '9':'jpg',
  '10':'json',
  '11':'mp3',
  '12':'mp4',
  '13':'pdf',
  '14': 'png',
  '15':'ppt', 
  '16': 'sql',
  '17':'txt',
  '18':'docx',
  '19':'doc',
  '20':'other'
}
const props = defineProps(['file'])
const suffix = typeMap[props.file.fileCategory] 

</script>

<style  scoped>
.file-icon{
    width: 42px;
    height: 100px;
    border-radius: 10px;
    overflow: hidden;
    margin-right: 20px;
}
.font{
    font-size: 12px;
    color:rgb(207, 191, 191);
    text-align: center;
    height: 17px;
    overflow: hidden;
}
img:active{
    transform: scale(0.4);
}
img{
    transition: transform 0.2s; /* 添加过渡效果 */
}
</style>
