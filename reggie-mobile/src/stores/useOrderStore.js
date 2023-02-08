import { defineStore } from 'pinia'

const useOrderStore = defineStore('orderStore', {
    state: () => {
        return {
            orderList: []
        }
    },

    persist: {
        storage: window.sessionStorage,
        paths: ['orderList']
    }
})

export default useOrderStore