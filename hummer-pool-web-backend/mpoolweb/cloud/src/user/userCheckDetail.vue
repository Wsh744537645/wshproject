<template>
  <div class="col-md-9 box">
    <!-- 点击导出，需要插件 -->
    <img src="../assets/pic@2x/out@2x.png" alt class="out" @click='derive'>
    <el-row>
      <el-col :span="24">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="每日产出" name="first">
            <el-col :span="24">
              <el-button round @click="currentTime(0)">全部时间</el-button>
              <el-button round @click="currentTime(7)">最近7天</el-button>
              <el-button round @click="currentTime(30)">最近30天</el-button>
              <!-- {{day}} -->
              
              <div class="block">
                <el-date-picker
                  @change="dateChange"
                  v-model="value1"
                  type="datetimerange"
                  start-placeholder="开始时间"
                  end-placeholder="结束时间"
                  :default-time="['12:00:00', '08:00:00']"
                  size="mini"
                ></el-date-picker>
              </div>
            </el-col>
            <el-table
              :data="tableData"
              :cell-style="text1"
              :header-cell-style="text2"
              :default-sort="{prop: 'date', order: 'descending'}"
              style="width: 100%;"
              id="day"
            >
              <!-- //prop均未修改 -->
              <el-table-column prop="day" label="时间" width="180"></el-table-column>
              <el-table-column prop="currencyName" label="币种" width="180"></el-table-column>
              <el-table-column prop="earnSum" label="收益" width="180"></el-table-column>
              <el-table-column prop="balance" label="账户余额"></el-table-column>
              <div slot="empty" class="empty">
                <img src="../assets/pic@2x/noData@2x.png" alt>
                <p>暂无数据...</p>
                <el-button plain @click="routeToBuy">购买矿机套餐</el-button>
              </div>
            </el-table>
            
            <el-pagination
              background
              :page-size="pageSize"
              pager-count = 5
              :current-page = "currentPage"
              :page-count ="pages"
              :total="total"
              layout="prev, pager, next"
              @current-change="currentChange"
            ></el-pagination>
          </el-tab-pane>
          <el-tab-pane label="每单产出" name="second">
            <el-col :span="24">
              <el-button round @click="currentState(1)">全部</el-button>
              <el-button round  @click="currentState(0)">生效中</el-button>
              <el-button round  @click="currentState(3)">已完成</el-button>
              <el-input v-model="search" size="mini" placeholder="输入关键字搜索" @change="byOrderId" suffix-icon="el-icon-search"/>
            </el-col>
            <el-table :data="tableData1"
              :cell-style="text1"
              :header-cell-style="text2"
              :default-sort="{prop: 'date', order: 'descending'}"
              style="width: 100%;"
              empty-text="当前数据没有，请添加数据"
              id="count"
            >
              <el-table-column prop="orderId" label="订单号" width="200"></el-table-column>
              <el-table-column prop="earnSum" label="累计收益" width="150"></el-table-column>
              <el-table-column prop="accept" label="算力" width="150"></el-table-column>
              <el-table-column prop="acceptDay" label="套餐剩余天数" width="150"></el-table-column>
              <el-table-column prop="powerDay" label="电费剩余天数"></el-table-column>
              
              <div slot="empty" class="empty">
                <img src="../assets/pic@2x/noData@2x.png" alt>
                <p>暂无数据...</p>
                <el-button plain @click="routeToBuy">购买矿机套餐</el-button>
              </div>
            </el-table>
            <el-pagination
              background
              :page-size="pageSize1"
              pager-count = 5
              :current-page = "currentPage1"
              :page-count ="pages1"
              :total="total1"
              layout="prev, pager, next"
              @current-change="currentChange1"
            ></el-pagination>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getOrderdayList,dateToMs,getOrdertimeList, getOrdersList} from '@/api/goods.js'
