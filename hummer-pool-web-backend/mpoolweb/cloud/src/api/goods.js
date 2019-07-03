import { stat } from "fs";

let baseUrl = '';
if (process.env.NODE_ENV == 'development') {
  baseUrl = "/api" 
} else {
  baseUrl = "" 
}
axios.defaults.baseURL = baseUrl;
export const all =()=>{
    return axios('/apis/product/activity/all')
}
//2、创建订单http://127.0.0.1:8888/apis/order/create/order
export const createOrder =(params)=>{
  return axios.post('/apis/order/create/order',{productId:params.productId,accept:params.accept,powerDay:params.powerDay,walleyAddress:params.walleyAddress})
}
//获得总算力和当前余额
export const billtotal =()=> {
  return axios.get('/apis/bill/total')
}
//获取订单列表
export const getOrderList = (params) =>{
  return axios(`/apis/order/list?type=${params.type}&state=${params.state}&size=${params.size}&current=${params.current}`)
}
//算力订单基本信息
export const getOrderDetail = (orderId) =>{
  return axios(`/apis/order/accept/normal?orderId=${orderId}`)
}
//算力订单产出信息
export const getOrderAccept = (orderId) =>{
  return axios(`/apis/order/accept/share?orderId=${orderId}`)
}
//算力订单付款信息
export const getOrderPay = (orderId) =>{
  return axios(`/apis/order/accept/paystate?orderId=${orderId}`)
}
//时间格式转换
export const timeFormat = (time) => {
  var str = new Date(time).getFullYear() + '-' +(new Date(time).getMonth() + 1) + '-' +   new Date(time).getDate() + ' ' + new Date(time).getHours() + ':' + new Date(time).getMinutes() + ':' + new Date(time).getSeconds()
  return str;
}
//状态code转名称
export const stateName = (code) => {
  switch(code) {
    case 0:
        return code = '已付款'
    case 1:
        return code = '等待付款'
    case 2:
        return code = '等待付款'
    case 3:
        return code = '到期'
    case 4:
        return code = '取消'
    default:
        return code = ''
  }
}
//转时间戳
export const dateToMs = (date) => {
  var result = new Date(date).getTime();
  return result;
}
//账单明细-每日产出
export const getOrderdayList = (params) =>{
  return axios(`/apis/bill/daily/day?day=${params.day}&size=${params.size}&current=${params.current}`)
}
//账单明细-每日产出(by时间戳)
export const getOrdertimeList = (params) =>{
  return axios(`/apis/bill/daily/time?begTime=${params.start}&endTime=${params.end}&current=${params.current}&size=${params.size}`)
}
//账单明细-每单产出
export const getOrdersList = (params) =>{
  return axios(`/apis/bill/get/order?orderId=${params.orderId}&current=${params.current}&size=${params.size}&state=${params.state}`)
}


//发送手机验证码-修改手机号
export const editNumCode =()=> {
  return axios.get('/apis/dashbaord/reset/phone/code')
}
//修改手机号
export const editNum = (params)=>{
  return axios.post('/apis/dashbaord/reset/phone',{phone:params.phone,phoneCode:params.check})
}

//发送手机验证码-修改邮箱
export const editEmailCode =()=> {
  return axios.get('/apis/dashbaord/reset/email/code')
}
//修改邮箱
export const editEmail = (mail)=>{
  return axios.post('/apis/dashbaord/reset/email',{email:mail.email,emailCode:mail.check})
}

//发送验证码-修改密码
export const sendPswCode =()=> {
  return axios.get('/apis/dashbaord/reset/password/code')
}

//邮箱修改密码
export const editPswEmail = (params)=>{
  return axios.post('/apis/dashbaord/reset/password/email',{email:params.password,emailCode:params.check})
}
//手机号修改密码
export const editPswPhone = (params)=>{
  return axios.post('/apis/dashbaord/reset/password/phone',{phone:params.password,phoneCode:params.check})
}

//发送验证码-修改收币地址 
export const sendAdrCode =()=> {
  return axios.get('/apis/dashbaord/reset/wallet/code')
}

//邮箱修改收币地址
export const editAdrEmail = (params)=>{
  return axios.post('/apis/dashbaord/reset/wallet/email',{email:params.address,emailCode:params.check})
}
//手机号修改收币地址
export const editAdrPhone = (params)=>{
  return axios.post('/apis/dashbaord/reset/wallet/phone',{phone:params.address,phoneCode:params.check})
}



//获取账户信息
export const getUserInfo =()=> {
  return axios.get('/apis/dashbaord/get/user')
}

//获取到期提醒信息
export const getExpireInfo =()=> {
  return axios.get('/apis/dashbaord/info/notify')
}

//修改到期提醒
export const editExpire =(params)=>{
  return axios.post('/dashbaord/notify/update',
      { puid:params.puid,
        phoneState:params.phoneState,
        emailState:params.emailState,
        before7:params.before7,
        before1:params.before1,
        expired:params.expired
      }
    )
}

