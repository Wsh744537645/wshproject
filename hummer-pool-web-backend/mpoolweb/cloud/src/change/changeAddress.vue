<template>
  <span>
    <el-button round size="mini" @click="dialogAddressMethods">修改</el-button>
    <!-- 修改密码弹出框 -->
    <el-dialog title="地址" :visible.sync="dialogAddress" center>
      <div slot="title" class="header-title">
        <img src="../assets/pic@2x/address@2x.png" alt>
        <div style="font-weight:300;color:rgba(0,0,0)">地址修改</div>
      </div>
      <el-form :model="form"  :rules="rule">
        <el-form-item label="收币地址修改" :label-width="formLabelWidth" prop="address">
          <el-input v-model="form.address" autocomplete="off" placeholder="请输入收货地址"></el-input>
        </el-form-item>
        <el-form-item label="验证码" :label-width="formLabelWidth" prop="check">
          <el-input v-model.trim="form.check" autocomplete="off" placeholder="请输入验证码" style="width:70%"></el-input>
          <el-button :class="{getCheck:true}" @click="sendCode">获取验证码</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="height:100px;">
        <el-button type="primary" @click="submit" style="float:none;width:40%;margin-right:0;">确 定 修 改</el-button>
      </div>
    </el-dialog>
  </span>
</template>
<script>
import {sendAdrCode,editAdrEmail,editAdrPhone} from '@/api/goods.js'
export default {
  data() {
    // 验证码自定义验证规则
    const validateVerifycode = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入验证码"));
      } else {
        callback();
      }
    };
    
    const validateAdr = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入地址"));
      } else {
        callback();
      }
    };
    return {
      dialogAddress: false,
      form: {
        address: "",
        code: ''
      },
      formLabelWidth: "120px",
      rule: {
        address: [
          {
            required: true,
            trigger: "blur",
            validator: validateAdr
          }
        ],
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
    dialogAddressMethods() {
      this.dialogAddress = !this.dialogAddress;
    },
    sendCode() {
      sendAdrCode().then(res=>{
        this.type = res.data.data.type
      })
    },
    submit() {
      if(this.type == 1) {
        editAdrPhone(this.form).then(res=> {
          if(res.data.msg == 'ok') { 
            this.dialogAddress = !this.dialogAddress;
          }
        })
      }else{
        editAdrEmail(this.form).then(res=> {
          if(res.data.msg == 'ok') { 
            this.dialogAddress = !this.dialogAddress;
          }
        })
      }
    },
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

