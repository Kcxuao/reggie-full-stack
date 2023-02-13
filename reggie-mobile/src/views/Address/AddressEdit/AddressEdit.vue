<template>
    <div class="divHead">
        <div class="divTitle">
            <van-icon name="arrow-left" @click="router.go(-1)" />编辑收货地址
        </div>
    </div>
    <div class="divContent">
        <div class="divItem">
            <van-cell-group inset>
                <van-field v-model="addressInfo.consignee" label="联系人" placeholder="请输入联系人的姓名"
                    :rules="[{ required: true, message: '请填写用户名' }]" />

                <van-field name="radio" label="性别">
                    <template #input>
                        <van-radio-group v-model="addressInfo.sex" direction="horizontal">
                            <van-radio name="1">男</van-radio>
                            <van-radio name="0">女</van-radio>
                        </van-radio-group>
                    </template>
                </van-field>

                <van-field v-model="addressInfo.phone" label="手机号" placeholder="请输入联系人的手机号码"
                    :rules="[{ required: true, message: '请填写手机号码' }]" />

                <van-field v-model="addressInfo.detail" label="收货地址" placeholder="请输入收货地址"
                    :rules="[{ required: true, message: '请填写收货地址' }]" />

                <van-field label="标签">
                    <template #input>
                        <van-tag size="medium" plain v-for="tag in tags" @click="addressInfo.label = tag" :class="{ 'active': addressInfo.label === tag }">{{ tag }}</van-tag>
                    </template>
                </van-field>
            </van-cell-group>

            <van-button round block color="#ffc200" @click="save">提交</van-button>
            <van-button round block @click="remove">删除该地址</van-button>
        </div>
    </div>
</template>

<script setup>
import { reactive } from 'vue';
import { saveOrUpdateApi,removeApi } from '@/api/addressApi.js'
import { showSuccessToast, showFailToast } from 'vant';
import { useRouter } from 'vue-router';
import useStore from '@/stores/index.js';


const router = new useRouter()

let addressInfo = reactive({
    consignee: '',
    sex: '',
    phone: '',
    detail: '',
    label: ''
})

const tags = ['无', '公司', '家', '学校']


const save = async () => {
    const {data: res} = await saveOrUpdateApi(addressInfo)
    if (res.code !== 1) return showFailToast(res.msg)

    showSuccessToast(res.data)
    router.push('/address')
}

const remove = async () => {
    const {data: res} = await removeApi(addressInfo)
    if (res.code !== 1) return showFailToast(res.msg)

    showSuccessToast(res.data)
    router.push('/address')
}

const __init__ = () => {
    const addressData = new useStore().address.addressInfo
    if (!addressData) return;

    addressInfo = addressData
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
    height: 100%;
    margin-top: 20rem;
    padding-left: 9rem;
    padding-right: 9rem;

    .divItem {
        height: 200px;

        :deep(.van-radio) {

            .van-radio__icon {
                font-size: 16px;
            }

            .van-radio__icon--checked .van-icon {
                background-color: var(--color);
                border-color: var(--color)
            }
        }

        .van-cell-group--inset {
            margin: 0;
        }

        .van-tag {
            margin: 0 15rem 0 0;
            padding: 3rem 10rem;
        }

        .active {
            background-color: var(--color);
        }

        .van-button {
            margin-top: 25rem;
        }
    }

    .divSave {
        height: 36rem;
        opacity: 1;
        background: #ffc200;
        border-radius: 18rem;
        font-size: 15rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 36rem;
        margin-top: 20rem;
    }

    .divDelete {
        height: 36rem;
        opacity: 1;
        background: #f6f6f6;
        border-radius: 18rem;
        font-size: 15rem;
        font-family: PingFangSC, PingFangSC-Regular;
        font-weight: 500;
        text-align: center;
        color: #333333;
        line-height: 36rem;
        margin-top: 20rem;
    }
}
</style>