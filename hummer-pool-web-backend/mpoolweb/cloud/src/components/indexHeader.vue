<template>
  <el-container>
    <!-- 头部 -->
    <el-header >
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
          <el-submenu index="1" :span="3">
              <template slot="title">账户面板</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
              <el-menu-item index="2-3">选项3</el-menu-item>
              <el-submenu index="2-4">
                <template slot="title" >选项4</template>
                <el-menu-item index="2-4-1">选项1</el-menu-item>
                <el-menu-item index="2-4-2">选项2</el-menu-item>
                <el-menu-item index="2-4-3">选项3</el-menu-item>
              </el-submenu>
              <el-menu-item index="2-5" @click="logout">退出登录</el-menu-item>
            </el-submenu>
             <el-menu-item index="2" :span="3" @click='toIndex'>购买算力</el-menu-item>
            <el-menu-item index="4" :span="3">简体中文</el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </el-header>
  </el-container>
</template>
<script>

import {
  logout
} from "@/api/login.js";
export default {
  data() {
    return {
      activeIndex: "1",
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      // console.log(key, keyPath);
    },
    //登陆完调回购买页
    toIndex(){
       this.$router.push(`/loginIndex/${this.$route.params.id}`)
    },
    logout() {
      logout().then(res => {
          if (res.data.msg == "ok") { 
            // this.$router.push(`/loginIndex`)
          }
      })
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
  height:137px !important;
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
      margin-top: 45px;
      background-color: #171c61;
      color: #fff;
      text-align: center;
      margin-left: 15%;
      font-weight:300;
      .el-menu-item {
        font-size: 26px;
        margin-right: 6%;
        font-weight:300;
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
</style>