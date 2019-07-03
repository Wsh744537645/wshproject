export default new Vuex.Store({
  state: {
    targetId:'targetId',
    productId:'',
    userInfo:[]||JSON.parse(sessionStorage.getItem('targetId')),
    power180:JSON.parse(sessionStorage.getItem('180')),
    power90:JSON.parse(sessionStorage.getItem('90')),
    MyorderInfo:JSON.parse(sessionStorage.getItem('orderInfo'))
  },
  getters:{
    
  },
  mutations: {
    //记录的登录进来的用户信息
    userData(state,data){
      state.userInfo = data;
      var objVal = data
      sessionStorage.setItem('targetId',JSON.stringify(objVal))
    },
    //保存产品id
    goodsInfo(state,data){
      state.productId=data
    },
    //180天购买的算力周期
    buy180(state,data){
      state.power180=data
      sessionStorage.setItem('180',JSON.stringify(data))
    },
     //90天购买的算力周期
    buy90(state,data){
      state.power90=data
      sessionStorage.setItem('90',JSON.stringify(data))
    },
    //
    orderInfo(state,data){
      state.MyorderInfo=data
      sessionStorage.setItem('orderInfo',JSON.stringify(data))
    }
},
  actions: {
  
  }
})
