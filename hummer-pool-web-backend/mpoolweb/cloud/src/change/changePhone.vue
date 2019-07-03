<template>
  <span>
    <el-button round size="mini" @click="dialogPhoneMethods">修改</el-button>
    <el-dialog title="收货地址" :visible.sync="dialogPhone" center>
      <div slot="title" class="header-title">
        <img src="../assets/pic@2x/shouji@2x.png" alt>
        <div style="font-weight:300;color:rgba(0,0,0)">手机号修改</div>
      </div>
      <el-form ref="phoneForm" :model="form" :rules="rule">
        <el-form-item label="手机号码" :label-width="formLabelWidth" prop="num">
          <el-input v-model.trim="form.num" autocomplete="off" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="手机验证码" :label-width="formLabelWidth" prop="check">
          <el-input v-model.trim="form.check" autocomplete="off" placeholder="请输入手机验证码" class="phoneCheck"></el-input>
          <el-button :class="{getCheck:true}" @click="sendPhoneCode">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="height:100px;">
        <el-button type="primary" @click="submit" style="float:none;width:50%">确 定</el-button>
      </div>
    </el-dialog>
  </span>
</template>
<script>
import {editNumCode,editNum} from '@/api/goods.js'
export default {
  data() {
    //手机号码
    var validatePhone = function(rule, value, callback) {
      const reg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (value == "" || value == undefined || value == null) {
        callback();
      } else {
        if (!reg.test(value)) {
          callback(new Error("请输入正确的手机号码"));
        } else {
          callback();
        }
      }
    };
    // 验证码自定义验证规则
    const validateVerifycode = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入验证码"));
      }else{
        callback();
      }
    };
    return {
      dialogPhone: false,
      form: {
        num: "",
        check: ""
      },
      formLabelWidth: "120px",
      rule: {
        num: [{ required: true, validator: validatePhone, trigger: "blur" }],
        check: [
          {
            required: true,
            trigger: "blur",
            validator: validateVerifycode
          }
        ]
      },
    };
  },
  methods: {
    dialogPhoneMethods() {
      this.dialogPhone = !this.dialogPhone;
    },
    submit() {
      editNum(this.form).then(res=> {
        if(res.data.msg == 'ok') { 
          this.dialogPhone = !this.dialogPhone;
        }
      })
    },
    sendPhoneCode() {
      editNumCode().then(res=> {})
    }
  }
};
</script>
<style lang="less" scoped>
.phoneCheck {
  width: 70%;
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
  .getCheck {
    width: 30%;
    position: absolute;
    margin: 0;
    float: none;
    background: rgba(238, 238, 238, 1);
    color: #171c61;
  }
}
</style>
