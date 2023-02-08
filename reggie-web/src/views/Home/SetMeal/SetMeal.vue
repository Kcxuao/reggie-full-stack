<template>
    <div class="tableBar">
        <el-input v-model="pageInfo.name" placeholder="请输入套餐名称" clearable @keyup.enter.native="getPage"
            @clear="getPage">
        </el-input>
        <div class="tableLab">
            <span @click="removeBatch">批量删除</span>
            <span @click="updateStatusBatch(1)">批量启售</span>
            <span @click="updateStatusBatch(0)">批量停售</span>
            <el-button type="primary" @click="router.push('/home/addSetMeal')">
                + 新建套餐
            </el-button>
        </div>
    </div>

    <el-table :data="setMealData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="30" />
        <el-table-column prop="name" label="套餐名称" />
        <el-table-column prop="image" label="图片" align="center">
            <template #default="{ row }">
                <img :src="`http://localhost/files/${row.image}`" alt="">
            </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="套餐分类" />
        <el-table-column label="售价" align="center">
            <template #default="{ row }">
                <span>￥{{ row.price / 100 }}</span>
            </template>
        </el-table-column>
        <el-table-column label="售卖状态" align="center">
            <template #default="{ row }">
                <span>{{ row.status == '0' ? '停售' : '启售' }}</span>
            </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后操作时间">
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
            <template #default="{ row }">
                <el-button link @click="update(row)">
                    修改
                </el-button>
                <el-button link @click="updateStatus(row)">
                    {{ row.status == '0' ? '启售' : '停售' }}
                </el-button>
                <el-button link @click="remove(row)">
                    删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pageInfo.pageNum" v-model:page-size="pageInfo.pageSize"
        :page-sizes="[5, 10, 15, 20]" layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { pageApi, updateApi, removeBatchApi } from '@/api/setMealApi.js'
import { useStore } from '@/stores/setMealStore.js'

let total = ref(0)

let setMealData = ref([])

let setMealBatch = []

const router = new useRouter()

const pageInfo = reactive({
    pageNum: 1,
    pageSize: 10,
    name: ''
})

const updateStatus = async (row) => {

    row.status === 0 ? row.status = 1 : row.status = 0

    const { data: res } = await updateApi([row], 1)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)
}

const updateStatusBatch = async (status) => {

    setMealBatch.forEach(item => {
        item.status = status
    })

    const { data: res } = await updateApi(setMealBatch, 1)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)
}

const removeBatch = async () => {
    let flag = false

    setMealBatch.forEach(item => {
        if (item.status === 1) {
            flag = true
        }
    })

    if (flag) {
        return ElMessage.error("当前套餐未停售，无法删除")
    }

    const { data: res } = await removeBatchApi(setMealBatch)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    getPage()
    ElMessage.success(res.data)

}

const remove = (row) => {
    setMealBatch = [row]
    removeBatch()
}

const getPage = async () => {
    const { data: res } = await pageApi(pageInfo);
    if (res.code == 1) {
        total.value = res.data.total
        // console.log(res.data.records);
        setMealData.value = res.data.records
    }
}

const update = (row) => {
    row.flag = 0
    new useStore().setMeal = row
    router.push('/home/addSetMeal')
}

const handleSelectionChange = (val) => {
    setMealBatch = val
}

const handleSizeChange = (val) => {
    pageInfo.pageSize = val
    getPage()
}

const handleCurrentChange = (val) => {
    pageInfo.pageNum = val
    getPage()
}

getPage()
</script>

<style lang='less' scoped>
.el-table {
    border: 2px solid rgb(240, 240, 240);

    img {
        width: 60px;
        height: 60px;
        margin-top: 8px;
    }

    .el-button {
        font-weight: 400;
        font-size: 12px;
        color: #f56c6c;
    }

    .el-button:nth-child(-n+2) {
        height: 14px;
        padding-right: 12px;
        border-radius: 0;
        color: #419eff;
        border-right: 1px solid rgb(167, 167, 167);
    }
}

.tableBar {
    display: flex;
    margin-bottom: 20px;
    justify-content: space-between;

    .el-input {
        width: 250px;
    }

    .tableLab {

        span {
            cursor: pointer;
            display: inline-block;
            font-size: 14px;
            padding: 0 20px;
            color: #818693;
            border-right: solid 1px #d8dde3;
        }

        span:nth-child(2n+1) {
            color: #f56c6c;
        }

        span:nth-child(2) {
            color: #419eff;
        }

        .el-button {
            height: 40px;
            border: 0;
            padding: 20px;
            color: #333;
            background-color: #ffc200;
        }
    }

}
</style>