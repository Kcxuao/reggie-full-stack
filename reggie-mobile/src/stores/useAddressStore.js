import { defineStore } from 'pinia'

const useAddressStore = defineStore('address', {
    state: () => {
        return {
            addressInfo: {}
        }
    }
})

export default useAddressStore