<template>
  <div class='assets_manage_wrap'>
    <div class="hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">按条件筛选</h2>
          <div class="form_wrap">
              <div class="form_item">
                  <Row :gutter="20">
                      <Col span="8">
                        <h3>资产名称</h3>
                        <Input v-model="queryData.assetName" placeholder="资产名称" />
                      </Col>
                      <Col span="8">
                        <h3>所属部门</h3>
                        <Select v-model="queryData.department" clearable>
                            <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                        </Select>
                      </Col>
                      <Col span="8">
                        <h3>申请科室</h3>
                        <Input v-model="queryData.applyDept" placeholder="申请科室" />
                      </Col>
                  </Row>
              </div>

              <div class="form_item">
                  <Row :gutter="20">
                      <Col span="12">
                      <h3>可选择显示项目</h3>
                            <CheckboxGroup v-model="chooseArr">
                                <Checkbox label="mac地址"></Checkbox>
                                <Checkbox label="购买日期"></Checkbox>
                                <Checkbox label="金额"></Checkbox>
                                <Checkbox label="电量"></Checkbox>
                            </CheckboxGroup>
                      </Col>
                    
                  </Row>
              </div>
          </div>
          <div class="submit_btn_wrap">
                <Button type="primary" @click="searchAssetsFn">搜索</Button>
                <Button type="error" @click="clearAssetsFn">清空</Button>
          </div>
      </div>
    </div>
   
    <div class="row_wrap hover_animat custom_bg_color_white">
      <div class="row_box">
          <h2 class="row_title">资产明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                <Button type="primary" icon="md-add" @click="toCreateFn">新增资产</Button>
                <Button v-if="$store.state.role==='admin'" type="error" icon="md-trash" @click="deleteFn">删除资产</Button>
                <Button type="success" icon="md-archive" @click=exportExcelFn>导出资产总表</Button>
                <div class="current_query_box">
                  <span>当前展示结果：</span> <h3>{{queryDataNowResult}}</h3>
                </div>
              </div>
              <div class="data_table_top_right">
                  <div class="pagination_wrap">
                    <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                  </div>
              </div>
            </div>

            <div class="table_scroll_wrap">
              <Table style="min-width:1500px" stripe :columns="assetsDetailColumn" :data="assetsDetailData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                  <template slot-scope="{row}" slot="assetID">
                    <a @click="toDetailFn(row.assetID)">{{row.assetID}}</a>
                  </template>

                  <template slot-scope="{row}" slot="applyDept">
                      <span>{{row.applyDept ? row.applyDept : '--'}}</span>
                  </template>
                  <!-- <template slot-scope="{row}" slot="locDept">
                      <span>{{row.locDept ? row.locDept : '--'}}</span>
                  </template> -->
                  <template slot-scope="{row}" slot="location">
                      <span>{{row.location ? row.location : '--'}}</span>
                  </template>
                  <!-- <template slot-scope="{row}" slot="operatingRoom">
                      <span>{{row.operatingRoom ? row.operatingRoom : '--'}}</span>
                  </template> -->
                  <template slot-scope="{row}" slot="check">
                      <span>{{row.check ? row.check : '--'}}</span>
                  </template>

                  <template slot-scope="{row}" slot="amount">
                      <span>{{Number(row.amount)? Number(row.amount) : '--'}}</span>
                  </template>
                  
                  <template slot-scope="{row}" slot="buyDate">
                    <span>2019-01-01</span>
                  </template>
                  
                  <template slot-scope="{row}" slot="money">
                    <span>10000</span>
                  </template>
                  <template slot-scope="{row}" slot="electric">
                      <span>{{row.electric}}%</span>
                  </template>

                  <template slot-scope="{ row }" slot="status">
                      <Tag v-if="row.status==='正常'" color="success"><span @click="statusFn(row.status)">{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='在修'" color="warning"><span @click="statusFn(row.status)">{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='待报废'" color="primary"><span @click="statusFn(row.status)">{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='已报废'" color="error"><span @click="statusFn(row.status)">{{row.status}}</span></Tag>
                      <Tag v-else-if="row.status==='--'"  color="default"><span>{{row.status}}</span></Tag>
                      <Tag v-else  color="default"><span>{{row.status}}</span></Tag>
                  </template>
                
              </Table>
            </div>
      </div>
    </div>



      <Modal v-model="deleteModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
          <div style="text-align:center;padding:20px 0">
              <Spin fix v-if="!achieveDelete&&!errorDelete">
                  <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                  <div>正在删除</div>
              </Spin>
              <Spin fix v-if="achieveDelete" style="color:#00ad19">
                  <Icon type="ios-checkmark-circle" size=18 />
                  <div>删除成功</div>
              </Spin>
              <Spin fix v-if="errorDelete" style="color:#f72b2b">
                  <Icon type="ios-close-circle" size=18 />
                  <div>删除失败</div>
              </Spin>
          </div>
      </Modal>


  </div>
</template>

<script>
import Vue from "vue"
import { Row,Col,Icon,Table ,Tag ,DatePicker,Select,Option,Page,Input,CheckboxGroup,Checkbox,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Page', Page);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('CheckboxGroup',CheckboxGroup);
Vue.component('Checkbox',Checkbox);

import departData from "../../assets/data/depart.js"
export default {
  name:'assets_manage',
  components:{},
  props:{},
  data(){
    return {
          departData:departData,
          isFirstEnter:false,
          chooseArr: [],
          queryData:{
            assetName:'',
            department:'',
            applyDept:'',
          },
          queryDataNow:{},
          chooseArrNow:[],

          tableLoading:true,

          selection:[],
          
          totalCount: 0,
          pageSize: 10,
          currentPage: 1,
            
          deleteModal:false,
          achieveDelete:false,
          errorDelete:false,

          assetsDetailColumnInit:[
            
            {
                title: '资产id',
                key: 'assetID',
                slot:'assetID',
                
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
                title: '部门',
                key: 'department'
            },
            {
                title: '规格型号',
                key: 'type'
            },
            {
                title: '品牌',
                key: 'brand'
            },
            {
                title: '申请科室',
                key: 'applyDept',
                slot: 'applyDept',
            },
            // {
            //     title: '放置科室',
            //     key: 'locDept',
            //     slot: 'locDept',
            // },
            {
                title: '坐落位置',
                key: 'location',
                slot: 'location',
            },
            // {
            //     title: '手术室',
            //     key: 'operatingRoom',
            //     slot: 'operatingRoom',
            // },
           
            {
                title: '数量',
                key: 'amount',
                slot:'amount'
            },
            {
                title: '单位',
                key: 'unit'
            },
           
            {
                title: '盘点日期',
                key: 'check',
                slot: 'check',
            },
            
            {
                title: '状态',
                key: 'status',
                slot:'status',
                width:100
            },

        ],

        assetsDetailColumn:[],

        assetsDetailData:[],

    }
  },
  watch:{},
  computed:{
    //生成当前展示结果
    queryDataNowResult(){
      var result_1=this.queryDataNow.assetName ? this.queryDataNow.assetName : "--";
      var result_2=this.queryDataNow.department ? this.queryDataNow.department : "--";
      var result_3=this.queryDataNow.applyDept ? this.queryDataNow.applyDept : "--";
      var configData='';
      if(this.chooseArrNow.length>0){
        this.chooseArrNow.forEach((item,index)=>{
          configData+=item+"&";
        })
        //截掉最后一个&符号
        configData=' / '+configData.slice(0,configData.length-1)
      }
       return result_1+" / "+result_2+" / "+result_3+configData;
    }
  },
  methods:{
     //点击状态去到不同页面
    statusFn(status){
      if(status==='正常'){
        this.$router.push('/device_manage');
      }else {
        this.$router.push('/damage_manage');
      }
    },


    //导出总excel
    exportExcelFn(){

        let href = process.env.API_HOST+'huaxi/poi/export'
        let a = document.createElement('a')
        a.setAttribute('download', '')
        a.setAttribute('href', href)
        a.click()

    },

     //点击多选按钮
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    //添加和删除指定的显示选项
    filterChooseFn(){
      // console.log(this.chooseArr)
      //先让属性数组等于必须显示的那些元素；
      this.assetsDetailColumn=JSON.parse(JSON.stringify(this.assetsDetailColumnInit))

      if(this.chooseArrNow.length){

        this.chooseArrNow.forEach((item,index)=>{
          switch(item){
            case 'mac地址' :
              this.assetsDetailColumn.push( 
                {
                    title: 'mac地址',
                    key: 'mac'
                }
              )
            break;
            case '购买日期' :
              this.assetsDetailColumn.push( 
                {
                    title: '购买日期',
                    key: 'buyDate',
                    slot:'buyDate'
                }
              )
            break;
            case '金额' :
              this.assetsDetailColumn.push( 
                {
                    title: '金额',
                    key: 'money',
                    slot:'money'
                }
              )
            break;
            case '电量' :
              this.assetsDetailColumn.push( 
                {
                    title: '电量',
                    key: 'electric',
                    slot:'electric'
                }
              )
            default:
              return;

          }
        })
       
      }
     
    },


      //点击搜索
      searchAssetsFn(){
          this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));
          this.chooseArrNow=JSON.parse(JSON.stringify(this.chooseArr))
          this.currentPage=1;
          
          //过滤多选显示属性
          this.filterChooseFn();
          //获取数据
          this.getDataFn();
      },

      //清除搜索数据
      clearAssetsFn(){
          for(var i in this.queryData){
            this.queryData[i]="";
          }
          this.chooseArr=[];
      },

      //点击多选按钮
      checkAllGroupChange(selection){
        this.selection=selection;
      },

      //点击改变页码
      changePageFn(page){
        
        this.currentPage=page;
        //获取数据
        this.getDataFn();
      },

      //点击删除资产按钮
    deleteFn(){
        if(this.selection.length){
              this.$Modal.confirm({
                    title:'删除提示',
                    okText:'确定删除',
                    cancelText:'取消',
                    content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个资产吗？</h2>',
                    onOk:()=>{
                          this.deleteCertainFn();
                    },
                    
              })

        }else{
          return this.$Message.error({
                    content:"请选择至少一项!",
                    duration:2,
          })
        }
    },

    //点击确认删除
    deleteCertainFn(){
      this.deleteModal=true;

      var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].assetID)
      }
      
      this.$axios.get(process.env.API_HOST+'huaxi/del?assetID='+list)
      .then((res)=>{

          if(res.data.msg==='ok'){
            this.achieveDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                //删除成功后隔1秒隐藏模态框并更新数据
                this.currentPage=1;
                this.getDataFn('all');
                
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
            
           
            
          }else if(res.data.msg==='failed'){
            this.errorDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
          }

      })
      .catch((err)=>{
            this.errorDelete=true;
            setTimeout(()=>{
                this.deleteModal=false;
                setTimeout(()=>{
                    this.achieveDelete=false;
                    this.errorDelete=false;
                },500)
                
            },1000)
      })

    },

      //点击资产新增按钮
      toCreateFn(){
        this.$router.push({
          path:'/assets_create'
        })
      },

      //点击资产id去到资产详情页面
      toDetailFn(id){
        for(var i=0;i<this.assetsDetailData.length;i++){
          if(this.assetsDetailData[i].assetID===id){
            window.sessionStorage.setItem('detailData',JSON.stringify(this.assetsDetailData[i]))
          }
        }

        this.$router.push({
          path:'/assets_detail'
        })

    },

    //向服务器获取资产数据
    getDataFn(){
            this.tableLoading=true;
            this.$Loading.start();
            var configData='';
            for (var m in this.queryDataNow){
              if(!this.queryDataNow[m]){
                this.queryDataNow[m]='';
              }

              //将参数拼接到一起，裁掉左右空格
              configData+=m+'='+this.queryDataNow[m].trim()+'&'

            }
            
            var isShowMac=this.chooseArrNow.find((item,index)=>{return item==='mac地址'}) ? '1' : '0'
            var isShowBuyDate=this.chooseArrNow.find((item,index)=>{return item==='购买日期'}) ? '1' : '0'
            var isShowMoney=this.chooseArrNow.find((item,index)=>{return item==='金额'}) ? '1' : '0'
            var isShowElectric=this.chooseArrNow.find((item,index)=>{return item==='电量'}) ? '1' : '0'
            
            // console.log(isShowMac,isShowBuyDate,isShowMoney,isShowElectric)

            configData=configData+"isShowMac="+isShowMac+"&isShowBuyDate="+isShowBuyDate+"&isShowMoney="+isShowMoney+"&isShowElectric="+isShowElectric
            this.$axios.get(process.env.API_HOST+'huaxi/assetManagement/param?currentPage='+this.currentPage+'&'+configData)
            .then((res)=>{
                var initData=res.data;

                var total=Number(initData.pop().total);
                var timeTest=Number(initData.pop().timeTest);

                this.assetsDetailData=[];
                    for(var i=0;i<initData.length;i++){
                        for(var k in initData[i]){
                            if(initData[i][k]===''||initData[i][k]==='null'){
                              initData[i][k]='--'; 
                            };

                        }
                        this.assetsDetailData.push(initData[i])
                    }
                  this.totalCount=total;
                  this.tableLoading=false;
                  this.$Loading.finish()

            })
            .catch((err)=>{
              console.log(err)
              this.$Loading.error();
            })

    },


    //获取搜索配置
    getSearchConfigFn(){
      var queryData=JSON.parse(window.sessionStorage.getItem('queryData'));
    
      this.queryData={
        assetName:'',
        department:queryData.department ? queryData.department : '',
        applyDept:queryData.applyDept ? queryData.applyDept : '',
      }

      this.queryDataNow=JSON.parse(JSON.stringify(this.queryData));

      queryData.isShowMac ? this.chooseArr.push('mac地址') : '',
      queryData.isShowBuyDate ? this.chooseArr.push('购买日期') : '',
      queryData.isShowMoney ? this.chooseArr.push('金额') : '',
      queryData.isShowElectric ? this.chooseArr.push('电量') : '',

      this.chooseArrNow=JSON.parse(JSON.stringify(this.chooseArr))

      this.filterChooseFn();
      this.getDataFn()
    },

    authorityFn(){
      //判断角色类型，为表格添加选择项
      if(this.$store.state.role==="admin"){
          var obj=this.assetsDetailColumnInit.find((item,index)=>{
            return item.title==='选择'
          })
          // console.log(obj)
          //如果不存在多选这个属性就添加进去
          if(!obj){
            this.assetsDetailColumnInit.unshift(
              {
                title:'选择',
                slot:'selection',
                type: 'selection',
              },
            )
          }
         
      }else{
        
      }
    },

    initAllDataFn(){
      this.chooseArr=[]
      this.queryData={
        assetName:'',
        department:'',
        applyDept:'',
      }
      this.queryDataNow={}
      this.tableLoading=true
      this.selection=[]
      this.totalCount= 0
      this.pageSize= 10
      this.currentPage= 1
      this.deleteModal=false
      this.achieveDelete=false
      this.errorDelete=false
      this.assetsDetailColumn=[]
      this.assetsDetailData=[]

      //判断角色类型，是否添加多选属性
      this.authorityFn();
      this.assetsDetailColumn=JSON.parse(JSON.stringify(this.assetsDetailColumnInit))

    }


  },

  beforeRouteEnter(to, from, next) {
    // console.log("我是资产管理 beforeRouteEnter 方法");
   
    if(from.name=='assets_detail'){
      
      if(from.meta.needFresh){
        to.meta.isBack=false;
      }else{
        to.meta.isBack=true;
      }
        
    }

    next();
  },


  created(){
      // console.log('这是资产管理 created 方法')
      this.isFirstEnter=true;
  },
  mounted(){
    // console.log('这是资产管理 mounted 方法')
  },

  activated() {
    // console.log("这是资产管理 activated 方法");
    if(!this.$route.meta.isBack || this.isFirstEnter){
        //先清空数据，避免让用户看到之前缓存的数据
        this.initAllDataFn();

        //执行获取参数的方法
        this.getSearchConfigFn();

        // //获取数据
        // this.getDataFn('all');
    }
    // 恢复成默认的false，避免isBack一直是true，导致下次无法获取数据
    this.$route.meta.isBack=false
    // 恢复成默认的false，避免isBack一直是true，导致每次都获取新数据
    this.isFirstEnter=false;

  },

  deactivated(){
    // console.log("这是资产管理 deactivated 方法");
      this.$Loading.finish();
      
  },


}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/form_data.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
  
</style>
