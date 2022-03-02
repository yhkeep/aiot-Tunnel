<template>
  <div class='damage_manage_wrap'>
      <div class="row_wrap hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">维修/报废明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
              
              </div>
              <div class="data_table_top_right">
                    <div class="pagination_wrap">
                        <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
              </div>
            </div>

            <div class="table_scroll_wrap">
               <Table style="min-width:1500px" stripe :columns="damageColumn" :data="damageData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                  <template slot-scope="{row}" slot="assetID">
                      <!-- <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a> -->
                      {{row.assetID}}
                      </template>
                      <template slot-scope="{row}" slot="assetName">
                      {{row.assetName ? row.assetName : '--'}}
                      </template>

                      <template slot-scope="{row}" slot="depart">
                      {{row.depart ? row.depart : '--'}} 
                      </template>
                      <template slot-scope="{row}" slot="applyPeople">
                      {{row.applyPeople ? row.applyPeople : '--'}} 
                      </template>
                      <template slot-scope="{row}" slot="reason">
                      
                      <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;">{{row.reason ? row.reason : '--'}}</div>
                      </template>
                      <template slot-scope="{row}" slot="position">
                      {{row.position ? row.position : '--'}} 
                      </template>
                      <template slot-scope="{row}" slot="type">
                      <Tag :color="row.type==='维修' ? 'orange' :'red' ">{{row.type}}</Tag>
                      </template>
                      <template slot-scope="{ row }" slot="status">
                      <Tag v-if="row.status==='在修'" color="warning"><span >{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='待报废'" color="primary"><span >{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='已报废'" color="error"><span >{{row.status}}</span></Tag>
                      <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
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

export default {
  name:'damage_manage',
  components:{},
  props:{},
  data(){
    return {
        selection:[],
        tableLoading:true,
        totalCount: 0,
        pageSize: 10,
        currentPage: 1,

        damageColumn:[
            {key: 'assetID', title:'设备id',slot:'assetID'},
            {key: 'assetName', title: '设备名称',slot:'assetName'},
            {key: 'depart', title: '申请部门',slot:'depart'},
            {key: 'applyPeople', title: '申请人',slot:'applyPeople'},
            {key: 'reason', title: '原因',slot:'reason'},
            {key: 'position', title: '所在位置',slot:'position'},
            {key: 'status', title: '状态',slot:'status'},
            ],
        damageData:[],
    }
  },
  watch:{},
  computed:{},
  methods:{
       //点击多选按钮
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    changePageFn(){

    },

    getDataFn(){
       var data=[
          {
              assetID:'ac88888',
              assetName:'核磁共振',
              depart:'手术室',
              applyPeople:'刘某某',
              reason:'设备故障，无法运行',
              position:'手术室',
              status:'在修'
          },
          {
              assetID:'ac6666',
              assetName:'超声波诊断仪',
              depart:'检验科',
              applyPeople:'王某某',
              reason:'设备故障，无法运行',
              position:'检验科',
              status:'待报废'
          },
          {
              assetID:'ac855555',
              assetName:'ct',
              depart:'检验科',
              applyPeople:'王某某',
              reason:'设备故障，无法运行',
              position:'检验科',
              status:'已报废'
          },
        ]
        this.damageData=data;
        this.totalCount=data.length;
        this.tableLoading=false;
    }
  },
  created(){},
  mounted(){
      this.getDataFn();
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
</style>