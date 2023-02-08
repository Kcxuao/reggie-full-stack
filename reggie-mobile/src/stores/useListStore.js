import { defineStore } from 'pinia'

const useListStore = defineStore('list', {
    state: () => {
        return {
            cateInfo: {}
        }
    }
})

export default useListStore