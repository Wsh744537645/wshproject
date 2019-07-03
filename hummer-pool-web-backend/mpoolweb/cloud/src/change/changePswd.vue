<template>
  <span>
    <el-button round size="mini" @click="dialogPswdMethods">修改</el-button>
          <!-- 修改密码弹出框 -->
              <el-dialog title="密码" :visible.sync="dialogPswd" center>
              <div slot="title" class="header-title">
                <img src="../assets/pic@2x/mima@2x.png" alt>
                <div style="font-weight:300;color:rgba(0,0,0)">密码修改</div>
              </div>
              <el-form :model="form" :rules="rule">
                <el-form-item label="新密码" :label-width="formLabelWidth" prop="password">
                  <el-input v-model="form.password" autocomplete="off" password placeholder="请输入新密码" show-password></el-input>
                </el-form-item>
                 <el-form-item label="确认新密码" :label-width="formLabelWidth" prop="repassword">
                  <el-input v-model="repassword" autocomplete="off" placeholder="请确认新密码" show-password></el-input>
                </el-form-item>
        <el-form-item label="验证码" :label-width="formLabelWidth" prop="check">
          <el-input v-model.trim="form.check" autocomplete="off" placeholder="请输入验证码" style="width:70%"></el-input>
          <el-button :class="{getCheck:true}" @click="sendPswCode">获取验证码</el-button>
        </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer" style="height:100px;">
                <el-button type="primary" @click="submit" style="float:none;width:40%;margin-right:0;">确 定 修 改</el-button>
              </div>
            </el-dialog>
  </span>
</template>
<script>
import {sendPswCode,editPswPhone, editPswEmail} from '@/api/goods.js'
export default {
  data() {
    //判断两次密码
    var validatePass2 = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    }; 
    // 验证码自定义验证规则
    const validateVerifycode = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入验证码"));
      } else {
        callback();
      }
    };
    return {
      dialogPswd: false,
      form: {
        password: '',
        check: ''
      },
      repassword: '',
      formLabelWidth: "120px",
      rule: {
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
        check: [
          {
            required: true,
            trigger: "blur",
            validator: validateVerifycode
          }
        ]
      }
    };
  },
   methods: {
    dialogPswdMethods() {
      this.dialogPswd = !this.dialogPswd;
    },
    sendPswCode() {
      sendPswCode().then(res=> {
        this.type = res.data.data.type
      })
    },
    submit() {
      if(this.type = 1){
        editPswPhone(this.form).then(res=> {
          if(res.data.msg == 'ok') { 
            this.dialogPhone = !this.dialogPhone;
          }
        })
      }else{
        editPswEmail(this.form).then(res=> {
          if(res.data.msg == 'ok') { 
            this.dialogPhone = !this.dialogPhone;
          }
        })
      }
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

.getCheck {
  width: 30%;
  position: absolute;
  margin: 0;
  float: none;
  background: rgba(238, 238, 238, 1);
  color: #171c61;
}

</style>

