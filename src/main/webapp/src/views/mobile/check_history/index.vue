<template>
  <div class='mobile_check_history_wrap manage_list_wrap custom_flex_column_wrap'>
        
        
        <div class="assets_table_wrap">
          <div class="assets_table_wrap_content  clearfix">
              <div class="assets_table_wrap_left">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name">
                         <div class="assets_table_item"><p>序号</p></div>
                      </li>
                      <li class="assets_table_li" v-for="(item,index) in historyData" :key="index">
                        <div class="assets_table_item"><p>{{index+1}}</p></div>
                      </li>
                  </ul>
              </div>
              <div class="assets_table_wrap_right">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name clearfix" >
                          <div class="assets_table_item"><p>盘点日期</p></div>
                          <div class="assets_table_item"><p>已盘点数量</p></div>
                          <div class="assets_table_item"><p>盘点人员</p></div>
                      </li>
                      <li class="assets_table_li clearfix" v-for="(item,index) in historyData" :key="index" @click="toDetailFn(item,index)">
                          <div class="assets_table_item"><p>{{item.checkTime ? item.checkTime : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.checkNum ? item.checkNum : '0'}}</p></div>
                          <div class="assets_table_item"><p>{{item.checkpep ? item.checkpep : '--'}}</p></div>
                      </li>
                  </ul>
              </div>
          </div>

            <div class="loading_info_wrap">
                <Loading v-show="loadingShow" size="24px">加载中...</Loading>
            </div>
        </div>

       

  </div>
</template>

<script>

import {Row,Col,Button,Tag,Loading,Search,Icon,Field,Popup } from 'vant';
import 'vant/lib/row/style';
import 'vant/lib/col/style';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/loading/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import NET_PORT from "../../../api/port.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'mobile_check_history',
  components:{
        Button,
        Row,
        Col,
        Tag,
        Loading,
        Search,
        Icon,
        Field,
        Popup 
  },
  props:{},
  data(){
    return {
        user:this.$store.state.user,
        historyData:[],
        loadingShow:true,

       
    }
  },
  watch:{},
  computed:{
    
  },
  methods:{
     
      toDetailFn(item,index){
            this.$router.push({
                path:'/mobile/check_history_detail/'+item.checkOnlyCode
            })
      },
     
      getDataFn(){
       
            this.loadingShow=true;
            let datas=[
                {
                    username:this.user,
                }
            ]
            this.$axios.post(NET_PORT.assetsCheckHistory,datas)
            .then((res)=>{
                if(res.data.code===0){
                    this.historyData=res.data.data;
                    this.loadingShow=false;
                }else{
                    
                }
            })
            .catch((error)=>{

            })
            
      },

    
  },
  

  created(){
     this.$emit('setTitle','盘点历史');
  },
  mounted(){
      this.getDataFn();
  },

  
}
</script>

<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
</style>