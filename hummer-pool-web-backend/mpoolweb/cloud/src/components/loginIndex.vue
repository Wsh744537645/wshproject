<template>
  <div class="all" :style="setBackground ">
    <img src="../assets/pic@2x/headlogo@2x.png" alt class="logo">
    <!-- 公告 -->
    <div class="gonggao">
      <img src="../assets/pic@2x/gonggao@2x.png" alt>
      <span>部分矿场电力维护公告</span>
      <a href="#">查看详情</a>
    </div>
    <div class="container">
      <!-- 头 -->
      <div class="row header">
        <div class="col-lg-7 col-lg-offset-6">
          <ul>
            <router-link :to="{name:'userindex',params:{id:id}}" class="first" tag="li">账户信息</router-link>
            <li>简体中文</li>
          </ul>
        </div>
      </div>
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
              <input
                type="number"
                class="number"
                value=""
                v-model="allInfo1.minAccept"
                @change="minPower90"
              >
            </div>
            <div class="col-md-3 btn">
              <el-button type="primary" plain @click="buyNow90">立即购买</el-button>
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
              <input
                type="number"
                v-model="allInfo2.minAccept"
                value=""
                class="number"
                @change="minPower180"
              >
            </div>
            <div class="col-md-3 btn">
              <el-button type="primary" plain @click="buyNow180">立即购买</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="indexfooter">
      <div class="container">
        <!-- 左边 -->
        <div class="left col-md-5">
          <!-- logo -->
          <img src="../assets/pic@2x/logo@2x.png" alt class="footLogo">
          <p class="conect">联系我们</p>
          <div class="phoneNum">0755-26922865</div>
          <i class="el-icon-location">&nbsp;&nbsp; 深圳市南山区豪威科技大厦2101</i>
          <br>
          <!-- 微信qq -->
          <img src="../assets/pic@2x/qq@2x.png" alt class="qq">
          <img src="../assets/pic@2x/wxtb@2x.png" alt class="wechat">
        </div>
        <!-- 右边 -->
        <div class="right col-md-5">
          <div>
            <dl>
              <dt role="presentation">云算力产品</dt>
              <dd>
                <a href="#">产品</a>
              </dd>
              <dd>
                <a href="#">订单</a>
              </dd>
              <dd>
                <a href="#">支付</a>
              </dd>
            </dl>
            <dl>
              <dt role="presentation">账户面板</dt>
              <dd>
                <a href="#">账户设置</a>
              </dd>
              <dd>
                <a href="#">云算力面板</a>
              </dd>
              <dd>
                <a href="#">订单记录</a>
              </dd>
              <dd>
                <a href="#">账单明细</a>
              </dd>
            </dl>
            <dl>
              <dt role="presentation">关于</dt>
              <dd>
                <a href="#">关于我们</a>
              </dd>
              <dd>
                <a href="#">常见问题</a>
              </dd>
              <dd>
                <a href="#">商务合作</a>
              </dd>
            </dl>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { all } from "@/api/goods.js";
