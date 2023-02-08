<template>
    <!-- 头部 -->
    <div class="header">
        <img src="@/assets/images/user.png" alt="" class="user" @click="router.push('/user')">

        <!-- 标签 -->
        <div class="title-box">
            <div class="title">
                <img src="@/assets/images/logo.png" class="logo">
                <div class="desc">
                    <p>菩提阁餐厅</p>
                    <div class="send">
                        <span>
                            <img src="@/assets/images/time.png" alt="" />
                            距离1.5km
                        </span>
                        <span>
                            <img src="@/assets/images/money.png" alt="" />
                            配送费6元
                        </span>
                        <span>
                            <img src="@/assets/images/location.png" alt="" />
                            预计时长12min
                        </span>
                    </div>
                </div>
            </div>
            <div class="info">
                简介: 菩提阁中餐厅是菩提阁点餐的独立的品牌，定位“大众 化的美食外送餐饮”，旨为顾客打造专业美食。
            </div>
        </div>
    </div>

    <!-- 内容区域 -->
    <div class="container">
        <!-- 左侧菜单 -->
        <div class="menu">
            <van-sidebar v-model="active" @change="getDishList(cate.type, cate.id)">
                <van-sidebar-item :title="item.name" v-for="item in cateList" @click="cate = item" />
            </van-sidebar>
        </div>

        <!-- 右侧菜品列表 -->
        <div class="list">
            <!-- 占位符 -->
            <div class="seize_a_seat"></div>

            <List :list="dishList" :getShopCartList="getShopCartList" />
        </div>
    </div>

    <!-- 购物车 -->
    <div class="shopping_cart">
        <img :src="cartStatus" alt="" @click="cartListRef.flag = true">
        <van-badge :content="cartList.length">
            <div class="child" />
        </van-badge>

        <span class="price">¥ {{ amount / 100 }}</span>
        <van-button round @click="goPay" :disabled="!cartList.length">去结算</van-button>
    </div>

    <CartList ref="cartListRef" :cartList="cartList" :flag="showBottom" :getShopCartList="getShopCartList" :getDishList="getDishList"  />
</template>

<script setup>
import { ref, computed, provide } from 'vue'
import { cateListApi } from '@/api/dishApi.js'
import { showFailToast } from 'vant';
import useStore from '../../stores';

import { getCartListApi } from '@/api/shopping.js'
import { useRouter } from 'vue-router'
import List from '@/components/List/List.vue';
import { dishListApi, setMealListApi } from '@/api/dishApi.js'
import CartList from './CartList/CartList.vue'

// 分类
let cate = ref({})

let cartListRef = ref(null)

let showBottom = ref(false)

// 当前选中菜单
let active = ref(0)

// 分类列表
let cateList = ref([])

// 购物车列表
let cartList = ref([])

let dishList = ref([])

const router = new useRouter()

// 将当前分类传递给Dialog组件
provide('cate', cate)

// 价格
const amount = computed(() => {

    return cartList.value.reduce(
        (acc, cur) => acc += cur.price * cur.number, 0
    )
})

const goPay = () => {

    let { shopping } = new useStore()

    shopping.shoppingList = cartList.value
    shopping.shoppingInfo = {
        amount: amount,
        number: cartList.value.length
    }

    router.push('/addOrders')
}

const cartStatus = computed(() => {
    if (cartList.value.length) {
        return 'src/assets/images/cart_active.png'
    } 

    return 'src/assets/images/cart.png'
})

// 获取购物车列表
const getShopCartList = async () => {

    const { data: res } = await getCartListApi()
    if (res.code !== 1) return showFailToast(res.msg)

    cartList.value = res.data
    
    // 为菜品列表赋值
    dishList.value.forEach(item => {
        cartList.value.forEach(c => {
            
            if (c.dishId === item.id || c.setmealId === item.id) {
                item.flag = true
                item.number = c.number
                item.shopId = c.id
                c.flag = true
            }
        })
    })
}

// 获取分类列表
const getCateList = async () => {
    const { data: res } = await cateListApi(0)
    if (res.code === 1) {
        cateList.value = res.data
    }
}

