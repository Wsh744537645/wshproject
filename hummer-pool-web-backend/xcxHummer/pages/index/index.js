const app = getApp();
Page({
  data: {
    // userInfo: {},
    // hasUserInfo: false,
    // canIUse: wx.canIUse('button.open-type.getUserInfo'),
    // bindText: ''
  },
  toBindUser: function () {
    wx.navigateTo({
      url: '../login/login',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    // wx.showLoading({
    //   title: '',
    // })

    // const that = this;
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //     wx.request({
    //       url: app.globalData.url + 'wx/user/login',
    //       method: 'GET',
    //       data: {
    //         code: res.code
    //       },
    //       success: function (r) {
    //         wx.hideLoading();
    //         if (!r.data.data || (!r.data.data.openid && !r.data.data.sessionId)) {
    //           wx.showToast({
    //             title: '网络异常,请稍后再试',
    //             icon: 'none',
    //             duration: 3000
    //           })
    //         }
    //         app.globalData.openId = r.data.data.openid
    //         if (!!r.data.data.sessionId) {
    //           app.globalData.sessionId = r.data.data.sessionId
    //           app.globalData.username = r.data.data.username
    //           wx.switchTab({
    //             url: '../dashboard/dashboard',
                
    //           })
    //         } else {
    //           that.setData({
    //             bindText: '去绑定用户账号>>'
    //           })
    //         }
    //       },
    //       fail: function() {
    //         wx.showToast({
    //           title: '网络异常,请稍后再试',
    //           icon: 'none',
    //           duration: 3000
    //         })
    //       }
    //     })
    //   }
    // })
    // if (app.globalData.userInfo) {
    //   that.setData({
    //     userInfo: app.globalData.userInfo,
    //     hasUserInfo: true
    //   })
    // } else if (that.data.canIUse) {
    //   // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //   // 所以此处加入 callback 以防止这种情况
    //   app.userInfoReadyCallback = res => {
    //     that.setData({
    //       userInfo: res.userInfo,
    //       hasUserInfo: true
    //     })
    //   }
    // } else {
    //   // 在没有 open-type=getUserInfo 版本的兼容处理
    //   wx.getUserInfo({
    //     success: res => {
    //       app.globalData.userInfo = res.userInfo
    //       that.setData({
    //         userInfo: res.userInfo,
    //         hasUserInfo: true
    //       })
    //     }
    //   })
    // }
  },
  // getUserInfo: function (e) {
  //   app.globalData.userInfo = e.detail.userInfo
  //   this.setData({
  //     userInfo: e.detail.userInfo,
  //     hasUserInfo: true
  //   })
  // },
})