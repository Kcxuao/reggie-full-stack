<template>
    <!-- 详情 -->
    <van-dialog :show="show" :show-confirm-button="false" class="dialog" @click-overlay="show = false">
        <div ref="infoRef">
            <div class="container">
                <div class="preview">
                    <img :src="`${app.backUrl}/files/${dish.value.image}`">
                </div>

                <div class="content">
                    <p class="title">{{ dish.value.name }}</p>
                    <p class="desc">{{ dish.value.description }}</p>
                </div>
            </div>

            <div class="footer">
                <span class="price">¥{{ dish.value.price / 100 }}</span>
                <van-button @click="showFlavor(dish.value)" v-if="!dish.value.setmealDishes">选择规格</van-button>
                <i @click="addCart(dish.value)" class="add" v-else>+</i>
            </div>
        </div>
    </van-dialog>

    <!-- 规格 -->
    <van-dialog :show="specsVisible" :show-confirm-button="false" class="flavor_dialog" @click-overlay="specsVisible = false">
        <div ref="specsRef">
            <p class="title">{{ dish.value.name }}</p>

            <div class="content">
                <div class="flavor" v-for="flavor, i in dish.value.flavors">
                    <p class="flavor_title">{{ flavor.name }}</p>
                    <span v-for="name in toFlavor(flavor.value)" @click="addFlavor(name, i, $event)" :class="`a${i}`">{{
                        name
                    }}</span>
                </div>
            </div>


            <div class="footer">
                <span class="price">¥{{ dish.value.price / 100 }}</span>
                <van-button class="" @click="addCart(dish.value)">添加到购物车</van-button>
            </div>
        </div>
    </van-dialog>
</template>

<script setup>
import { defineProps, ref, reactive, defineExpose, computed, inject } from 'vue'
import { app } from '@/config/config.js'
import { saveApi } from '@/api/shopping.js'
import { showSuccessToast, showFailToast } from 'vant';


// 显示详情
let show = ref(false)

// 显示关闭按钮
let closeVisible = ref(false)

// 显示口味选择
let specsVisible = ref(false)

// 口味列表
let flavorList = reactive([])

// 分类
let cate = inject('cate')

// 详情dom
const infoRef = ref(null)

// 口味dom
const specsRef = ref(null)


const props = defineProps({
    dish: {},
    getShopCartList: undefined
})

// 展示口味选择弹窗
const showFlavor = (item) => {

    if (item.setmealDishes) {
        return;
    }

    props.dish.value = item;
    specsVisible.value = true
    closeVisible.value = true

}

// 展示菜品详情弹窗
const showDishInfo = (item, e) => {

    if (item.userId) {
        return;
    }

    if (e.srcElement.nodeName === 'BUTTON' || e.srcElement.nodeName === 'I') {
        return;
    }

    show.value = true
    props.dish.value = item;

    closeVisible.value = true
}

// 添加口味
const addFlavor = (name, index, e) => {

    flavorList[index] = name

    const tabs = document.querySelectorAll(`.a${index}`)
    tabs.forEach((tab) => {
        tab.style.backgroundColor = ''
    })

    e.srcElement.style.backgroundColor = '#ffc200'
}

// 将口味的值转为数组
const toFlavor = computed(() => {
    return (item) => {
        return JSON.parse(item)
    }
})

// 添加到购物车
const addCart = async (item) => {
    
    // 购物车信息
    const cartInfo = {
        name: item.name,
        userId: JSON.parse(sessionStorage.getItem('user')).id,
        dishFlavor: flavorList.toString(),
        number: 1,
        price: item.price,
        image: item.image
    }

    if (cate.value.type === 1) {
        // 菜品
        cartInfo.dishId = item.id
    } else {
        // 套餐
        cartInfo.setmealId = item.id
    }

    const { data: res } = await saveApi(cartInfo);
    if (res.code !== 1) return showFailToast("添加失败");

    showSuccessToast("添加成功")

    const userId = JSON.parse(sessionStorage.getItem('user'))['id']
    props.getShopCartList(userId)
}

defineExpose({
    showFlavor,
    showDishInfo,
    addCart
})
</script>

<style lang='less' scoped>
.dialog {

    width: 85%;
    padding-bottom: 10px;
    box-sizing: content-box;

    .container {
        width: 100%;
        max-height: 380rem;
        overflow: scroll;
        padding-bottom: 20px;
    }

    .preview {
        width: 85%;
        height: 50%;
        margin: 20rem auto 0;

        img {
            width: 100%;
            height: 90%;
            border-radius: 10px;
        }
    }

    .content {

        width: 85%;
        margin: auto;

        .title {
            font-size: 20rem;
            font-weight: 600;
            line-height: 28rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            text-align: center;
        }

        .desc {
            margin-top: 20rem;
            font-size: 14rem;
            font-weight: 400;
            line-height: 24rem;
        }
    }

    .footer {
        width: 85%;
        margin: 0 auto;
        margin-top: 10px;
        padding-bottom: 30px;
        position: relative;
        bottom: 10px;

        .price {
            float: left;
            font-size: 20rem;
            font-family: DIN, DIN-Medium;
            font-weight: 500;
            color: #e94e3c;
            line-height: 36rem;

        }

        .van-button, .add {
            float: right;
            height: 30rem;
            background: #ffc200;
            border-radius: 12rem;
            font-size: 12rem;
            color: #3c3c3c;
        }

        :deep(.add) {
            width: 25rem;
            height: 25rem;
            margin-top: 4rem;
            border-radius: 50%;
            text-align: center;
            line-height: 23px;
            font-size: 20rem;
        }
    }
}

.flavor_dialog {

    .title {
        margin-top: 26rem;
        margin-bottom: 14rem;
        font-size: 18rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        color: #333333;
        text-align: center;
    }

    .flavor {
        width: 90%;
        margin: 0 auto;

        .flavor_title {
            font-size: 14rem;
            color: #666666;
            margin: 10rem 0 10rem 5rem;
        }

        span {
            display: inline-block;
            height: 30rem;
            opacity: 1;
            background: #ffffff;
            border: 1rem solid #ffc200;
            border-radius: 7rem;
            line-height: 30rem;
            padding-left: 13rem;
            padding-right: 13rem;
            margin: 0 5rem 10rem 5rem;
            font-size: 14rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: center;
            color: #333333;
        }

        .active {
            background-color: var(--color);
        }
    }

    .footer {
        position: relative;
        top: -10px;
        width: 85%;
        margin: 20px auto;

        .price {
            float: left;
            font-size: 20rem;
            font-family: DIN, DIN-Medium;
            font-weight: 500;
            color: #e94e3c;
            line-height: 36rem;

        }

        .van-button {
            float: right;
            height: 30rem;
            background: #ffc200;
            border-radius: 12rem;
            font-size: 12rem;
            color: #333333;
        }
    }


}

.close {
    position: absolute;
    left: 50%;
    width: 44rem;
    transform: translate(-50%);
    z-index: 9999;
}
</style>