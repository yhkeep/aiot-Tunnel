<template>
  <div class='mobile_temp_create_wrap'>
        <div class="temp_create_box assets_upload_box">
           
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="仪器编号" required v-model="createData.freezernumber" placeholder="请输入仪器编号"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="仪器名称" required v-model="createData.freezername" placeholder="请输入仪器名称"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field  label="部门名称" clearable required center v-model="createData.department" placeholder="请输入部门名称">
                      <template #button>
                          <Button size="small" type="primary" @click="departmentDialogShow=true">云数据</Button>
                      </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="所在位置"  v-model="createData.location" placeholder="请输入所在位置"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="mac地址" required v-model="createData.mac" placeholder="请输入mac地址"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="typeDialogShow=true">
                    <Field  label="仪器类型" readonly required v-model="createData.type" placeholder="请选择仪器类型"/>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="intervalDialogShow=true">
                    <Field  label="仪器记录时间间隔（分钟）" readonly required v-model="createData.saveinterval" placeholder="请选择仪器记录时间间隔"/>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>温度上限(℃)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="createData.tempTop" integer  min="-500" max="500" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>温度下限(℃)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="createData.tempBottom" integer  min="-500" max="500" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="cell_item" v-show="createData.type!=='GS1W'">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>湿度上限(%)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="createData.humTop" integer  min="0" max="100" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="cell_item" v-show="createData.type!=='GS1W'">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>湿度下限(%)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="createData.humBottom" integer  min="0" max="100" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="tools_wrap">
              <Button round class="tools_button" type="primary" @click="saveFn">保存</Button>
            </div>
        </div>

        <Popup v-model="typeDialogShow" position="bottom">
            <Picker  :columns="instrumentType" show-toolbar title="请选择仪器类型" @cancel="typeDialogShow = false" @confirm="getTypeFn" :visible-item-count=5 />
        </Popup>
        <Popup v-model="intervalDialogShow" position="bottom">
            <Picker  :columns="tempInterval" show-toolbar title="请选择间隔时间" @cancel="intervalDialogShow = false" @confirm="getIntervalFn" :visible-item-count=5 />
        </Popup>
        <Popup v-model="departmentDialogShow" position="bottom">
            <Picker  :columns="departmentroomArr" show-toolbar title="请选择部门" @cancel="departmentDialogShow = false" @confirm="getDepartmentFn" :visible-item-count=5 />
        </Popup>

  </div>
</template>

<script>
import {Button,Notify,Tag,Icon,Field,Dialog,Toast,Picker,Popup,Loading,Stepper,Search} from 'vant';
import 'vant/lib/button/style';
import 'vant/lib/notify/style';
import 'vant/lib/tag/style';
import 'vant/lib/icon/style';
import 'vant/lib/field/style';
import 'vant/lib/dialog/style';
import 'vant/lib/picker/style';
import 'vant/lib/popup/style';
import 'vant/lib/toast/style';
import 'vant/lib/loading/style';
import 'vant/lib/stepper/style';
import 'vant/lib/search/style';
import NET_PORT from "../../../api/port.js"
import formatScanCodeFn from "../../../utils/formatScanCode.js"
import instrumentType from "../../../assets/data/instrumentType.js"
import tempInterval from "../../../assets/data/tempInterval.js"
export default {
  name:'mobile_temp_create',
  components:{
      Button,
      Tag,
      Icon,
      Field,
      Dialog,
      Picker,
      Popup,
      Toast,
      Loading,
      Stepper,
      Search
  },
  props:{},
  data(){
    return {
      // locDept:this.$store.state.locDept==='超级管理员' ? '' :(this.$store.state.locDept==='' ? '-' :this.$store.state.locDept),
      typeDialogShow:false,
      intervalDialogShow:false,
      departmentDialogShow:false,
      createData:{
          freezernumber:'',
          freezername:'',
          department:'',
          location:'',
          mac:'',
          type:'',
          tempTop: 0,
          tempBottom: 0,
          humTop: 0,
          humBottom: 0,
          saveinterval:'10',
      },
      instrumentType:instrumentType,
      tempInterval:tempInterval,
      departmentroomArr:[],
    }
  },
  watch:{},
  computed:{},
  methods:{
    getTypeFn(value){
      this.createData.type=value.key;
      this.typeDialogShow=false;
      this.instrumentChangeFn(value.key)
    },
    getIntervalFn(value){
      this.createData.saveinterval=value.key;
      this.intervalDialogShow=false;
    },
    getDepartmentFn(value){
      this.createData.department=value.key;
      this.departmentDialogShow=false;
    },

    instrumentChangeFn(value){
        this.instrumentType.forEach((item,index)=>{
            if(item.key===value){
                for(let i in item.range){
                    this.createData[i]=item.range[i];
                }
            }
        })
    },

    saveFn(){

      if(this.createData.freezernumber.trim()&&this.createData.department.trim()&&this.createData.freezername.trim()&&this.createData.mac.trim()&&this.createData.type.trim()){

            if(this.createData.tempBottom!==null&&this.createData.tempTop!==null){
                    if(this.createData.tempBottom===this.createData.tempTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '温度上下限不能相等！',
                            duration: 2000
                        });
                        return
                    }
                    if(this.createData.tempBottom>this.createData.tempTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '温度上限不能小于下限！',
                            duration: 2000
                        });
                        return
                    }
                }

                if(this.createData.humBottom!==null&&this.createData.humTop!==null){
                    if(this.createData.humBottom===this.createData.humTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '湿度上下限不能相等！',
                            duration: 2000
                        });
                        return 
                    }
                    if(this.createData.humBottom>this.createData.humTop){
                        this.$notify({
                            type:'warning',
                            message: '湿度上限不能小于下限！',
                            duration: 2000
                        });
                        return
                    }
                }

                let uploadLoading=Toast.loading({
                    message: '正在保存...',
                    forbidClick: true,
                    duration:0
                });

                let datas={
                    freezernumber:this.createData.freezernumber.trim(),
                    freezername :this.createData.freezername.trim(),
                    department:this.createData.department.trim(),
                    location:this.createData.location.trim(),
                    mac:formatScanCodeFn(this.createData.mac.trim()),
                    type:this.createData.type,
                    temperaturefitted:this.createData.tempBottom+'~'+this.createData.tempTop,
                    humidityfitted:this.createData.humBottom+'~'+this.createData.humTop,
                    saveinterval:this.createData.saveinterval,
                    address:1,
                }

                 this.$axios.post(NET_PORT.tempCreate,datas)
                .then((res)=>{
                    if(res.data.code===0){
                        uploadLoading.clear();
                        Toast.success('保存成功！');
                        setTimeout(()=>{
                          this.$router.go(-1)
                        },2000)
                    }else{
                        uploadLoading.clear();
                        Toast.fail('保存失败！');
                    }
                })
                .catch((err)=>{
                    uploadLoading.clear();
                    Toast.fail('保存失败！');
                })
              
            }else{
                this.$notify({
                    type:'warning',
                    message: '请补充完整必填信息！',
                    duration: 2000
                });
            }
    }
  },
  created(){
      this.$emit('setTitle','温湿度仪器新增');
      let arr=[];
      this.$store.state.departmentroom.split(',').forEach((item,index)=>{
          arr.push(
              {
                  key:item,
                  text:item,
              }
          )
      })
      this.departmentroomArr=arr;
      // if(this.locDept!==''){
      //     this.createData.department=this.locDept;
      // }
  },
  mounted(){}
}
</script>
<style lang="scss" scoped>
 @import "../../../assets/scss/mobile/common/assets_upload.scss";
</style>