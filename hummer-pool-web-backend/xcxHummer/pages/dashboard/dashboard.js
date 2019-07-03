import * as echarts from '../../utils/ec-canvas/echarts';

function getOneOption(names,xdata, ydata) {
  return {
    title: {
      left: 'center'
    },
    color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570'],
    legend: {
      data: names,
      center: 'center',
    },
    grid: {
      containLabel: true,
      tooltip: {
          show: true,
          trigger: 'axis',
          // axisPointer :{
          //   show: true,
          //   axis: 'auto'
          // }
        },
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata,
      // show: false
    },
    yAxis: {
      name: '云计算数据',
      type: 'value',
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      },
      boundaryGap: false
      // show: false
    },
    grid: [{
      top: '30%',
    }],
    series: ydata
  };
}

function get24mOption(names, xdata, ydata) {
  return {
    title: {
      // text: '测试下面legend的红色区域不应被裁剪',
      left: 'center'
    },
    color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570'],
    legend: {
      data: names,
      center: 'center',
    },
    grid: {
      containLabel: true,
      tooltip: {
        show: true,
        trigger: 'axis',
        // axisPointer :{
        //   show: true,
        //   axis: 'auto'
        // }
      },
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata,
      // show: false
    },
    yAxis: {
      name: '台',
      type: 'value',
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      },
      boundaryGap: false
      // show: false
    },
    grid: [{
      top: '30%',
    }],
    series: ydata
  };
}
function get30dOption(names, xdata, ydata) {
  return {
    title: {
      // text: '测试下面legend的红色区域不应被裁剪',
      left: 'center'
    },
    color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570'],
    legend: {
      data: names,
      center: 'center',
    },
    grid: {
      containLabel: true,
      tooltip: {
        show: true,
        trigger: 'axis',
        // axisPointer :{
        //   show: true,
        //   axis: 'auto'
        // }
      },
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata,
      // show: false
    },
    yAxis: {
      name: '云计算数据',
      type: 'value',
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      },
      boundaryGap: false
      // show: false
    },
    grid: [{
      top: '30%',
    }],
    series: ydata
  };
}
var chart1 = null;
var chart24m = null;
var chart30d = null;
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
    totalDue: '' , 
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
    },
    ecMiner: {
      onInit: function (canvas, width, height) {
        chart24m = echarts.init(canvas, null, {
          width: width,
          height: height
        });
        canvas.setChart(chart24m);
        return chart24m;
      },
    },
    ec30d: {
      onInit: function (canvas, width, height) {
        chart30d = echarts.init(canvas, null, {
          width: width,
          height: height
        });
        canvas.setChart(chart30d);
        return chart30d;
      },
    }
  },
  getData: function (names,xdata, ydata) {
    // console.log('2' + chart1)
    chart1.setOption(getOneOption(names, xdata, ydata));
  },
  get24mData: function (names, xdata, ydata) {
    chart24m.setOption(get24mOption(names, xdata, ydata));
  },
  get30dData: function (names, xdata, ydata) {
    chart30d.setOption(get30dOption(names, xdata, ydata));
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    wx.showLoading({
      title: '加载中',
    })
    const that = this;
    if (!app.globalData.sessionId) {
      wx.redirectTo({
        url: '../login/login',
      })
    }else{
      wx.request({
        url: app.globalData.url + 'apis/user/dashbaord/getMasterRuntimeInfo/converge',
        method: 'GET',
        data: {},
        header: {
          'content-type': 'application/json', // 默认值
          'token': app.globalData.sessionId
        },
        success: function (res) {
          wx.hideLoading();
          const rep = res.data.data
          if (res.data.code !== "-1") {
            that.setData({
              userName: app.globalData.username,
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
        fail: function() {
          wx.showToast({
            title: '网络异常,请稍后再试',
            icon: 'none'
          })
        }
      })
    }
    //24h chartData
    wx.request({
      url: app.globalData.url + 'apis/user/dashbaord/getMasterShareChartInfo/24',
      method: 'GET',
      data: {},
      header: {
        'content-type': 'application/json', // 默认值
        'token': app.globalData.sessionId
      },
      success: function (res) {
        const oneline = res.data.data;
        const lineNames = []
        const timeAry = []
        const lineData = []
        for (var o in oneline) {
          lineNames.push(o)
          let data = oneline[o]
          const date = [];
          const ary = [];
          data.forEach((d) => {
            date.push(d.date)
            ary.push(d.hashAcceptT)
          })
          lineData.push({
            name: o,
            type: 'line',
            smooth: true,
            data: ary,
          })
          timeAry.push(date)
        }
        const timeData = []
        timeAry[0].forEach((t) => {
          const tim = parseInt(t.substring(8,10)[1]) + 8
          timeData.push(((tim > 24) ? (tim - 24) : tim) + ':00')
        })
        that.setData({
          xdata: timeData.reverse(),
          ydata: lineData.reverse(),
          lineNames: lineNames
        })
      },
      fail: function () {
        wx.showToast({
          title: '网络异常,请稍后再试',
          icon: 'none'
        })
      }
    })

    //24hMiner chartData
    wx.request({
      url: app.globalData.url + 'apis/user/dashbaord/getMasterWorkerInfo/24',
      method: 'GET',
      data: {},
      header: {
        'content-type': 'application/json', // 默认值
        'token': app.globalData.sessionId
      },
      success: function (res) {
        const oneline = res.data.data;
        const lineNames = []
        const timeAry = []
        const lineData = []
        for (var o in oneline) {
          lineNames.push(o)
          let data = oneline[o]
          const date = [];
          const ary = [];
          data.forEach((d) => {
            date.push(d.date)
            ary.push(d.onLine)
          })
          lineData.push({
            name: o,
            type: 'line',
            smooth: true,
            data: ary,
          })
          timeAry.push(date)
        }
        const timeData = []
        timeAry[0].forEach((t) => {
          const a = t.substr(4, 4)
          const tim = a.split("")[0] + a.split("")[1] + '-' + a.split("")[2] + a.split("")[3]
          timeData.push(tim)
        })
        that.setData({
          xdata24m: timeData,
          ydata24m: lineData,
          lineNames24m: lineNames
        })
      },
      fail: function () {
        wx.showToast({
          title: '网络异常,请稍后再试',
          icon: 'none'
        })
      }
    })
    //30d chartData
    wx.request({
      url: app.globalData.url + 'apis/user/dashbaord/getMasterShareChartInfo/30',
      method: 'GET',
      data: {},
      header: {
        'content-type': 'application/json', // 默认值
        'token': app.globalData.sessionId
      },
      success: function (res) {
        const oneline = res.data.data;
        const lineNames = []
        const timeAry = []
        const lineData = []
        for (var o in oneline) {
          lineNames.push(o)
          let data = oneline[o]
          const date = [];
          const ary = [];
          data.forEach((d) => {
            date.push(d.date)
            ary.push(d.hashAcceptT)
          })
          lineData.push({
            name: o,
            type: 'line',
            smooth: true,
            data: ary,
          })
          timeAry.push(date)
        }
        const timeData = []
        timeAry[0].forEach((t) => {
          const a = t.substr(4, 4)
          const tim = a.split("")[0] + a.split("")[1] + '-' + a.split("")[2] + a.split("")[3]
          timeData.push(tim)
        })
        that.setData({
          xdata30: timeData,
          ydata30: lineData,
          lineNames30: lineNames
        })
      },
      fail: function () {
        wx.showToast({
          title: '网络异常,请稍后再试',
          icon: 'none'
        })
      }
    })
    setTimeout(function () {
      // console.log(that.data.xdata);
      // console.log(that.data.ydata);
      that.getData(that.data.lineNames, that.data.xdata, that.data.ydata);
      that.get24mData(that.data.lineNames24m, that.data.xdata24m, that.data.ydata24m);
      that.get30dData(that.data.lineNames30, that.data.xdata30, that.data.ydata30);
    }, 1000)
  }
})
