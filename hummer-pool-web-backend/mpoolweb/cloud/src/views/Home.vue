<template>
  <div class="container">
    <div class="row box">
      <div class="col-xs-6 col-lg-12 underBox">
        <div class="box_header col-lg-12">
          <span class="title col-lg-12">
            <span class="day">{{allInfo1.period}}天</span> 合约
          </span>
        </div>
        <div class="power col-lg-12">
          <div class="col-md-3">理论化年收益</div>
          <div class="col-md-3">预计日产出</div>
          <div class="col-md-3">电费</div>
          <div class="col-md-3">租赁费</div>
          <div class="col-md-3">
            <span>{{allInfo1.shareYear}}</span> %
          </div>
          <div class="col-md-3">
            <span>{{allInfo1.period}}</span> BTC/T
          </div>
          <div class="col-md-3">
            $
            <span>{{allInfo1.powerFee}}</span> 1T/天
          </div>
          <div class="col-md-3">
            $
            <span>{{allInfo1.acceptFee}}</span> 1T/天
          </div>
        </div>
        <div class="footer col-lg-12">
          <div class="col-md-6 money">$ {{allprice90}}</div>
          <div class="col-md-3">
            <input type="number" class="number" value v-model="allInfo1.minAccept">
          </div>
          <div class="col-md-3 btn">
            <el-button type="primary" plain @click="buyNow">立即购买</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="row box">
      <div class="col-xs-6 col-lg-12 underBox">
        <div class="box_header col-lg-12">
          <span class="title col-lg-12">
            <span class="day">{{allInfo2.period}}天</span> 合约
          </span>
        </div>
        <div class="power col-lg-12">
          <div class="col-md-3">理论化年收益</div>
          <div class="col-md-3">预计日产出</div>
          <div class="col-md-3">电费</div>
          <div class="col-md-3">租赁费</div>
          <div class="col-md-3">
            <span>{{allInfo2.shareYear}}</span> %
          </div>
          <div class="col-md-3">
            <span>{{allInfo2.period}}</span> BTC/T
          </div>
          <div class="col-md-3">
            $
            <span>{{allInfo2.powerFee}}</span> 1T/天
          </div>
          <div class="col-md-3">
            $
            <span>{{allInfo2.acceptFee}}</span>1T/天
          </div>
        </div>
        <div class="footer col-lg-12">
          <div class="col-md-6 money">$ {{allprice180}}</div>
          <div class="col-md-3">
            <input type="number" v-model="allInfo2.minAccept" value class="number">
          </div>
          <div class="col-md-3 btn">
            <el-button type="primary" plain @click="buyNow">立即购买</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { all } from "@/api/goods.js";
export default {
  data() {
    return {
     
      allInfo1: {},
      allInfo2: {}
    };
  },
  created() {
    //将productId写入store里 在点击立即购买时，跳转的页面是本人id下的某一产品，通过productId判断
    all()
      .then(res => {
        // console.log(res);
        this.allInfo1 = res.data.data[0];
        this.allInfo2 = res.data.data[1];
      })
      .catch(err => {
        console.log(err);
      });
  },
  methods: {
    buyNow() {
        this.$notify.error({
          message: "请先登录"
        });   
    }
  },
  computed: {
    allprice90() {
      return parseFloat(
        this.allInfo1.acceptFee * this.allInfo1.minAccept * this.allInfo1.period
      ).toFixed(2);
    },
    allprice180() {
      return parseFloat(
        this.allInfo1.acceptFee * this.allInfo1.minAccept * this.allInfo1.period
      ).toFixed(2);
    }
  }
};
</script>
<style lang="less" scoped>
.container {
  margin-bottom: 100px;
  .row.lease {
    margin-bottom: 70px;
    text-align: center;
    .el-button--primary.is-plain {
      width: 277px;
      height: 70px;
      // background: #171c61;
      font-size: 27px;
      font-weight: 300;
    }
  }
  .row.box {
    border: 1px solid #d5d5d5;
    border-radius: 5px;
    margin-bottom: 73px;
    .underBox {
      padding: 0;
      .box_header {
        width: 100%;
        height: 66px;
        background-color: #171c61;
        line-height: 66px;
        .title {
          color: rgba(255, 255, 255, 1);
          font-size: 18px;
          font-weight: 300;
          text-align: left;
          .day {
            font-size: 36px;
            // font-weight:bold;
          }
        }
      }
      .power {
        background-color: #fff;
        text-align: center;
        font-size: 18px;
        font-weight: 300;
        padding-bottom: 50px;
        padding-top: 61px;
        div {
          span {
            font-size: 25px;
            font-weight: bold;
          }
        }
      }
      .footer {
        background: rgba(238, 238, 238, 1);
        padding-right: 0;
        height: 127px;
        text-align: center;
        .money {
          text-align: left;
          line-height: 127px;
          font-size: 48px;
          font-weight: 400;
          color: rgba(255, 96, 0, 1);
          padding-left: 93px;
        }
        .number {
          height: 57px;
          width: 100%;
          margin-top: 30px;
          text-align: center;
          font-size: 28px;
          font-weight: 400;
          margin-bottom: 10px;
        }
        .btn {
          padding-top: 26px;
          .el-button {
            width: 100%;
            height: 64px;
            background-color: #171c61;
            font-size: 30px;
            font-weight: 400;
            color: rgba(255, 255, 255, 1);
          }
        }
      }
    }
  }
}
</style>

