import {createRouter , createWebHistory} from "vue-router";
import Main from "../pages/Main.vue"
import NotFound from "../pages/NotFound.vue"
import Login from "../components/Login.vue"
import File from "../pages/file/File.vue"
import Chat from "@/pages/chat/ChatFrame.vue";

const route = [
    {
        name:'main',
        path:"/main",
        component : Main,
        children:[
            {
                name:'file',
                path:"file",
                component:File
            },
            {
                name:'chat',
                path:"chat",
                component:Chat
            }
        ]
    },
    {
        name:'login',
        path:"/login",
        component : Login
    },
    {
        name:'index',
        path:'/',
        component:Main,
    }
]
const router = createRouter({
    history: createWebHistory(),
    routes: route,
})

//404
router.addRoute("/",{
    path:"/:pathMatch(.*)*",
    name:"notFound",
    component:NotFound
})
export default router