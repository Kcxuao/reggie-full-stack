<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />菩提阁
        </div>
    </div>

    <div class="divContent">
        <img src="@/assets/images/success.png" />
        <div class="divSuccess">下单成功</div>
        <div class="divDesc">预计 {{ finishTime }} 到达</div>
        <div class="divDesc1">后厨正在加紧制作中，请耐心等待~</div>
        <div class="btnView" @click="router.replace('/orders')">查看订单</div>
    </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = new useRouter()

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

.divContent {
    height: calc(100vh - 88rem);
    width: 100%;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    text-align: center;
    align-items: center;

    img {
        margin-top: 148rem;
        margin-bottom: 19rem;
        width: 90rem;
        height: 86rem;
    }

    .divSuccess {
        height: 33rem;
        opacity: 1;
        font-size: 24rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 33rem;
        margin-top: 19rem;
        margin-bottom: 10rem;
    }

    .divDesc,
    .divDesc1 {
        height: 22rem;
        opacity: 1;
        font-size: 16rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 400;
        text-align: center;
        color: #666666;
        line-height: 22rem;
    }

    .divDesc1 {
        margin-top: 7rem;
        margin-bottom: 20rem;
    }

    .btnView {
        width: 124rem;
        height: 36rem;
        opacity: 1;
        background: #ffc200;
        border-radius: 18px;
        opacity: 1;
        font-size: 15rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 21rem;
        letter-spacing: 0;
        line-height: 36rem;
    }
}
</style>