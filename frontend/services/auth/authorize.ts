import api from "../api";

export async function getAuthorized() {
    try{
        const response = await api.get("/api/v1/authorize");
        if (response == null) {
            return null;
        }
        return response.data;
    } catch (e) {
        console.error("Authorization error", e);
    }
}