const app = getApp();
Page({
  data: {
    username: '',
    password: '',
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    this.setData({
      username: app.globalData.username
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
  },
  submit: function () {
    wx.showLoading({
      title: '正在解绑，请稍后',
    })
    wx.request({
      url: app.globalData.url + 'wx/user/deleteBinding',
      method: 'POST',
      data: {
        openid: app.globalData.openId,
        username: this.data.username,
        password: this.data.password
      },
      header: {
        'token': app.globalData.sessionId
      },
      success: function (r) {
        wx.hideLoading()
        if (r.data.data.resultCode == 'ok'){
          wx.showToast({
            title: '解除绑定成功',
            duration: 1000
          })
          wx.navigateTo({
            url: '../login/login'
          })
          app.globalData.sessionId = ''
          app.globalData.openId = ''
        }else{
          wx.showToast({
            title: '您输入的账号密码不正确，请重新输入！',
            duration: 3000,
            icon: 'none'
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