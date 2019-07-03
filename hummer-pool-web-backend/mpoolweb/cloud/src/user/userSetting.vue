<template>
  <div class="col-md-9 box">
    <div class="top col-md-12">
      <div class="top_title col-md-12">
        <div class="list col-md-2">&nbsp;&nbsp;账户管理</div>
      </div>
      <div class="col-md-12 top_bottom">
        <!-- //左边列表 -->
        <div class="top_bottom_left col-md-3">
          <p>UID</p>
          <p v-for="(item,i) in plists" :key="i">{{item.name}}</p>
        </div>
        <!-- 右边列表 -->
        <div class="top_bottom_right col-md-9">
          <P>{{user.uid}}</P>
          <P>{{user.phone}}
            <!-- 手机弹出框 -->
            <changePhone></changePhone>
          </P>
          <P>
            {{user.mail}}
            <!-- 邮箱弹出框 -->
            <changeEmail></changeEmail>
          </P>
          <P>
            **********
            <!-- 密码弹出框 -->
            <changePwsd></changePwsd>
          </P>
          <P>
            {{user.walletAddress}}
            <!-- 地址弹出框 -->
            <changeAddress></changeAddress>
          </P>
        </div>
      </div>
    </div>
    <div class="bottom col-md-12">
      <div class="bottom_title col-md-12">
        <div class="list col-md-2">&nbsp;&nbsp;到期提醒</div>
      </div>
      <div class="col-md-12 bottom_bottom">
        <!-- //左边列表 -->
        <div class="bottom_bottom_left col-md-9">
          <p v-for="(item,i) in paylists" :key="i">{{item.name}}</p>
        </div>
        <!-- 右边列表 -->
        <div class="bottom_bottom_right col-md-3">
          <p class="email">
            <el-radio-group v-model="radio" @change="editRadio">
              <el-radio :label="1">邮件</el-radio>
              <el-radio :label="2">手机</el-radio>
            </el-radio-group>
          </p>
          
          <p>
            <el-switch v-model="value1" active-color="#171c61" inactive-color="#ccc" @change="editBefore7"></el-switch>
          </p>
          <p>
            <el-switch v-model="value2" active-color="#171c61" inactive-color="#ccc" @change="editBefore1"></el-switch>
          </p>
          <p>
            <el-switch v-model="value3" active-color="#171c61" inactive-color="#ccc" @change="editExpired"></el-switch>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {getUserInfo, getExpireInfo, editExpire} from '@/api/goods.js'
import changeAddress from "../change/changeAddress.vue";
import changePhone from "../change/changePhone.vue";
import changeEmail from "../change/changeEmail.vue";
import changePwsd from "../change/changePswd.vue";
export default {
  data() {
    return {
      user: {},
      form: {
        name: ""
      },
      formLabelWidth: "120px",
      //邮件选择器
      radio: 1,
      value1: false,
      value2: false,
      value3: false,
      activeName: "first",
      //账户管理左
      plists: [
        {
          name: "手机绑定"
        },
        {
          name: "邮箱绑定"
        },
        {
          name: "登录密码"
        },
        {
          name: "收币地址"
        }
      ],
      //到期提醒左
      paylists: [
        {
          name: "提醒类型"
        },
        {
          name: "套餐到期前7天提醒我"
        },
        {
          name: "套餐到期前一天提醒我"
        },
        {
          name: "订单失效后提醒我"
        }
      ],
      data: {}
    };
  },
  components: {
    changeAddress,
    changePhone,
    changeEmail,
    changePwsd
  },
  created() {
    getUserInfo().then(res=> {
      const rep = res.data.data
      this.user = { 
        uid: rep.uid,
        phone: rep.phone? rep.phone: '未绑定',
        mail: rep.mail? rep.mail: '未绑定',
        walletAddress: rep.walletAddress
      }
    })
    getExpireInfo().then(res=>{
        this.data = res.data.data 
        this.value1 = this.data.before7 == 1 ? true: false
        this.value2 = this.data.before1 == 1 ? true: false
        this.value3 = this.data.expired == 1 ? true: false
        this.radio = this.data.emailState == 1? 1:2
    })
  },
  methods: {
    editRadio(){
      this.data.emailState = this.radio == 1 ? 1: 0
      this.data.phoneState = this.radio == 2 ? 1: 0
      editExpire(this.data).then(res=>{})
    },
    editBefore1(){
      this.data.before1 = this.value2 == true ? 1: 0
      editExpire(this.data).then(res=>{})
    },
    editBefore7(){
      this.data.before7 = this.value1 == true ? 1: 0
      editExpire(this.data).then(res=>{})
    },
    editExpired(){
      this.data.expired = this.value3 == true ? 1: 0
      editExpire(this.data).then(res=>{})
    }
  }
};
</script>
<style lang="less" scoped>
.box {
  background-color: #fff;
  padding: 36px 59px 45px 45px;
  overflow: hidden;
  .top,
  .bottom {
    font-size: 22px;
    margin-bottom: 45px;
    .top_title,
    .bottom_title {
      border-bottom: 1px solid rgba(210, 210, 210, 1);
      margin-bottom: 43px;
      color: rgba(23, 28, 97, 1);
      .list {
        border-bottom: 1px solid #171c61;
        padding-bottom: 6px !important;
        padding: 0;
      }
    }
    .top_bottom,
    .bottom_bottom {
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
        font-size: 18px;
        font-weight: 300;
      }
      /deep/a {
        cursor: pointer;
      }
      .top_bottom_left,
      .top_bottom_right,
      .bottom_bottom_left,
      .bottom_bottom_right {
        padding: 0;
      }
      .top_bottom_right {
        border-left: 1px solid #fff;
        text-align: left;
        .el-dialog {
          width: 45%;
        }

        /deep/.el-input__inner {
          background-color: rgba(238, 238, 238, 1);
        }
        p {
          padding-left: 70px;
          .el-button {
            float: right;
            margin: 20px 30px 0 0;
            background: #171c61;
            color: #fff;
          }
        }
      }
    }
    //不同点
    .bottom_bottom {
      text-align: left;
      .bottom_bottom_left {
        p {
          padding-left: 55px;
        }
      }
      .bottom_bottom_right {
        .email {
          /deep/.el-radio__label {
            font-size: 18px;
            font-weight: 300;
            color: rgba(0, 0, 0, 1);
          }
        }
      }
    }
  }
}
</style>
