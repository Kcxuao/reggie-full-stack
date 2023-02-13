<template>
    <div class="item" v-for="item in props.list" @click="dialog.showDishInfo(item, $event)">
        <img :src="`${app.backUrl}/files/${item.image}`" alt="">
        <div class="desc">
            <span class="title">{{ item.name }}</span>
            <span class="info">{{ item.description }}</span>
            <span class="num">月销0</span>
            <span class="price">¥ {{ item.price / 100 }}</span>
            <van-button @click="dialog.showFlavor(item)" v-if="showFlag(item) === 1">规格</van-button>
            <i @click="dialog.addCart(item)" v-if="showFlag(item) === 2" class="add">+</i>

            <van-stepper v-model="item.number" :min="0" :max="99" theme="round" @plus="updateNum(item, 1)"
                @minus="updateNum(item, 0, item)" v-if="showFlag(item) === 3" />
        </div>
    </div>

    <Dialog ref="dialog" :dish="dish" :getShopCartList="getShopCartList" />

</template>

<script setup>
import { defineProps, ref, computed } from 'vue'
import { app } from '@/config/config.js'
import { updateNumApi } from '@/api/shopping.js'
import Dialog from './Dialog/Dialog.vue'

// 当前点击的菜品
let dish = ref({})

// dialog组件dom
const dialog = ref(null)

const showFlag = computed(() => {
    return (item) => {

        if ((item.setmealDishes && item.flag || item.userId)) {
            return 3
        }

        if (item.setmealDishes) {
            return 2
        }

        if (!item.flag) {
            return 1
        }

        return 3
    }
})

// 父组件传递的值
const props = defineProps({
    getShopCartList: undefined,
    list: {}
})

// 更改菜品或套餐数量
const updateNum = async (item, flag) => {

    if (item.number === 1) {
        item.flag = false
    }

    let shopId = ''
    if (item.userId) {
        shopId = item.id
    } else {
        shopId = item.shopId
    }

    await updateNumApi(shopId, flag)

    const userId = JSON.parse(sessionStorage.getItem('user'))['id']
    props.getShopCartList(userId)
}


// 暴露属性和方法
</script>

<style lang='less' scoped>
.seize_a_seat {
    padding-bottom: 120rem;
}

.item {
    display: flex;
    margin: 20px;


    img {
        width: 86rem;
        height: 86rem;
        border-radius: 5rem;
    }

    .desc {
        display: flex;
        left: 10px;
        position: relative;
        flex-flow: column;
        width: 65%;

        span {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .title {
            height: 22rem;
            font-size: 16rem;
            font-weight: 600;
            text-align: left;
            color: #333333;
            line-height: 22rem;
            margin-bottom: 5rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .info,
        .num {
            height: 16rem;
            opacity: 1;
            font-size: 12rem;
            color: #666666;
            line-height: 16rem;
            margin-bottom: 4rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 144rem;
        }

        .price {
            font-size: 15rem;
            font-family: DIN, DIN-Medium;
            font-weight: 500;
            text-align: left;
            color: #e94e3c;
            line-height: 20rem;
            letter-spacing: 0rem;
        }

        .van-button,.add  {
            position: absolute;
            right: 12rem;
            bottom: 0;
            width: 70rem;
            height: 24rem;
            background: #ffc200;
            border-radius: 12rem;
            font-size: 12rem;
            font-family: PingFangSC, PingFangSC-Regular;
            color: #333333;
        }

        :deep(.van-stepper) {
            position: absolute;
            right: 12rem;
            bottom: 0;

            .van-stepper__minus,
            .van-stepper__plus {
                width: 20rem;
                height: 20rem;
            }

            .van-stepper__minus {
                border: 1px solid var(--color);
            }

            .van-stepper__plus {
                background-color: var(--color);
            }

            .van-stepper__input {
                width: 22rem;
            }
        }

        .add {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            text-align: center;
            line-height: 23px;
            font-size: 20px;
        }
    }


}

.item:nth-last-child(1){
    padding-bottom: 110px;
}
</style>