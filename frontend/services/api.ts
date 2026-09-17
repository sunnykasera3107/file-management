import axios from "axios";
const api = axios.create({
    baseURL: "http://localhost:8083",
    withCredentials: true,
});


let csrfToken: string | null = null;

export function setCsrfToken(token: string) {
    csrfToken = token;
}

api.interceptors.request.use((config:any) => {
    if (csrfToken &&
        ["post", "put", "patch", "delete"].includes(
            config.method?.toLowerCase() ?? ""
        )        
    ) {
        config.headers["X-XSRF-TOKEN"] = csrfToken;
    }

    return config;
});

export default api;