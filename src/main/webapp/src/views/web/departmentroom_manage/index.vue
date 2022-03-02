<template>
  <div class='departmentroom_manage_wrap'>
      <div class="hover_animat custom_bg_color_white">
        <div class="row_box">
            <h2 class="row_title">部门列表</h2>
            <div class="data_table_top clearfix">
              <div class="data_table_top_left">
                  <Button @click="createData.show=true;" type="primary" shape="circle" icon="md-add">新增部门</Button>
                  <Button type="error" shape="circle" icon="md-trash" @click="deleteFn">删除部门</Button>
              </div>
              <div class="data_table_top_right">
                  
              </div>
            </div>

            <div class="table_scroll_wrap">
                <Table style="min-width:1000px" stripe :columns="departmentroomColumn" :data="departmentroomData" size='large' :loading="tableLoading" @on-selection-change="checkAllGroupChange">
                   
                    <template slot-scope="{ row }" slot="operation">
                        <Button shape="circle" type="default">编辑</Button>
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
    <Modal v-model="createData.show" title="部门新增" :mask-closable="false" :closable="false" width="600" okText="确定新增">
        <Input  v-model="createData.data.departmentroomName" placeholder="部门名称" />
    </Modal>
  </div>
</template>

<script>
import Vue from "vue"
import {Icon,Table ,Tag ,Page,Button,Modal,Spin,Input} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Table', Table);
Vue.component('Tag', Tag);
Vue.component('Page', Page);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('Spin',Spin);
Vue.component('Input',Input);

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

        createData:{
            show:false,
            data:{
                departmentroomName:''
            }
        },
        selection:[],

        deleteModal:false,
        achieveDelete:false,
        errorDelete:false,

        departmentroomData:[],
        departmentroomColumn:[
            {type: 'selection',width:100},
            {key: 'departmentroom',title: '部门名称',},
            {key: 'operation',title: '操作',slot:'operation'},
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
          content:'<h2 style="color:#f94040">你确定要删除这'+this.selection.length+'个部门吗？</h2>',
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

   

    deleteCertainFn(){

     this.deleteModal=true;

     var list=[];
      for(var i=0;i<this.selection.length;i++){
        list.push(this.selection[i].username)
      }
      
     
      
    },

    getDataFn(){
        this.$Loading.start();
        setTimeout(()=>{
            this.$Loading.finish();
            this.tableLoading=false;
        },200)
        
    },

   
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