<template>
  <div id="app">
    <Header />
    <div id="content">
      <el-col :span="3">
        <el-menu
          default-active="1"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-submenu index="1">
            <template slot="title" @click="handleTab(1)">
              <i class="el-icon-folder-opened"></i>
              <span>全部文件</span>
            </template>

            <el-menu-item index="1-1" @click="handleTab(2)">图片</el-menu-item>
            <el-menu-item index="1-2" @click="handleTab(3)">文档</el-menu-item>
            <el-menu-item index="1-3" @click="handleTab(4)">视频</el-menu-item>
            <el-menu-item index="1-4" @click="handleTab(4)">音乐</el-menu-item>
            <el-menu-item index="1-5" @click="handleTab(5)">其他</el-menu-item>
          </el-submenu>
          <el-menu-item index="2">
            <i class="el-icon-delete"></i>
            <span slot="title">回收站</span>
          </el-menu-item>
          <el-menu-item index="3" disabled>
            <i class="el-icon-loading"></i>
            <span slot="title">待开发</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <ContentFile :name="this.name"></ContentFile>
      </el-col>
    </div>
  </div>
</template>

<script>
import Header from '@/components/header.vue'
import ContentFile from '@/components/content-file.vue'
export default {
  name: 'App',
  components: {
    Header,
    ContentFile
  },
  data () {
    return {
      username: null
    }
  },
  props: ['name']
}
</script>

<style>
#content {
  height: 80%;
  width: 100%;
}
#left-menu {
  height: 100%;
}
</style>
