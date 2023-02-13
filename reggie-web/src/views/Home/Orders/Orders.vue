<template>
    <!-- 搜索项 -->
    <div class="tableBar">
        <el-input v-model="pageInfo.name" placeholder="请输入订单号" style="width: 250px">
            <i slot="prefix" class="el-input__icon el-icon-search" style="cursor: pointer" @click="init"></i>
        </el-input>
        <el-date-picker v-model="orderTime" clearable value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
            placeholder="选择日期" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']" />
        <el-button class="search-btn" @click="">查询</el-button>
    </div>

    <el-table :data="tableData" stripe class="tableBox">
        <el-table-column prop="number" label="订单号" min-width="110"></el-table-column>
        <el-table-column prop="订单状态" label="订单状态">
            <template slot-scope="{ row }">
                <span>{{ getOrderType(row) }}</span>
            </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="orderTime" label="下单时间" min-width="100"></el-table-column>
        <el-table-column prop="amount" label="实收金额">
            <template slot-scope="{ row }">
                <span>￥{{ row.amount }}</span>
            </template>
        </el-table-column>
        <el-table-column prop="btn" label="操作">
            <template slot-scope="{ row }">
                <el-button type="text" @click="goDetail(row)" class="blueBug">
                    查看
                </el-button>
                <el-divider v-if="row.status === 2" direction="vertical"></el-divider>
                <el-button v-if="row.status === 2" type="text" @click="cancelOrDeliveryOrComplete(3, row.id)"
                    class="blueBug">
                    派送
                </el-button>
                <el-divider v-if="row.status === 3" direction="vertical"></el-divider>
                <el-button v-if="row.status === 3" type="text" @click="cancelOrDeliveryOrComplete(4, row.id)"
                    class="blueBug">
                    完成
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="pageInfo.pageNum" v-model:page-size="pageInfo.pageSize"
        :page-sizes="[5, 10, 15, 20]" layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <!-- 查看弹框部分 -->
    <el-dialog title="订单信息" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
        <div class="info-box">
            <div class="item-box">
                <span class="label">订单号：</span>
                <span class="des">{{ diaForm.number }}</span>
            </div>
            <div class="item-box">
                <span class="label">订单状态：</span>
                <span class="des">{{ getOrderType(diaForm) }}</span>
            </div>
            <div class="item-box">
                <span class="label">收货人：</span>
                <span class="des">{{ diaForm.consignee }}</span>
            </div>
            <div class="item-box">
                <span class="label">联系电话：</span>
                <span class="des">{{ diaForm.phone }}</span>
            </div>
            <div class="item-box">
                <span class="label">地址：</span>
                <span class="des">{{ diaForm.address }}</span>
            </div>
            <div class="item-box">
                <span class="label">支付金额：</span>
                <span class="des">{{ diaForm.amount }}</span>
            </div>
            <div class="item-box">
                <span class="label">下单时间：</span>
                <span class="des">{{ diaForm.orderTime }}</span>
            </div>
        </div>
    </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue';


let total = ref(0)

let dialogVisible = ref(false)

let orderTime = ref()

const pageInfo = reactive({
    pageNum: 1,
    pageSize: 10,
    name: ''
})

const handleSizeChange = (val) => {
    pageInfo.pageSize = val
    getPage()
}

const handleCurrentChange = (val) => {
    pageInfo.pageNum = val
    getPage()
}

</script>

<style lang='less' scoped>

.tableBar {

    :deep(.el-range-editor.el-input__wrapper) {
        position: relative;
        top: 3px;
        height: 40px;
        margin-left: 20px;
    }

    .search-btn {
        padding: 20px;
        margin-left: 20px;
        border: none;
        background-color: #ffc200;
    }
}

.el-table {
    margin-top: 20px;
    border: 2px solid rgb(240, 240, 240);
}
</style>