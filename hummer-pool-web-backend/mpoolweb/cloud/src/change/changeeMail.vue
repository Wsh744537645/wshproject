<template>
  <span>
    <el-button round size="mini" @click="dialogEmailMethods">修改</el-button>
    <!-- 邮箱验证弹出框 -->
    <el-dialog title="邮箱" :visible.sync="dialogEmail" center>
      <div slot="title" class="header-title">
        <img src="../assets/pic@2x/youxiang@2x.png" alt>
        <div style="font-weight:300;color:rgba(0,0,0)">邮箱绑定</div>
      </div>
      
        <el-form ref="EmailForm" :model="form" :rules="rule">
          <el-form-item label="邮箱地址" :label-width="formLabelWidth" prop="email">
            <el-input v-model.trim="form.email" autocomplete="off" placeholder="请输入邮箱地址"></el-input>
          </el-form-item>
          <el-form-item label="邮箱验证码" :label-width="formLabelWidth" prop="check">
            <el-input v-model.trim="form.check" autocomplete="off" placeholder="请输入手机验证码" class="phoneCheck"></el-input>
            <el-button :class="{getCheck:true}" @click="sendCode">获取验证码</el-button>
          </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer" style="height:100px;">
        <el-button type="primary" @click="submit"
          style="float:none;width:40%;margin-right:0;">确 定 修 改</el-button>
      </div>
    </el-dialog>
  </span>
</template>
<script>
import {editEmailCode,editEmail} from '@/api/goods.js'
export default {
  data() {
    // 验证码自定义验证规则
    const validateVerifycode = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入验证码"));
      }else {
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
      dialogEmail: false,
      form: {
        email: "",
        check: ""
      },
      formLabelWidth: "120px",
      rule: {
        check: [
          {
            required: true,
            trigger: "blur",
            validator: validateVerifycode
          }
        ],
        email: [{ required: true, trigger: "blur", validator: validateEMail }]
      },
    };
  },
  methods: {
    dialogEmailMethods() {
      this.dialogEmail = !this.dialogEmail;
    },
    submit() {
      editEmail(this.form).then(res=> {
        if(res.data.msg == 'ok') { 
          this.dialogEmail = !this.dialogEmail;
        }
      })
    },
    sendCode() {
      editEmailCode().then(res=> {})
    }
  }
};
</script>
<style lang="less" scoped>
.el-button {
  float: right;
  margin: 20px 30px 0 0;
  background: #171c61;
  color: #fff;
}
.phoneCheck {
  width: 70%;
}
.getCheck {
  width: 30%;
  position: absolute;
  margin: 0;
  float: none;
  background: rgba(238, 238, 238, 1);
  color: #171c61;
}
</style>

