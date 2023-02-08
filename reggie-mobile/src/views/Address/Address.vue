<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />地址管理
        </div>
    </div>
    <div class="divContent">
        <div class="divItem" v-if="addressList.length" v-for="item in addressList" :key="item.id">
            <div class="divAddress">
                <van-tag plain type="primary">{{ item.label }}</van-tag>
                {{ item.detail }}
            </div>
            <div class="divUserPhone">
                <span>{{ item.consignee }}</span>
                <span>{{ item.sex === '1' ? '男' : '女' }}</span>
                <span>{{ item.phone }}</span>
            </div>
            <img src="@/assets/images/edit.png" @click="update(item)" />
            <div class="divSplit"></div>
            <div class="divDefault">
                <van-radio :name="item.id" v-model="checked">设为默认地址</van-radio>
            </div>
        </div>
    </div>
    <div class="divBottom" @click="router.push('/addressEdit')">+ 添加收货地址</div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { addressListApi, setDefaultApi } from '@/api/addressApi.js'
import { showSuccessToast, showFailToast } from 'vant';
import useStore from '@/stores/index.js';

let addressList = ref([])

let checked = ref()

const router = new useRouter()

const getAddressList = async () => {
    const {data: res} = await addressListApi('all')
    if (res.code !== 1) return showFailToast(res.msg)

    addressList.value = res.data
}

const update = (item) => {
    new useStore().address.addressInfo = item
    router.push('/addressEdit')
}

watch(checked, async (newVal, oldVal) => {
    if (!oldVal) return;

    const {data: res} = await setDefaultApi(newVal);
    if (res.code !== 1) return showFailToast(res.msg)

    showSuccessToast(res.data)
})

const __init__ = async () => {
    await getAddressList()

    addressList.value.forEach((i) => i.isDefault ? checked.value = i.id : '')
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

.divContent {
    height: calc(100vh - 157rem);
    overflow: auto;

    .divItem {
        height: 128rem;
        opacity: 1;
        background: #ffffff;
        border-radius: 6rem;
        margin-top: 10rem;
        margin-left: 10rem;
        margin-right: 9rem;
        padding-left: 12rem;
        position: relative;

        img {
            width: 16rem;
            height: 16rem;
            position: absolute;
            top: 40rem;
            right: 24rem;
        }

        :deep(.van-radio) {

            .van-radio__icon {
                font-size: 16px;
            }

            .van-radio__icon--checked .van-icon {
                background-color: var(--color);
                border-color: var(--color)
            }
        }

        

        .divAddress {
            font-size: 14rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #333333;
            line-height: 20rem;
            letter-spacing: 0;
            padding-top: 21rem;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 280rem;

            span {
                width: 34rem;
                height: 20rem;
                opacity: 1;
                font-size: 12rem;
                display: inline-block;
                text-align: center;
                margin-right: 4rem;
                margin-bottom: 10rem;
            }

            .van-tag {
                line-height: 20px;
                color: var(--color);
                border: 1px solid var(--color);
            }

            .spanCompany {
                background-color: #e1f1fe;
            }

            .spanHome {
                background: #fef8e7;
            }

            .spanSchool {
                background: #e7fef8;
            }
        }

        .divUserPhone {

            span {
                height: 20rem;
                opacity: 1;
                font-size: 14rem;
                font-family: PingFangSC, PingFangSC-Regular;
                font-weight: 400;
                text-align: left;
                color: #999999;
                line-height: 20rem;
                letter-spacing: 0;
                margin-right: 10rem;
            }

            span:first-child {
                margin-right: 2rem;
            }
        }

        .divSplit {
            height: 1px;
            opacity: 1;
            background: #efefef;
            border: 0;
            margin-top: 16rem;
            margin-bottom: 10rem;
            margin-right: 10rem;
        }

        .divDefault {
            height: 18rem;
            opacity: 1;
            font-size: 13rem;
            font-family: PingFangSC, PingFangSC-Regular;
            font-weight: 400;
            text-align: left;
            color: #333333;
            line-height: 18rem;
            letter-spacing: 0;

            img {
                height: 18rem;
                width: 18rem;
                margin-right: 5rem;
                vertical-align: bottom;
            }
        }

    }
}

.divBottom {
    height: 36rem;
    opacity: 1;
    background: #ffc200;
    border-radius: 18rem;
    opacity: 1;
    font-size: 15rem;
    font-family: PingFangSC, PingFangSC-Regular;
    font-weight: 500;
    text-align: center;
    color: #333333;
    line-height: 36rem;
    letter-spacing: 0;
    position: absolute;
    bottom: 23rem;
    left: 50%;
    transform: translate(-50%, 0);
    width: 334rem;
}
</style>