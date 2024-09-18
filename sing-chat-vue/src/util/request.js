import axios from "axios"; //引用axios
import { useUserStore } from '../stores/useUserStore'
import { base_request_url } from "@/env";

axios.defaults.timeout = 5000
axios.defaults.baseURL = base_request_url

const service = axios.create({
    timeout: 5000,
    baseUrl: base_request_url, 

})

/**请求拦截器 */
service.interceptors.request.use(
    config => {
        const {getUserInfo} = useUserStore()
        const token = getUserInfo().token
        config.data = JSON.stringify(config.data);     // 这里我们也可以在过滤下 请求参数数据
        config.headers = {
            // 设置请求头
            "Content-Type": "application/json",
        };
        if (token) {
            config.headers.token =  token;            //存在的话 就携带 token
        }
        return config;                                //然后把配置返回
    },
    err => {
        console.log(err)                              //如果出错的话 打印出来错误看看
    }
);

/**响应拦截器 */
service.interceptors.response.use(
    response => {
        return response;
    },
    err => {
        if(err.response.status === 401){
            window.location.href = "/login"
        }
    }
);
export default service; 