// 获取菜品列表
const getDishList = async (type, id) => {

    let res
    if (type === 1) {
        res = (await dishListApi(id)).data
    } else {
        res = (await setMealListApi(id)).data
    }

    if (res.code === 1) {
        dishList.value = res.data

        const userId = JSON.parse(sessionStorage.getItem('user'))['id']
        getShopCartList(userId)
    }
}

// 初始化首页
const __init__ = async () => {
    await getCateList()

    const cateInfo = cateList.value[0]
    cate.value = cateInfo
    new useStore().list.cateInfo = cateInfo

    await getDishList(cateInfo.type, cateInfo.id)
}

__init__()
</script>

<style lang='less' scoped>
.header {
    background: url(../../assets/images/mainBg.png);
    background-repeat: inherit;
    height: 152rem;

    .user {
        position: absolute;
        left: 5%;
        top: 41rem;
        width: 28rem;
        height: 28rem;
    }

    .title-box {
        width: 90%;
        height: 118rem;
        background: #ffffff;
        border-radius: 6rem;
        box-shadow: 0rem 2rem 5rem 0rem rgb(69 69 69 / 10%);
        position: absolute;
        left: 50%;
        top: 77rem;
        transform: translate(-50%, 0);
        box-sizing: border-box;
        padding: 14rem 0 0 8rem;
        z-index: 99;

        .title {
            display: flex;
            border-bottom: 1px dashed #ebebeb;

            .logo {
                width: 39rem;
                height: 39rem;
                background: #333333;
                border-radius: 6rem;
                margin-right: 10rem;
            }

            .desc {
                // width: 30rem;

                p {
                    width: 90rem;
                    height: 25rem;
                    font-size: 18rem;
                    font-family: PingFangSC, PingFangSC-Regular;
                    font-weight: 500;
                    text-align: left;
                    color: #20232a;
                    line-height: 25rem;

                }

                .send {
                    // opacity: 1;
                    font-size: 11rem;
                    font-family: PingFangSC, PingFangSC-Regular;
                    font-weight: 400;
                    text-align: left;
                    color: #333333;
                    margin-bottom: 10rem;

                    span {
                        width: 11rem;
                        margin-right: 10rem;
                    }

                    img {
                        width: 14rem;
                        opacity: 1;
                        vertical-align: sub;
                    }
                }
            }
        }

        .info {
            font-size: 12rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #9b9b9b;
            line-height: 17rem;
            margin-right: 18rem;
            padding-top: 9rem;
            border-top: 1rem dashed #ebebeb;
        }
    }
}

.container {
    .menu {
        float: left;
        position: sticky;
        top: 0;
        width: 25%;
        height: 100vh;
        background-color: #f6f6f6;
        overflow: scroll;

        .van-sidebar {
            position: sticky;
            top: 100px;
            margin-top: 45px;
            width: 100%;
            padding-bottom: 220px;

            .van-sidebar-item--select:before {
                content: none;
            }
        }
    }

    .list {
        float: right;
        margin-top: 45px;
        width: 75%;
        height: 80vh;
        overflow: scroll;
    }
}

.shopping_cart {
    display: flex;
    width: 345rem;
    height: 44rem;
    background: #000000;
    border-radius: 25rem;
    box-shadow: 0rem 3rem 5rem 0rem rgb(0 0 0 / 25%);
    margin: 0 auto;
    bottom: 24rem;
    position: fixed;
    left: 50%;
    transform: translate(-50%, 0);
    z-index: 3000;

    img {
        width: 60rem;
        height: 60rem;
        position: absolute;
        left: 11rem;
        bottom: 0;
        background-size: 60rem 60rem;
    }

    :deep(.van-badge) {
        left: 45px;
    }

    .van-button {
        width: 102rem;
        height: 36rem;
        border-radius: 18rem;
        font-size: 15rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        line-height: 36rem;
        position: absolute;
        right: 5rem;
        top: 4rem;
        border: none;
        background-color: var(--color);
    }

    .price {
        font-size: 18rem;
        font-family: DIN, DIN-Medium;
        font-weight: 500;
        text-align: left;
        color: #ffffff;
        position: absolute;
        left: 100rem;
        top: 10rem;
    }
}
</style>