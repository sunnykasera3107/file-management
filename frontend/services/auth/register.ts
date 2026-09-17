import User from "@/objects/User";
import api from "../api"

export async function registerService(form: User) {
    try{
        const response = await api.post("/api/v1/register", {
            fullname: form.fullname,
            email: form.email,
            username: form.email,
            password: form.password,
            phone: form.phone
        });
        return response.data;
    } catch (e) {
        console.error("Registration error" + e);
    }
}