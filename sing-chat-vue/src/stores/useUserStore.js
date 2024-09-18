import { defineStore } from 'pinia';

export const useUserStore = defineStore('userStore', {
    // 定义用户信息实体
    state: () => ({
        user: {
            id: null,
            nickName: null,
            avatar: null,
            email:null,
            age:null,
            phone:null,
            token: null,
        },
        lastNickIndex: 0,
        reloadUserList: 1,
        reloadMessage:1,
        lastNickSideIndex: 'chat',
        currentPath:[{id:'root',path:'root'}]
    }),
    persist:{
        enabled:true,
    },

    // 修改登录
    actions: {
        changeUser(id, nickName, avatar, token,email,phone,age) {
            this.user.id = id;
            this.user.nickName = nickName;
            this.user.avatar = avatar;
            this.user.token = token;
            this.user.age = age;
            this.user.phone = phone;
            this.user.email = email;
        },
        getLastNickSideIndex(){
            return this.lastNickSideIndex;
        },
        changeLastClickSideIndex(index){
            this.lastNickSideIndex = index;
        },
        changeReloadUserList() {
            this.reloadUserList = this.user.reloadUserList + 1;
        },
        changeReloadMessage() {
            this.reloadMessage = this.user.reloadMessage + 1;
        },
        addNodeToCurrentPath(path){
            this.currentPath.push(path);
        },
        backCurrentPath(id){
            //直到把前面的的路径都删除
            
            for(let i = this.currentPath.length - 1;i >= 1 ;i --){
                if(this.currentPath[i].id == id)
                    break;
                else
                    this.currentPath.pop();
            }
        },
        getCurrentId(){
            return this.currentPath[this.currentPath.length-1].id;
        },
        getAllPath(){
            return this.currentPath;
        },

        // 注销
        logout() {
            this.user = {
                id: null,
                nickName: null,
                avatar: null,
                token: null,
                phone:null,
                email:null,
                age:null
            };
        },

        // 获得用户信息
        getUserInfo() {
            return this.user;
        },
    },

},);