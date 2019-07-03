<template>
  <div class="col-md-9">
    <el-row>
      <el-col :span="24">
        <el-tabs v-model="activeName" @tab-click="handleClick" class="right_top">
          <el-tab-pane label="合约订单" name="first">
            <el-table
              :data="tableData"
              :cell-style="colStyle"
              :header-cell-style="tableheadStyle"
              :default-sort="{prop: 'date', order: 'descending'}"
              style="width: 100%">
              <el-table-column  prop="orderId" label="订单编号" width="190"></el-table-column>
              <el-table-column  prop="createTime" label="下单日期" width="100"></el-table-column>
              <el-table-column prop="currencyName" label="币种" width="100"></el-table-column>
              <el-table-column prop="period" label="套餐周期" width="100"></el-table-column>
              <el-table-column prop="hadPay" label="已付金额" width="100"></el-table-column>
              <el-table-column sortable prop="state" label="状态" width="100"></el-table-column>
              <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                  <el-button @click="handle(scope.row)" type="primary" plain size="small">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
            <!-- 分页 -->
            <el-pagination
              background
              :page-size="pageSize"
              pager-count=5
              :page-count ="pages"
              layout="prev, pager, next"
              :total="total"
              @current-change="currentChange"
            ></el-pagination>
          </el-tab-pane>
          <el-tab-pane label="电费订单" name="second">
             <el-table
              :data="tableData2"
              :cell-style="colStyle"
              :header-cell-style="tableheadStyle"
              :default-sort="{prop: 'date', order: 'descending'}"
              style="width: 100%">
              <el-table-column  prop="orderId" label="订单编号" width="190"></el-table-column>
              <el-table-column  prop="createTime" label="下单日期" width="100"></el-table-column>
              <el-table-column prop="id" label="关联合约订单" width="200"></el-table-column>
              <el-table-column prop="period" label="电费天数" width="118"></el-table-column>
              <el-table-column prop="hadPay" label="已付金额" width="120"></el-table-column>
              <el-table-column prop="state" label="状态" width="120"></el-table-column>
            </el-table>
            <el-pagination background pager-count=5 layout="prev, pager, next"
              :page-size="pageSize2"
              :page-count ="pages2"
              :total="total2"
              @current-change="currentChange2"
            ></el-pagination>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import {getOrderList,stateName} from '@/api/goods.js'
export default {
  data() {
    return {
      fatherId:'',
      activeName: "first",
      tableData: [],
      pageSize: 5,
      currentPage: 1,
      pages: 1,
      tableData2: [],
      pageSize2: 5,
      currentPage2: 1,
      pages2: 1
    };
  },
  
  created() {
    this.getData()
  },
  methods: {
    handleClick(tab, event) {
      if(event.target.id == "tab-first"){
        this.getData()
      }else{
        this.getData2()
      }
    },
    getData() {
      const param = {
        type: 1,
        state: 100,
        size: this.pageSize,
        current: this.currentPage
      }
      getOrderList(param).then(res=> {
        const _this = this
        _this.total = parseInt(res.data.data.total)
        _this.tableData = res.data.data.records
        _this.tableData.forEach(e => {
          e.state = stateName(e.state)
        });
        _this.pages = res.data.data.pages
      })
    },
    getData2() {
      const param = {
        type: 2,
        state: 100,
        size: this.pageSize2,
        current: this.currentPage2
      }
      getOrderList(param).then(res=> {
        const _this = this
        _this.total2 = parseInt(res.data.data.total)
        _this.tableData2 = res.data.data.records
        _this.tableData2.forEach(e => {
          e.state = stateName(e.state)
        });
        _this.pages2 = res.data.data.pages
      })
    },
    //查看
    handle(row) {
      // this.fatherId=this.fatherId=sessionStorage.getItem('userInfo')
      this.$router.push(`/user/${row.orderId}/3`);
    },
    //拿到当前页码进行分页
    currentChange(val) {
      this.currentPage = val;
      this.getData()
    },
    currentChange2(val) {
      this.currentPage2 = val;
      this.getData2()
    },
    //单元格回调，修改样式
    colStyle({ row, rowIndex }) {
      return "text-align: center";
    },
    //表头回调，修改样式
    tableheadStyle({ row, rowIndex }) {
      return "text-align: center;font-weight:400;color:rgba(0,0,0,1);";
    }
  },
  computed: {
    
  }
};
</script>
<style lang="less" scoped>
/deep/.el-tabs__header {
  background-color: #fff;
  border-radius: 5px;
  .el-tabs__active-bar {
    margin-left: 70px;
  }
  /deep/.el-table td,
  .el-table th.is-leaf {
    padding: 18px 0;
  }
  .el-tabs__item {
    height: 85px;
    line-height: 85px;
    margin-left: 70px;
    font-size: 22px;
    &:nth-child(3) {
      margin-left: 0;
    }
  }
}
.el-pagination {
  text-align: center;
  padding: 42px 0;
  background-color: #fff;
  border-radius: 5px;
}
</style>
