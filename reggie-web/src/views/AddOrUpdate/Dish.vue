<template>
    <el-form ref="dishRef" :model="dishForm" :rules="rules">
        <el-form-item label="菜品名称:" prop="name">
            <el-input v-model.trim="dishForm.name" placeholder="请填写菜品名称" maxlength="20" />
        </el-form-item>

        <el-form-item label="菜品分类:" prop="categoryId">
            <el-select v-model.trim="dishForm.categoryId" placeholder="请选择菜品分类">
                <el-option v-for="(item, index) in categoryList" :key="index" :label="item.name" :value="item.id" />
            </el-select>
        </el-form-item>

        <el-form-item label="菜品价格:" prop="price">
            <el-input v-model.number="dishForm.price" type="number" placeholder="请设置套餐价格" />
        </el-form-item>

        <el-form-item label="口味做法配置:" class="dishFlavor">
            <el-button @click="showAddFlavor" v-if="!visible">+ 添加口味</el-button>

            <div v-else class="add-dish-flavor">
                <div class="container">
                    <p>口味名（3个字内）口味标签（输入标签回车添加）</p>

                    <div class="row" v-for="item, i in flavors" :key="i">
                        <el-input v-model="item.name" class="select" @click="item.flag = true" placeholder="请输入口味" />

                        <div class="flavor" v-if="item.flag">
                            <div v-for="item1 in flavorSelect" @click="setFlavorValue(item, item1)">
                                {{ item1.name }}
                            </div>
                        </div>

                        <div class="value-box">
                            <el-tag v-for="tag, i1 in item.value" :key="i1" closable :disable-transitions="false"
                                @close="handleClose(item, i1)">
                                {{ tag }}
                            </el-tag>

                            <input ref="inputRef" v-if="item.showOption" class="tagInput"
                                @blur="showTag(item, $event.target.value)" @keyup.enter="$event.target.blur()" />

                            <el-button v-else class="tagBtn" size="small" @click="showInput(item)">
                                + New Tag
                            </el-button>
                        </div>

                        <a @click="removeFlavor(i)">删除</a>
                    </div>

                    <el-button class="addbth" @click="addFlavor">添加口味</el-button>
                </div>
            </div>
        </el-form-item>

        <el-form-item label="菜品图片:" class="uploadImg" prop="image">
            <el-upload class="avatar-uploader" :action="app.backUrl + '/files'" :headers="header"
                :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                <img v-if="dishForm.image" :src="app.backUrl + '/files/' + dishForm.image" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                </el-icon>
            </el-upload>
        </el-form-item>

        <el-form-item label="菜品描述:">
            <el-input v-model.trim="dishForm.description" type="textarea" placeholder="菜品描述，最长200字" />
        </el-form-item>

    </el-form>

    <div class="btns">
        <el-button @click="router.go(-1)"> 取消 </el-button>
        <el-button type="primary" @click="saveOrupadte(dishRef)"> 保存 </el-button>
        <el-button v-if="new useStore().dish.flag !== 0" type="primary"  @click="saveOrupadte(dishRef, true)">
            保存并继续添加套餐
        </el-button>
    </div>


</template>

<script setup>
import { reactive, ref, nextTick } from 'vue';
import { Plus } from '@element-plus/icons-vue'
import { ElInput, ElMessage } from 'element-plus'
import { app } from '@/config/config.js'
import { categoryApi } from '@/api/CategoryApi.js'
import { saveApi, updateApi } from '@/api/dishApi.js'
import { useRouter } from 'vue-router';
import { useStore } from '@/stores/dishStore.js'


let visible = ref(false)

const inputRef = ref()

const dishRef = ref()

const router = new useRouter()

const flavors = reactive([])

const categoryList = reactive([])

const header = {
    token: localStorage.getItem('token')
}

const flavorSelect = [
    { 'name': '甜味', 'value': ['无糖', '少糖', '半糖', '多糖', '全糖'] },
    { 'name': '温度', 'value': ['热饮', '常温', '去冰', '少冰', '多冰'] },
    { 'name': '忌口', 'value': ['不要葱', '不要蒜', '不要香菜', '不要辣'] },
    { 'name': '辣度', 'value': ['不辣', '微辣', '中辣', '重辣'] }
]

let dishForm = reactive({
    name: '',
    categoryId: '',
    price: '',
    flavors: [],
    image: '',
    description: '',
    status: 1,
    code: ''
})

const categorySelect = async () => {
    // type 1 : 菜品 ； 2 : 套餐
    const { data: res } = await categoryApi(1)
    categoryList.push(...res.data)
}

const initValue = () => {

    const { dish } = new useStore()

    // flag 0: 修改操作； 无: 新增
    if (dish.flag === 0) {
        dishForm = dish

        dishForm.flavors.forEach(item => {
            flavors.push({ name: item.name, value: JSON.parse(item.value) })
        })

        visible.value = true
    }
}

