<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />个人中心
        </div>
        <div class="divUser">
            <img src="@/assets/images/headPage.png" />
            <div class="desc">
                <div class="divName">
                    <span class="name">{{ userInfo.name }}</span>
                    <img src="@/assets/images/women.png" />
                </div>
                <div class="divPhone">{{ userInfo.email }}</div>
            </div>
        </div>
    </div>

    <div class="divContent">
        <div class="divLinks">
            <div class="item" @click="router.push('/address')">
                <img src="@/assets/images/locations.png" />
                <span>地址管理</span>
                <van-icon name="arrow" />
            </div>
            <div class="divSplit"></div>
            <div class="item" @click="router.push('/orders')">
                <img src="@/assets/images/orders.png" />
                <span>历史订单</span>
                <van-icon name="arrow" />
            </div>
        </div>



        <div class="divOrders">
            <div class="title">最新订单</div>
            <OrderItem :order-list="orderList" />
        </div>
        <div class="quitLogin" @click="logout">
            退出登录
        </div>
    </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { logoutApi } from '@/api/userApi.js'
import { showSuccessToast, showFailToast } from 'vant';
import { useRouter } from 'vue-router'
import { listApi } from '@/api/orderApi.js'
import OrderItem from '../../components/OrderItem/OrderItem.vue';

let orderList = reactive([])

const router = new useRouter()

const userInfo = JSON.parse(sessionStorage.getItem('user'))

const logout = async () => {
    const { data: res } = await logoutApi()
    if (res.code !== 1) return showFailToast(res.msg)

    showSuccessToast(res.data)
    sessionStorage.clear('user')

    router.push('/login')

}

const getOrder = async () => {

    const { data: res } = await listApi()
    if (res.code !== 1) return showFailToast(res.msg)

    if (res.data.length > 0) {
        orderList.push(res.data[0])
    }
}

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

const __init__ = () => {
    getOrder()
}

__init__()
</script>

<style lang='less' scoped>
.divHead {
    width: 100%;
    height: 150rem;
    opacity: 1;
    background: #ffc200;
    box-sizing: border-box;
    padding-left: 12rem;
    padding-right: 12rem;

    .divTitle {
        height: 25rem;
        opacity: 1;
        font-size: 18rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 25rem;
        letter-spacing: 0;
        padding-top: 20rem;
        margin-bottom: 25rem;
        position: relative;

        i {
            position: absolute;
            left: 0;
            margin-top: 5rem;
        }
    }

    .divUser {
        display: flex;

        img {
            width: 58rem;
            height: 58rem;
            border-radius: 50%;
            margin-right: 16rem;
        }

        .desc {
            display: flex;
            flex-direction: column;
            justify-content: center;

            .divName {

                font-size: 16rem;
                font-family: PingFangSC, PingFangSC-Regular;
                font-weight: 500;
                text-align: left;
                color: #333333;
                margin-right: 6rem;
                margin-bottom: 5rem;
                display: flex;
                align-items: center;

                .name {
                    width: 100rem;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }

                img {
                    width: 16rem;
                    height: 16rem;
                    opacity: 1;
                    margin-left: 6rem;
                }
            }

            .divPhone {
                font-size: 14px;
                font-family: PingFangSC, PingFangSC-Regular;
                font-weight: 400;
                text-align: left;
                color: #333333;
            }
        }
    }
}

.divContent {
    height: calc(100% - 174rem);
    overflow-y: auto;

    .divLinks {
        height: 104rem;
        opacity: 1;
        background: #ffffff;
        border-radius: 6rem;
        padding-left: 17rem;
        padding-right: 11rem;
        margin: 10rem;

        .item {
            height: 51rem;
            line-height: 51rem;
            position: relative;
            display: flex;
            align-items: center;
            font-size: 14px;

            img {
                width: 18rem;
                height: 18rem;
                margin-right: 5rem;
            }

            .van-icon {
                position: absolute;
                right: 0;
                top: 50%;
                transform: translate(0, -50%);
            }
        }

        .divSplit {
            height: 1rem;
            opacity: 1;
            background-color: #ebebeb;
            border: 0;
        }
    }
}

.divOrders {
    margin: 0 10rem 10rem 10rem;
    background: #ffffff;
    border-radius: 6rem;
    padding-left: 10rem;
    padding-right: 10rem;
    padding-bottom: 6rem;

    .title {
        height: 60rem;
        line-height: 60rem;
        opacity: 1;
        font-size: 16rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: left;
        color: #333333;
        letter-spacing: 0;
        border-bottom: 2px solid #efefef;
    }
}

.quitLogin {
    margin: 0 10rem 10rem 10rem;
    height: 50rem;
    opacity: 1;
    background: #ffffff;
    border-radius: 6rem;
    opacity: 1;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 50rem;
}
</style>