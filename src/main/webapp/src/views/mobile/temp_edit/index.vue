<template>
  <div class='mobile_temp_edit_wrap'>
       <div class="temp_edit_box assets_upload_box">
           
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable  label="仪器编号" required v-model="tempData.freezernumber" placeholder="仪器编号"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="仪器名称" required v-model="tempData.freezername" placeholder="请输入仪器名称"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field  label="部门名称" clearable required center v-model="tempData.department" placeholder="请输入部门名称">
                      <template #button>
                          <Button size="small" type="primary" @click="departmentDialogShow=true">云数据</Button>
                      </template>
                    </Field>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable label="所在位置"  v-model="tempData.location" placeholder="请输入所在位置"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                    <Field clearable disabled label="mac地址" required v-model="tempData.mac" placeholder="请输入mac地址"/>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="typeDialogShow=true">
                    <Field  label="仪器类型" readonly required v-model="tempData.type" placeholder="请输入仪器类型"/>
                  </div>
                </div>
            </div>
            <div class="cell_item">
                <div class="cell_item_box">
                  <div @click="intervalDialogShow=true">
                    <Field  label="仪器记录时间间隔（分钟）" readonly required v-model="tempData.saveinterval" placeholder="请选择仪器记录时间间隔"/>
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
                      <Stepper v-model="tempData.tempTop" integer  min="-500" max="500" style="text-align:left"/>
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
                      <Stepper v-model="tempData.tempBottom" integer  min="-500" max="500" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="cell_item" v-show="tempData.type!=='GS1W'">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>湿度上限(%)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="tempData.humTop" integer  min="0" max="100" style="text-align:left"/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="cell_item" v-show="tempData.type!=='GS1W'">
                <div class="cell_item_box">
                  <div class="van-cell van-field">
                    <div class="van-cell__title van-field__label">
                      <span>湿度下限(%)</span>
                    </div>
                    <div class="van-cell__value">
                      <Stepper v-model="tempData.humBottom" integer  min="0" max="100" style="text-align:left"/>
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
  name:'mobile_temp_edit',
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
      mac:'',
      typeDialogShow:false,
      intervalDialogShow:false,
      departmentDialogShow:false,
      tempData:{
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
      this.tempData.type=value.key;
      this.typeDialogShow=false;
    },
     getIntervalFn(value){
      this.tempData.saveinterval=value.key;
      this.intervalDialogShow=false;
    },
    getDepartmentFn(value){
      this.tempData.department=value.key;
      this.departmentDialogShow=false;
    },

    saveFn(){

      if(this.tempData.freezernumber.trim()&&this.tempData.department.trim()&&this.tempData.freezername.trim()&&this.tempData.mac.trim()&&this.tempData.type.trim()){

            if(this.tempData.tempBottom!==null&&this.tempData.tempTop!==null){
                    if(this.tempData.tempBottom===this.tempData.tempTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '温度上下限不能相等！',
                            duration: 2000
                        });
                        return
                    }
                    if(this.tempData.tempBottom>this.tempData.tempTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '温度上限不能小于下限！',
                            duration: 2000
                        });
                        return
                    }
                }

                if(this.tempData.humBottom!==null&&this.tempData.humTop!==null){
                    if(this.tempData.humBottom===this.tempData.humTop){
                       
                        this.$notify({
                            type:'warning',
                            message: '湿度上下限不能相等！',
                            duration: 2000
                        });
                        return 
                    }
                    if(this.tempData.humBottom>this.tempData.humTop){
                        this.$notify({
                            type:'warning',
                            message: '湿度上限不能小于下限！',
                            duration: 2000
                        });
                        return
                    }
                }

                let uploadLoading=Toast.loading({
                    message: '正在更新...',
                    forbidClick: true,
                    duration:0
                });


                let datas={
                    freezernumber:this.tempData.freezernumber.trim(),
                    freezername:this.tempData.freezername.trim(),
                    department:this.tempData.department.trim(),
                    type:this.tempData.type,
                    location:this.tempData.location.trim(),
                    temperaturefitted:this.tempData.tempBottom+'~'+this.tempData.tempTop,
                    humidityfitted:this.tempData.humBottom+'~'+this.tempData.humTop,
                    saveinterval:this.tempData.saveinterval,
                }

                let configData='';
                for (let m in datas){
                    if(!datas[m]){
                        datas[m]='';
                    }
                    configData+=m+'='+datas[m].trim()+'&'
                }

                this.$axios.get(NET_PORT.tempEditUpdate+'?mac='+this.mac+'&'+configData)
                .then((res)=>{
                    if(res.data.code===0){
                        uploadLoading.clear();
                        Toast.success('更新成功！');
                        setTimeout(()=>{
                          this.$router.go(-1)
                        },2000)
                    }else{
                        uploadLoading.clear();
                        Toast.fail('更新失败！');
                    }
                })
                .catch((err)=>{
                    uploadLoading.clear();
                    Toast.fail('更新失败！');
                })

              
            }else{
                this.$notify({
                    type:'warning',
                    message: '请补充完整必填信息！',
                    duration: 2000
                });
            }
    },

    tempRangeComFn(data,index){
        if(data){
            return data.split('~')[index] ==='null' ? null : Number(data.split('~')[index])
        }else{
            return null
        }
    },

     getDataFn(){

        this.$axios.get(NET_PORT.tempEditDetail+'?mac='+this.mac)
        .then((res)=>{
          
            let result=res.data[0]

            this.tempData={
                freezernumber:result.freezernumber ? result.freezernumber : '',
                freezername:result.freezername,
                department:result.department? result.department : '',
                location:result.location ? result.location : '',
                mac:result.mac ? result.mac : '',
                type:result.type ? result.type : '',
                tempTop:this.tempRangeComFn(result.temperaturefitted,1),
                tempBottom: this.tempRangeComFn(result.temperaturefitted,0),
                humTop: this.tempRangeComFn(result.humidityfitted,1),
                humBottom: this.tempRangeComFn(result.humidityfitted,0),
                saveinterval:result.saveinterval ? result.saveinterval : '10',
            }


        })
        .catch((err)=>{
            // console.log(err)
        })

  
    }
  },
  created(){
    this.$emit('setTitle','温湿度仪器编辑');
    this.mac=this.$route.params.id;
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
  },
  mounted(){
    this.getDataFn();
  }
}
</script>
<style lang="scss" scoped>
  @import "../../../assets/scss/mobile/common/assets_upload.scss";
</style>