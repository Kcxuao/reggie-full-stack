<template>
    <div class="tableBar">
        <el-input v-model="pageInfo.name" clearable placeholder="请输入员工姓名" :prefix-icon="Search" @keyup="search($event)"
            @clear="getPage" />

        <el-button @click="addEmployee">+ 添加员工</el-button>
    </div>

    <el-table :data="tableData" stripe>
        <el-table-column prop="name" label="员工姓名"></el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column label="账号状态">
            <template #default="{ row }">
                <span>{{ row.status === 1 ? '正常' : '禁用' }}</span>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" v-if="identity === '管理员'">
            <template #default="{ row }">
                <el-button link size="small" @click="update(row)">
                    编辑
                </el-button>
                <el-button link size="small" type="danger" @click="updateStatus(row)">
                    {{ row.status == '1' ? '禁用' : '启用' }}
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
import { Search } from '@element-plus/icons-vue'
import { pageApi, saveOrUpdateApi } from '@/api/employeeApi.js'
import { useRouter } from 'vue-router';
import { useStore } from '@/stores/employeeStores.js'

const router = new useRouter();

const store = useStore()

const identity = localStorage.getItem('identity')

let total = ref(0)

let tableData = ref([])

const pageInfo = reactive({
    name: '',
    pageNum: 1,
    pageSize: 10
})

const getPage = async () => {
    const { data: res } = await pageApi(pageInfo)

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    total.value = res.data.total
    tableData.value = res.data.records
}

const addEmployee = () => {
    router.push('/home/addOrUpdateEmployee')
}

const search = (e) => {
    if (e.key === 'Enter') {
        getPage()
    }
}

const updateStatus = (data) => {
    ElMessageBox.confirm(
        '确认调整该账号的状态?',
        '提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            data.status === 1 ? data.status = 0 : data.status = 1

            const { data: res } = await saveOrUpdateApi(data, 1)

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
        })
}

const update = (row) => {
    store.employee = row
    router.push('/home/addOrUpdateEmployee')
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
.tableBar {
    margin-bottom: 20px;

    .el-input {
        width: 250px;
        height: 40px;
    }

    .el-button {
        float: right;
        height: 40px;
        border: 0;
        background-color: #ffc200;
        color: #333;
    }
}

.el-table {
    border: 2px solid rgb(240, 240, 240);

    .el-button:last-child {
        border-radius: 0;
        height: 12px;
        padding-left: 12px;
        border-left: 1px solid rgb(220, 220, 220);
    }

    .el-button:first-child {
        color: #419EFF;
    }
}
</style>