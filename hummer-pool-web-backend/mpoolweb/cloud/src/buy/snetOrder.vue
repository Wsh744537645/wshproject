<template>
  <div class="box">
    <div class="container">
      <!-- 提交订单状态图 -->
      <div class="row">
        <div class="col-md-12">
          <img src="../assets/pic@2x/progress@2x.png" class="plan col-md-offset-3">
        </div>
      </div>
      <div class="row">
        <div class="col-md-9 left">
          <div class="col-md-3 left_left">
            <p>币种</p>
            <p>机型</p>
            <p>算力</p>
            <p>套餐周期</p>
            <p>电费缴纳天数</p>
          </div>
          <div class="col-md-9 left_right">
            <!-- 币种 -->
            <input placeholder="BTC" :disabled="true" class="input">
            <P class="c7">蜂鸟矿机C7</P>
            <div class="power">
              <input type="text" class="powerNum" :disabled="true" v-model="input">
              <span class="ths">TH/S</span>
            </div>
            <!-- 套餐周期 -->
            <input placeholder="BTC" v-model="powerTime" :disabled="true" class="input">
            <!-- computed里计算出来这个时间，用时间戳算 -->
            <p class="date">(2019.06.01 ~ 2019.08.29)</p>
            <!-- 电费天数 -->
            <input type="text" placeholder="输入天数 （≥10）" class="input" v-model="time">
            <!-- 收货地址 -->
            <div class="address">
              <!-- <input type="textarea" value="BTC收获地址："> -->
              <textarea cols="37" rows="2" placeholder="BTC收获地址：" v-model="adress"></textarea>
              <el-button type="warning" class="add">添加</el-button>
            </div>
          </div>
        </div>
        <div class="col-md-3 right">
          <div class="top">
            <p>算力费 $</p>
            <input type="text" value="$438.00" v-model="powerMoney" :disabled="true">
            <p>电费 $</p>
            <input type="text" value="$48.50" v-model="eleMoney" :disabled="true">
            <span>=每天电费*算力*天数</span>
          </div>
          <div class="bottom">
            <p>总计 $</p>
            <!-- 等于算力费加电费 -->
            <input type="text" :value="totalMpney" :disabled="true">
            <el-checkbox v-model="checked">
              <a href>我接受Hummer miner 用户协议</a>
            </el-checkbox>
          </div>
        </div>
        <div class="row">
          <div class="col-md-3 btnBox">
            <span class="btn" @click="submit">提交</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { createOrder } from "@/api/goods.js";
