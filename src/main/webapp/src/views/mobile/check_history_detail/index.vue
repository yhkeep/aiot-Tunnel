<template>
  <div class='mobile_check_history_detail_wrap manage_list_wrap custom_flex_column_wrap'>
        <div class="assets_table_wrap">
          <div class="assets_table_wrap_content  clearfix">
              <div class="assets_table_wrap_left">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name">
                        <div class="assets_table_item"><p>序号</p></div>
                      </li>
                      <li class="assets_table_li" v-for="(item,index) in checkDetailData" :key="index">
                        <div class="assets_table_item"><p>{{index+1}}</p></div>
                      </li>
                  </ul>
              </div>
              <div class="assets_table_wrap_right">
                  <ul class="assets_table_ul" >
                      <li class="assets_table_li assets_table_li_name clearfix" >
                            <div class="assets_table_item"><p>资产编号</p></div>
                            <div class="assets_table_item"><p>资产名称</p></div>
                            <div class="assets_table_item"><p>科室名称</p></div>
                            <div class="assets_table_item"><p>存放地点</p></div>
                            <div class="assets_table_item"><p>标签mac</p></div>
                            <div class="assets_table_item"><p>状态</p></div>
                      </li>
                      <li class="assets_table_li clearfix" v-for="(item,index) in checkDetailData" :key="index" >
                          <div class="assets_table_item"><p>{{item.assetID ? item.assetID : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.assetName ? item.assetName : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.locDept ? item.locDept : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.location ? item.location : '--'}}</p></div>
                          <div class="assets_table_item"><p>{{item.mac ? item.mac : '--'}}</p></div>
                          <div class="assets_table_item">
                              <Tag  size="medium" v-if="item.status==='正常'" :color="statusColor[0]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else-if="item.status==='待维修'" :color="statusColor[1]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else-if="item.status==='维修接单'" :color="statusColor[2]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else-if="item.status==='待报废'" :color="statusColor[3]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else-if="item.status==='报废'" :color="statusColor[4]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else-if="item.status==='外借'" :color="statusColor[5]">{{item.status}}</Tag>
                              <Tag  size="medium" v-else color="#f7f7f7" text-color="#515a6e">{{item.status}}</Tag>
                          </div>
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
  name:'mobile_check_history_detail',
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
        statusColor:statusColor,
        id:'',
        checkDetailData:[],
        loadingShow:true,
    }
  },
  watch:{},
  computed:{
    
  },
  methods:{
     

      getDataFn(){
       
            this.loadingShow=true;
             let datas=[
                {
                    checkOnlyCode:this.id,
                }
            ]

            this.$axios.post(NET_PORT.assetsCheckDetail,datas)
            .then((res)=>{
                if(res.data.code===0){
                    let data=res.data.data;
                    let arrResult=[]
                    data.forEach((item,index)=>{
                        let obj={
                            amount:item.Amount? item.Amount : 0,
                            assetID:item.AssetID ? item.AssetID : '--',
                            assetName:item.AssetName ? item.AssetName : '--',
                            brand:item.Brand ? item.Brand : '--',
                            buyDate:item.BuyDate ? item.BuyDate : '--',
                            check: item.Check ? item.Check : '--',
                            generalName:item.GeneralName ? item.GeneralName : '--',
                            locDept:item.LocDept ? item.LocDept : '--',
                            location:item.Location ? item.Location : '--',
                            mac:item.Mac ? item.Mac : '--',
                            money:item.Money ? item.Money : '--',
                            status:item.Status ? item.Status : '--',
                            specification:item.specification ? item.specification : '--',
                            placeoforigin:item.placeoforigin ? item.placeoforigin : '--',
                            type:item.Type ? item.Type : '--',
                            unit:item.Unit ? item.Unit : '--',
                            floor:item.floor ? item.floor : '--',
                        }

                        arrResult.push(obj)
                    })

                    this.checkDetailData=arrResult;
                    this.loadingShow=false;
                }else{

                }
            })
            .catch((err)=>{

            })
            
      },

    
  },
  

  created(){
     this.$emit('setTitle','盘点历史明细');
     this.id=this.$route.params.id;
  },
  mounted(){
      this.getDataFn();
  },

  
}
</script>

<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
</style>