
import SparkMD5 from 'spark-md5'
import axios from 'axios';
import {env_server_production} from '@/env/index'
//默认分片大小
const chunkSize = 5 * 1024 * 1024

//计算文件的md5
function generateMD5OfFile(file) {
    return new Promise((resolve, reject) => {
        let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice,                        // Read in chunks of 2MB
            chunks = Math.ceil(file.size / chunkSize),
            currentChunk = 0,
            spark = new SparkMD5.ArrayBuffer(),
            fileReader = new FileReader();

        fileReader.onload = function (e) {
            console.log('read chunk nr', currentChunk + 1, 'of', chunks);
            spark.append(e.target.result);                   // Append array buffer
            currentChunk++;

            if (currentChunk < chunks) {
                loadNext();
            } else {
                resolve(spark.end())
            }
        };

        fileReader.onerror = function () {

            reject('MD5 calc error')
        };

        function loadNext() {
            let start = currentChunk * chunkSize,
                end = ((start + chunkSize) >= file.size) ? file.size : start + chunkSize;

            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        }

        loadNext();
    })
}

// 上传文件到服务器
const uploadFileToServer = async (file, chunkNumber, chunkTotal, fileName,filePid, fileMd5,userId) => {
    const form = new FormData();

    // 这里的data是文件
    form.append("file", file);
    form.append("chunkNumber", chunkNumber);
    form.append("chunkTotal", chunkTotal);
    form.append("fileName", fileName)
    form.append("fileMd5", fileMd5)
    form.append("filePid", filePid)
    form.append("userId", userId)


    var result = await axios({
        url: env_server_production + '/file/upload',
        headers: { 'Content-Type': 'multipart/form-data' },
        method: "post",
        timeout: 1000000,
        data: form
    })

    return result
}
export { generateMD5OfFile ,uploadFileToServer};