const saveOrupadte = async (dishRef) => {

    if (!dishRef) return

    await dishRef.validate(async (valid) => {
        if (valid) {
            flavors.forEach(item => {
                dishForm.flavors.push({ name: item.name, value: JSON.stringify(item.value) })
            })

            let res;
            if (new useStore().dish.flag === 0) {
                res = (await updateApi(dishForm, 0)).data
            } else {
                res = (await saveApi(dishForm)).data
            }

            if (res.code !== 1) {
                return ElMessage.error(res.msg)
            }

            ElMessage.success(res.data)
            router.go(-1)
        } else {
            ElMessage.error('参数错误，请检查输入的信息')
        }
    })

}

const setFlavorValue = (flavor, val) => {
    flavor.name = val.name
    flavor.value = val.value
    flavor.flag = false
}

const addFlavor = () => {
    flavors.push({ name: '', value: [], flag: false })

}

const handleClose = (flavor, i) => {
    flavor.value.splice(i, 1)
}

const showInput = (item) => {

    item.showOption = true

    nextTick(() => {
        inputRef.value[0].focus()
    })
}

const removeFlavor = (index) => {
    flavors.splice(index, 1)

    if (flavors.length === 0) {
        visible.value = false
    }
}

const handleAvatarSuccess = (response, uploadFile) => {
    dishForm.image = response.data
}

const beforeAvatarUpload = (rawFile) => {
    if (rawFile.type !== 'image/jpeg') {

        ElMessage.error('Avatar picture must be JPG format!')
        return false

    } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error('Avatar picture size can not exceed 2MB!')
        return false
    }
    return true
}

const showAddFlavor = () => {
    flavors.push({ name: '', value: [], showOption: false })
    visible.value = true
}

const showTag = (flavor, val) => {
    flavor.showOption = false

    if (val.trim() !== '') {
        flavor.value.push(val)
    }
}

const rules = reactive({
    name: [{ required: true, message: '请输入菜品名称', trigger: 'blur' }],
    image: [{ required: true, message: '请上传菜品图片', trigger: 'blur' }],
    price: [{ required: true, message: '请输入菜品价格', trigger: 'blur' }],
    categoryId: [{ required: true, message: '请选择菜品分类', trigger: 'blur' }],
})

categorySelect()

initValue()

</script>

<style lang='less' scoped>
.el-form {
    margin-top: 20px;
    width: 1000px;
    margin-left: 180px;

    .add-dish-flavor {
        width: 100%;
        min-height: 172px;
        border: solid 1px #dfe2e8;
        border-radius: 3px;
        background-color: #fafafb;

        .container {
            margin: 20px;

            .flavor {
                position: absolute;
                width: 150px;
                border-radius: 3px;
                border: solid 1px #FF903D;
                text-align: center;
                background: #fff;
                margin-top: 50px;
                z-index: 99;

                div {
                    padding: 3px 0;
                    color: rgb(154, 154, 154);
                    cursor: pointer;
                    margin: 0 auto;
                    padding-right: 80px;
                }

                div:nth-child(-n+3) {
                    width: 130px;
                    border-bottom: 1px solid rgb(225, 225, 225);
                }
            }

            p {
                color: #606168;
                margin-bottom: 20px;
            }

            .row {
                display: flex;
                margin-top: 20px;

                a {
                    color: red;
                    cursor: pointer;
                }

                .select {
                    width: 150px;
                    margin-right: 20px;

                }

                .value-box {
                    position: relative;
                    top: -3px;
                    min-height: 40px;
                    min-width: 520px;
                    padding-left: 10px;
                    line-height: 40px;
                    border: 1px solid rgb(217, 217, 217);
                    border-radius: 3px;
                    margin-right: 20px;

                    .el-tag {
                        margin-right: 10px;
                    }

                    .tagBtn {
                        height: 25px;
                        width: 80px;
                        border: 1px solid rgb(227, 227, 227);
                        background-color: #fff;
                    }

                    .tagInput {
                        width: 80px;
                        height: 25px;
                        border: 1px solid rgb(224, 224, 224);
                        padding-left: 10px;
                        border-radius: 3px;
                    }

                    .tagInput:focus {
                        outline: none;
                        border: 1px solid #ffc200;
                    }

                    .el-input__wrapper {
                        height: 25px !important
                    }

                }

                :deep([contenteditable]:focus) {
                    border: 1px solid #ffc200;
                    outline: none
                }
            }

            .addbth {
                margin-top: 20px;
            }

        }
    }

    .el-form-item {
        display: flex;

        .el-input {
            width: 293px;
        }

        .avatar-uploader .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }
    }

    .el-form-item:last-child {
        margin-left: 10px;
    }

    :deep(.el-textarea__inner) {
        height: 100px;
    }

    .el-form-item:nth-child(2) {
        float: right;
        margin-top: -62px;
    }

    :deep(.el-input__wrapper) {
        width: 293px;
        height: 40px;
        margin-top: -3px;
    }

    .dishFlavor {
        margin-left: -17px;

        .el-button {
            width: 110px;
            height: 40px;
            margin-top: -3px;
            border: 0;
            color: #333;
            background-color: #ffc200;
        }
    }


}





.btns {
    display: flex;
    justify-content: center;
    padding: 30px 0;
    border-top: 1px solid rgb(239, 239, 239);


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

<style>
.avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
}

.el-upload-list {
    width: 0;
    height: 0;
    left: 99999px;
    position: absolute;
}
</style>