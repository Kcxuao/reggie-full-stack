<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />菩提阁
        </div>
    </div>

    <div class="divContent">
        <div class="divAddress">
            <div @click="router.push('/address')">
                <div class="address">{{ address.detail }}</div>
                <div class="name">
                    <span>{{ address.consignee }}{{ address.sex === '1' ? '先生' : '女士' }}</span>
                    <span>{{ address.phone }}</span>
                </div>
                <van-icon name="arrow" />
            </div>
            <div class="divSplit"></div>
            <div class="divFinishTime">预计 {{ finishTime }} 送达</div>
        </div>
        <div class="order">
            <div class="title">订单明细</div>
            <div class="divSplit"></div>
            <div class="itemList">
                <div class="item" v-for="item in shoppingList" :key="item.id">
                    <img :src="`${app.backUrl}/files/${item.image}`" alt="">
                    <div class="desc">
                        <div class="name">{{ item.name }}</div>
                        <div class="numPrice">
                            <span class="num">x{{ item.number }}</span>
                            <div class="price">
                                <span class="spanMoney">￥</span>{{ item.price / 100 }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="note">
            <div class="title">备注</div>
            <van-field rows="2" autosize type="textarea" maxlength="50" placeholder="请输入您需要备注的信息" show-word-limit />
        </div>
    </div>

    <div class="shopping_cart">
        <img src="@/assets/images/cart_active.png">
        <van-badge :content="shoppingInfo.number">
            <div class="child" />
        </van-badge>

        <span class="price">¥ {{ shoppingInfo.amount / 100 }}</span>
        <van-button round @click="pay">去支付</van-button>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { app } from '@/config/config.js'
import { addressListApi } from '@/api/addressApi.js'
import useStore from '@/stores';
import { payApi } from '@/api/orderApi.js'
import { showFailToast, showSuccessToast } from 'vant';

let address = ref({})

let shoppingList = ref([])

let shoppingInfo = ref({})

const router = new useRouter()

const getAddressList = async () => {
    const { data: res } = await addressListApi('default')
    if (res.code !== 1) return showFailToast(res.msg)

    if (res.data.length === 0) return router.push('/addressEdit')
    address.value = res.data[0]
}

const finishTime = computed(() => {
    let now = new Date()
    let hour = now.getHours() + 1
    let minute = now.getMinutes()
    if (hour.toString().length < 2) {
        hour = '0' + hour
    }
    if (minute.toString().length < 2) {
        minute = '0' + minute
    }
    return hour + ':' + minute
})

const pay = async () => {
    const payInfo = {
        addressBookId: address.value.id,
        payMethod: 1,
        remark: ''
    }

    const {data: res} = await payApi(payInfo)
    if (res.code !== 1) return showFailToast(res.msg)

    showSuccessToast(res.data)
    router.replace('/success')
}

const __init__ = () => {
    getAddressList()

    const { shopping } = new useStore()
    shoppingList.value = shopping.shoppingList
    shoppingInfo.value = shopping.shoppingInfo
}

__init__()
</script>

<style lang='less' scoped>
.divContent .order .itemList .item .desc .numPrice .price .spanMoney {
    color: #e94e3c;
    font-size: 12rem;
}

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

.divContent {
    margin: 10rem 10rem 0 10rem;
    height: calc(100vh - 56rem - 110rem);
    overflow-y: auto;

    .divAddress {
        height: 120rem;
        opacity: 1;
        background: #ffffff;
        border-radius: 6rem;
        position: relative;
        padding: 11rem 10rem 0 16rem;

        .address {
            height: 25rem;
            opacity: 1;
            font-size: 18rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 500;
            text-align: left;
            color: #20232a;
            line-height: 25rem;
            margin-bottom: 4rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 300rem;
        }

        .name {
            height: 17rem;
            opacity: 1;
            font-size: 12rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #333333;
            line-height: 17rem;

            span:first-child {
                margin-right: 10rem;
            }
        }

        i {
            position: absolute;
            right: 14rem;
            top: 32rem;
        }

        .divSplit {
            width: 100%;
            height: 1px;
            opacity: 1;
            border: 0;
            background-color: #ebebeb;
            margin-top: 14rem;
        }

        .divFinishTime {
            height: 47rem;
            opacity: 1;
            font-size: 12rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #333333;
            line-height: 47rem;
            margin-left: 2rem;
        }
    }

    .order {
        background: #ffffff;
        border-radius: 6rem;
        margin-top: 10rem;
        margin-bottom: 10rem;
        padding: 3rem 10rem 7rem 16rem;

        .title {
            height: 56rem;
            line-height: 56rem;
            opacity: 1;
            font-size: 16rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 500;
            text-align: left;
            color: #333333;
            letter-spacing: 0;
        }

        .divSplit {
            height: 1px;
            opacity: 1;
            background-color: #efefef;
            border: 0;
        }

        .itemList {
            .item {
                display: flex;
                border-top: 2px solid #ebeef5;

                img {
                    padding-top: 20rem;
                    padding-bottom: 20rem;
                    width: 64rem;
                    height: 64rem;
                    width: 64rem;
                    height: 64rem;
                }


                .desc {
                    padding-top: 20rem;
                    padding-bottom: 20rem;
                    width: calc(100% - 64rem);

                    .name {
                        height: 22rem;
                        opacity: 1;
                        font-size: 16rem;
                        font-family: PingFangSC, PingFangSC-Semibold;
                        font-weight: 600;
                        text-align: left;
                        color: #20232a;
                        line-height: 22rem;
                        letter-spacing: 0;
                        margin-left: 10rem;
                        margin-bottom: 16rem;
                    }

                    .numPrice {
                        height: 22rem;
                        display: flex;
                        justify-content: space-between;

                        span {
                            margin-left: 12rem;
                            height: 20rem;
                            opacity: 1;
                            font-size: 14rem;
                            font-family: PingFangSC, PingFangSC-Regular;
                            font-weight: 400;
                            text-align: left;
                            color: #818693;
                            line-height: 20rem;
                            letter-spacing: 0;
                            display: inline-block;
                        }

                        .price {
                            font-size: 20rem;
                            font-family: DIN, DIN-Medium;
                            font-weight: 500;
                            text-align: left;
                            color: #e94e3c;
                        }
                    }
                }
            }

            .item:first-child {
                border: 0;
            }
        }
    }

    .note {
        height: 164rem;
        opacity: 1;
        background: #ffffff;
        border-radius: 6px;
        margin-top: 11rem;
        padding: 3rem 10rem 10rem 11rem;

        .title {
            height: 56rem;
            opacity: 1;
            font-size: 16rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 500;
            text-align: left;
            color: #333333;
            line-height: 56rem;
            letter-spacing: 0px;
            border-bottom: 2rem solid #efefef;
        }

        .van-cell {
            height: 103rem;
        }
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