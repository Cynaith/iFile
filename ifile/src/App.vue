<template>
  <div id="app">
    <div v-if="!islogin">
      <login v-on:loginback="loginback"></login>
    </div>
    <div v-if="islogin">
      <index :name="this.username"></index>
    </div>
  </div>
</template>

<script>
import index from '@/page/index.vue'
import login from '@/page/login.vue'
export default {
  name: 'App',
  components: {
    'index': index,
    'login': login
  },
  data () {
    return {
      islogin: false,
      username: null
    }
  },
  methods: {
    loginback (islogin, name) {
      this.islogin = islogin,
        this.username = name
    }
  }
}
</script>

<style>
html,
body {
  margin: auto;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #303133;
  margin: 0;
  padding: 0%;
  width: 100vw;
  height: 100vh;
}
</style>
