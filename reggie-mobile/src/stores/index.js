import useAddressStore from "./useAddressStore"
import useListStore from "./useListStore"
import useShopStore from "./useShopStore"
import useOrderStore from "./useOrderStore"

export default function useStore() {
    return {
        list: useListStore(),
        shopping: useShopStore(),
        address: useAddressStore(),
        order: useOrderStore()
    }
}