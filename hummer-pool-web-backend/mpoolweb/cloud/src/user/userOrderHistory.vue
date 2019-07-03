<template>
  <div class="col-md-9 box">
    <div class="top col-md-12">
      <div class="top_title">
        <div>&lt;&lt;&nbsp;&nbsp;订单列表</div>
      </div>
    </div>
    <div class="mid col-md-12">
      <div class="mid_left col-md-6">
        <div class="orderNum">订单号</div>
        <p class="num">{{$route.params.orderId}}</p>
      </div>
      <div class="mid_right col-md-6">
        <div class="orderStatus">订单状态</div>
        <span class="status">等待付款</span>
        <button type="button" class="btn btn-primary pay" @click="toPay">继续支付</button>
        <button type="button" class="btn btn-default cancel">取消</button>
      </div>
    </div>
    <div class="bottom col-md-12">
      <el-row>
        <el-col :span="24">
          <el-tabs v-model="activeName">
            <el-tab-pane label="基本信息" name="first">
              <div class="col-md-11 main_top_bottom">
                <!-- 左边列表 -->
                <div class="main_top_bottom_left col-md-3">
                  <p v-for="(item,i) in plists" :key="i" class="main_top_bottom_left_text">{{item.name}}</p>
                </div>
                <!-- 右边列表 -->
                <div class="main_top_bottom_right col-md-9">
                  <p v-for="(item,i) in rlists" :key="i" class="main_top_bottom_right_text">{{item}}</p>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="产出" name="second">
              <div class="col-md-11 main_top_bottom">
                <div class="main_top_bottom_left col-md-3">
                  <p v-for="(item,i) in exportlists" :key="i" class="main_top_bottom_left_text">{{item.name}}</p>
                </div>
                <div class="main_top_bottom_right col-md-9">
                  <!-- <router-link :to="{name:'outputDetail',params:{Outid:Outid} }" tag="a">查看</router-link> -->
                  <p v-for="(item,i) in exportRlists" :key="i"  class="main_top_bottom_right_text">{{item}}</p>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="付款" name="third">
              
              <div class="col-md-11 main_top_bottom">
                <div class="main_top_bottom_left col-md-3">
                  <p v-for="(item,i) in paylists" :key="i" class="main_top_bottom_left_text">{{item}}</p>
                </div>
                <div class="main_top_bottom_right col-md-9">
                  <p v-for="(item,i) in payRlists" :key="i" class="main_top_bottom_right_text">{{item}}</p>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { getOrderDetail,getOrderAccept,getOrderPay,stateName,timeFormat } from '@/api/goods.js'
import { finished } from 'stream';
export default {
  data() {
    return {
      //测试id，用在判断不同的id查看不同的产量
      Outid:1,
      activeName: "first",
      //基本信息
      plists: [
        {
          name: "套餐周期"
        },
        {
          name: "币种"
        },
        {
          name: "下单时间"
        },
        {
          name: "套餐开始时间"
        },
        {
          name: "套餐结束时间"
        },

        {
          name: "算力"
        }
      ],
      //基本信息右
      rlists: [],
      //产出左label
      exportlists: [
        {
          name: "挖矿周期"
        },
        {
          name: "累计产出"
        },
        {
          name: "算力"
        },
        {
          name: "套餐剩余天数"
        },
        {
          name: "电费剩余天数"
        }
      ],
      //产出右边边框
      exportRlists: [],
      //付款左label
      paylists: ["订单金额","支付方式","已付金额","支付状态"],
      //付款右边边框
      payRlists: [],
      data: [],
      productId: ''
    };
  },
  created() {
    getOrderDetail(this.$route.params.orderId).then(res=>{
      const data = res.data.data
      this.productId = res.data.data.productId
      this.rlists = [data.period, data.currencyName, timeFormat(data.createTime), timeFormat(data.workTime), timeFormat(data.expireTime), data.accept]
    })
    getOrderAccept(this.$route.params.orderId).then(res=>{
      const data = res.data.data
      this.exportRlists = [data.period,data.totalShare,data.accept,data.acceptDay,data.powerDay]
    })
    getOrderPay(this.$route.params.orderId).then(res=>{
      const data = res.data.data
      data.state = stateName(data.state)
      this.payRlists = [data.payCoin,data.currencyName,data.hadPay,data.state]
    })
  },
  methods: {
    toPay() {
      this.$router.push(`/home/pay/${JSON.parse(sessionStorage.getItem("targetId")).userId}/${this.productId}`)
    }
  },
};
</script>
<style lang="less" scoped>
.box {
  background-color: #fff;
  padding: 36px 59px 45px 45px;
  overflow: hidden;
  .top {
    border-bottom: 1px solid rgba(210, 210, 210, 1);
    font-size: 22px;
    height: 45px;
    margin-bottom: 45px;
  }
  .mid {
    margin-bottom: 74px;
    .orderNum,
    .orderStatus {
      font-size: 17px;
      font-weight: 300;
      color: rgba(100, 100, 100, 1);
      margin-bottom: 26px;
    }
    .num,
    .status {
      font-size: 25px;
      font-weight: bold;
      margin: 0;
      margin-right: 33px;
    }
    .pay,
    .cancel {
      vertical-align: top;
      cursor: pointer;
    }
    .pay {
      margin-right: 10px;
    }
  }
  .bottom {
    /deep/.el-tabs__content {
      padding: 30px 0 0 20px;
    }
    /deep/.el-tabs__item {
      font-size: 22px;
    }
    .main_top_bottom {
      text-align: center;
      background: rgba(238, 238, 238, 1);
      border-radius: 5px;
      padding: 0;
      p,
      a {
        display: block;
        border-bottom: 1px solid #fff;
        margin-bottom: 0;
        height: 60px;
        line-height: 60px;
        font-size: 17px;
        font-weight: 300;
      }
      /deep/a {
        cursor: pointer;
      }
      .main_top_bottom_left,
      .main_top_bottom_right {
        text-align: left;
        padding: 0;
      }
      .main_top_bottom_left_text{
        padding-left:30px
      }
      .main_top_bottom_right_text{
        padding-left:65px
      }
      .main_top_bottom_right {
        border-left: 1px solid #fff;
      }
    }
  }
}
</style>
