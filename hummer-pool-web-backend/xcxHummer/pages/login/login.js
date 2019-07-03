const app = getApp();
Page({
  data: {
    username: '',
    password: '',
    disable: false,
    nobind: false
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    wx.showLoading({
      title: '',
    })
    const that = this;
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: app.globalData.url + 'wx/user/login',
          method: 'GET',
          data: {
            code: res.code
          },
          success: function (r) {
            wx.hideLoading();
            if (!r.data.data || (!r.data.data.openid && !r.data.data.sessionId)) {
              wx.showToast({
                title: '网络异常,请稍后再试',
                icon: 'none',
                duration: 3000
              })
            }
            app.globalData.openId = r.data.data.openid
            if (!!r.data.data.sessionId) {
              app.globalData.sessionId = r.data.data.sessionId
              app.globalData.username = r.data.data.username
              wx.switchTab({
                url: '../dashboard/dashboard',

              })

              // test  
              // wx.navigateTo({
              //   url: '../echart/echart',
              // })
            } else {
              that.setData({
                nobind: true
              })
            }
          },
          fail: function () {
            wx.showToast({
              title: '网络异常,请稍后再试',
              icon: 'none',
              duration: 3000
            })
          }
        })
      }
    })
  },
  inputUsr: function (e) {
    this.setData({
      username: e.detail.value
    })
  },
  inputPswd: function (e) {
    this.setData({
      password: e.detail.value
    })
    // wx.base64ToArrayBuffer(base64)
  },
  submit: function () {
    if (!app.globalData.openId) {
      wx.redirectTo({
        url: '../login/login',
      })
    }else{
      const that = this;
      if(this.data.disable == false) {
        wx.request({
          url: app.globalData.url + 'wx/user/bindingUser',
          method: 'POST',
          data: {
            openid: app.globalData.openId,
            username: this.data.username,
            password: this.data.password
          },
          success: function (r) {
            if (r.data.code == '0') {
              if (r.data.data.code == '0') {
                that.setData({
                  disable: true
                })
                wx.navigateTo({
                  url: '../login/login',
                })
              }else{
                wx.showToast({
                  title: r.data.data.code,
                  icon: 'none',
                  duration: 3000
                })
              }
            }else{
              wx.showToast({
                title: '网络异常,请稍后再试',
                icon: 'none',
                duration: 3000
              })
            }
          },
          fail: function () {
            wx.showToast({
              title: '网络异常,请稍后再试',
              icon: 'none',
              duration: 3000
            })
          }
        })
      }
    }
  },
    /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})