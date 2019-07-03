<template>
  <div class="right col-md-9">
    <div class="right_top">
      <img src="../assets/pic@2x/btcCoin@2x.png" alt>
      <p>BTC</p>
      <span>{{size}}个套餐正在挖矿</span>
    </div>
    <div class="right_mid">
      <div class="pic col-md-1">
        <img src="../assets/pic@2x/power@2x.png">
        <p class="allpower">总算力</p>
      </div>
      <div class="power col-md-2">
        <div class="power_num">
          <span>{{accept}}</span>
          <span>TH/S</span>
        </div>
        <div class="more"  @click='toIndex'>购买更多</div>
      </div>
      <div class="picEarn col-md-1 col-md-offset-2">
        <img src="../assets/pic@2x/allEarn@2x.png" alt>
        <p class="allEarn">总收入</p>
      </div>
      <div class="earn col-md-6">
        <div class="power_num">
          <span>{{balance}}</span>
          <span>TH/S</span>
        </div>
        <p class="money">≈${{balance_usd}}</p>
      </div>
    </div>
    <!-- <div class="right_bottom">
      <div class="right_bottom_top col-md-12">市场信息</div>
      <div class="right_bottom_bottom col-md-12">
        <div class="BTC col-md-3">BTC</div>
        <div class="ETH col-md-3">ETH</div>
        <div class="BCH col-md-3">BCH</div>
      </div>
    </div> -->
  </div>
</template>
<script>
import {billtotal} from '@/api/goods.js'
export default {
  data() {
    return {
      id: this.$route.params.id,
      balance: '0.00000',
      balance_usd: '0.00000',
      accept: 0,
      size: 0
    }
  },
  created() {
    billtotal().then(res=> {
        this.balance = res.data.data.balance
        this.balance_usd = res.data.data.balance_usd
        this.accept = res.data.data.accept
        this.size = res.data.data.size
    })
    console.log(this.$route.params.id);
  },
  methods: {
     //登陆完点击购买更多到购买页
    toIndex(){
       this.$router.push(`/loginIndex/${this.$route.params.id}`)
    }
  },
}
</script>
<style lang="less" scoped>
.right {
  .right_top {
    background-color: #fff;
    border-radius: 5px;
    width: 100%;
    padding-left: 44px;
    height: 85px;
    overflow: hidden;
    margin-bottom: 13px;
    img {
      position: absolute;
      top: 30px;
    }
    p {
      font-size: 18px;
      font-weight: 400;
    }
    span {
      font-size: 14px;
      font-weight: 300;
    }
    p,
    span {
      margin: 0;
      margin-top: 22px;
      margin-left: 56px;
    }
  }
  .right_mid {
    background-color: #fff;
    border-radius: 5px;
    height: 205px;
    text-align: center;
    padding-top: 40px;
    padding-left: 45px;
    margin-bottom: 13px;
    .pic,
    .picEarn {
      padding: 0;
    }
    .power_num {
      border-bottom: 1px solid rgba(238, 238, 238, 1);
      span {
        &:nth-child(1) {
          font-size: 45px;
        }
        &:nth-child(2) {
          margin-left: 10px;
          font-size: 20px;
          font-weight: 100;
        }
      }
    }
    .more {
      width: 100%;
      border: 1px solid #000;
      border-radius: 5px;
      height: 35px;
      line-height: 35px;
      font-size: 15px;
      margin-top: 40px;
      cursor: pointer;
    }
    .money {
      text-align: left;
      margin-top: 18px;
      font-size: 23px;
      font-weight: 300;
    }
  }
  .right_bottom {
    overflow: hidden;
    background-color: #fff;
    border-radius: 5px;
    .right_bottom_top {
      height: 75px;
      line-height: 75px;
      font-size: 22px;
      border-bottom: 1px solid rgba(238, 238, 238, 1);
      padding-left: 30px;
      margin-bottom: 45px;
    }
    .right_bottom_bottom{
        overflow: hidden;
        display: flex;
        justify-content: space-around;
        height: 123px;
        margin-bottom: 46px;
        .BTC,.ETH,.BCH{
            height: 100%;
           background-color: #eee;
           border-radius:5px;
        }
    }
  }
}
</style>
