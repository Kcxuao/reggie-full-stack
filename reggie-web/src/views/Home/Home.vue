<template>
    <el-container>
        <el-aside>
            <div class="logo">
                <img src="@/assets/images/login/login-logo.png" alt="" />
            </div>

            <div class="menu">
                <div :class="['menu-item', item.id === activeId ? 'active' : '']" v-for="item in menuList"
                    @click="activeHandler(item.id, item.url)">
                    <i :class="item.icon"></i>
                    <span>{{ item.name }}</span>
                </div>
            </div>

        </el-aside>
        <el-container>
            <el-header>
                <span class="title">员工管理</span>
                <div class="logout">
                    <span>{{ identity }}</span>
                    <i class="icon iconfont icon-Logout" @click="logout" />
                </div>

            </el-header>
            <el-main>
                <el-card>
                    <router-view></router-view>
                </el-card>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { logoutApi } from '@/api/employeeApi.js'
import { useRouter } from 'vue-router';

let activeId = ref(1)

const router = new useRouter()

const identity = localStorage.getItem('identity')

const menuList = reactive([
    {
        id: '1',
        name: '员工管理',
        url: '/home/employee',
        icon: 'iconfont icon-icmember'
    },
    {
        id: '2',
        name: '分类管理',
        url: '/home/category',
        icon: 'iconfont icon-dingdan'
    },
    {
        id: '3',
        name: '菜品管理',
        url: '/home/dish',
        icon: 'iconfont icon-caipin-'
    },
    {
        id: '4',
        name: '套餐管理',
        url: '/home/setmeal',
        icon: 'iconfont icon-Category'
    },
    {
        id: '5',
        name: '订单明细',
        url: '/home/orders',
        icon: 'iconfont icon-dingdan'
    }
])

const activeHandler = (val, path) => {
    activeId.value = val
    router.push(path)
}

const logout = async () => {
    const { data: res } = await logoutApi()
    localStorage.removeItem('token')

    if (res.code !== 1) {
        return ElMessage.error(res.msg)
    }

    ElMessage.success(res.data)
    router.replace('/login')

}
</script>

<style lang='less' scoped>
.el-container {
    height: 100%;
    background-color: rgb(240, 240, 240);

    .el-aside {
        width: 190px;
        height: 100%;
        background-color: #343744;

        .logo {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 100%;
            height: 148.5px;

            img {
                width: 117px;
                height: 32px;
            }
        }

        .menu {
            background-color: #343744;

            .menu-item {
                display: flex;
                justify-content: center;
                width: 150px;
                height: 40px;
                margin: 20px 0;
                cursor: pointer;

                span {
                    line-height: 40px;
                    font-size: 14px;
                    color: #BFCBD9;
                }

                i {
                    padding: 10px;
                    font-size: 20px;
                    color: #BFCBD9;
                }
            }

            .active {
                // color: #333;
                border-radius: 0 21px 21px 0 !important;
                background-color: #ffc200;

                i,
                span {
                    color: #333333;
                    font-weight: 700;
                }
            }
        }

    }

    .el-header {
        background-color: #fff;
        height: 64px;
        // box-shadow: 0 0 2px 0;

        .title {
            color: #333333;
            line-height: 64px;
            font-weight: 700;
            font-size: 16px;
        }

        .logout {
            float: right;

            span {
                position: relative;
                top: -2px;
                right: 8px;
                font-weight: 400;
                font-size: 14px;
                
            }

            i {
                color: #8d8d8d;
                cursor: pointer;
                font-size: 20px;
                line-height: 64px;
                margin-right: 16px;

            }
        }




    }

    .el-card {
        min-height: 500px;
        border-radius: 10px;
    }
}
</style>