// pages/echart/echart.js

import * as echarts from '../../utils/ec-canvas/echarts';

function getOneOption(names, xdata, ydata) {
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
      // show: false
    },
    grid: [{
      top: '30%',
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
  getData: function (names, xdata, ydata) {
    // console.log('2' + chart1)
    chart1.setOption(getOneOption(names, xdata, ydata));
  },
  onLoad: function () {
    const that = this;

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
            ary.push(d.hashAcceptT)
          })
          lineData.push({
            name: o,
            type: 'line',
            smooth: true,
            data: ary,
            areaStyle: {}
          })
          timeAry.push(date)
        }
        const timeData = []
        timeAry[0].forEach((t) => {
          const tim = parseInt(t.substring(8, 10)[1]) + 8
          timeData.push(((tim > 24) ? (tim - 24) : tim) + ':00')
        })
        that.setData({
          xdata: timeData,
          ydata: lineData,
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
    setTimeout(function () {
      // console.log(that.data.xdata);
      // console.log(that.data.ydata);
      that.getData(that.data.lineNames, that.data.xdata, that.data.ydata);
    }, 1000)
  }
})