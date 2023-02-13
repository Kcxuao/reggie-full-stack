<template>
    <el-form ref="setMealRef" :model="setMealData" :rules="rules">
        <el-form-item label="套餐名称:" prop="name">
            <el-input v-model.trim="setMealData.name" placeholder="请填写套餐名称" maxlength="20" />
        </el-form-item>

        <el-form-item label="套餐分类:" prop="categoryId">
            <el-select v-model.trim="setMealData.categoryId" placeholder="请选择套餐分类">
                <el-option v-for="(item, index) in categoryList" :key="index" :label="item.name" :value="item.id" />
            </el-select>
        </el-form-item>

        <el-form-item label="套餐价格:" prop="price">
            <el-input v-model.number="setMealData.price" type="number" placeholder="请设置套餐价格" />
        </el-form-item>

        <el-form-item label="套餐菜品:" prop="" class="setmeal-dish">
            <i class="required">*</i>

            <el-button @click="showAddMenu(0)" v-if="setMealData.setmealDishes.length === 0">+ 添加菜品</el-button>

            <div class="dish-table" v-else>
                <el-button @click="showAddMenu(1)">+ 添加菜品</el-button>

                <el-table :data="setMealData.setmealDishes">
                    <el-table-column prop="name" label="名称" width="180" />
                    <el-table-column prop="price" label="原价" width="180">
                        <template #default="{ row }">
                            <span>¥ {{ row.price / 100 }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="copies" label="份数" align="center">
                        <template #default="{ row }">
                            <el-input-number v-model="row.copies" :min="1" :max="100" />
                        </template>
                    </el-table-column>
                    <el-table-column prop="address" label="操作" align="center">
                        <template #default="{ row }">
                            <button @click="setMealData.setmealDishes.splice(setMealData.setmealDishes.indexOf(row), 1)"
                                class="remove" type="button">删除</button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <el-dialog v-model="visible" class="dish-menu">
                <template #header="{ titleId, titleClass }">
                    <div class="my-header">
                        <h4 :id="titleId" :class="titleClass">添加菜品</h4>
                        <el-input v-model="dishName" placeholder="请输入菜品名称进行搜索"
                            @keyup.enter.native="getDishList(dishName, 'name')" />
                    </div>
                </template>


                <div class="container">
                    <div class="left">
                        <el-tabs v-if="searchVisible" v-model="dishCateId" :tab-position="'left'"
                            @tab-change="getDishList(dishCateId)">
                            <el-tab-pane :label="dishCate.name" v-for="dishCate in dishCateList" :key="dishCate.id"
                                :name="dishCate.id">
                                <el-checkbox-group v-model="tempSetmealDishes">
                                    <el-checkbox
                                        :label="{ copies: 1, dishId: `${dish.id}`, name: `${dish.name}`, price: `${dish.price}` }"
                                        v-for="dish in dishList" :key="dish.id">
                                        <span>{{ dish.name }}</span>
                                        <span class="status">{{ dish.status === 1 ? '在售' : '停售' }}</span>
                                        <span class="price">¥ {{ dish.price / 100 }}</span>
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-tab-pane>
                        </el-tabs>

                        <div v-else class="serach">
                            <el-checkbox-group v-model="tempSetmealDishes">
                                <el-checkbox
                                    :label="{ copies: 1, dishId: `${dish.id}`, name: `${dish.name}`, price: `${dish.price}` }"
                                    v-for="dish in dishList" :key="dish.id">
                                    <span>{{ dish.name }}</span>
                                    <span class="status">{{ dish.status === 1 ? '在售' : '停售' }}</span>
                                    <span class="price">¥ {{ dish.price / 100 }}</span>
                                </el-checkbox>
                            </el-checkbox-group>
                        </div>
                    </div>

                    <div class="right">
                        <p class="title">已选菜品({{ tempSetmealDishes.length }})</p>

                        <el-tag v-for="tag, i in tempSetmealDishes" :key="i">
                            <span>{{ tag.name }}</span>
                            <span class="price">¥ {{ tag.price / 100 }}</span>
                            <i class="iconfont icon-close" @click="tempSetmealDishes.splice(i, 1)" />
                        </el-tag>
                    </div>
                </div>

                <template #footer>
                    <span class="dialog-footer">
                        <el-button
                            @click="visible = false; tempSetmealDishes = setMealData.setmealDishes">取消</el-button>
                        <el-button type="primary"
                            @click="setMealData.setmealDishes = tempSetmealDishes; visible = false">
                            确认
                        </el-button>
                    </span>
                </template>
            </el-dialog>
        </el-form-item>

        <el-form-item label="套餐图片:" class="uploadImg" prop="image">
            <el-upload class="avatar-uploader" :action="app.backUrl + '/files'" :headers="header"
                :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                <img v-if="setMealData.image" :src="app.backUrl + '/files/' + setMealData.image" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                    <Plus />
                </el-icon>
            </el-upload>
        </el-form-item>

        <el-form-item label="套餐描述:">
            <el-input v-model.trim="setMealData.description" type="textarea" placeholder="套餐描述，最长200字" />
        </el-form-item>

    </el-form>

    <div class="btns">
        <el-button @click="router.go(-1)"> 取消 </el-button>
        <el-button type="primary" @click="saveOrupadte(setMealRef)"> 保存 </el-button>
        <el-button v-if="new useStore().setMeal.flag !== 0" type="primary" @click="saveOrupadte(setMealRef, true)">
            保存并继续添加套餐
        </el-button>
    </div>


</template>

<script setup>
import { reactive, ref } from 'vue';
import { Plus } from '@element-plus/icons-vue'
import { ElInput, ElMessage } from 'element-plus'
import { app } from '@/config/config.js'
import { categoryApi } from '@/api/CategoryApi.js'
import { listApi } from '@/api/dishApi.js'
import { saveApi, updateApi } from '@/api/setMealApi.js'
import { useRouter } from 'vue-router';
import { useStore } from '@/stores/setMealStore.js'


let visible = ref(false)

let dishName = ref()

let dishCateId = ref()

let dishList = ref([])

let searchVisible = ref(true)

let tempSetmealDishes = ref([])

// 套餐信息
let setMealData = reactive({
    name: '',
    categoryId: '',
    price: '',
    setmealDishes: [],
    image: '',
    description: '',
    status: 1,
    code: ''
})

// 表单dom节点
const setMealRef = ref()

// 路由实例
const router = new useRouter()

// 分类列表
const categoryList = reactive([])

// 菜系分类列表
const dishCateList = reactive([])

// 文件上传请求头
const header = {
    token: localStorage.getItem('token')
}

// 显示添加菜品菜单并初始化页面
const showAddMenu = () => {
    const id = dishCateList[0].id
    getDishList(id)

    dishCateId.value = id
    visible.value = true
}

// 获取分类列表
const getSetMealCategory = async () => {
    // type 1: 菜系 ； 2: 套餐
    const { data: res } = await categoryApi(2)
    categoryList.push(...res.data)
}

const getDishCategory = async () => {
    const { data: res } = await categoryApi(1)
    dishCateList.push(...res.data)
}

const getDishList = async (val, flag) => {

    if (flag === 'name' && !val) {
        searchVisible.value = true
    } else if (flag === 'name') {
        searchVisible.value = false
    }

    const { data: res } = await listApi(val)
    dishList.value = res.data
}

const initValue = () => {
    const setMeal = new useStore().setMeal

    if (setMeal.flag === 0) {
        setMealData = setMeal
        tempSetmealDishes.value = setMealData.setmealDishes
    }

}

const saveOrupadte = async (setMealRef) => {

    if (!setMealRef) return

    await setMealRef.validate(async (valid) => {
        if (setMealData.setmealDishes.length === 0) {
            return ElMessage.error("未添加菜品")
        }

        if (valid) {
            let res;
            if (new useStore().setMeal.flag === 0) {
                res = (await updateApi([setMealData], 0)).data
            } else {
                res = (await saveApi([setMealData])).data
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

const handleAvatarSuccess = (response, uploadFile) => {
    setMealData.image = response.data
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

const rules = reactive({
    name: [{ required: true, message: '请输入套餐名称', trigger: 'blur' }],
    image: [{ required: true, message: '请上传套餐图片', trigger: 'blur' }],
    price: [{ required: true, message: '请输入套餐价格', trigger: 'blur' }],
    categoryId: [{ required: true, message: '请选择套餐分类', trigger: 'blur' }],
})

getSetMealCategory()

getDishCategory()

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

    .el-form-item:nth-child(4) {
        margin-left: 11px;

        .required {
            position: absolute;
            top: 0px;
            left: -84px;
            font-style: normal;
            color: rgb(247, 101, 101);

        }
    }

    .el-form-item:last-child {
        margin-left: 11px;
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

    .setmeal-dish {

        .el-dialog__header {
            background-color: #333;
        }

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


:deep(.dish-menu) {
    min-width: 800px;
    border-radius: 5px;

    .el-dialog__header {
        margin: 0;
        padding: 20px;
        border-radius: 5px 5px 0 0;
        border-bottom: 1px solid #e5e4e4;
        background-color: #fbfbfa;

        .my-header {

            .el-input {
                position: absolute;
                top: 15px;
                right: 50px;
                width: 250px;
            }
        }

    }

    .el-dialog__body {
        padding: 0;

        .container {
            display: flex;
            border-bottom: 2px solid #E4E7ED;

            .left {
                width: 60%;
                height: 100%;
                padding: 20px;
                border-right: solid 2px #E4E7ED;

                .el-checkbox-group {
                    width: 100px;
                    margin-left: 20px;

                    .el-checkbox {
                        width: 260px;
                        padding: 20px;
                        border: 1px solid #E4E7ED;
                        border-radius: 6px;
                        margin-bottom: 10px;

                        .price {
                            position: absolute;
                            right: 10px;
                        }

                        .status {
                            position: absolute;
                            right: 80px;
                        }
                    }

                    .el-checkbox:last-child {
                        margin: 0;
                    }
                }
            }

            .right {
                width: 35%;
                height: 100%;
                margin: 0 auto;

                .el-tag {
                    display: block;
                    width: 90%;
                    height: 40px;
                    margin: 10px 0;
                    line-height: 38px;
                    background-color: #fff;
                    border: 1px solid #E4E7ED;

                    .price {
                        position: absolute;
                        right: 100px;
                    }

                    i {
                        float: right;
                        cursor: pointer;
                        font-size: 18px;
                        color: #c6c6c6;
                    }
                }
            }
        }
    }

    .dialog-footer {
        .el-button {
            margin-top: 10px;
            width: 80px;
        }

        .el-button:first-child {
            background-color: #fff;
            border: 1px solid rgb(222, 222, 222);
        }
    }
}

.dish-table {

    width: 100%;
    border: 2px solid rgb(220, 220, 220);
    border-radius: 6px;
    padding: 20px;
    background-color: #fafafb;

    .el-table {
        margin-top: 20px;
        border: 1px solid #ccc;
        border-radius: 6px;

        .el-input-number {
            margin: 5px;
            // border: 1px solid #333;

            :deep(.el-input-number__decrease) {
                top: -2px;
                height: 38px;
            }

            :deep(.el-input-number__increase) {
                top: -2px;
                height: 38px;
            }
        }

        .remove {
            border: none; //自定义边框
            color: #ffc200;
            background-color: transparent;
            cursor: pointer;
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