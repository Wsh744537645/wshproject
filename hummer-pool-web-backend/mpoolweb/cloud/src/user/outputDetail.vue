<template>
  <div class="col-md-9 box">
    <div class="mid col-md-12">
      <div class="mid_left col-md-6">
        <div class="orderNum">订单号</div>
        <p class="num">1111111111111115111</p>
      </div>
      <div class="mid_right col-md-6">
        <div class="orderStatus">挖矿周期</div>
        <span class="status">90天</span>
      </div>
    </div>
    <div class="bottom col-md-12">
      <div class="col-md-11 main_top_bottom">
        <!-- //左边列表 -->
        <div class="main_top_bottom_left col-md-3">
          <p v-for="(item,i) in plists" :key="i">{{item.name}}</p>
        </div>
        <!-- 右边列表 -->
        <div class="main_top_bottom_right col-md-9">
          <p v-for="(item,i) in rlists" :key="i">{{item.name}}</p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      //记录传递来的ID
      targetId: "",
      activeName: "first",
      //基本信息
      plists: [
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
      //基本信息右边边框(需要传值)
      rlists: [{ name: "--" }, { name: "--" }, { name: "--" }, { name: "--" }]
    };
  },
  created() {
    console.log(this.$route.params.id);
    this.targetId = this.$route.params.id;
  },
  watch: {
    targetId(val) {
      console.log(val, 999);
      //通过id展示不同数据
    }
  }
};
</script>
<style lang="less" scoped>
.box {
  background-color: #fff;
  padding: 36px 59px 45px 45px;
  overflow: hidden;

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
        padding: 0;
      }
      .main_top_bottom_right {
        border-left: 1px solid #fff;
      }
    }
  }
}
</style>
