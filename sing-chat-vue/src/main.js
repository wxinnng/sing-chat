import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './route'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'


/**引入pinia */
import { createPinia } from 'pinia'
/**引入pinia持久化 */
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(router)
    .use(ElementPlus,{
        locale:zhCn
    })
    .use(pinia)
    .mount('#app')
    
