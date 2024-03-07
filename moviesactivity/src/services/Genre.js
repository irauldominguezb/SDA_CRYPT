import api from "@/config/http-client.gateway"


const getGeneres = async () => {
    try {
        const response = await api.doGet("gender/")
        return response
    } catch (error) {
        throw new Error(error)
    }
}


export default {getGeneres}