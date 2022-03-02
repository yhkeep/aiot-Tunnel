<template>
  <div class='mobile_check_manage_wrap manage_list_wrap custom_flex_column_wrap'>
   
    <div class="mobile_search_wrap">
        <div v-if="!selectMode" class="mobile_search_box" ref="mobileSearchBoxFixed">
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
              <!-- <div class="mobile_search_box_right_tools" style="width:80px">
                <Button size="small" round type="primary"  @click="checkFn">盘点</Button>
              </div> -->
              <div class="mobile_search_box_right_tools" style="width:40px">
                <!-- <Icon name="clock-o"  @click="toCheckHistoryFn"/> -->
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

      <div class="assets_table_wrap" ref="assetsTableWrap">
        <div class="assets_table_wrap_content  clearfix">
            <div class="assets_table_wrap_left">
                <ul class="assets_table_ul" >
                    <li class="assets_table_li assets_table_li_name">
                        <div class="assets_table_li_checkbox" v-if="selectMode">
                          <Checkbox  v-model="checkAll" @click="checkAllFn" checked-color="#07c160" ></Checkbox>
                        </div>
                        <div v-else class="assets_table_item"><p>序号</p></div>
                    </li>
                    <CheckboxGroup v-model="checkedArr" ref="checkboxGroup" v-if="selectMode">
                      <li class="assets_table_li" v-for="(item,index) in assetsDetailData" :key="index">
                          <div class="assets_table_li_checkbox">
                            <Checkbox :name='item.assetID'  checked-color="#07c160" @click="(e)=>{e.stopPropagation();}"></Checkbox>
                          </div>
                      </li>
                    </CheckboxGroup>
                    <li v-else class="assets_table_li" v-for="(item,index) in assetsDetailData" :key="index">
                         <div class="assets_table_item"><p>{{index+1}}</p></div>
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
                    <li class="assets_table_li clearfix" v-for="(item,index) in assetsDetailData" :key="index">
                       
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
            <Pagination 
              v-model="currentPage" 
              :total-items="totalCount" 
              :show-page-size="2"
              prev-text="上一页"
              next-text="下一页"
              force-ellipses
              mode="simple"
              @change="changePageFn"
              v-show="showPagination&&assetsDetailData.length"
            />

            <Loading v-show="loadingShow" size="24px">加载中...</Loading>
            <p v-show="!loadingShow&&!assetsDetailData.length">暂无数据</p>
        </div>

       
      </div>

      <Popup v-model="showSearchPane" position="top" round >
          <div class="query_data_wrap">
            <div class="query_data_box">
              <Field clearable label="资产编号"  v-model="queryData.assetID" placeholder="请输入资产编号"/>
              <Field clearable label="资产名称" center  v-model="queryData.assetName" placeholder="请输入资产名称">
                    <template #button>
                      <Button size="small" type="primary"  @click="showInputSearchFn('assetName')">云数据</Button>
                    </template>
              </Field>
              <Field clearable label="部门名称" center  v-model="queryData.memorydepart" placeholder="请输入部门名称">
                    <template #button>
                        <Button size="small" type="primary"  @click="showInputSearchFn('departmentroom')">云数据</Button>
                    </template>
              </Field>
              <Field  clearable label="所在科室" center  v-model="queryData.memorylocDept" placeholder="请输入所在科室">
                    <template #button>
                        <Button  size="small" type="primary"  @click="showInputSearchFn('locDept')">云数据</Button>
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
          <Picker  :columns="statusData" show-toolbar title="请选择状态"@cancel="statusDialogShow=false" @confirm="getStatusFn" :visible-item-count=5 />
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

      <div :class="selectMode ? 'header_tools_wrap show':'header_tools_wrap'">
          <!-- <span class="tools_wrap_operation" @click="exitSelectFn">完成</span> -->
          <Button  round size="small" type="info"  @click="exitSelectFn">完成</Button>
      </div>
      <div :class="selectMode ? 'footer_tools_wrap show' : 'footer_tools_wrap'">
          <!-- <span class="tools_wrap_operation warning" @click="toCheckFn">盘点</span> -->
          <Button  round size="small" type="warning"  @click="toCheckFn">盘点</Button>
      </div>

  </div>