export default {
  data() {
    return {
      //选中
      checked: true,
      //币种
      value: "BTC",
      time: "10",
      //算力
      input: "500",
      // 套餐周期
      powerTime: "90",
      powerMoney: "",
      eleMoney: "",
      adress: "",
      subProductId: ""
    };
  },
  created() {
    //获取到产品id
    this.subProductId = this.$route.params.productId;
    //地址等于用户收币地址
    this.adress = this.$store.state.userInfo.walletAddress;
    //获取套餐周期
    if (this.$route.params.productId == 1) {
      this.powerTime = this.$store.state.power90.period;
      this.input=this.$store.state.power90.minAccept
      // 算力费=周期*算力*套餐时间
      this.powerMoney =
        this.$store.state.power90.period *
        this.$store.state.power90.minAccept *
        this.$store.state.power90.acceptFee;
      //电费=缴纳天数*电价
      this.eleMoney =
        this.time *
        this.$store.state.power90.powerFee *
        this.$store.state.power90.minAccept;
      //总钱数
      this.totalMpney = this.powerMoney + this.eleMoney;
    } else {
      this.powerTime = this.$store.state.power180.period;
      this.input=this.$store.state.power180.minAccept
      // 算力费=周期*算力*套餐时间
      this.powerMoney =
        this.$store.state.power180.period *
        this.$store.state.power180.minAccept *
        this.$store.state.power180.acceptFee;
      //电费=缴纳天数*电价
      this.eleMoney =
        this.time *
        this.$store.state.power180.powerFee *
        this.$store.state.power180.minAccept;
      this.totalMpney = this.powerMoney + this.eleMoney;
    }
  },
  watch: {
    // 动态修改电费和总价
    time(val) {
      if (val >= 10) {
        if (this.$route.params.productId == 1) {
          this.eleMoney =
            val *
            this.$store.state.power90.powerFee *
            this.$store.state.power90.minAccept;
          this.totalMpney = this.powerMoney + this.eleMoney;
        } else {
          this.eleMoney =
            val *
            this.$store.state.power180.powerFee *
            this.$store.state.power90.minAccept;
           this.totalMpney =this.powerMoney + this.eleMoney;
        }
      } else {
        this.time = 10;
        this.$notify.error({
          message: "最小电费缴纳天数10天"
        });
      }
    }
   
  },
  methods: {
    submit() {
      if (this.adress) {
        if (this.checked) {
          createOrder({
            productId: this.subProductId,
            accept: this.input,
            powerDay: this.time,
            walleyAddress: this.adress
          }).then(res => {
            this.$store.commit("orderInfo", res.data.data);
          });
          this.$router.push(`/home/pay/${this.$route.params.id}/${this.subProductId}`);
        } else {
          this.$notify.error({
            message: "请阅读并同意用户协议"
          });
        }
      } else {
        this.$notify.error({
          message: "请先填写收币地址"
        });
      }
    }
  }
};
</script>
<style lang="less" scoped>
html {
  width: 100%;
  overflow-x: hidden;
}
.box {
  background: rgba(238, 238, 238, 1);

  .container {
    margin-top: 300px;
    .plan {
      margin-bottom: 50px;
      text-align: center;
    }
    // z左盒子
    .left {
      height: 872px;
      //   border: 1px solid white;
      background-color: white;
      float: left;
      padding-top: 30px;
      //   左盒子左半边
      .left_left {
        height: 100%;
        background-color: white;
        p {
          margin-top: 58px;
          font-size: 24px;
          font-weight: 400;
          color: rgba(0, 0, 0, 1);
          &:nth-child(4) {
            margin-top: 70px;
          }
          &:nth-child(5) {
            margin-top: 95px;
          }
          &:nth-child(6) {
            margin-top: 180px;
          }
        }
      }
      //左盒子右半边
      .left_right {
        height: 100%;
        .input {
          width: 50%;
          margin-top: 45px;
        }
        .c7 {
          font-size: 26px;
          font-weight: 400;
          margin-top: 45px;
          margin-bottom: 0;
        }
        .date {
          margin-left: 23%;
        }
        input {
          outline: none;
          width: 45%;
          height: 57px;
          margin-top: 45px;
          border-radius: 5px;
          border: none;
          font-size: 17px;
          padding-left: 10px;
          border: 1px solid #ccc;
          border-radius: 5px;
        }
        .powerNum {
          width: 50%;
        }
        .ths {
          position: absolute;
          top: 248px;
          left: 270px;
        }
        textarea {
          outline: none;
          resize: none;
          padding: 10px;
          box-sizing: border-box;
          margin-top: 45px;
          border: none;
          font-size: 16px;
          border: 1px solid #ccc;
          border-top-left-radius: 5px;
          border-bottom-left-radius: 5px;
        }
        .add {
          height: 66px;
          position: absolute;
          bottom: 211px;
          border-radius: 0;
          border-top-right-radius: 5px;
          border-bottom-right-radius: 5px;
        }
      }
    }
    // 右盒子
    .right {
      background-color: white;
      border-left: 2px solid #eeeeee;
      //公共样式
      p {
        margin-top: 58px;
        font-size: 24px;
        font-weight: 400;
        color: rgba(0, 0, 0, 1);
      }
      input {
        outline: none;
        width: 100%;
        height: 57px;
        margin-top: 22px;
        border-radius: 5px;
        border: none;
        font-size: 27px;
        font-weight: 400;
        padding-left: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
      }
      .top {
        border-bottom: 1px solid rgba(191, 191, 191, 0.5);
        span {
          display: inline-block;
          text-align: right;
          margin-top: 5px;
          margin-bottom: 100px;
        }
      }
      .bottom {
        input {
          height: 78px;
          font-size: 38px;
          font-weight: 700;
          margin-bottom: 25px;
        }
        a {
          display: inline-block;
          margin-bottom: 26px;
        }
      }
    }
    .btnBox {
      padding-left: 0;
      padding-right: 7.5px;
      .btn {
        height: 106px;
        width: 100%;
        background-color: #171c61;
        border-radius: 0;
        font-size: 31px;
        color: white;
        line-height: 106px;
      }
    }
  }
}
</style>
