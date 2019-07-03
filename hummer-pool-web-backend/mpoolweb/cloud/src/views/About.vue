<template>
  <div>
    <!-- 盒子容器 -->
    <el-container>
      <!-- 头部 -->
      <indexHeader v-if="$route.path=='/home'"></indexHeader>
      <orderHeader v-else-if="$route.name=='order'"></orderHeader>
      <orderHeader v-else-if="$route.name=='pay'"></orderHeader>
      <elseHeader v-else></elseHeader>
      <!-- 内容部分 -->
      <el-main>
        <router-view></router-view>
        {{id}}{{payId}}
      </el-main>
      <!-- 底部 -->
      <el-footer>
        <el-row>
          <!-- 左边 -->
          <el-col :span="12">
            <div class="left">
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
          </el-col>
          <!-- 右边 -->
          <el-col :span="12" class="right">
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
          </el-col>
        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>
<script>
// 首页头部组件
import indexHeader from "../components/indexHeader_index.vue";
import elseHeader from "../components/indexHeader.vue";
import orderHeader from "../components/indexHeader_order";
import {test} from "@/api/login.js"
export default {
  data() {
    return {
      activeIndex: "1",
      //跳到购买订单表格的路由们
    };
  },
  //引入首页头部
  components: {
    indexHeader,
    elseHeader,
    orderHeader,
  },
  created() {
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style lang="less" scoped>
a {
  text-decoration: none;
  color: #fff;
}

//内容
.el-main {
  background-color: #e9eef3;
  color: #333;
  padding-top: 90px;
  width: 100%;
  // height: 1852px !important;
}
//底部
.el-footer {
  width: 100%;
  background-color: #171c61;
  color: #fff;
  text-align: center;
  height: 334px !important;
  .left {
    text-align: left;
    margin-left: 30%;
    .footLogo {
      margin-top: 22px;
      margin-bottom: 23px;
    }
    .conect {
      margin: 0;
      font-size: 17px;
      margin-bottom: 13px;
      font-weight: 300;
    }
    .phoneNum {
      font-size: 46px;
      font-weight: 400;
    }
    i {
      font-size: 17px;
      margin-bottom: 27px;
      font-weight: 300;
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
      margin-left: 89px;
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
        color: rgba(200, 200, 200, 1);
      }
    }
  }
}
</style>