</template>

<script>

import {Button,Tag,CheckboxGroup ,Checkbox ,Icon,Dialog,Toast,Loading,Pagination,Field,Search,Popup,Notify,Picker,Popover} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/tag/style';
import 'vant/lib/checkbox/style';
import 'vant/lib/checkbox-group/style';
import 'vant/lib/dialog/style';
import 'vant/lib/toast/style';
import 'vant/lib/loading/style';
import 'vant/lib/pagination/style';
import 'vant/lib/field/style';
import 'vant/lib/popup/style';
import 'vant/lib/search/style';
import 'vant/lib/icon/style';
import 'vant/lib/notify/style';
import 'vant/lib/picker/style';
import 'vant/lib/popover/style';
import NET_PORT from "../../../api/port.js"
import {getToken} from "../../../utils/auth.js"
import initTimeFn from "../../../utils/initTime.js"
import statusData from "../../../assets/data/status.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'mobile_check_manage',
  components:{
        Button,
        Tag,
        CheckboxGroup,
        Checkbox,
        Dialog,
        Toast,
        Loading,
        Pagination,
        Field,
        Popup,
        Search,
        Icon,
        Notify,
        Picker,
        Popover
  },
  props:{},
  data(){
    return {
      inputData:[],
      inputDataAll:[],
      inputSearchValue:'',
      activeKey:'',
      showInputSearch:false,

      statusColor:statusColor,
      user:this.$store.state.user,
      // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
        // totalCount: 0,
        // pageSize: 10,
        // currentPage: 1,

        currentPage:1,
        totalCount:0,
        hasMore:true,
        checked:true,
        assetsDetailData:[],

        deviceDetailData:[],
        checkedArr:[],
        checkAll:false,
        loadingShow:true,
        showPagination:false,
        queryData:{
          assetID:'',
          assetName:'',
          memorydepart:'',
          memorylocDept:'',
          mac:'',
          status:'',
        },
        queryDataNow:{},
        websock:null,
        value:'',
        showSearchPane:false,

        showSearchPane:false,
        statusDialogShow:false,
        statusData:statusData,

        selectMode:false,
        showPopover:false,
        actions: [{ text: '盘点历史',icon:'todo-list-o' }, { text: '选择' ,icon:'certificate'}],
    }
  },
  watch:{

    deviceDetailData(){
      this.deviceDetailData.forEach((item,index)=>{
        this.assetsDetailData.forEach((m,n)=>{
          if(item.Mac===m.mac){
            m.location=item.Location;
          }
        })
      })
    },

    checkedArr(){
      if(this.checkedArr.length&&this.checkedArr.length===this.assetsDetailData.length){
        this.checkAll=true;
      }else{
        this.checkAll=false;
      }
    },

    mobileScroll(){
      if(!this.selectMode){
        let scrollTop=this.mobileScroll;
        if(scrollTop>54){
          this.$refs.mobileSearchBoxFixed.classList.add('show');
        }else if(scrollTop===0){
          this.$refs.mobileSearchBoxFixed.classList.remove('show');
        }
      }
        
    }

  },
  computed:{
    assetsDetailDataNow(){
      return this.assetsDetailData
    },
    mobileScroll(){
      return this.$store.state.mobileScroll
    }
  },
  methods:{

     onSelect(action) {
        switch(action.text){
                case '盘点历史' :
                this.toCheckHistoryFn();
                break;
                case '选择':
                   this.enterSelectFn();
                break;
        }
        
    },
    enterSelectFn(){
      this.selectMode=true;
      this.$refs.assetsTableWrap.style.paddingBottom="60px"
    },

    exitSelectFn(){
        this.showPopover=false;
        this.selectMode=false;
        this.checkedArr=[]
        this.$refs.assetsTableWrap.style.paddingBottom="0"
    },

    toCheckFn(){
      let arr=[];
      this.checkedArr.forEach((item,index)=>{
        this.assetsDetailData.forEach((m,n)=>{
          if(item===m.assetID){
            arr.push(m)
          }
        })
      })
      if(arr.length){
              
          Dialog.confirm({
            title: '提示！',
            message: '<span style="color:#ee0a24;">你确定要盘点这'+arr.length+'个资产吗？</span>',
            confirmButtonColor:'#ee0a24',
          }).then(() => {
            this.checkCertainFn(arr);
          }).catch(() => {

          });
            
      }else{
          this.$notify({
              type:'warning',
              message: '请至少选择一个资产！',
              duration: 2000
          });

      }
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

    getStatusFn(value) {
        this.queryData.status=value.text;
        this.statusDialogShow=false;
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

    showMoreMenuFn(){
      this.$emit('getMessage', true);
    },


    toCheckHistoryFn(){
      this.$router.push('/mobile/check_history')
    },

    openSearchPaneFn(){
      this.showSearchPane=true;
    },

    searchFn(){
        this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
        this.currentPage=1;
        this.assetsDetailData=[]
        this.checkedArr=[];
        this.checkAll=false;
        this.totalCount=0;
        this.getDataFn();
        this.showSearchPane=false;
    },

    checkAllFn(){
        if(this.checkAll===false){
            this.$refs.checkboxGroup.toggleAll();
          
        }else{
            this.$refs.checkboxGroup.toggleAll(true);
        }
    },

    checkCertainFn(arr){
        var datas=[]
        arr.forEach((item,index)=>{
          var obj={
            "assetID":item.assetID.trim(),
          };

          datas.push(obj);
        })

        var checkLoading=this.$toast.loading({
          message: '正在盘点...',
          forbidClick: true,
          duration:0
        });

         this.$axios.post(NET_PORT.assetsCheck+"checkpep="+this.user,datas)
          .then((res)=>{
            if(res.data.code===0){
                checkLoading.clear();
                this.$toast.success('盘点成功！');
                this.exitSelectFn();
                this.assetsDetailData=[]
                this.totalCount=0;
                this.getDataFn();
            }else{
                checkLoading.clear();
                this.$toast.fail('盘点失败！');
            }
          })
          .catch((error)=>{
              checkLoading.clear();
            
          })

    },



     getDataFn(){
            // this.$emit('setLoadingFn', true);
            this.loadingShow=true;
            this.assetsDetailData=[];

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

            this.$axios.post(NET_PORT.assetsQuery+'?currentPage='+'&'+configData)
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


                  this.totalCount=total;
                  this.loadingShow=false;
                  this.showPagination=true;
            })
            .catch((err)=>{

            })
      },

      changePageFn(){
        this.checkedArr=[]
        this.checkAll=false;
        this.getDataFn();
      },
      transWebsocketFn(data){
          let dataArr=[];
          //注意：长连接我们是后台直接1秒推送一条数据
          let toArr=data.split('{')
          toArr.splice(0,1);
        
          toArr.forEach((item,index)=>{
              let k=item.split('}')
              k.splice(k.length-1,1);
              let m=k[0].split(',')
              let itemObj={};
              m.forEach((g,h)=>{
                  let x=g.split('=');
                  itemObj[x[0].trim()]=x[1];
              })
              
              dataArr.push(itemObj)
          })

          return dataArr;
    },

    initWebSocket(){ 
        this.websock = new WebSocket(NET_PORT.mapSocket+'/'+getToken());
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
    },

    websocketonmessage(e){
        var dataArr=this.transWebsocketFn(e.data)
        this.deviceDetailData=dataArr;
    },

    getSearchConfigFn(){
      // this.queryData.memorylocDept=this.locDept;
      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
      this.getDataFn()
    }
      
  },
  created(){
     
  },
  mounted(){
    this.getSearchConfigFn();
  },

  activated(){
    
    this.initWebSocket()
    this.exitSelectFn();
  },
  deactivated(){
    this.exitSelectFn();
    this.websock.close()
  },

}
</script>
<style lang="scss" scoped>
    @import "../../../assets/scss/mobile/common/manage_list.scss";
    
</style>