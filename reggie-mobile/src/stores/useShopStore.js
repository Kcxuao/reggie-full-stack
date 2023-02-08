import { defineStore } from 'pinia'

const useShopStore = defineStore('shopping', {
    state: () => {
        return {
            shoppingList: [],
            shoppingInfo: {}
        }
    },

    persist: {
        storage: window.sessionStorage,
        paths: ['shoppingList', 'shoppingInfo']
    }
})

export default useShopStore