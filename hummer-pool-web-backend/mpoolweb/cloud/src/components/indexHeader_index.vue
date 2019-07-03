<template>
  <div class="all" :style="setBackground ">
    <router-link to="/">
      <img src="../assets/pic@2x/headlogo@2x.png" alt class="logo">
    </router-link>
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
            <li v-if="flag==true">关于</li>
            <router-link v-else :to="{name:'userindex',params:{id:this.id}}" class="first" tag="li">账户信息</router-link>
            <li>简体中文</li>
          </ul>
        </div>
      </div>
      <!-- 登录 -->
      <div class="row">
        <div class="col-md-5" :style="hummerLogin" >
          <div class="login col-md-12" v-if="flag==true">
            <img src="../assets/pic@2x/hummer@2x.png" alt class="hummer">
            <div class="user col-md-12">
              <img src="../assets/pic@2x/user@2x.png" alt class="picName">
              <input type="text" placeholder="请输入用户名" class="name" v-model.trim="username">
            </div>
            <div class="password col-md-12">
              <img src="../assets/pic@2x/password@2x.png" alt class="picWord">
              <input type="password" placeholder="请输入密码" autocomplete="off" class="pass"  v-model.trim="userpswd">
            </div>
            <el-button type="primary" class="btn col-md-12" @click="changePath">登 录</el-button>
            <span class="forget"><router-link to="/reset" class="register">忘记密码？</router-link></span>
            <router-link to="/register" class="register">注册账号</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {getUserLoginModel,login} from "@/api/login.js"
export default {
  data() {
    return {
      //路由跳转的id，通过登录接口的获取的ID来判断
     flag:true,
      id: 1,
      activeIndex: "1",
      username:'',
      userpswd:'',
      //背景图样式
      setBackground: {
        backgroundImage:
          "url(" + require("../assets/pic@2x/indexLogo.jpg") + ")",
        backgroundPosition: "center",
        width: "100%",
        height: "937px"
        // backgroundSize: "length"
      },
      hummerLogin: {
        float: "right"
      }
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    //按ID登录
    changePath() {
      if (this.username&&this.userpswd) {
         login({username:this.username,password:Base64.encode(this.userpswd)}).then(res=>{
            // console.log(res,8899);
            if(res.data.code=='0'){
              this.$store.commit('userData',res.data.data)
              this.id=res.data.data.userId;
              this.$router.push(`/user/${this.id}`);
            } else{
              this.$notify.error({
              title: '用户名或密码错误',
              message: '请输入合法用户名和密码'
        });
            }
          }) 
      } else {
         this.$notify.error({
          message: '请输入用户名和密码'
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
.all {
  .logo {
    z-index: 999;
    position: absolute;
    top: 31px;
    left: 80px;
  }
  //头
  .container {
    color: #333;
    text-align: center;
    height: 1080px !important;
    padding-top: 57px;
    .header {
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
              }
            }
          }
        }
      }
    }
    //登陆框
    .login {
      position: absolute;
      top: 200px;
      right: 0 !important;
      padding: 0;
      .hummer {
        margin-bottom: 36px;
      }
      //密码和用户名共同的样式
      .user,
      .password {
        position: relative;
        .name,
        .pass {
          height: 100px;
          border: 0;
          box-sizing: border-box;
          background: rgba(255, 255, 255, 0.5);
          border-radius: 6px;
          font-size: 25px;
          font-weight: 300;
          &::-webkit-input-placeholder {
            color: rgba(255, 255, 255);
            font-size: 25px;
            font-weight: 300;
            line-height: 100px;
          }
          &::-moz-placeholder {
            color: rgba(255, 255, 255);
            font-size: 25px;
            font-weight: 300;
          }
          &:-moz-placeholder {
            /* Mozilla Firefox 4 to 18 */
            color: rgba(255, 255, 255);
            font-size: 25px;
            font-weight: 300;
          }
          &:-ms-input-placeholder {
            /* Internet Explorer 10-11 */
            color: rgba(255, 255, 255);
            font-size: 25px;
            font-weight: 300;
          }
        }
      }

      .user,
      .password {
        padding: 0;
        //user框 s
        .name {
          border-bottom: 1px solid rgba(255, 255, 255, 0.7);
          border-bottom-left-radius: 0;
          border-bottom-right-radius: 0;
          width: 100%;
          padding-left: 100px;
        }
        //密码框
        .pass {
          margin-bottom: 36px;
          border-top-left-radius: 0;
          border-top-right-radius: 0;
          width: 100%;
          padding-left: 100px;
          border-bottom: 1px solid rgba(255, 255, 255, 0.7);
        }
        .picName,
        .picWord {
          position: absolute;
          top: 30%;
          left: 6%;
          border-radius: 50%;
          z-index: 999;
          font-size: 25px;
          font-weight: 300;
        }
        .picWord {
          top: 22%;
        }
      }

      //按钮
      .btn {
        font-size: 30px;
        font-weight: 300;
        color: rgba(254, 254, 255);
        opacity: 0.7;
        margin-bottom: 20px;
      }
      .forget,
      .register {
        color: #fff;
        opacity: 0.8;
        cursor: pointer;
        &:hover {
          color: orange;
        }
      }
      .forget {
        float: left;
      }
      .register {
        float: right;
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
}
</style>