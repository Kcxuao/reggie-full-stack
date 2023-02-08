<template>
    <van-popup
        v-model:show="flag"
        round
        position="bottom"
        :style="{ height: '35%' }"
    >
        <div class="header">
            <div class="title">购物车</div>
            <div class="clear" @click="clean">清空</div>
        </div>

        <div class="content">
            <List :list="props.cartList" :getShopCartList="getShopCartList"/>
        </div>
    </van-popup>
</template>

<script setup>
import { ref, defineProps, defineExpose } from 'vue'
import List from '@/components/List/List.vue'
import { cleanApi } from '@/api/shopping.js'
import { showSuccessToast, showFailToast } from 'vant';
import useStore from '../../../stores';

let flag = ref(false)

const props = defineProps({
    cartList: {},
    flag: '',
    getShopCartList: undefined,
    getDishList: undefined
})

const clean = async () => {
    const {data: res} = await cleanApi()
    if (res.code !== 1) return showFailToast(res.msg)
    showSuccessToast(res.data)

    const cateInfo = new useStore().list.cateInfo
    props.getDishList(cateInfo.type, cateInfo.id)
    props.getShopCartList()

}

defineExpose({
    flag
})
</script>

<style lang='less' scoped>

.van-popup {

    .header {
        padding: 10rem;
        height: 28px;
        border-bottom: 1px solid rgb(241, 241, 241);
        position: sticky;
        z-index: 99;
        top: 0;
        background-color: rgb(255, 255, 255);

        .title {
            float: left;
            font-size: 20px;
        }

        .clear {
            float: right;
            margin-right: 10rem;
            line-height: 30rem;
        }
    }

    .content {
        height: 40px !important;
        padding-bottom: 200px;
    }
}
</style>