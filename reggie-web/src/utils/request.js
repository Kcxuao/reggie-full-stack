import axios from 'axios'
import { app } from '@/config/config.js'

const request = axios.create({
    baseURL: app.backUrl
})

request.interceptors.request.use(config => {

    if (localStorage.getItem('token')) {
        config.headers.token = localStorage.getItem('token')
    }

    return config
}, error => {
    Promise.reject(error)
})

request.interceptors.response.use(response => {

    return response;
}, error => {
    Promise.reject(error)
})

export default request;