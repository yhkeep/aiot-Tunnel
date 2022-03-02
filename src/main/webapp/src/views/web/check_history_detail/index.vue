<template>
  <div class='check_history_detail'>
        <div class="hover_animat custom_bg_color_white">
           <div class="row_box">
                <h2 class="row_title">资产盘点历史明细</h2>
                <div class="table_scroll_wrap">
                    <Table style="min-width:1500px" stripe :columns="checkDetailColumn" :data="checkDetailData" size="large" :loading="tableLoading">
                        <template slot-scope="{ row }" slot="status">
                            <Tag v-if="row.status==='正常'" :color="statusColor[0]" ><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='待维修'" :color="statusColor[1]" ><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='维修接单'" :color="statusColor[2]" ><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='待报废'" :color="statusColor[3]" ><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='报废'" :color="statusColor[4]" ><span>{{row.status}}</span></Tag>
                            <Tag v-else-if="row.status==='外借'" :color="statusColor[5]" ><span>{{row.status}}</span></Tag>
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
import { Table ,Tag ,Icon} from 'iview';
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Icon', Icon);

import NET_PORT from "../../../api/port.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'',
  components:{},
  props:{},
  data(){
    return {
        statusColor:statusColor,
        id:'',
        tableLoading:true,
        checkDetailColumn:[
            {
                title: '资产编号',
                key: 'assetID',
            },
            {
                title: '通用名称',
                key: 'generalName'
            },
            {
                title: '资产名称',
                key: 'assetName'
            },

            {
                title: '所在科室名称',
                key: 'locDept'
            },
           
            {
              title:'规格',
              key:'specification',
            },
            {
                title: '型号',
                key: 'type'
            },
           
            {
                title: '存放地点',
                key: 'location',
            },
            {
                title: '产地',
                key: 'placeoforigin',
            },
            {
                title: '品牌',
                key: 'brand'
            },
          
           {
               title:'标签mac',
               key:'mac',
           },
           {
               title:'楼层',
               key:'floor',
           },
           {
               title:'原值',
               key:'money',
           },
           {
               title:'购买日期',
               key:'buyDate',
           },
        //    {
        //        title:'盘点日期',
        //        key:'check',
        //    },
           {
               title:'SN',
               key:'sn',
           },
           
           
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:120
            },
        ],
        checkDetailData:[],
    }
  },
  watch:{},
  computed:{},
  methods:{
      getDataFn(){
            this.$Loading.start()
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
                    this.$Loading.finish();
                    this.tableLoading=false;
                }else{
                    this.$Loading.error();
                }
            })
            .catch((err)=>{
                this.$Loading.error();
                // this.$Message.error({
                //     content:'创建请求失败！',
                //     duration:1,
                // })
            })
      }
  },
  created(){
      this.id=this.$route.params.id;
      this.getDataFn();
  },
  mounted(){}
}
</script>
<style scoped>
</style>