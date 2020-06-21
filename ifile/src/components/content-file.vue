<template>
  <div id="content-file">
    <el-upload
      class="upload-button"
      action="http://localhost:8002/upload"
      :beforeUpload="beforeAvatarUpload"
      :on-success="(response, file, fileList) => fileUpload(response)"
    >
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload>
    <transition name="el-fade-in">
      <el-button
        id="seleted-button"
        size="small"
        type="primary"
        icon="el-icon-delete"
        v-show="select_delete_button"
      ></el-button>
    </transition>
    <transition name="el-fade-in">
      <el-button
        id="seleted-button"
        size="small"
        type="primary"
        icon="el-icon-download"
        v-show="select_delete_button"
      ></el-button>
    </transition>
    <el-table
      v-if="tableData"
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="文件名" width="400">
        <template slot-scope="scope">{{ scope.row.filename }}</template>
      </el-table-column>
      <el-table-column prop="filesize" label="大小" width="100"></el-table-column>
      <!-- <el-table-column prop="date" label="修改时间" show-overflow-tooltip></el-table-column> -->
      <el-table-column label="修改时间" width="200">
        <i class="el-icon-time"></i>
        <template slot-scope="scope">{{ scope.row.filetype }}</template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button @click="handleShare(scope.row)">分享</el-button>
          <el-button @click="handleDownload(scope.row)">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {
  },
  data () {
    return {
      select_delete_button: false,
      tableData: [],
      multipleSelection: []
    }
  },
  props: ['name'],
  created: function () {
    this.getlist();
  },
  methods: {
    getlist () {
      var that = this;
      let data = { "username": this.name };
      this.$axios
        .post('http://localhost:8000/api/file/filelist', data)
        .then(response => (that.tableData = response.data.obj))
      console.log(that.tableData)
    },
    handleSelectionChange (val) {
      this.multipleSelection = val;
      this.select_delete_button = !this.select_delete_button;
    },
    beforeAvatarUpload (file) {
      const isLt500KB = file.size / 1024.0 / 1024.0 < 0.5;

      if (!isLt500KB) {
        this.$message.error('上传文件不能超过 500KB!');
      }
      return isLt500KB;
    },
    fileUpload (response) {

      this.tableData.push(response.obj)
    },
    handleDownload (row) {
      window.location.href = "http://localhost:8002/api/file/file?fileid=" + row.fileid;
    }
  }
}
</script>

<style>
.upload-button {
  float: left;
  margin-left: 20px;
  margin-top: 15px;
  margin-bottom: 5px;
}
#seleted-button {
  float: left;
  margin-left: 20px;
  margin-top: 15px;
  margin-bottom: 5px;
}
</style>
