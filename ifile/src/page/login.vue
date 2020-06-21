<template>
  <div id="login">
    <el-container style="height:100%;width:100%">
      <el-header>
        <div id="header-logo">
          <img id="logo" src="../../public/cloud_host.png" />
        </div>
      </el-header>
      <el-main style="height:80%;width:100%">
        <div class="login-box">
          <div class="login-box-title">登录</div>
          <el-input
            id="input-username"
            v-model="username"
            placeholder="请输入账户"
            @input="updateView($event)"
          ></el-input>
          <div style="height:20px"></div>
          <el-input
            id="input-password"
            v-model="password"
            placeholder="请输入密码"
            show-password
            @input="updateView()"
          ></el-input>
          <div style="height:30px"></div>
          <div id="login-button">
            <el-button
              style="width:100%;height:40px"
              size="medium"
              type="primary"
              @click="login()"
            >登录</el-button>
          </div>
        </div>
      </el-main>
      <el-footer></el-footer>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {
  },
  date () {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    login () {
      var that = this;
      let data = { "username": this.username, "password": this.password };
      this.$axios
        .post("http://localhost:8000/api/user/login", data)
        .then(response => {
          that.$emit('loginback', response.data);
          that.$message({
            message: '登陆',
            type: 'success'
          });
        })

    },
    updateView () {
      this.$forceUpdate()
    }
  }

}
</script>

<style>
#login {
  height: 100%;
  width: 100%;
  background-color: #4793f788;
}
.login-box {
  float: right;
  width: 250px;
  height: 280px;
  min-width: 250px;
  min-height: 280px;
  border-radius: 5px;
  padding: 20px;
  margin-top: 10%;
  margin-right: 10%;
  background-color: rgb(253, 253, 253);
}
.login-box-title {
  font-family: "Helvetica Neue";
  font-size: 18px;
  margin-bottom: 10px;
}
</style>
