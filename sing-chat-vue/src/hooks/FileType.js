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
const getSuffix = (fileCategory) => {
    if(fileCategory == 20)
        return ""
    return '.' + typeMap[fileCategory+""]
}

export default {
    getSuffix,
}