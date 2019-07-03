const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mTotal: '',
    hTotal: '',
    sizeArray: [10, 20],
    sizeIndex: 0,
    currentArray: [1],
    currentIndex: 0,
    dataList: [],
    array: [1,2,3,4],
  },
  bindSizeChange(e) {
    this.setData({
      sizeIndex: e.detail.value,
      currentIndex: 0
    })
    this.getData();
},
  bindCurrentChange(e) {
    this.setData({
      currentIndex: e.detail.value
    })
    this.getData();  
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
    this.getData();
  },
  getData: function () {
    wx.showLoading({
      title: '加载中',
    })
    const that = this;
    let list = [];
    let ary = [];
    const a = this.data.sizeArray[this.data.sizeIndex];
    const b = this.data.currentArray[this.data.currentIndex];
    if (!app.globalData.sessionId) {
      wx.redirectTo({
        url: '../login/login',
      })
    } else {
      wx.request({
        url: app.globalData.url + 'apis/user/dashbaord/getMasterRuntimeInfo',
        method: 'GET',
        data: {
          size: a,
          current: b,
        },
        header: {
          'token': app.globalData.sessionId
        },
        success: function (res) {
          wx.hideLoading();
          if (!!res.data.data) {
            const records = res.data.data.records
            for (var i = 0; i < records.length; i++) {
              const one = {
                backcolor: (i % 2 == 0) ? false: true,
                userId: records[i].userId,
                userName: records[i].username,
                share15m: records[i].share15m.toString().indexOf('.') != -1 ? records[i].share15m.toFixed(3) : records[i].share15m,
                share24h: records[i].share24h.toString().indexOf('.') != -1 ? records[i].share24h.toFixed(3) : records[i].share24h,
              }
              list.push(one);
            }
            const pages = res.data.data.pages;
            for (var i = 1; i <= pages; i++) {
              ary.push(i);
            }
            let mshare = []
            let hshare = []
            let mresult = 0
            let hresult = 0
            list.forEach((item) => {
              mshare.push(parseInt(item.share15m))
              hshare.push(parseInt(item.share24h))
            })
            for (var i = 0; i < mshare.length; i++) {
              mresult += mshare[i];
            }
            for (var i = 0; i < hshare.length; i++) {
              hresult += hshare[i]
            }
            that.setData({
              dataList: list,
              currentArray: ary,
              mTotal: mresult.toString().indexOf('.') != -1 ? mresult.toFixed(3) : mresult,
              hTotal: hresult.toString().indexOf('.') != -1 ? hresult.toFixed(3) : hresult,
              totalNum: res.data.data.total
            })
          }
        },
        fail: function () {
          wx.showToast({
            title: '网络异常,请稍后再试',
            icon: 'none'
          })
        }
      })
    }
  },
  goDetail: function(event) {
    app.globalData.subUserId = event.currentTarget.dataset.id
    wx.navigateTo({
      url: '../sub-detail/sub-detail',
    })
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