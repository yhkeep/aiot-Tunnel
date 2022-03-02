<template>
  <div class='mobile_assets_manage_wrap manage_list_wrap custom_flex_column_wrap'>
      <div class="mobile_search_wrap">
          <div class="mobile_search_box" ref="mobileSearchBoxFixed">
            <div class="mobile_search_box_left">
              <Search
                  v-model="value"
                  disabled 
                  placeholder="点击展开搜索面板"
                  shape="round"
                  @click="openSearchPaneFn"
                />
            </div>
            <div class="mobile_search_box_right" >
                <div class="mobile_search_box_right_tools" style="width:40px">
                  <!-- <Icon name="add-o"  @click="toCreateFn"/> -->
                  <Popover
                    v-model="showPopover"
                      trigger="click"
                      :actions="actions"
                      @select="onSelect"
                      placement="bottom-end"
                      theme="dark"
                      >
                      <template #reference>
                          <Icon name="add-o" />
                      </template>
                  </Popover>
                </div>
                <div class="mobile_search_box_right_tools" style="width:40px">
                  <Icon name="bars"  @click="showMoreMenuFn"/>
                </div>
            </div>
          </div>
      </div>

      <div class="assets_table_wrap">
        <div class="assets_table_wrap_content  clearfix">
            <div class="assets_table_wrap_left">
                <ul class="assets_table_ul" >
                    <li class="assets_table_li assets_table_li_name">
                        <div class="assets_table_item">
                          <p>序号</p>
                        </div>
                    </li>
                    <li class="assets_table_li" v-for="(item,index) in assetsDetailData" :key="index">
                      <div class="assets_table_item">
                        <p>{{index+1}}</p>
                      </div>
                    </li>
                </ul>
            </div>
            <div class="assets_table_wrap_right">
                <ul class="assets_table_ul" >
                    <li class="assets_table_li assets_table_li_name clearfix">
                        <div class="assets_table_item"><p>资产编号</p></div>
                        <div class="assets_table_item"><p>资产名称</p></div>
                        <div class="assets_table_item"><p>科室名称</p></div>
                        <div class="assets_table_item"><p>存放地点</p></div>
                        <div class="assets_table_item"><p>标签mac</p></div>
                        <div class="assets_table_item"><p>状态</p></div>
                        <!-- <div class="assets_table_item"><p>SN</p></div> -->
                    </li>
                    <li class="assets_table_li clearfix" v-for="(item,index) in assetsDetailData" :key="index" @click="toDetailFn(item,index)">
                        <div class="assets_table_item">
                          <p>{{item.assetID ? item.assetID : '--'}}</p>
                        </div>
                        <div class="assets_table_item">
                          <p>{{item.assetName ? item.assetName : '--'}}</p>
                        </div>
                        <div class="assets_table_item">
                          <p>{{item.locDept ? item.locDept : '--'}}</p>
                        </div>
                        <div class="assets_table_item">
                          <p>{{item.location ? item.location : '--'}}</p>
                        </div>
                        <div class="assets_table_item">
                          <p>{{item.mac ? item.mac : '--'}}</p>
                        </div>
                        <div class="assets_table_item">
                            <Tag  size="medium" v-if="item.status==='正常'" :color="statusColor[0]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else-if="item.status==='待维修'" :color="statusColor[1]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else-if="item.status==='维修接单'" :color="statusColor[2]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else-if="item.status==='待报废'" :color="statusColor[3]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else-if="item.status==='报废'" :color="statusColor[4]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else-if="item.status==='外借'" :color="statusColor[5]">{{item.status}}</Tag>
                            <Tag  size="medium" v-else color="#f7f7f7" text-color="#515a6e">{{item.status}}</Tag>
                        </div>
                        <!-- <div class="assets_table_item">
                          <p>{{item.sn ? item.sn : '--'}}</p>
                        </div> -->
                    </li>
                </ul>
            </div>
        </div>

        <div class="loading_info_wrap">
            <Button style="border:none" v-show="!loadingShow" :disabled="!hasMore" @click="getMoreFn" >{{hasMore===true? '点击加载更多' : '已无更多数据'}}</Button>
            <Loading v-show="loadingShow" size="24px">加载中...</Loading>
            
        </div>
      </div>
      <Popup v-model="showSearchPane" position="top" round >
          <div class="query_data_wrap">
            <div class="query_data_box">
                <Field clearable label="资产编号"  v-model="queryData.assetID" placeholder="请输入资产编号"/>
                <Field clearable label="资产名称" center  v-model="queryData.assetName" placeholder="请输入资产名称">
                      <template #button>
                          <Button size="small" type="primary" @click="showInputSearchFn('assetName')">云数据</Button>
                      </template>
                </Field>
                <Field clearable label="部门名称" center  v-model="queryData.memorydepart" placeholder="请输入部门名称">
                      <template #button>
                          <Button size="small" type="primary" @click="showInputSearchFn('departmentroom')">云数据</Button>
                      </template>
                </Field>
                <Field  clearable label="所在科室" center  v-model="queryData.memorylocDept" placeholder="请输入所在科室">
                      <template #button>
                          <Button  size="small" type="primary" @click="showInputSearchFn('locDept')">云数据</Button>
                      </template>
                </Field>
                <Field clearable label="标签mac"  v-model="queryData.mac" placeholder="请输入标签mac"/>
                <Field readonly label="状态"  v-model="queryData.status" placeholder="请选择状态" @click="statusDialogShow=true"/>
            </div>

            <div class="tools_button_wrap">
              <Button class="tools_button"   color="linear-gradient(to right, #ff6034, #ee0a24)" round @click="clearAssetsFn">清空</Button>
              <Button class="tools_button"   color="linear-gradient(to right, #69aff5, #1989fa)" round  @click="searchFn">搜索</Button>
            </div>
          </div>
      </Popup>

      <Popup v-model="statusDialogShow" position="bottom">
          <Picker  :columns="statusData" show-toolbar title="请选择状态" @cancel="statusDialogShow=false" @confirm="getStatusFn" :visible-item-count=5 />
      </Popup>

      <Popup v-model="showInputSearch" position="bottom">
          <Search
              v-model="inputSearchValue"
              placeholder="请输入搜索关键词"
              input-align="center"
              @input="handleSearchFn"
          />
          <Picker ref="inputSelect"  show-toolbar toolbar-position="bottom" @cancel="showInputSearch = false" @confirm="certainSelectFn"  :columns="inputData"  title=""  :visible-item-count=5 />
      </Popup>

  </div>
