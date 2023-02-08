import axios from 'axios'
import { app } from '@/config/config.js'
import { useRouter } from 'vue-router'
import { showFailToast } from 'vant';

const router = new useRouter()

const request = axios.create({
    baseURL: app.backUrl
})

request.interceptors.request.use(config => {

    const user = JSON.parse(sessionStorage.getItem('user'))
    if (user && user.token) {
        config.headers.token = user.token
    }

    return config
}, error => {
    Promise.reject(error)
})

request.interceptors.response.use(response => {
    if (response.data.msg === 'token无效' ) {
        router.replace('/login')
    }

    return response;
}, error => {
    showFailToast("接口错误")
})

export default request;