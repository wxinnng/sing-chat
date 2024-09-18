<template>
    <div class="header-main">
      <FileButton content="创建文件夹" type="default" class="file-button"/>
      <FileButton content="删除文件" type="danger" class="file-button"/>
      <el-upload
        action="#"
        :multiple="true"
        :auto-upload="false"
        v-model:fileList="fileList"
        :on-change="handleChange"
        :on-remove="handleRemove"
      >
        <FileButton content="上传文件" type="primary" class="file-button"/>
      </el-upload>
      <FileSearch class="search-box"/>
    </div>
  </template>
  
  <script setup>
  import FileButton from './FileButton.vue'
  import FileSearch from './FileSearch.vue'
  import service from '@/util/request';
  
  // 分片大小
  const chunkSize = 5 * 1024 * 1024
  // 上传文件列表
  const fileList = ref([])
  // 文件表
  const tableData = ref([])
  
  
  const uploadFileToServer = async (file, chunkNumber, chunkTotal, fileName) => {
      const form = new FormData();
      // 这里的data是文件
      form.append("file", file);
      form.append("chunkNumber", chunkNumber);
      form.append("chunkTotal", chunkTotal);
      form.append("fileName", fileName)
      const result = await service.post("http://localhost:8080/file/upload", form)
      return result
    }
    
    
    
    /**
     * 合并文件
     * @param chunkTotal 文件分块的总数量
     * @param fileName 文件名称
     */
    const mergeFiles = async (chunkTotal ,fileName) => {
      // const form = new FormData();
      // form.append("chunkTotal", chunkTotal);
      // form.append("fileName", fileName)
      // await axios.post("http://localhost:9090/uploadVideo/merge", form)
    
      // 这两种方式都可以，毕竟后台是@RequestMapping
    //   const result = await axios.get(`http://localhost:9090/uploadVideo/merge?chunkTotal=${chunkTotal}&fileName=${fileName}`)
      // 这个result.data是axios相关的了，这里就不说了
      return result.data
    }
  // 上传文件列表变化
  const handleChange = async (uploadFile,uploadFiles) => {
  
    tableData.value.push({ ...uploadFile })
    const index = tableData.value.findIndex(item => item.uid === uploadFile.uid)
    //文件名字
    let fileName = uploadFile.name
    //文件的大小
    const fileSize = uploadFile.size || 0
  
    //分片上传
    let chunkTotals = Math.ceil(fileSize / chunkSize);
  
    //分片上传
    if (chunkTotals > 0) {
        for (let chunkNumber = 0, start = 0; chunkNumber < chunkTotals; chunkNumber++, start += chunkSize) {
          
          //文件最后的end
          let end = Math.min(fileSize, start + chunkSize);
          // el-mement - plus中，上传的文件就在raw里面
          const files = uploadFile.raw?.slice(start, end)
          //上传的结果
          const result = await uploadFileToServer(files, chunkNumber + 1, chunkTotals, fileName)
          //上传的进度
          const percents = parseFloat(result.data.replace("%", ''))
          //上传的百分比
          uploadFile.percentage = percents
          //tableData总显示
          tableData.value[index].percentage = percents
          // percentage.value = percents
          console.log(result.data)
        }
        //这里可以tableData.value[index].percentage先判断一下进度是否100%了，然后再合并，防止只上传了一半中途出现问题了也合并
        // const videoUrl = await mergeFiles(chunkTotals, fileName)
        // tableData.value[index].url = videoUrl
      }
  
  }
  
  // 删除文件列表
  const handleRemove = async (file) => {
        // 这个删除表格tableData中的列表数据
        const index2 = tableData.value.findIndex((item2) => item2.uid === uploadFile.uid)
      if (index2 !== -1) {
        tableData.value.splice(index2, 1)
      }
      await deleteFileByFileName(uploadFile.name)
      // handleRemove内置的删除文件方法，
      // 这里的uploadFiles跟绑定的v-model:file-list="fileList"可以看成是同一个数组，这两个数组的数据是一样的，从这两个数组中删除或者移除任何一个数据，
      // 另外一个数据也会跟着变化的，这个element plus已经实现了
      console.log(uploadFiles)
  }
  
  </script>
  
  <style lang="scss" scoped>
  .header-main {
      display: flex;
  }
  .file-button {
      margin-right: 10px;
  }
  .search-box {
      margin-left: auto;
  }
  </style>
  