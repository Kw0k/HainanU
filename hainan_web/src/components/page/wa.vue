<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-s-order"></i> 网安学硕
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-table
                :data="tableData"
                border
                class="table"
                ref="multipleTable"
                header-cell-class-name="table-header"
            >
                <el-table-column prop="rank" label="排名" width="55" align="center"></el-table-column>
                <el-table-column prop="score1" label="思想政治基础"></el-table-column>
                <el-table-column prop="score2" label="英语（一）"></el-table-column>
                <el-table-column prop="score3" label="数学（一）"></el-table-column>
                <el-table-column prop="score4" label="计算机学科专业基础"></el-table-column>
                <el-table-column prop="countScore" label="总分"></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    :current-page="currentPage"
                    :page-sizes="[20, 50, 100]"
                    :page-size="20"
                    background
                    layout="total, sizes, prev, pager, next"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                ></el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'wa',
    data() {
        return {
            query: {
                userid: null,
                status: null,
                id:null,
                paytype:null
            },
          serverURL:axios.defaults.baseURL,
          tableData: [],
          pageSize: 20,
          currentPage:1,
          total:50,
        };
    },
  activated() {
    this.getData();
  },
    created() {
        this.getData();
    },
    methods: {
        getData() {
            axios.post(
                '/getwa',this.qs.stringify({
                      pageNum:this.currentPage,
                      pageSize:this.pageSize,
                }
                )).then((response) =>{
              if (response.data.flag){
                this.$message.success(response.data.msg);
                this.tableData=response.data.data.list;
                this.total=response.data.data.total;
              }else {
                this.$message.error(response.data.msg);
                this.$router.push("/login")
              }
            })
                .catch(function (error) {
                    console.log(error);
                });
        },
      // 分页导航
      handleSizeChange(val) {
        this.pageSize =val;
        this.getData()
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        this.getData()
      }
    }
};
</script>
<style scoped>
.table {
    width: 100%;
    font-size: 14px;
}
</style>
