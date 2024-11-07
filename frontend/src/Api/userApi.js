import baseApi from "./base";

const login = (data) => baseApi.baseApi({
    method: 'POST',
    url : `http://localhost:8001/log-in`,
    data: data
})

const signup = (data) => baseApi.baseApi({
    method: 'POST',
    url : `http://localhost:8001/users/processregister`,
    data: data
})

const logout = (data) => baseApi.baseApi({
    method: 'POST',
    url : `http://localhost:8001/log-out`,
    data: data
})

const getMyInfo = () => baseApi.baseApi({
    method: 'GET',
    url : `http://localhost:8001/users/getMyInfo`,
    data: ""
})


export default {
    login,
    signup,
    logout,
    getMyInfo
}