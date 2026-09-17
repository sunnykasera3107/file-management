import api from "../api"

export async function logoutService() {
    try{
        const response = await api.post("/api/v1/logout");

        return response.data;
    } catch (e) {
        console.error("Logout error" + e);
    }
}