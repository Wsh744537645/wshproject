<template>
  <div class="container">
    <!-- 流程图 -->
    <div class="row">
      <div class="col-md-12">
        <img src="../assets/pic@2x/complete@2x.png" class="plan col-md-offset-3">
      </div>
    </div>
    <div class="row">
      <div class="col-md-12 main">
        <img src="../assets/pic@2x/orderCpl@2x.png" alt class="sure">
        <p>交易成功</p>
        <router-link to="/user" class="btn">查看订单</router-link>
      </div>
    </div>
  </div>
</template>
<style lang="less" scoped>
.container {
  margin-top: 280px;
  //测试保留高度
  height: 1000px;
  .plan {
    margin-bottom: 50px;
    text-align: center;
  }
  .main {
    background-color: #fff;
    border-radius: 5px;
    text-align: center;
    .sure {
      margin-top: 104px;
      margin-bottom: 33px;
    }
    p {
      font-size: 39px;
      font-weight: 300;
      margin-bottom: 70px;
    }
    .btn{
      width: 25%;
      height: 60px;
      background-color: #171c61;
      color: #fff;
      line-height: 50px;
      font-size:28px;
      font-weight: 300;
      border-radius: 5px;
      margin-bottom: 90px;
    }
  }
}
</style>
