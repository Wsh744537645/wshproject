<template>
  <div class="box">
    <div>
      <div :style="setBackground" class="all"></div>
      <div class="container registerBox">
        <div class="row">
          <div class="col-md-5 col-md-offset-3 register">
            <img src="../assets/pic@2x/hummer@2x.png" class="pic">
            <el-tabs v-model="activeName" @tab-click="handleClick" type="border-card">
              <el-tab-pane label="手机注册" name="first">
                <el-form ref="ruleForm" :model="form" :rules="rule">
                  <el-form-item label prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入用户名" @change="hasName"></el-input>
                  </el-form-item>
                  <el-form-item label prop="password">
                    <el-input v-model.trim="form.password" placeholder="请输入密码" show-password></el-input>
                  </el-form-item>
                  <el-form-item label prop="repassword">
                    <el-input v-model.trim="form.repassword" placeholder="请输入再次确认密码" show-password></el-input>
                  </el-form-item>
                  <el-form-item label prop="num">
                    <el-input v-model.trim="form.num" placeholder="请输入注册手机号码" @change="hasNum"></el-input>
                  </el-form-item>
                  <el-form-item label prop="check">
                    <el-input v-model.trim="form.check" placeholder="请输入手机验证码" class="check"></el-input>
                    <span class="btn" @click="sendPhoneCode">获取验证码</span>
                  </el-form-item>
                  <h5>注册表示你已经同意用户协议</h5>
                  <span class="registerBtn" @click="phoneRegister('ruleForm')">手机注册</span>
                  <span class="link">
                    <router-link to="/">已有账户？登录</router-link>
                  </span>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="邮箱注册" name="second">
                <el-form ref="EmailForm" :model="form" :rules="rule">
                  <el-form-item label prop="name">
                    <el-input v-model.trim="form.name" placeholder="请输入用户名" @change="hasName"></el-input>
                  </el-form-item>
                  <el-form-item label prop="password">
                    <el-input v-model.trim="form.password" placeholder="请输入密码" show-password></el-input>
                  </el-form-item>
                  <el-form-item label prop="repassword">
                    <el-input v-model.trim="form.repassword" placeholder="请输入再次确认密码" show-password></el-input>
                  </el-form-item>
                  <el-form-item label prop="email">
                    <el-input v-model.trim="form.email" placeholder="请输入注册邮箱" @change="hasEmail"></el-input>
                  </el-form-item>
                  <el-form-item label prop="check">
                    <el-input v-model.trim="form.check" placeholder="请输入邮箱验证码" class="check"></el-input>
                    <span class="btn" @click="sendEmailCode" ref="code">获取验证码</span>
                  </el-form-item>
                  <h5>注册表示你已经同意用户协议</h5>
                  <span class="registerBtn" @click="EmailRegister('EmailForm')">邮箱注册</span>
                  <span class="link">
                    <router-link to="/">已有账户？登录</router-link>
                  </span>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {
  emailCode,
  phoneCode,
  sms,
  email,
  checkUsername,
  checkPhone,
  checkMail
} from "@/api/register.js";
export default {
  data() {
    //判断两次密码
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    }; 
    //手机号码
    var validatePhone = function(rule, value, callback) {
      const reg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (value == "" || value == undefined || value == null) {
        callback();
      } else {
        if (!reg.test(value)) {
          callback(new Error("请输入正确的电话号码"));
        } else {
          callback();
        }
      }
    };
    // 验证码自定义验证规则
    const validateVerifycode = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入验证码"));
      } else if (value !== this.form.check) {
        console.log("validateVerifycode:", value);
        callback(new Error("验证码不正确!"));
      } else {
        callback();
      }
    };
    /* 是否邮箱*/
    const validateEMail = function(rule, value, callback) {
      const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
      if (value == "" || value == undefined || value == null) {
        callback(new Error("请输入邮箱"));
      } else {
        if (!reg.test(value)) {
          callback(new Error("请输入正确的邮箱地址"));
        } else {
          callback();
        }
      }
    };
    return {
      // 节流阀
      flag: true,
      //正则
      form: {
        name: "",
        password: "",
        repassword: "",
        num: "",
        check: "",
        email: ""
      },
      rule: {
        name: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 2, max: 10, message: "长度在 2 到 10 个字符" },
          { pattern: /^[a-zA-Z]+$/, message: "用户名只能为英文" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_]+$)(?![a-z0-9]+$)(?![a-z\W_]+$)(?![0-9\W_]+$)[a-zA-Z0-9\W_]{8,30}$/,
            message: "密码为数字，小写字母，大写字母，长度为 8 - 30位"
          }
        ],
        repassword: [
          { required: true, validator: validatePass2, trigger: "blur" }
        ],
        num: [{ required: true, validator: validatePhone, trigger: "blur" }],
        check: [
          {
            required: true,
            trigger: "blur",
            validator: validateVerifycode
          }
        ],
        email: [{ required: true, validator: validateEMail, trigger: "blur" }]
      },
      //当前注册页索引，是手机注册还是邮箱
      index: 0,
      activeName: "first",
      setBackground: {
        backgroundImage: "url(" + require("../assets/pic@2x/register.jpg") + ")",
        backgroundPosition: "center",
        width: "100%",
        height: "937px",
        backgroundSize: "cover"
      }
    };
  },
  //点击切换
  methods: {
    handleClick(tab) {
      console.log(tab.index);
      this.index = tab.index;
    },
    //验证用户名是否存在
    hasName() {
      checkUsername(this.form.name).then(res => {
        console.log(res, 7755);
        if (res.data.code == "mpool.admin.username.exists") {
          (this.form.name = ""),
            this.$notify.error({
              message: "用户名已被占用，请重新输入"
            });
        }
      });
    },
    //验证手机号是否存在
    hasNum() {
      checkPhone(this.form.num).then(res => {
        console.log(res, 33333);
        if (res.data.code == "mpool.admin.username.exists") {
          (this.form.num = ""),
            this.$notify.error({
              message: "手机号已经注册，请更换手机"
            });
        }
      });
    },
    //验证邮箱是否存在
    hasEmail() {
      checkMail(this.form.email).then(res => {
        console.log(res, 1122);
        if (res.data.code == "mpool.admin.mail.exists") {
          (this.form.email = ""),
            this.$notify.error({
              message: "邮箱已被占用，请重新输入"
            });
        }
      });
    },
    //手机验证码
    sendPhoneCode() {
      if (this.form.num && this.form.num != "") {
        phoneCode(this.form.num)
          .then(res => {
            if (res.data.msg == "ok") {
              // this.form.check = res.data.data;
              console.log(res.data.data,8899); 
            } else {
              this.$notify.error({
                title: "错误",
                message: "验证码错误"
              });
            }
          })
          .catch(err => {
            console.log(err);
          });
      } else {
        this.$notify.error({
          title: "错误",
          message: "请先输入手机号"
        });
      }
    },
    //点击手机注册
    phoneRegister(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // console.log(this.form.check,8899);
          sms({
            username: this.form.name,
            password: Base64.encode(this.form.password),
            share_phoneCode: this.form.check,
            share_phone: this.form.num
          }).then(res => {
            this.$notify({
              message: "注册成功",
              type: "success"
            });
            setTimeout(() => {
               this.$router.push("/");
            }, 1000);
          });
          
        } else {
          // 如果不符合验证
          this.$notify.error({
            title: "错误",
            message: "请填写完整信息"
          });
        }
      });
    },

    // 点击邮箱注册
    EmailRegister(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          email({
            username: this.form.name,
            password: Base64.encode(this.form.password),
            emailCode: this.form.check,
            email: this.form.email
          }).then(res => {});
        } else {
          // 如果不符合验证
          this.$notify.error({
            title: "错误",
            message: "请填写完整信息"
          });
        }
      });
    },
    // 点击获取邮箱验证码
    sendEmailCode() {
      if (this.form.email && this.form.email != "") {
        emailCode(this.form.email)
          .then(res => {
            if (res.data.msg == "ok") {
              this.form.check = res.data.data;
            } else {
              this.$notify.error({
                title: "错误",
                message: "验证码错误"
              });
            }
          })
          .catch(err => {
            console.log(err);
          });
      } else {
        this.$notify.error({
          title: "错误",
          message: "请先输入邮箱"
        });
      }
    }
  }
};
</script>
<style lang="less" scoped>
/deep/.el-tabs__nav .is-top {
  width: 229px;
  font-weight: 400;
  // color:rgba(23,28,97,1);
  font-size: 28px;
}
/deep/.el-tabs--border-card > .el-tabs__header .el-tabs__item {
  color: #171c61;
  height: 77px;
  line-height: 77px;
}
/deep/.el-tabs__content {
  height: 740px;
  padding: 46px;
}
.all{
   position: absolute;
  top: 0;
}
.registerBox {
 
  .row {
    .register {
      margin-top: 20px;
      text-align: center;
      .pic {
        margin-bottom: 32px;
      }
      .el-input {
        height: 64px;
        margin-bottom: 15px;
      }
      /deep/.el-input__inner {
        height: 64px;
        line-height: 64px;
        font-size: 18px;
        border-radius: 5px;
        background: rgba(238, 238, 238, 1);
      }
      .check {
        width: 55%;
        float: left;
      }
      .btn {
        width: 151px;
        height: 63px;
        line-height: 43px;
        margin-left: 10px;
        font-size: 18px;
        font-weight: 300;
        color: rgba(255, 255, 255, 1);
        background-color: #171c61;
      }
      h5 {
        //   float: left;
        margin-top: 20px;
        text-align: left;
        font-weight: 300;
        font-size: 14px;
        color: rgba(0, 0, 0, 1);
        margin-bottom: 21px;
      }
      .registerBtn {
        display: block;
        width: 100%;
        height: 63px;
        line-height: 63px;
        font-size: 18px;
        font-weight: 300;
        color: rgba(255, 255, 255, 1);
        background-color: #171c61;
        border-radius: 5px;
        margin-bottom: 10px;
        border: none;
      }
      .link {
        display: flex;
        justify-content: space-between;
      }
      .up,
      .down {
        position: absolute;
        right: 60px;
        z-index: 999;
        cursor: pointer;
      }
      .up {
        top: 150px;
      }
      .down {
        top: 230px;
      }
    }
  }
}
</style>
