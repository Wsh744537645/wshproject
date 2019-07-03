let baseUrl = '';
if (process.env.NODE_ENV == 'development') {
  baseUrl = "/api" 
} else {
  baseUrl = "http://39.104.152.238:8888" 
}
//判断登录方式
axios.defaults.baseURL = baseUrl;
export const getUserLoginModel =()=>{
    return axios('/apis/user/getUserLoginModel')
}
// http://39.104.152.238:8888/apis/user/login?captchaCode=123
export const login=(parmas)=>{
  return axios.post('/apis/user/login',{username:parmas.username,password:parmas.password})
}
// 注销退出
export const logout=()=>{
  return axios.get('/apis/user/logout')
}