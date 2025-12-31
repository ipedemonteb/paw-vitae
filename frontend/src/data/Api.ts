
import axios from "axios";

const BASE_URL = "http://localhost:8080/";
//TODO: change in production

export const api = axios.create({
    baseURL: BASE_URL,
    timeout: 5000,
});

//TODO: ADD GLOBAL INTERCEPTORS?