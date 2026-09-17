import api from "../api"

export async function loginService(form: {email: String, password: String}) {
    try{
        const response = await api.post("/api/v1/login", {
            username: form.email,
            password: form.password
        });

        return response.data;
    } catch (e) {
        console.error("Login error" + e);
    }
}