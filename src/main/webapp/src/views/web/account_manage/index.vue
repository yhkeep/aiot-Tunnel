<template>
  <div class='account_manage_wrap'>
    <div class="hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">账户明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                  <Button to="/web/account_create"  type="primary" shape="circle" icon="md-add">新增账户</Button>
                  <Button type="error" shape="circle" icon="md-trash" @click="deleteFn">删除账户</Button>
                  <!-- <Button to="/web/departmentroom_manage" type="warning" shape="circle" icon="md-settings" >部门配置</Button> -->
              </div>
              <div class="data_table_top_right">
                  
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="accountColumn" :data="accountData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    <template slot-scope="{ row }" slot="username">
                      <!-- <a @click="toDetailFn(row.username)">{{row.username}}</a> -->
                      {{row.username}}
                    </template>
                    <template slot-scope="{ row }" slot="departmentroom">
                        <Dropdown transfer style="margin-right: 20px" trigger="hover">
                            <Button shape="circle" type="primary">
                                查看
                                <Icon type="ios-arrow-down"></Icon>
                            </Button>
                            <div slot="list">
                                <div style="width:250px;padding:20px;">
                                    <Tag v-for="(item,index) in row.departmentroom.split(',')" :key="index" color="default">{{item}}</Tag>
                                </div>
                            </div>
                        </Dropdown>
                    </template>
                    <template slot-scope="{ row }" slot="role">
                        <Tag v-if="row.role==='admin'" color="success">管理员</Tag>
                        <Tag v-else  color="warning">操作员</Tag>
                    </template>
                    <template slot-scope="{ row }" slot="operation">
                        <Button shape="circle" type="default" icon="md-create" @click="toEditFn(row.username)">编辑</Button>
                    </template>
                </Table>
            </div>
            <div class="data_table_pagination_wrap">
                <!-- <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/> -->
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
import {Icon,Table ,Tag ,Page,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);

import { exitFn } from "../../../api/logout.js"
import NET_PORT from "../../../api/port.js"

export default {
  name:'account_manage',
  components:{},
  props:{},
  data(){
    return {
        tableLoading:true,
      
        totalCount: 0,
        pageSize: 10,
        currentPage: 1,

        selection:[],

        deleteModal:false,
        achieveDelete:false,
        errorDelete:false,

        accountData:[],
        accountColumn:[
            {key: 'account',title: '选择',type: 'selection',width:100},
            {key: 'username',title: '账户名',slot:'username'},
            {key: 'phone',title: '手机号',},
            {key: 'departmentroom',title: '授权部门',slot:'departmentroom',width:150},
            // {key: 'locDept',title: '所在科室名称',},
            {key: 'role',title: '账户级别',slot:'role',width:100},
            {key: 'operation',title: '操作',slot:'operation',width:150},
        ]
    }
  },
  watch:{},
  computed:{},
  methods:{
    checkAllGroupChange(selection){
      this.selection=selection;
    },

    changePageFn(page){
      this.tableLoading=true;
      this.currentPage=page
    },

    deleteFn(){
      if(this.selection.length>0){
        this.$Modal.confirm({
          title:'删除提示',
          okText:'确定删除',
          cancelText:'取消',
          content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个账户吗？</h2>',
          onOk:()=>{
                this.deleteCertainFn();
          },
        })
      }else{
        this.$Message.error({
          content:'请至少选择一项!',
          duration:1,
        })
      }
    },

    successFn(){
      this.achieveDelete=true;
      setTimeout(()=>{
          this.deleteModal=false;
          //删除成功后隔1秒隐藏模态框并更新数据
          this.currentPage=1;
          this.getDataFn();
          
          setTimeout(()=>{
              this.achieveDelete=false;
              this.errorDelete=false;
          },500)
          
      },1000)
    },

    errorFn(){
      this.errorDelete=true;
      setTimeout(()=>{
          this.deleteModal=false;
          setTimeout(()=>{
              this.achieveDelete=false;
              this.errorDelete=false;
          },500)
          
      },1000)
    },

    deleteMySelfFn(){
        this.achieveDelete=true;
        setTimeout(()=>{
            this.deleteModal=false;
            //删除成功后隔1秒隐藏模态框并更新数据
            this.currentPage=1;
            
            setTimeout(()=>{
                this.achieveDelete=false;
                this.errorDelete=false;

                this.$Modal.warning({
                  title: '提示！',
                  content: '当前登录用户已删除，需要重新登录！',
                  okText: '立即登录',
                  onOk: () => {
                    exitFn()
                  }
                })

            },500)
            
        },1000)
    },

    deleteCertainFn(){

     this.deleteModal=true;

     var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].username)
      }
      
      this.$axios.post(NET_PORT.accountDelete+list)
      .then((res)=>{
          if(res.data.msg==='ok'){
            
            this.successFn();
          }else if(res.data.msg==='remove_myself'){
            this.deleteMySelfFn();
          }
          else if(res.data.msg==='failed'){
            this.errorFn();
          }

      })
      .catch((err)=>{
           this.errorFn();
      })
      
    },

    getDataFn(){
        this.$Loading.start();
        this.$axios.get(NET_PORT.accountQuery)
        .then((res)=>{
          // console.log(res)
          this.accountData=res.data;
          this.totalCount=res.data.length;
          this.tableLoading=false;
          this.$Loading.finish();
        })
        .catch((err)=>{
          this.$Loading.error();
        })
    },

    toEditFn(username){
      this.$router.push('/web/account_edit/'+username)
    },

    // toDetailFn(username){
    //   this.$router.push('/web/account_detail/'+username)
    // }
  },
  created(){},
  mounted(){
      this.getDataFn();
  },
  
}
</script>



<style lang='scss' scoped>
@import '../../../assets/scss/web/common/table.scss';

</style>