<template>
  <div class="container">
    <!-- 流程图 -->
    <div class="row">
      <div class="col-md-12">
        <img src="../assets/pic@2x/jingdutiao@2x.png" class="plan col-md-offset-3">
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 main">
        <div class="col-md-12 main_top">
          <div class="col-md-4 main_top_left">
            <img src="../assets/pic@2x/btcCoin@2x.png" alt class="btcCoin">
            <span>&nbsp;&nbsp; BTC付款</span>
          </div>
          <div class="col-md-4 col-md-offset-4 main_top_right">请及时支付&nbsp;&nbsp;{{minute}}:{{second}}</div>
          <!-- 这里要改成倒计时，收藏里有组件 -->
        </div>
        <!-- 二维码 -->
        <div class="col-md-12 main_top_middle">
          <img src="../assets/pic@2x/erweima@2x.png" alt class="erweima">
          <p class="pay">扫描二维码付款</p>
        </div>
        <div class="col-md-12 main_top_bottom">
          <!-- //左边列表 -->
          <div class="main_top_bottom_left col-md-3">
            <p v-for="(item,i) in plists" :key="i">{{item.name}}</p>
          </div>
          <!-- 右边列表 -->
          <div class="main_top_bottom_right col-md-9">
            <p>{{pRlists.miningName}}</p>
            <p>{{pRlists.orderId}}</p>
            <p>{{orderTime}}</p>
            <p>{{pRlists.exchangeRate}}</p>
            <p>{{waitPay}}</p>
            <p>{{pRlists.collectionAddress}}</p>
            <p>{{pRlists.accept}}</p>
            <p>{{powerMoney}}</p>
            <p>{{eleMoney}}</p>
            <p>{{all}}</p>
          </div>
        </div>
        <div class="col-md-12 btn">
          <el-button>完成订单</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { constants } from 'crypto';
export default {
  data() {
    return {
      //左边列表
      plists: [
        {
          name: "订单信息"
        },
        {
          name: "订单编号"
        },
        {
          name: "下单时间"
        },
        {
          name: "BTC价格"
        },
        {
          name: "待付金额"
        },
        {
          name: "收币地址"
        },
        {
          name: "算力"
        },
        {
          name: "算力费（$）"
        },
        {
          name: "电费（$）"
        },
        {
          name: "订单总价（$）"
        }
      ],
      pRlists: [
       
      ],
      waitPay: "",
      orderTime: "",
      minutes: 15,
      seconds: 0
    }
},
  created() {
    this.pRlists = JSON.parse(sessionStorage.getItem('orderInfo'));
    this.waitPay = this.pRlists.payCoin -this.pRlists.hadPay;
    //下单时间格式化
    this.orderTime = this.pRlists.createTime;  
    var date = new Date(this.orderTime)
    var Str=date.getFullYear() + '-' +
    (date.getMonth() + 1) + '-' + 
    date.getDate() + ' ' + 
    date.getHours() + ':' + 
    date.getMinutes() + ':' + 
    date.getSeconds()
    this.orderTime=Str
  },
  methods: {
    num(n) {  
        return n < 10 ? '0' + n : '' + n
    },
    timer () {
      var _this = this
      var time = window.setInterval(function () {
        if (_this.seconds === 0 && _this.minutes !== 0) {
          _this.seconds = 59
          _this.minutes -= 1
        } else if (_this.minutes === 0 && _this.seconds === 0) {
          _this.seconds = 0
          window.clearInterval(time)
          _this.$router.push(`/home/${_this.$route.params.id}/order/${_this.$route.params.productId}`)
        } else {
          _this.seconds -= 1
        }
      }, 1000)
    }
  },
  mounted() {
    this.timer()
  },
  watch: {
    second: {
      handler (newVal) {
        this.num(newVal)
      }
    },
    minute: {
      handler (newVal) {
        this.num(newVal)
      }
    }
  },
  computed: {
    powerMoney() {
      if (this.$route.params.productId == 1) {
        return this.pRlists.acceptFee * this.pRlists.accept * 90;
      } else {
        return this.pRlists.acceptFee * this.pRlists.accept * 180;
      }
    },
    eleMoney() {
      return (
        this.pRlists.powerFee * this.pRlists.powerDay * this.pRlists.accept
      );
    },
    all() {
      return this.powerMoney + this.eleMoney;
    },
    second: function() {
      return this.num(this.seconds);
    },
    minute: function() {
      return this.num(this.minutes);
    }

  }
}
</script>
<style lang="less" scoped>
.container {
  margin-top: 280px;
  .plan {
    margin-bottom: 50px;
    text-align: center;
  }
  .main {
    padding: 45px 65px 65px 65px;
    background-color: #fff;

    .main_top {
      border-bottom: 1px solid rgba(191, 191, 191, 0.5);
      height: 63px;
      .main_top_left {
        .btcCoin {
          z-index: 999;
          vertical-align: middle;
          position: absolute;
        }
        span {
          font-size: 28px;
          font-weight: 400;
          margin-left: 30px;
        }
      }
      .main_top_right {
        text-align: right;
        font-size: 28px;
        font-weight: 400;
      }
    }
    .main_top_middle {
      text-align: center;
      .erweima {
        margin-top: 56px;
      }
      .pay {
        margin-top: 15px;
        font-size: 26px;
      }
    }
    .main_top_bottom {
      text-align: center;
      background: rgba(238, 238, 238, 1);
      border-radius: 5px;

      p {
        border-bottom: 1px solid #fff;
        margin-bottom: 0;
        height: 75px;
        line-height: 75px;
        font-size: 28px;
        font-weight: 300;
      }
      .main_top_bottom_left,
      .main_top_bottom_right {
        padding: 0;
      }
      .main_top_bottom_right {
        border-left: 1px solid #fff;
        p {
          &:nth-last-child(-n + 3) {
            color: #ff0000;
          }
        }
      }
      
    }
  }
  .btn {
      margin-top: 49px;
      .el-button {
        font-size: 30px;
        font-weight: 400;
        color: rgba(255, 255, 255, 1);
        width:318px;
        height:71px;
        background:rgba(23,28,97,1);
        border-radius:15px;
      }
  }
}
</style>
