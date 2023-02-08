<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />菩提阁
        </div>
    </div>

    <div class="divBody" v-if="orderList.length">
        <van-list finished-text="没有更多了">
            <OrderItem :order-list="orderList" />
        </van-list>
    </div>

    <div class="divNoData" v-else>
        <div class="divContainer">
            <img src="@/assets/images/no_order.png" />
            <div>暂无订单</div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { listApi } from '@/api/orderApi.js'
import { showFailToast } from 'vant';
import OrderItem from '../../components/OrderItem/OrderItem.vue';


let orderList = ref([])

const router = new useRouter()

const getOrderList = async () => {

    const { data: res } = await listApi()
    if (res.code !== 1) return showFailToast(res.msg)

    orderList.value = res.data
}

const __init__ = () => {
    getOrderList()
}

__init__()


</script>

<style lang='less' scoped>
.divHead {
    width: 100%;
    height: 50rem;
    opacity: 1;
    background: #333333;
    position: relative;

    .divTitle {
        font-size: 18rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #ffffff;
        line-height: 25rem;
        letter-spacing: 0;
        position: absolute;
        bottom: 13rem;
        width: 100%;

        i {
            position: absolute;
            left: 16rem;
            top: 50%;
            transform: translate(0, -50%);
        }
    }
}

.divBody {
    margin: 10rem 12rem 10rem 12rem;
    background: #ffffff;
    border-radius: 6rem;
    padding-left: 10rem;
    padding-right: 10rem;
    height: calc(90vh);
    overflow: scroll;

    .van-list .van-cell::after {
        border: 0;
    }


}


.divNoData {
    width: 100%;
    height: calc(100% - 88rem);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    .divContainer {

        img {
            width: 240rem;
            height: 129rem;
        }

        div {
            font-size: 24rem;
            font-family: PingFangSC, PingFangSC-Medium;
            font-weight: 500;
            text-align: center;
            color: #333333;
            line-height: 33rem;
            height: 33rem;
            margin-top: 20rem;
        }
    }
}
</style>