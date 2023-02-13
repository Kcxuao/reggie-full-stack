<template>
    <div class="tableBar">
        <el-input v-model="pageInfo.name" placeholder="请输入菜品名称" clearable
            @keyup.enter.native="getPage" @clear="getPage">
        </el-input>
        <div class="tableLab">
            <span @click="removeBatch">批量删除</span>
            <span @click="updateStatusBatch(1)">批量启售</span>
            <span @click="updateStatusBatch(0)">批量停售</span>
            <el-button type="primary" @click="router.push('/home/adddish')">
                + 新建菜品
            </el-button>
        </div>
    </div>

    <el-table :data="dishData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="30" />
        <el-table-column prop="name" label="菜品名称" />
        <el-table-column prop="image" label="图片" align="center">
            <template #default="{ row }">
                <img :src="`${app.backUrl}/files/${row.image}`" alt="">
            </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="菜品分类" />
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
                <el-button link size="small" @click="update(row)">
                    修改
                </el-button>
                <el-button link size="small" @click="updateStatus(row, 1)">
                    {{ row.status == '0' ? '启售' : '停售' }}
                </el-button>
                <el-button link size="small" class="delBut non" @click="remove(row)">
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
import { pageApi, removeBatchApi, updateApi, updateBatchApi } from '@/api/dishApi.js'
import { useRouter } from 'vue-router';
import { useStore } from '@/stores/dishStore.js'
import { ElMessage } from 'element-plus';
import { app } from '@/config/config.js'

let total = ref(0)

let dishData = ref([])

let dishBatch = []

const router = new useRouter()

const pageInfo = reactive({
    pageNum: 1,
    pageSize: 10,
    name: ''
})

const updateStatus = async (row, flag) => {

    if (flag === 1) {
        row.status === 0 ? row.status = 1 : row.status = 0
    }

    const { data: res } = await updateApi(row, flag)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)
}

const update = (row) => {
    row.flag = 0
    new useStore().dish = row

    router.push('/home/adddish')

}

const getPage = async () => {
    const { data: res } = await pageApi(pageInfo);
    if (res.code == 1) {
        total.value = res.data.total
        // console.log(res.data.records);
        dishData.value = res.data.records
    }
}

const updateStatusBatch = async (status) => {

    dishBatch.forEach(item => {
        item.status = status
    })

    const { data: res } = await updateBatchApi(dishBatch)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)
}

const handleSelectionChange = (val) => {
    dishBatch = val
}

const handleSizeChange = (val) => {
    pageInfo.pageSize = val
    getPage()
}

const handleCurrentChange = (val) => {
    pageInfo.pageNum = val
    getPage()
}

const removeBatch = async () => {
    let flag = false

    dishBatch.forEach(item => {
        if (item.status === 1) {
            flag = true
        }
    })

    if (flag) {
        return ElMessage.error("当前菜品未停售，无法删除")
    }

    const { data: res } = await removeBatchApi(dishBatch)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    getPage()
    ElMessage.success(res.data)

}

const remove = (row) => {
    dishBatch.push(row)
    removeBatch()
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