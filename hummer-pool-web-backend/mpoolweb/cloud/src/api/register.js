let baseUrl = '';
if (process.env.NODE_ENV == 'development') {
  baseUrl = "/api"
} else {
  baseUrl = "http://39.104.152.238:8888"
}
axios.defaults.baseURL = baseUrl;
//http request 请求拦截器，有token值则配置上token值
axios.interceptors.request.use(
  config => {
    const token = JSON.parse(sessionStorage.getItem('targetId'))
    if (token) { // 每次发送请求之前判断是否存在token，如果存在，则统一在http请求的header都加上token，不用每次请求都手动添加
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });
// http response 拦截器 ,拦截401状态（token过期），重新登录
axios.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 返回 401 清除token信息并跳转到登录页面
          sessionStorage.removeItem('targetId')
          router.replace({
            path: 'home',
            // query: {redirect: router.currentRoute.fullPath}
          })
      }
    }
    return Promise.reject(error.response.data) // 返回接口返回的错误信息
  });
// 邮箱验证码
export const emailCode = (email) => {
  return axios(`/apis/user/sendEmail?mail=${email}`)
}
//手机注册验证码
export const phoneCode = (num) => {
  //http://39.104.152.238:8888/apis/user/reg/sendPhone?phoneNumber=13462051594

  return axios(`/apis/user/reg/sendPhone?phoneNumber=${num}`)
}
// 邮箱重置验证码
export const emailResetCode = (email) => {
  return axios(`/apis/user/resetPasswordCode?mail=${email}`)
}
//手机重置验证码
export const phoneResetCode = (num) => {
  return axios(`/apis/user/resetPassword/phoneCode?phoneNumber=${num}`)
}

//手机号重置密码
export const phoneReset = (params) => {
  return axios.post(`/apis/user/resetPassword/phone?share_phoneCode=${params.phoneCode }&phone=${params.phone}`, {
    password: params.password
  })
}

//邮箱重置密码
export const emailReset = (params) => {
  return axios.post(`/apis/user/resetPassword?share_emailCode=${params.emailCode }&email=${params.email}`, {
    password: params.password
  })
}
//手机号注册
export const sms = (params) => {
  return axios.post(`/apis/user/reg/sms?share_phoneCode=${params.share_phoneCode }&phone=${params.share_phone}`, {
    username: params.username,
    password: params.password
  })
}
// 邮箱注册
// http://39.104.152.238:8888/apis/user/reg?emailCode=123456&email=584689161%40qq.com
export const email = (params) => {
  return axios.post(`/apis/user/reg?emailCode=${params.emailCode}&email=${params.email}`, {
    username: params.username,
    password: params.password
  })
}
//检测用户名是否存在
// http://39.104.152.238:8888/apis/user/checkUsername?username=111
export const checkUsername = (name) => {
  return axios(`/apis/user/checkUsername?username=${name}`)
}
//检测手机号是否存在
// http://39.104.152.238:8888/apis/user/checkPhone?phone=13462051594
export const checkPhone = (params) => {
  return axios(`/apis/user/checkPhone?phone=${params}`)
}
//检测邮箱是否存在
// http://39.104.152.238:8888/apis/user/checkMail?mail=584689161%40qq.com
export const checkMail = (params) => {
  return axios(`/apis/user/checkMail?mail=${params}`)
}