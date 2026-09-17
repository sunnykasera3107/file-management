import api, { setCsrfToken } from "../api";

export async function generateCSRF() {
    try{
        const response = await api.get("/api/v1/csrf");
        setCsrfToken(response.data.token);
        return response.data;
    } catch (e) {
        console.error("CSRF initialisation error", e);
    }
}