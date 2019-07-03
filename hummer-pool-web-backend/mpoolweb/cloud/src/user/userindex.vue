<template>
  <div class="container">
    <div class="row">
      <div class="left col-md-2">
        <div class="left_top">
          <div class="pic">
            <el-upload
              class="avatar-uploader"
              action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
          <p>UID:111111111</p>
        </div>
        <!-- 左侧导航条 -->
        <el-col :span="24" class="nav">
          <el-menu
            :default-active="$route.path"
            class="el-menu-vertical-demo"
             @select="handleSelect"
            router
            active-text-color="#171C61"
          >
            <el-menu-item :index="`/user/${this.fatherId.userId}/1`">
              <i class="el-icon-s-platform"></i>
              <span slot="title">云算力面板</span>
            </el-menu-item>
            <el-menu-item :index="`/user/${this.fatherId.userId}/2`">
              <i class="el-icon-s-order"></i>
              <span slot="title">合约记录</span>
            </el-menu-item>
            <el-menu-item :index="`/user/${this.fatherId.userId}/detail`">
              <i class="el-icon-document"></i>
              <span slot="title">账单明细</span>
            </el-menu-item>
            <el-menu-item :index="`/user/${this.fatherId.userId}/4`">
              <i class="el-icon-setting"></i>
              <span slot="title">账户设置</span>
            </el-menu-item>
          </el-menu>
        </el-col>
      </div>
      <router-view></router-view>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      imageUrl: "",
      fatherId:[],
    };
  },
 created(){
   //获取到不用用户的不同ID
   this.fatherId=JSON.parse(sessionStorage.getItem('targetId'))
  //  console.log(this.$route.fullPath);
 },
 mounted(){
  
 },
  methods: {
     handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      // 上传图片
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    }
  }
};
</script>
<style lang="less" scoped>
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 97px;
  height: 97px;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
/deep/.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 97px;
  height: 97px;
  line-height: 97px;
  text-align: center;
}
/deep/.avatar {
  width: 97px;
  height: 97px;
  display: block;
}
.left {
  margin-left: 15px;
  margin-bottom: 20px;
  margin-right: 18px;
  padding: 0;
  .left_top {
    background-color: #fff;
    width: 100%;
    padding: 26px 0;
    text-align: center;
    margin-bottom: 20px;
    .pic {
      width: 97px;
      height: 97px;
      border-radius: 50%;
      background: rgba(210, 210, 210, 1);
      margin: 0 auto;
      margin-bottom: 16px;
    }
    p {
      font-size: 17px;
      font-weight: 400;
    }
  }
}
.nav {
  height: 534px;
  background-color: #fff;
  border-radius: 5px;
  ul {
    border-right: none;
    li {
      font-size: 19px;
      text-align: center;
      border-bottom: 1px solid #eee;
    }
    .is-active {
      border-left: 2px solid #171c61;
    }
  }
}
</style>
