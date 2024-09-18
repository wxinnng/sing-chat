import { ElMessage, ElNotification } from 'element-plus'

/**信息提示 */
const message = (msg, type) => {
    ElMessage({
        message: msg,
        type: type
    })
}
/**信息通知**/
const notification = (title, msg, type) => {
    ElNotification({
        title: title,
        message: msg,
        type: type,
    })
}
const getRandom = (min, max) => {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
const getCurrentFormattedDateTime = () => {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的，所以要加1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
const timeArrayToFormattedDateTime = (time) => {
    return `${time[0]}-${time[1]}-${time[2]} ${time[3]}:${time[4]}:${time[5]}`;
}
const typeMap = {
    '1': 'c',
    '2': 'cpp',
    '3': 'css',
    '4': 'folder',
    '5': 'gif',
    '6': 'html',
    '7': 'java',
    '8': 'js',
    '9': 'jpg',
    '10': 'json',
    '11': 'mp3',
    '12': 'mp4',
    '13': 'pdf',
    '14': 'png',
    '15': 'pptx',
    '16': 'sql',
    '17': 'txt',
    '18': 'docx',
    '19': 'doc',
    '20': 'other',
    '21': 'md',
    '99': 'err'
}
const typesArray = [
    'c', 'cpp', 'css', 'folder', 'gif', 'html', 'java',
    'js', 'jpg', 'json', 'mp3', 'mp4', 'pdf', 'png',
    'pptx', 'sql', 'txt', 'docx', 'doc', 'other', 'md', 'err'
];
const getSuffix = (fileCategory) => {
    if (fileCategory in typeMap) {
        return '.' + typeMap[fileCategory];
    } else {
        return '';
    }
};
const getFileCategoryByFileName = (fileName,err = false) => {
    if(err)
        return 'err'


    let fileExtension = fileName.split('.').pop();

    return typesArray.indexOf(fileExtension) != -1 ? fileExtension : 'other';
}
function formatFileSize(fileSizeInBytes) {
    const units = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    let i = 0;

    while (fileSizeInBytes >= 1024 && i < units.length - 1) {
        fileSizeInBytes /= 1024;
        i++;
    }

    // 四舍五入并转换为字符串
    const formattedSize = `${fileSizeInBytes.toFixed(2)} ${units[i]}`;

    return formattedSize;
}
export {
    getRandom,
    getCurrentFormattedDateTime,
    message,
    notification,
    timeArrayToFormattedDateTime,
    getSuffix,
    typeMap,
    formatFileSize,
    getFileCategoryByFileName
}