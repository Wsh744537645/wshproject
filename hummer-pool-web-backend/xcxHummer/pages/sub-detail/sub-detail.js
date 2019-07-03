
import * as echarts from '../../utils/ec-canvas/echarts';

function getOneOption(names, xdata, ydata) {
  return {
    title: {
      // text: '测试下面legend的红色区域不应被裁剪',
      left: 'center'
    },
    color: ['#c23531', '#2f4554'],
    legend: {
      data: ['1小时平均云计算数据', '1小时拒绝率'],
      top: 0,
      left: 'center',
    },
    grid: {
      containLabel: true,
      tooltip: {
        show: true,
        trigger: 'axis',
      },
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata,
      // show: false
    },
    // yAxis: {
    //   x: 'center',
    //   type: 'value',
    //   splitLine: {
    //     lineStyle: {
    //       type: 'dashed'
    //     }
    //   }
    //   // show: false
    // },
    yAxis: [{
      name: '云计算数据（T）',
      type: 'value',
      position: 'left',
      splitNumber: 5,
      // min: $scope.minValue,
      // max: $scope.maxValue,
      splitLine: {
        show: true,
        lineStyle: {
          color: '#c23531'
        }
      }
    }, {
      name: '拒绝率（%）',
      min: 0,
      max: 10,
      splitNumber: 5,
      splitLine: {
        show: true,
        lineStyle: {
          color: '#2f4554'
        }
      }
    }],
    series: ydata
  };
}

var chart1 = null;
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userName: '',
    share15m: '',
    share24h: '',
    today: '',
    totalPaid: '',
    yesterday: '',
    totalDue: '',
    workerActive: '',
    workerTotal: '',
    onlineRate: '',
    ecOne: {
      onInit: function (canvas, width, height) {
        chart1 = echarts.init(canvas, null, {
          width: width,
          height: height
        });
        canvas.setChart(chart1);
        // console.log('1'+chart1)
        return chart1;
      },
    }

  },
  // getData: function (names, xdata, ydata) {
  //   // console.log('2' + chart1)
  //   chart1.setOption(getOneOption(names, xdata, ydata));
  // },
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    const that = this;
    if (!app.globalData.sessionId) {
      wx.redirectTo({
        url: '../login/login',
      })
    } else {
      wx.request({
        url: app.globalData.url + '/wx/user/getWxSubRuntimeInfo?userId=6',
        method: 'GET',
        data: {
          userId: app.globalData.subUserId
        },
        header: {
          'content-type': 'application/json', // 默认值
          'token': app.globalData.sessionId
        },
        success: function (res) {
          wx.hideLoading();
          const rep = res.data.data
          if (res.data.code !== "-1") {
            that.setData({
              userName: rep.username,
              share15m: rep.share15m.toString().indexOf('.') != -1 ? rep.share15m.toFixed(3) : rep.share15m,
              share24h: rep.share24h.toString().indexOf('.') != -1 ? rep.share24h.toFixed(3) : rep.share24h,
              today: (rep.today / 100000000 || 0).toFixed(8),
              yesterday: (rep.yesterday / 100000000 || 0).toFixed(8),
              totalPaid: (rep.totalPaid / 100000000 || 0).toFixed(8),
              totalDue: (rep.totalDue / 100000000 || 0).toFixed(8),
              workerActive: rep.workerActive,
              workerTotal: rep.workerTotal,
              onlineRate: (rep.onlineRate * 100).toFixed(1),
            })
          }
        },
        fail:function() {
          wx.showToast({
            title: '网络异常,请稍后再试',
            icon: 'none',
            duration: 3000
          })
        }
      })
    }
    wx.request({
      url: app.globalData.url + 'user/dashbaord/getUser24H',
      method: 'GET',
      data: {},
      header: {
        'content-type': 'application/json', // 默认值
        'token': app.globalData.sessionId
      },
      success: function (res) {
        // const oneline = res.data.data;
        // const lineNames = []
        // const timeAry = []
        // const lineData = []
        // for (var o in oneline) {
        //   lineNames.push(o)
        //   let data = oneline[o]
        //   const date = [];
        //   const ary = [];
        //   data.forEach((d) => {
        //     date.push(d.date)
        //     ary.push(d.hashAcceptT)
        //   })
        //   const one = {
        //     name: o,
        //     type: 'line',
        //     smooth: true,
        //     data: ary
        //   }
        //   lineData.push(one)
        //   timeAry.push(date)
        // }
        // const timeData = []
        // timeAry[0].forEach((t) => {
        //   timeData.push(((parseInt(t.split(8)[1]) + 8 > 24) ? (parseInt(t.split(8)[1]) + 8 - 24) : parseInt(t.split(8)[1]) + 8) + ':00')
        // })
        // that.setData({
        //   xdata: timeData,
        //   ydata: lineData,
        //   lineNames: lineNames
        // })
      },
      fail: function () {
        wx.showToast({
          title: '网络异常,请稍后再试',
          icon: 'none'
        })
      }
    })
    // setTimeout(function () {
    //   // console.log(that.data.lineNames);
    //   // console.log(that.data.xdata);
    //   // console.log(that.data.ydata);
    //   that.getData(that.data.lineNames, that.data.xdata, [
    //     {
    //       name: '云计算数据',
    //       type: 'line',
    //       smooth: true,
    //       data: ['29', '30']
    //     },
    //     {
    //       name: '拒绝率',
    //       type: 'line',
    //       smooth: true,
    //       data: ['29', '30', '29', '30', '33']
    //     }
    //   ]);
    // }, 500)
  },

})