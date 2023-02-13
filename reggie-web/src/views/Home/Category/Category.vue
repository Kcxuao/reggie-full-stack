<template>

    <div class="tableBar" style="display: inline-block">
        <el-button type="primary" @click="addOrUpdateCategory('新增菜品分类', 0)">
            + 新增菜品分类
        </el-button>
        <el-button type="primary" @click="addOrUpdateCategory('新增套餐分类', 0)">
            + 新增套餐分类
        </el-button>
    </div>

    <el-table :data="tableData" stripe class="tableBox">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="type" label="分类类型">
            <template #default="{ row }">
                <span>{{ row.type == '1' ? '菜品分类' : '套餐分类' }}</span>
            </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="操作时间">
            <template #default="{ row }">
                {{ row.updateTime }}
            </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" />
        <el-table-column label="操作" width="160" align="center">
            <template #default="{ row }">
                <el-button link type="primary" @click="addOrUpdateCategory('修改分类', 1, row)">
                    修改
                </el-button>
                <el-button link type="danger" @click="remove(row.id)">
                    删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pageInfo.pageNum" v-model:page-size="pageInfo.pageSize"
        :page-sizes="[5, 10, 15, 20]" layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <el-dialog :title="title" v-model="dialogFormVisible">
        <el-form :model="categoryInfo" ref="categoryRef" label-width="100px">
            <el-form-item label="分类名称：">
                <el-input v-model="categoryInfo.name" placeholder="请输入分类名称" maxlength="14" />
            </el-form-item>
            <el-form-item label="排序：">
                <el-input v-model="categoryInfo.sort" type="number" placeholder="请输入排序" />
            </el-form-item>
        </el-form>

        <div class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
            <el-button type="primary" @click="save(1)">保存并继续添加 </el-button>
        </div>
    </el-dialog>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { pageApi, saveOrUpdateApi, removeApi } from '@/api/CategoryApi.js'

let dialogFormVisible = ref(false)

const title = ref('')

let flag = ref()

const categoryRef = ref()

const total = ref(0)

const tableData = ref([])

const pageInfo = reactive({
    pageNum: 1,
    pageSize: 10
})

const categoryInfo = reactive({
    name: '',
    sort: undefined,
    type: 1
})

const addOrUpdateCategory = (val, num, row) => {
    // 赋值标题与flag
    title.value = val
    flag.value = num

    // 清空表单
    categoryInfo.name = ''
    categoryInfo.sort = undefined

    // 判断点击的按钮赋予对应的type值
    if (val === '新增菜品分类') {
        categoryInfo.type = 1
    } else {
        categoryInfo.type = 2
    }

    // 点击编辑后为表单赋值
    if (row) {
        categoryInfo.id = row.id
        categoryInfo.name = row.name
        categoryInfo.sort = row.sort
    }

    // 显示表单弹窗
    dialogFormVisible.value = true
}

const getPage = async () => {
    const { data: res } = await pageApi(pageInfo)
    tableData.value = res.data.records
    total.value = res.data.total
}

const save = async (sign) => {

    if (categoryInfo.name === '' || categoryInfo.sort === undefined) {
        console.log(typeof categoryInfo.sort);
        return ElMessage.error("参数不能为空")
    }

    const { data: res } = await saveOrUpdateApi(categoryInfo, flag.value)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)

    if (sign === 1) {
        categoryInfo.name = ''
        categoryInfo.sort = undefined
    } else {
        dialogFormVisible.value = false
        getPage()

        categoryInfo.name = ''
        categoryInfo.sort = undefined
    }
}

const handleSizeChange = (val) => {
    pageInfo.pageSize = val
    getPage()
}

const handleCurrentChange = (val) => {
    pageInfo.pageNum = val
    getPage()
}

const remove = async (id) => {
    ElMessageBox.confirm(
        '确认删除该分类?',
        '提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            const { data: res } = await removeApi(id)

            if (res.code !== 1) {
                return ElMessage.error(res.msg)
            }

            getPage()
            ElMessage.success(res.data)
        })

}

getPage()

</script>
<style lang='less' scoped>
.tableBar {

    margin-top: 10px;

    .el-button {
        height: 40px;
        border: 0;
        background-color: #ffc200;
        color: #333;
    }

    .el-button:first-child {
        background-color: #333;
        color: rgb(239, 239, 239);
    }
}

.el-table {
    margin-top: 20px;
    border: 2px solid rgb(229, 229, 229);

    .el-button {
        font-weight: 400;
        font-size: 12px;
    }

    .el-button:first-child {
        height: 14px;
        padding-right: 12px;
        border-radius: 0;
        color: #419EFF;
        border-right: 1px solid rgb(167, 167, 167);
    }
}

.el-dialog {


    :deep(.el-form) {
        margin-left: 10%;

        .el-form-item__label {

            margin-top: 10px;
            color: #ffc200;
        }
    }

    .el-input {
        width: 300px;
    }
}

.dialog-footer {

    margin-left: 30%;
    margin-top: 30px;

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
.el-dialog {
    width: 568px;
}

.el-dialog__header {
    padding: 20px 30px;
    background: #fbfbfa;
    margin-right: 0;
    border-radius: 8px 8px 0px 0px;
    border-bottom: 1px solid #e5e4e4;
}
</style>