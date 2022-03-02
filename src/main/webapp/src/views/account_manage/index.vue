<template>
  <div class='account_manage_wrap'>
       <div class="row_wrap hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">账户明细表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                    <Button type="primary" icon="md-add" @click="toCreateFn">新增账户</Button>
                    <Button type="error" icon="md-trash" @click="deleteFn">删除账户</Button>
              </div>
              <div class="data_table_top_right">
                    <div class="pagination_wrap">
                        <Page show-elevator show-total :total="totalCount" @on-change="changePageFn" :current="currentPage" :page-size="pageSize"/>
                    </div>
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="accountColumn" :data="accountData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                    <template slot-scope="{ row }" slot="applyDept">
                      {{row.applyDept? row.applyDept : '--'}}
                    </template>
                    <template slot-scope="{ row }" slot="role">
                        <Tag v-if="row.role==='admin'" color="success">管理员</Tag>
                        <Tag v-else  color="warning">操作员</Tag>
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
import {Icon,Table ,Tag ,Page,Button,Modal,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);

//引入退出登录返回到登录页面方法
import { exitFn } from "../../api/logout.js"

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
            {key: 'username',title: '姓名',},
            {key: 'phone',title: '手机号',},
            {key: 'department',title: '部门',},
            {key: 'applyDept',title: '申请科室',slot:'applyDept'},
            {key: 'role',title: '权限',slot:'role',width:100},
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

    toCreateFn(){
        this.$router.push({
          path:'/account_create'
        })
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

    deleteCertainFn(){

     this.deleteModal=true;

     var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].username)
      }
      
      this.$axios.post(process.env.API_HOST+'huaxi/user/del?username='+list)
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

        this.$axios.get(process.env.API_HOST+'huaxi/user/query')
        .then((res)=>{
          
          // console.log(res.data)
          this.accountData=res.data;
          this.totalCount=res.data.length;
          this.tableLoading=false;
          this.$Loading.finish();

        })
        .catch((err)=>{
          console.log(err)
        })
    }
  },
  created(){},
  mounted(){
      this.getDataFn();
  },
   beforeDestroy(){
      this.$Loading.finish();
  }
}
</script>
<style scoped src="../../assets/css/common/default.css"></style>
<style scoped src="../../assets/css/common/table.css"></style>
<style scoped>
</style>