import { parse } from 'path';
export default {
  data() {
    return {
      productId: "2",
      checked: true,
      id: this.$route.params.id,
      //背景图样式
      setBackground: {
        backgroundImage:
          "url(" + require("../assets/pic@2x/indexLogo.jpg") + ")",
        backgroundPosition: "center",
        width: "100%",
        height: "937px"
        // backgroundSize: "length"
      },
      allInfo1: {},
      allInfo2: {},
     
    };
  },
  created() {
    //将productId写入store里 在点击立即购买时，跳转的页面是本人id下的某一产品，通过productId判断
   
    all()
      .then(res => {
        this.allInfo1 = res.data.data[0];
        this.allInfo2 = res.data.data[1];
      })
      .catch(err => {
        console.log(err);
      });
  },
  methods: {
     minPower90() {
      if (this.allInfo1.minAccept <= 500) {
        this.$notify.error({
          message: "最小购买数500TH/S"
        });
      }else{
        
         console.log(this.$store.state.power90,9999); 
      }
    },
     minPower180() {
      if (this.allInfo2.minAccept <= 500) {
        this.$notify.error({
        message: "最小购买数500TH/S"
        });
      }else{
       
      }
    },
    // 90天套餐
    buyNow90() {
      this.productId = this.allInfo1.productId; 
      if (this.allInfo1.minAccept >=500) {  
         this.$store.commit('buy90',{minAccept:this.allInfo1.minAccept,period:this.allInfo1.period,acceptFee:this.allInfo1.acceptFee,powerFee:this.allInfo1.powerFee,productId:this.allInfo1.productId})
        this.$router.push(
          `/home/${
            JSON.parse(sessionStorage.getItem("targetId")).userId
          }/order/${this.productId}`
        );
      } else {
        this.$notify.error({
          message: "最小购买数500TH/S"
        });
      }
    },
    // 180套餐
    buyNow180() {
      this.productId = this.allInfo2.productId;
      if (this.allInfo2.minAccept >= 500) {
         // 这里要传store的数据是算力的多少，周期是多少,
        this.$store.commit('buy180',{minAccept:this.allInfo2.minAccept,period:this.allInfo2.period,acceptFee:this.allInfo2.acceptFee,powerFee:this.allInfo2.powerFee,productId:this.allInfo2.productId})
        this.$router.push(
          `/home/${
            JSON.parse(sessionStorage.getItem("targetId")).userId
          }/order/${this.productId}`
        );
      } else {
        this.$notify.error({
          message: "最小购买数500TH/S"
        });
      }
    },
  },
  computed: {
    allprice90() {
      return parseFloat(
        this.allInfo1.acceptFee * this.allInfo1.minAccept * this.allInfo1.period
      ).toFixed(2);
    },
    allprice180() {
      return parseFloat(
        this.allInfo2.acceptFee * this.allInfo2.minAccept * this.allInfo2.period
      ).toFixed(2);
    }
  },
};
</script>
<style lang="less" scoped>
html {
  width: 100%;
  overflow-x: hidden;
}
.all {
  .indexfooter {
    margin-top: 100px;
    width: 100%;
    background-color: #171c61;
    color: #fff;
    text-align: center;
    height: 334px !important;
    .container {
      padding: 0;
      .left {
        text-align: left;
        margin-top: 30px;
        .footLogo {
          margin-bottom: 23px;
        }
        .conect {
          margin: 0;
          font-size: 17px;
          margin-bottom: 13px;
          font-weight: 300;
          color: #fff;
        }
        .phoneNum {
          font-size: 46px;
          font-weight: 400;
          color: #fff;
        }
        i {
          font-size: 17px;
          margin-bottom: 27px;
          font-weight: 300;
          color: #fff;
        }
        .qq,
        .wechat {
          margin-right: 20px;
        }
      }
      .right {
        text-align: left;
        dl {
          float: left;
          margin-left: 60px;
          color: #fff;
          dt {
            // margin-left: 40px;
            font-size: 22px;
            font-weight: 300;
            line-height: 117px;
          }
          dd {
            height: 34px;
            font-size: 18px;
            font-weight: 300;
            a {
              color: rgba(200, 200, 200, 1) !important;
            }
          }
        }
      }
    }
  }
  //头
  .container {
    color: #333;
    text-align: center;
    // height: 1080px !important;
    padding-top: 57px;
    .header {
      margin-bottom: 937px;
      ul {
        li {
          list-style: none;
          float: left;
          font-size: 28px;
          color: #fff;
          margin-left: 93px;
          a {
            color: #fff;
          }
          .dropdown-menu {
            background-color: #171c61;
            li {
              background-color: #171c61;
              .first,
              .second {
                color: orange;
                cursor: pointer;
              }
            }
          }
        }
      }
    }
    //购买框
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
  //公告
  .gonggao {
    width: 690px;
    position: absolute;
    top: 800px;
    left: 8%;
    border: 1px solid white;
    border-radius: 15px;
    height: 59px;
    text-align: left;
    line-height: 59px;
    img {
      margin-left: 7%;
      vertical-align: middle;
      margin-bottom: 10px;
      display: inline-block;
    }
    span {
      font-size: 28px;
      font-weight: 300;
      color: rgba(255, 255, 255, 1);
      margin-left: 5%;
    }
    a {
      font-size: 28px;
      font-weight: 300;
      color: rgba(255, 255, 255, 1);
      margin-left: 15%;
      &:hover {
        color: orange;
      }
    }
  }
  .logo {
    z-index: 999;
    position: absolute;
    top: 31px;
    left: 80px;
  }
}
</style>