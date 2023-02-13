<template>
    <van-cell class="item" v-for="order in props.orderList" :key="order.id">
        <div class="timeStatus">
            <span>{{ order.orderTime }}</span>
            <span>{{ getStatus(order.status) }}</span>
        </div>
        <div class="dishList">
            <div class="item" v-for="item in order.orderDetails" :key="item.id">
                <span>{{ item.name }}</span>
                <span>x{{ item.number }}</span>
            </div>
        </div>
        <div class="result">
            <span>共{{ order.orderDetails.length }} 件商品,实付</span><span class="price">￥{{
                order.amount /
                    100
            }}</span>
        </div>
        <div class="btn">
            <div class="btnAgain" @click="encore(order.id)">再来一单</div>
        </div>
    </van-cell>
</template>

<script setup>
import { computed, defineProps } from 'vue'
import { useRouter } from 'vue-router'
import { encoreApi } from '@/api/orderApi.js'
import { showFailToast } from 'vant';
 
const router = new useRouter()

const props = defineProps({
    orderList: []
})

const getStatus = computed(() => {
    return (status) => {
        let str = ''
        switch (status) {
            case 1:
                str = '待付款'
                break;
            case 2:
                str = '正在派送'
                break;
            case 3:
                str = '已派送'
                break;
            case 4:
                str = '已完成'
                break;
            case 5:
                str = '已取消'
                break;
        }
        return str
    }
})

const encore = async (id) => {
    const {data: res} = await encoreApi(id)
    if (res.code !== 1) return showFailToast(res.msg)

    router.push('/addOrders')
}


</script>

<style lang='less' scoped>
.item {
    .timeStatus {
        height: 46rem;
        line-height: 16rem;
        font-size: 14rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 400;
        text-align: left;
        color: #666666;
        line-height: 20rem;
        letter-spacing: 0;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 2rem dashed #efefef;
        border-top: 1px solid #efefef;

        span:first-child {
            color: #333333;
        }
    }

    .dishList {
        padding-top: 10rem;
        padding-bottom: 11rem;

        .item {
            padding-top: 5rem;
            padding-bottom: 5rem;
            display: flex;
            justify-content: space-between;
            height: 20rem;
            opacity: 1;
            font-size: 14rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #666666;
            line-height: 20rem;
            letter-spacing: 0;

        }
    }
}

.result {
    display: flex;
    justify-content: flex-end;
    height: 20rem;
    opacity: 1;
    font-size: 14rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 400;
    text-align: left;
    color: #666666;
    line-height: 20rem;

    .price {
        color: #343434;
    }
}

.btn {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 17rem;
    margin-top: 20rem;

    .btnAgain {
        width: 124rem;
        height: 36rem;
        opacity: 1;
        border: 1px solid #e5e4e4;
        border-radius: 19rem;
        opacity: 1;
        font-size: 14rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 36rem;
        letter-spacing: 0;
        position: relative;
        background-color: var(--color);
    }
}
</style>