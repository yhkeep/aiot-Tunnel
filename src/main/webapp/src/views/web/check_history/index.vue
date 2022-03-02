<template>
  <div class='check_history_wrap'>
       <div class="hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">盘点历史列表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">

              </div>
              <div class="data_table_top_right">
                   
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="historyColumn" :data="historyData" size='large' :loading="tableLoading">
                    <!-- <template slot-scope="{row}" slot="checkOnlyCode">
                        <a @click="toDetailFn(row.checkOnlyCode)">明细</a>
                    </template> -->
                    <template slot-scope="{ row }" slot="operation">
                        <Button shape="circle" type="default" icon="ios-book" @click="toDetailFn(row.checkOnlyCode)">查看</Button>
                    </template>
                </Table>
            </div>
           
        </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue"
import {Icon,Table ,Tag ,Page,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);

import NET_PORT from "../../../api/port.js"
export default {
  name:'check_history',
  components:{},
  props:{},
  data(){
    return {
        user:this.$store.state.user,
        tableLoading:true,
        totalCount: 0,
        pageSize: 10,
        currentPage: 1,
        historyColumn:[
            {
                key: 'id',
                title: '序号',
                width:200
            },
            {
                key: 'checkTime',
                title: '盘点日期',
            },
           
            {
                key: 'checkNum',
                title: '已盘点数量',
            },
            
            {
                key: 'checkpep',
                title: '盘点人员',
            },
            {
              key: 'operation',
              title: '操作',
              slot:'operation',
              width:200,
            }
        ],
        historyData:[],
    }
  },
  watch:{},
  computed:{},
  methods:{
      getDataFn(){
            this.$Loading.start()
            this.tableLoading=true;
            let datas=[
                {
                    username:this.user,
                }
            ]
            this.$axios.post(NET_PORT.assetsCheckHistory,datas)
            .then((res)=>{
                if(res.data.code===0){
                    this.historyData=res.data.data;
                    this.$Loading.finish();
                    this.tableLoading=false;
                }else{
                    
                }
            })
            .catch((error)=>{
               this.$Loading.error();
            })

      },

      toDetailFn(id){
          this.$router.push({
              path:'/web/check_history_detail/'+id
          })
      }
  },
  created(){
      this.getDataFn();
  },
  mounted(){}
}
</script>
<style scoped>
</style>