import FileSaver from "file-saver";
import { escape } from 'querystring';
export default {
  data() {
    return {
      //判断是哪个表
      tableIndex:'',
      // 导航页默认
      activeName: "first",
      //日期选择器
      value1: "",
      day: 0,
      orderId: 'all',
      state: '1',
      tableData: [],
      tableData1: [],
      pageSize: 5,
      pages: 1,
      currentPage: 1,
      pageSize1: 5,
      pages1: 1,
      currentPage1: 1,
      timeMethod: 'day',
      search: ''
    };
  },
  created() {
    this.getdayData(this.day)
  },
  methods: {
    //选择框的触发事件
    handleClick(tab, event) {
      if(tab.index == 1) {
        this.getEachOrder(this.orderId,this.state)
      }else{
        this.getdayData(this.day)
      }
    },
    currentChange(val) {
      this.currentPage = val;
      if(this.timeMethod == 'day'){
          this.getdayData(this.day)
      }else{
          // this.getdayDatabytime(1556698460, 1557648860)
          this.getdayDatabytime(dateToMs(this.value1[0]), dateToMs(this.value1[1]))
      }
    },
    currentChange1(val) {
      this.currentPage1 = val;
      this.getEachOrder(this.orderId,this.state)
    },
    currentTime(val) {
      this.timeMethod = 'day'
      this.day = val
      this.currentPage = 1;
      this.getdayData(val)
    },
    currentState(val) {
      this.state = val
      this.currentPage1 = 1;
      this.getEachOrder(this.orderId,this.state)
    },
    byOrderId(val) {
      if(!!val) {
        this.orderId = val;
      }else{
         this.orderId = 'all'
      }
      this.currentPage1 = 1;
      this.getEachOrder(this.orderId,this.state)
    },
    //单元格回调，修改样式
    text1({ row, rowIndex }) {  
      return "text-align: center;";
    },
    //表头回调，修改样式
    text2({ row, rowIndex }) {
      return "text-align: center;font-weight:400;color:rgba(0,0,0,1);";
    },
    getdayData(day) {
      const param = {
        day: day,
        size: this.pageSize,
        current: this.currentPage
      }
      getOrderdayList(param).then(res=> {
         this.total = parseInt(res.data.data.total)
         this.tableData1 = res.data.data.records
         this.pages = res.data.data.pages
      })
    },
    getdayDatabytime (start, end) {
      const param = {
        start: start,
        end: end,
        size: this.pageSize,
        current: this.currentPage
      }
      getOrdertimeList(param).then(res=> {
         this.total = parseInt(res.data.data.total)
         this.tableData = res.data.data.records
         this.pages = res.data.data.pages
      })
    },
    getEachOrder (orderId, state) {
      const param = {
        orderId: orderId,
        state: state,
        size: this.pageSize1,
        current: this.currentPage1
      }
      getOrdersList(param).then(res=> {
         this.total1 = parseInt(res.data.data.total)
         this.tableData1 = res.data.data.records
         this.pages1 = res.data.data.pages
      })
    },
    routeToBuy() {
      this.$router.push(`/loginIndex/${this.$route.params.id}`);
    },
    dateChange() {
      this.timeMethod = 'time'
      this.currentPage = 1;
      this.getdayDatabytime(dateToMs(this.value1[0]), dateToMs(this.value1[1]))
      // this.getdayDatabytime(1556698460, 1557648860)
    },
    derive(){
      var wb;
      if(this.tableIndex==0){
         wb = XLSX.utils.table_to_book(document.querySelector("#day"));
      }else{
         wb = XLSX.utils.table_to_book(document.querySelector("#count"));
      }
      var wbout = XLSX.write(wb, {
            bookType: "xlsx",
            bookSST: true,
            type: "array"
        });
        try {
            FileSaver.saveAs(
            new Blob([wbout], { type: "application/octet-stream" }),
            //设置导出文件名称
            "table.xlsx"
            );
        } catch (e) {
            if (typeof console !== "undefined") console.log(e, wbout);
        }
        return wbout;
      }
    },
};
</script>
<style lang="less" scoped>
.box {
  background-color: #fff;
  padding: 0 60px 200px 60px;
  .out {
    position: absolute;
    z-index: 9999;
    right: 60px;
    top: 45px;
  }
  //空白区域
  /deep/.el-table__empty-text {
    width: 100%;
    line-height: 40px;
    .empty {
      padding: 50px 0;
      width: 100%;
      background-color: rgba(238, 238, 238, 1);
      p {
        margin: 0;
      }
      .el-button {
        padding: 9px 20px;
        color: #171c61;
        border: 1px solid #171c61;
        cursor: pointer;
      }
    }
  }

  //导航条
  /deep/.el-tabs__header {
    background-color: #fff;
    border-radius: 5px;
    overflow: hidden;
    .el-tabs__active-bar {
      margin-left: 30px;
    }
    /deep/.el-table td,
    .el-table th.is-leaf {
      padding: 18px 0;
    }
    .el-tabs__item {
      height: 81px;
      line-height: 110px;
      margin-left: 30px;
      font-size: 21px;
      &:nth-child(3) {
        margin-left: 0;
      }
    }
  }
  //button
  .el-button.is-round {
    padding: 5px 16px;
    margin: 30px 20px 40px 0px;
    &:nth-child(1) {
      margin-left: 30px;
    }
  }
  //日期选择器
  .block {
    width: 50%;
    display: inline-block;
    text-align: right;
    .el-date-editor--datetimerange.el-input__inner {
      width: 81%;
    }
    /deep/.el-date-editor .el-range-input {
      //   width: 60px;
      color: rgba(100, 100, 100, 1);
    }
  }
}

.el-pagination {
  text-align: center;
  padding: 42px 0;
  background-color: #fff;
  border-radius: 5px;
}
.el-input{
  width: 196px;
  float: right;
  margin-top: 30px
}
</style>