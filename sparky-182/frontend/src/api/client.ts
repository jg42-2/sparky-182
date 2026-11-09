import axios from "axios";

export const api = axios.create({
    baseURL: "http://localhost:8080/api",
})

api.interceptors.request.use((config) =>{

    const token = localStorage.getItem("token");

    if (token){
        config.headers.Authorization = `Bearer ${token}`;
    }


    return config;
})

api.interceptors.response.use(
    (response) => response,

    (error) => {
        const status = error.response?.status;
        const url: string = error.config?.url ?? "";


        if (status === 401 && !url.startsWith("/auth")){
            localStorage.removeItem("token");
            localStorage.removeItem("email");
            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
    
)

