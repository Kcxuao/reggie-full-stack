<template>
    <div class="login" id="login-app">
        <div class="login-box">
            <img src="@/assets/images/login/login-l.png" alt="">
            <div class="login-form">
                <el-form :model="loginInfo" :rule="loginRules">
                    <div class="login-form-title">
                        <img src="@/assets/images/login/logo.png" style="width:139px;height:42px;" alt="" />
                    </div>
                    <el-form-item prop="username">
                        <el-input v-model="loginInfo.username" style="color: red" placeholder="账号" maxlength="20" />
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="loginInfo.password" type="password" placeholder="密码" maxlength="20" />
                    </el-form-item>
                    <el-form-item style="width:100%;">
                        <el-button :loading="loading" class="login-btn" type="primary" style="width:100%;"
                            @click="login">
                            <span v-if="!loading">登录</span>
                            <span v-else>登录中...</span>
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { loginApi } from '@/api/employeeApi.js'
import { useRouter } from 'vue-router'
import md5 from 'js-md5'

let loading = ref(false)

const router = new useRouter()

const loginInfo = reactive({
    username: 'admin',
    password: '123456'
})

const login = async () => {

    loading.value = true
    const loginData = { ...loginInfo }

    loginData.password = md5(loginInfo.password)

    const { data: res } = await loginApi(loginData)
    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success("登录成功")

    localStorage.setItem("identity", res.data.identity)
    localStorage.setItem("token", res.data.token)
    router.push('/')
}

const loginRules = reactive({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 5, message: '用户名长度应在5-10位', trigger: 'blur' },
    ],
    password: [
        { required: true, message: 'Please input Activity name', trigger: 'blur' },
        { min: 3, max: 5, message: 'Length 长度应在5-10位', trigger: 'blur' },
    ]
})
</script>

<style lang='less' scoped>
.login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-color: #333;
}

.login-box {
    width: 1000px;
    height: 474.38px;
    border-radius: 8px;
    display: flex;
}

.login-box img {
    width: 60%;
    height: auto;
}

.title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
}

.login-form {
    background: #ffffff;
    width: 40%;
    border-radius: 0px 8px 8px 0px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-form .el-form {
    width: 214px;
    height: 307px;
}

.login-form .el-form-item {
    margin-bottom: 30px;
}

.login-form .el-form-item.is-error .el-input__inner {
    border: 0 !important;
    border-bottom: 1px solid #fd7065 !important;
    background: #fff !important;
}

.login-form .input-icon {
    height: 32px;
    width: 18px;
    margin-left: -2px;
}

.login-form .el-input__inner {
    border: 0;
    border-bottom: 1px solid #e9e9e8;
    border-radius: 0;
    font-size: 14px;
    font-weight: 400;
    color: #333333;
    height: 32px;
    line-height: 32px;
}

.login-form .el-input__prefix {
    left: 0;
}

.login-form .el-input--prefix .el-input__inner {
    padding-left: 26px;
}

.login-form .el-input__inner::placeholder {
    color: #aeb5c4;
}

.login-form .el-form-item--medium .el-form-item__content {
    line-height: 32px;
}

.login-form .el-input--medium .el-input__icon {
    line-height: 32px;
}

.login-btn {
    border-radius: 17px;
    padding: 11px 20px !important;
    margin-top: 10px;
    font-weight: 500;
    font-size: 14px;
    border: 0;
    background-color: #ffc200;
}

.login-form-title {
    height: 36px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 40px;
}

.login-form-title .title-label {
    font-weight: 500;
    font-size: 20px;
    color: #333333;
    margin-left: 10px;
}
</style>