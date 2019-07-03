<template>
  <div class="headerTop">
    <el-container>
      <!-- 头部 -->
      <el-header>
        <el-row>
          <el-col :span="12">
           
              <img src="../assets/pic@2x/headlogo@2x.png" alt class="logo" @click="toIndex">
         
          </el-col>
          <el-col :span="12">
            <el-menu
              :default-active="activeIndex"
              class="el-menu-demo"
              mode="horizontal"
              @select="handleSelect"
              active-text-color="orange"
              text-color="white"
              background-color="#171c61"
            >
               <el-menu-item index="2" :span="3" @click='toUser'>个人账户</el-menu-item>
              <el-menu-item index="4" :span="3">简体中文</el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-header>
    </el-container>
    <!-- 默认布局有padding，写在container外，会100%； -->
    <img src="../assets/pic@2x/orderbgc@2x.png" class="topPic"> 
  </div>
</template>
<script>
export default {
  data() {
    return {
      activeIndex: "1"
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
     toUser(){
       this.$router.push(`/user/${this.$route.params.id}`)
    },
     //登陆完调回购买页
    toIndex(){
       this.$router.push(`/loginIndex/${this.$route.params.id}`)
    }
  }
};
</script>
<style lang="less" scoped>
//头

.el-header {
  background-color: #171c61;
  color: #333;
  text-align: center;
  height: 137px !important;
  overflow: hidden;
  //必须提高层级，不然被图片盖住不能点击
  z-index: 999;
  .el-row {
    .el-menu.el-menu--horizontal {
      border-bottom: none;
    }
    .logo {
      float: left;
      margin-left: 10%;
      margin-top: 31px;
    }
    .el-menu {
      margin-top: 31px;
      background-color: #171c61;
      color: #fff;
      text-align: center;
      margin-left: 13%;
      font-weight: 300;
      .el-menu-item {
        font-size: 26px;
        margin-right: 6%;
        font-weight: 300;
      }
      .el-submenu {
        margin-right: 6%;
       
        /deep/.el-submenu__title {
          font-size: 26px;
          i {
            font-size: 26px;
            font-weight: 300;
            color: #fff;
          }
        }
      }
    }
  }
}
.topPic {
  position: absolute;
  top: 20px;
  left: 0;
  width: 100%;  
  overflow: hidden;
}
</style>