</template>

<script>

import {Row,Col,Button,Tag,Loading,Search,Icon,Field,Popup,Picker,Popover} from 'vant';
import 'vant/lib/row/style';
import 'vant/lib/col/style';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/loading/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import 'vant/lib/picker/style';
import 'vant/lib/popover/style';
import NET_PORT from "../../../api/port.js"
import statusData from "../../../assets/data/status.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'mobile_assets_manage',
  components:{
        Button,
        Row,
        Col,
        Tag,
        Loading,
        Search,
        Icon,
        Field,
        Popup,
        Picker,
        Popover
  },
  props:{},
  data(){
    return {
        // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        inputData:[],
        inputDataAll:[],
        inputSearchValue:'',
        activeKey:'',
        showInputSearch:false,

        statusColor:statusColor,
        isFirstEnter:true,
        currentPage:1,
        totalCount:0,
        hasMore:true,
        assetsDetailData:[],
        loadingShow:true,

        queryData:{
          assetID:'',
          assetName:'',
          memorydepart:'',
          memorylocDept:'',
          mac:'',
          status:'',
        },
        queryDataNow:{},
        value:'',
        showSearchPane:false,
        statusDialogShow:false,
        statusData:statusData,

        showPopover:false,
        actions: [{ text: '资产新增',icon:'plus'}],
    }
  },
  watch:{
    mobileScroll(){
        let scrollTop=this.mobileScroll;
        if(scrollTop>54){
          this.$refs.mobileSearchBoxFixed.classList.add('show');
        }else if(scrollTop===0){
          this.$refs.mobileSearchBoxFixed.classList.remove('show');
        }
    }
  },
  computed:{
    mobileScroll(){
      return this.$store.state.mobileScroll
    }
  },
  methods:{
     onSelect(action) {
        switch(action.text){
            case '资产新增' :
              this.toCreateFn();
            break;
              
        }
        
    },
    showMoreMenuFn(){
      this.$emit('getMessage', true);
    },
    openSearchPaneFn(){
      this.showSearchPane=true;
    },
    toDetailFn(item,index){
      this.$router.push({
          path:'/mobile/assets_detail'+'/'+item.assetID
      })
    },
    toCreateFn(){
        this.$router.push({
            path:'/mobile/assets_create'
        })
    },

    searchFn(){
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.currentPage=1;
      this.assetsDetailData=[];
      this.getDataFn();
      this.showSearchPane=false;
    },

    clearAssetsFn(){
        for(let i in this.queryData){
          // if(this.locDept!==''){
          //   if(i!=='memorylocDept'){
          //     this.queryData[i]="";
          //   }
          // }else{
          //     this.queryData[i]="";
          // }
          this.queryData[i]="";
        }
    },

    getStatusFn(value) {
        this.queryData.status=value.text;
        this.statusDialogShow=false;
    },
      
    certainSelectFn(val){
      if(this.activeKey==='departmentroom'){
        this.queryData.memorydepart=val
      }else if(this.activeKey==='locDept'){
        this.queryData.memorylocDept=val
      }else{
        this.queryData[this.activeKey]=val;
      }
        
        this.showInputSearch=false;
        this.inputSearchValue='';
        this.activeKey='';
    },
    showInputSearchFn(key){
        this.showInputSearch=true;
        this.activeKey=key;
        this.inputFocusFn(key);
    },

    handleSearchFn(data){
        this.inputData = this.inputDataAll.filter(item => item.toLowerCase().indexOf(data.toLowerCase()) > -1);
    },

    inputFocusFn(key){
        this.inputDataAll=[];
        this.inputData=[];
        let queryArr=[];
        let obj={};
        obj[key]="1"
        queryArr.push(obj)
        this.$axios.post(NET_PORT.inputQuery,queryArr)
        .then((res)=>{
            res.data.data.forEach((item,index)=>{
            
                if(this.inputDataAll.find((result)=>{return result===item[key]})){

                }else{
                    this.inputDataAll.push(item[key])
                }
            })

            this.inputData=JSON.parse(JSON.stringify(this.inputDataAll))
        })
        .catch((err)=>{
            console.log(err)
        })
    },

    getDataFn(){
      
      this.loadingShow=true;

      let configData='';
      for (let m in this.queryDataNow){
        if(!this.queryDataNow[m]){
          this.queryDataNow[m]='';
        }
        //将参数拼接到一起，裁掉左右空格
        configData+=m+'='+this.queryDataNow[m].trim()+'&'
      }

      //裁掉最后一个&
      configData=configData.substring(0,configData.length-1)

      this.$axios.post(NET_PORT.assetsQuery+'?currentPage='+this.currentPage+'&'+configData)
      .then((res)=>{
          var initData=res.data;
          var total=Number(initData.pop().total);
              for(var i=0;i<initData.length;i++){
                  for(var k in initData[i]){
                      if(initData[i][k]===''||initData[i][k]==='null'){
                        initData[i][k]='--'; 
                      };
                  }
                  this.assetsDetailData.push(initData[i])
              }
              //判断是否还有更多数据
              if(total<= this.assetsDetailData.length){
                  this.hasMore=false;
              }else{
                  this.hasMore=true;
              }
              
            this.totalCount=total;
            this.loadingShow=false;
      })
      .catch((err)=>{

      })
          
    },

    getMoreFn(){
        this.currentPage+=1;
        this.getDataFn();
    },

    initAllDataFn(){
        this.loadingKey=true
        this.currentPage=1
        this.totalCount=0
        this.hasMore=true
        this.assetsDetailData=[]
        this.queryData={
          assetID:'',
          assetName:'',
          memorydepart:'',
          memorylocDept:'',
          mac:'',
          status:'',
        };
        this.queryDataNow={}
    },

    getSearchConfigFn(){
      // this.queryData.memorylocDept=this.locDept;
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.getDataFn();
    }
   
  },

  beforeRouteEnter(to, from, next) {
    to.meta.isBack=true;
    next();
  },


  beforeRouteLeave(to, from, next) {
    window.sessionStorage.setItem('mobileScroll',this.mobileScroll);
    next();
  },
  


  created(){
      this.isFirstEnter=true;
  },
  mounted(){
   
  },

  activated() {
    this.showPopover=false;
    let mobileScroll=window.sessionStorage.getItem('mobileScroll');
    let mobileWrap=document.getElementById('mobile_wrap');
    mobileWrap.scrollTop=mobileScroll

    if(!this.$route.meta.isBack || this.isFirstEnter){
        this.initAllDataFn();
        this.getSearchConfigFn();
        
    }
    this.$route.meta.isBack=false
    this.isFirstEnter=false;
  },

  deactivated(){
    this.showPopover=false;
  },

  
}
</script>

<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
</style>