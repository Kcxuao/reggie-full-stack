<template>
    <van-nav-bar title="登 录" class="title" />

    <!-- 输入表单 -->
    <van-cell-group inset class="login-form">
        <van-field v-model="loginForm.email" placeholder="请输入邮箱">
            <template #button>
                <van-button class="code" @click="getCode" v-if="visible" :disabled="!loginForm.email"
                    :loading="codeVisible" loading-text="发送中...">获取验证码</van-button>

                <!-- 倒计时组件 -->
                <van-count-down @finish="visible = true" :time="time" format="ss" v-else />
            </template>
        </van-field>
        <van-field v-model="loginForm.code" placeholder="请输入验证码" />
    </van-cell-group>

    <van-button round class="submit" :disabled="!loginForm.email" @click="login" :loading="loginVisible"
        loading-text="登录中...">登录</van-button>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { codeApi, loginApi } from '@/api/userApi.js'
import { showSuccessToast, showFailToast } from 'vant';
import { useRouter } from 'vue-router';

// 展示倒计时
let visible = ref(true)

// 展示加载动画
let codeVisible = ref(false)

let loginVisible = ref(false)

// 倒计时时间
let time = ref(60 * 1000)

// 路由实例
const router = new useRouter()

// 用户信息
const loginForm = reactive({
    email: '',
    code: ''  // 验证码
})

// 获取验证码
const getCode = async () => {

    codeVisible.value = true

    const flag = checkEmail(loginForm.email)

    if (!flag) return showFailToast("请输入正确的邮箱")

    const { data: res } = await codeApi(loginForm)

    if (res.code === 1) {
        showSuccessToast(res.data);
        // 展示倒计时
        visible.value = false
    } else {
        showFailToast(res.msg);
    }
}

// 登录
const login = async () => {
    loginVisible.value = true
    const { data: res } = await loginApi(loginForm)
    if (res.code !== 1) return showFailToast(res.msg);
    
    sessionStorage.setItem('user', JSON.stringify(res.data))
    router.push('/home')
    showSuccessToast('登录成功')
}

// 校验邮箱
const checkEmail = (email) => {
    const reg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    return reg.test(email)
}
</script>

<style lang='less' scoped>
.title {
    width: 100%;
    background-color: #333;

    :deep(.van-nav-bar__title) {
        color: #fff;
        font-size: 20px;
    }
}

.login-form {
    margin-top: 20px;

    .van-field {
        padding: 20px;

        .code,
        .van-count-down {
            border: none;
            color: var(--color);
        }

        .code {
            height: 0;
            top: -4px
        }
    }
}

.submit {
    position: relative;
    left: 50%;
    width: 92%;
    margin-top: 20px;
    transform: translateX(-50%);
    background-color: var(--color);
}
</style>