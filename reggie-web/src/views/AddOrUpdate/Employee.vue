<template>
    <el-form ref="ruleForm" :model="employeeInfo" :rules="rules" :inline="false" label-width="180px">
        <el-form-item label="账号:" prop="username">
            <el-input v-model="employeeInfo.username" placeholder="请输入账号" maxlength="20" />
        </el-form-item>
        <el-form-item label="员工姓名:" prop="name">
            <el-input v-model="employeeInfo.name" placeholder="请输入员工姓名" maxlength="20" />
        </el-form-item>

        <el-form-item label="手机号:" prop="phone">
            <el-input v-model="employeeInfo.phone" placeholder="请输入手机号" maxlength="20" />
        </el-form-item>
        <el-form-item label="性别:" prop="sex">
            <el-radio-group v-model="employeeInfo.sex">
                <el-radio label="男"></el-radio>
                <el-radio label="女"></el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="身份证号:" prop="idNumber">
            <el-input v-model="employeeInfo.idNumber" placeholder="请输入身份证号" maxlength="20" />
        </el-form-item>
    </el-form>

    <div class="subBox">
        <el-form-item>
            <el-button @click="goBack()">
                取消
            </el-button>
            <el-button type="primary" @click="save(ruleForm)">
                保存
            </el-button>
            <el-button type="primary" @click="save(ruleForm, true)">
                保存并继续添加
            </el-button>
        </el-form-item>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { saveOrUpdateApi } from '@/api/employeeApi.js'
import { useRouter } from 'vue-router';
import { useStore } from '@/stores/employeeStores.js'

const ruleForm = ref()

const store = useStore()

const router = new useRouter()
// 员工信息
const employeeInfo = reactive({
    username: '',
    name: '',
    phone: '',
    sex: '男',
    idNumber: ''
})

const save = async (ruleForm, flag) => {

    if (!ruleForm) return

    await ruleForm.validate(async (valid) => {
        if (valid) {
            employeeInfo.sex === '男' ? employeeInfo.sex = 1 : employeeInfo.sex = 0
            const { data: res } = await saveOrUpdateApi(employeeInfo, 0)

            if (res.code !== 1) {
                return ElMessage({
                    type: 'error',
                    message: res.msg,
                })
            }

            ElMessage({
                type: 'success',
                message: res.data,
            })

            if (flag) {
                ruleForm.resetFields()
            } else {
                router.go(-1)
            }

        } else {
            ElMessage({
                type: 'error',
                message: "参数错误，请检查输入的信息",
            })
        }
    })


}

const goBack = () => {
    router.go(-1)
}

// 表单自定义校验
// 校验手机号
const checkPhone = (rule, value, callback) => {
    if (employeeInfo.phone === '') {
        return callback(new Error('请输入手机号'))
    }

    const phoneRegular = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1589]))\d{8}$/
    const flag = phoneRegular.test(employeeInfo.phone)

    if (!flag) {
        return callback(new Error('手机号格式错误'))
    }

    callback()
}

// 校验身份证号
const validID = (rule, value, callback) => {
    if (employeeInfo.idNumber === '') {
        return callback(new Error('请输入身份证号'))
    }

    const idRegular = /^[1-9]\d{5}(?:18|19|20)\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\d|30|31)\d{3}[\dXx]$/
    const flag = idRegular.test(employeeInfo.idNumber)

    if (!flag) {
        return callback(new Error('身份证格式错误'))
    }

    callback()

}

// 校验用户名
const checkUsername = (rule, vlaue, callback) => {
    if (employeeInfo.username === '') {
        return callback(new Error('请输入用户名'))
    }

    const regular = /^[\w-]{4,16}$/
    const flag = regular.test(employeeInfo.username)

    if (!flag) {
        return callback(new Error('用户名格式错误'))
    }

    callback()
}

// 校验姓名
const checkName = (rule, vlaue, callback) => {
    if (employeeInfo.name === '') {
        return callback(new Error('请输入姓名'))
    }

    const regular = /^(?:[\u4e00-\u9fa5·]{2,16})$/
    const flag = regular.test(employeeInfo.name)

    if (!flag) {
        return callback(new Error('姓名格式错误'))
    }

    callback()
}

const rules = reactive({
    username: [{ required: true, validator: checkUsername, 'trigger': 'blur' }],
    name: [{ required: true, validator: checkName, 'trigger': 'blur' }],
    phone: [{ required: true, validator: checkPhone, 'trigger': 'blur' }],
    idNumber: [{ required: true, validator: validID, 'trigger': 'blur' }]
})

const assig = () => {
    const { id, username, name, sex, phone, idNumber } = store.employee
    employeeInfo.id = id
    employeeInfo.username = username
    employeeInfo.name = name
    employeeInfo.sex === 1 ? employeeInfo.sex = '男' : '女'
    employeeInfo.phone = phone
    employeeInfo.idNumber = idNumber
}

assig()
</script>

<style lang='less' scoped>
.el-form {

    margin-top: 30px;
    margin-bottom: 20px;
    border-bottom: 1px solid rgb(221, 221, 221);

    .el-input {
        width: 300px;
    }

    :deep(.el-radio__input.is-checked .el-radio__inner) {
        background-color: #ffc200;
        border-color: #ffc200;
    }

    :deep(.el-radio__input.is-checked+.el-radio__label) {
        color: #ffc200;
    }

    :deep(.el-radio__label) {
        color: #8e8e8e;

    }
}

.subBox {

    margin-left: 40%;

    .el-button {
        height: 40px;
        padding: 20px;
    }

    .el-button:nth-child(2) {
        border: 0;
        background-color: #ffc200;
    }

    .el-button:nth-child(3) {
        border: 0;
        background-color: #333;
    }
}
</style>