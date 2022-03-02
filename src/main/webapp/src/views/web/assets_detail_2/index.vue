<template>
  <div class='assets_detail_wrap'>
      <Button icon="ios-arrow-back" @click="returnFn" class="return_btn" >返回</Button>
        <div class="row_box hover_animat custom_bg_color_white">
            <h2 class="row_title">图像</h2>
            <div id="img_box">
                <img :src="editData.image" alt="" >
                <div class="upload_wrap">
                    <div class="bigger_img">
                        <button @click="showImgModal=true">查看大图</button>
                    </div>
                    <div class="upload_box">
                        <span class="upload_span" ref="upload_span">更换图片</span>
                        <input class="upload_input" type="file" accept="image/jpg, image/jpeg,image/png" @change="handleImgFn($event)">
                    </div>
                </div>
            </div>
        </div>

          <div class="row_wrap hover_animat custom_bg_color_white">
            <div class="row_box">
                <h2 class="row_title">资产明细</h2>
                <div class="form_wrap">
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>资产编号</h3>
                                <Input disabled v-model="editData.assetID" placeholder="资产id"/>
                            </Col>
                            
                            <Col span="8">
                                <h3>资产名称</h3>
                                <Input v-model="editData.assetName" placeholder="资产名称" />
                            </Col>
                            <Col span="8">
                                <h3>所属部门</h3>
                                <Select v-model="editData.department">
                                <Option v-for="item in departData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>

                        </Row>
                    </div>

                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>一级分类</h3>
                                <Select v-model="editData.fstCat">
                                    <Option v-for="item in fstCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>二级分类</h3>
                                <Select v-model="editData.secCat">
                                    <Option v-for="item in secCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                            <Col span="8">
                                <h3>三级分类</h3>
                                <Select v-model="editData.thdCat">
                                    <Option v-for="item in thdCatData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>
                        </Row>
                    </div>
                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>mac地址</h3>
                                <Input v-model="editData.mac" placeholder="mac地址" />
                            </Col>

                            

                            <Col span="8">
                                <h3>数量</h3>
                                <InputNumber  :min="1" v-model="editData.amount" placeholder="数量" style="width:100%;"></InputNumber>
                            </Col>
                            <Col span="8">
                                <h3>单位</h3>
                                <Select v-model="editData.unit">
                                <Option v-for="item in unitData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </Col>

                        </Row>
                    </div>


                    <div class="form_item">
                        <Row :gutter="20">
                            <Col span="8">
                                <h3>购入日期</h3>
                                <DatePicker v-model="buyTime" type="date" @on-change="dateBuyFn"  placeholder="购入日期" style="width:100%;"></DatePicker>
                            </Col>
                            <Col span="8">
                                <h3>金额</h3>
                                <InputNumber  :min="0" v-model="editData.money" placeholder="金额" style="width:100%;"></InputNumber>
                            </Col>

                            <Col span="8">
                                <h3>状态</h3>
                                <Select v-model="editData.status">
                                <Option v-for="item in statusData" :value="item.value" :key="item.value">{{ item.label }}</Option>
                                </Select>
                            </col>
                        </Row>
                    </div>

                    <div class="form_item">
                        <Row>
                            <Col>
                                <h3>备注</h3>
                                <Input v-model="editData.remark" type="textarea" placeholder="输入备注信息..." />
                            </Col>
                        </Row>
                    </div>

                </div>
                <div class="submit_btn_wrap">
                       <Button type="primary" @click="saveFn">保存</Button>
                </div>
            </div>
        </div>

        <div id="imgModal_wrap" :class="showImgModal? 'imgModal_show' : 'imgModal_hide'"  @click="showImgModal=false">
            <img id="imgModal" :src="editData.image" alt="">
        </div>

        <Modal v-model="uploadModal" title="" :footer-hide="true" :mask-closable="false" :closable="false" width="360">
            
            <div style="text-align:center;padding:20px 0">
                <Spin fix v-if="!achieveUpload&&!errorUpload">
                    <Icon type="ios-loading" size=18 class="loding_icon"></Icon>
                    <div>正在更新</div>
                </Spin>
                <Spin fix v-if="achieveUpload" style="color:#00ad19">
                    <Icon type="ios-checkmark-circle" size=18 />
                    <div>更新成功</div>
                </Spin>
                <Spin fix v-if="errorUpload" style="color:#f72b2b">
                    <Icon type="ios-close-circle" size=18 />
                    <div>更新失败</div>
                </Spin>

            </div>
            
        </Modal>

  </div>
</template>

<script>

import Vue from "vue"
import { Row,Col,Icon,DatePicker,Select,Option,Input,Button,Modal,InputNumber,Spin} from 'iview';
Vue.component('Icon', Icon);
Vue.component('Row', Row);
Vue.component('Col', Col);
Vue.component('DatePicker', DatePicker);
Vue.component('Select', Select);
Vue.component('Option', Option);
Vue.component('Input',Input);
Vue.component('Button',Button);
Vue.component('Modal',Modal);
Vue.component('InputNumber',InputNumber);
Vue.component('Spin',Spin);

import departData from "../../../assets/data/depart2.js"
import unitData from "../../../assets/data/unit.js"
import statusData from "../../../assets/data/status.js"
import {fstCatData,secCatAllData,thdCatAllData} from "../../../assets/data/category.js"

export default {
  name:'assets_detail',
  components:{},
  props:{},
  data(){
    return {
        id:'',
        showImgModal:false,
        buyTime:'2019-01-01',
        editData:{
          image:'',
          assetID: "",
          assetName: "",
          department:"",
          mac: "",
          amount: null,
          unit: "",
          buyDate: "2019-01-01",
          money: 10000,
          status: "",
          remark:"",
          fstCat:'',
          secCat:'',
          thdCat:'',
        },

        departData:departData,
        unitData:unitData,
        secCatData:[],
        thdCatData:[],
        fstCatData:fstCatData,
        secCatAllData:secCatAllData,
        thdCatAllData:thdCatAllData,

        uploadModal:false,
        achieveUpload:false,
        errorUpload:false,

        statusData:statusData,

    }
  },
  watch:{

       //监视一级分类和二级分类的变化动态改变数据
        "editData.fstCat"(){
            this.secCatData=this.secCatAllData.filter((item,index)=>{
              return item.parent===this.editData.fstCat;
            })
            this.thdCatData=[];
            this.editData.thdCat='';
        },
        "editData.secCat"(){
            this.thdCatData=this.thdCatAllData.filter((item,index)=>{
              return item.parent===this.editData.secCat;
            })

            this.editData.thdCat='';
        },
        "editData.thdCat"(){
            console.log(this.editData.thdCat)
            if(this.editData.thdCat){
                var code='';
                this.thdCatData.forEach((item,index)=>{
                  if(item.value===this.editData.thdCat){
                    code=item.code;
                  }
                })
                //这里拿到所选择分类的代码
                // console.log(code)
                this.editData.assetID=code
            }else{
              this.editData.assetID='';
            }
        }
  },
  computed:{},
  methods:{

       returnFn(){
        this.$router.go(-1)
      },

      handleImgFn(e){
        if(e.target.files[0]){
          this.$refs.upload_span.innerText="重新上传"
          var reader = new FileReader();
          reader.readAsDataURL(e.target.files[0]);//发起异步请求
          reader.onload = (e)=>{
            //读取完成后，数据保存在对象的result属性中
            this.editData.image=e.target.result
          };

         

        }

      },

      dateBuyFn(value){
        this.editData.buyDate=value
      },

      successFn(){
          this.achieveUpload=true;
          setTimeout(()=>{
              this.uploadModal=false;
              setTimeout(()=>{
                      this.achieveUpload=false;
                      this.errorUpload=false;
                      //回到上一页
                      this.$router.go(-1)
              },500)

          },1000)
      },

      errorFn(){
         this.errorUpload=true;
          setTimeout(()=>{
                  this.uploadModal=false;
                  setTimeout(()=>{
                      this.achieveUpload=false;
                      this.errorUpload=false;
                  },500)
              
          },1000)
      },

      saveFn(){
        // 判断有几个必需填的没填
        var k=0;
        for( var i in this.editData){
            if(!this.editData[i]){
                if(i==='operatingRoom'||i==='applyDept'||i==='locDept'||i==='remark'||i==='image'||this.editData.money===0){
                }
                else{
                    k++;
                }
            }
        }

         //保证必填项都已经填好后
        if(k===0){
              
          //显示出正在上传
          this.uploadModal=true;
          
          let datas = {
            "assetID": this.editData.assetID.trim(),
            "assetName": this.editData.assetName.trim(),
            "department":this.editData.department.trim(),
            "mac": this.editData.mac.trim(),
            "amount": this.editData.amount,
            "unit":this.editData.unit.trim(),
            "buyDate": this.editData.buyDate.trim(),
            "money":this.editData.money,
            "status": this.editData.status.trim(),
            "remark":this.editData.remark.trim(),
            "fstCat":this.editData.fstCat.trim(),
            "secCat":this.editData.secCat.trim(),
            "thdCat":this.editData.thdCat.trim(),
          };

          let imgDatas = {
            "image":this.editData.image,
          }

          // this.$axios.post(process.env.API_HOST+"uploadimage",
          //   imgDatas
          // ) 
          // .then((res)=>{
          //   if(res.data.msg==='ok'){
          //       this.$axios.post(process.env.API_HOST+"huaxi/update",
          //         datas
          //       ) 
          //       .then((response)=>{
          //         if(response.data.msg==='ok'){
          //               this.successFn();
          //         }else if(response.data.msg==='failed'){
          //               this.errorFn();
          //         }
          //       })
          //       .catch((error)=>{
          //          console.log(error)
          //       })

          //   }else if(res.data.msg==='failed'){
          //       this.errorFn();
          //   }
          // })
          // .catch((err)=>{
          //   console.log(err)
          // })


        }else{
            this.$Message.error({
                        content:"请补充完整信息!",
                        duration:2,
            })
        }

      },

      getIdFn(){

      },

      getDataFn(){
            //查询内存是否有明细数据
            if(window.sessionStorage.getItem('detailData')){

              var data=JSON.parse(window.sessionStorage.getItem('detailData'));
              //将内存中数据赋值给this.editData;
              for(var k in data){
                if(k==='amount'){
                  if(data[k]){
                    if(data[k]==='--'){
                      this.editData[k]=1
                    }else{
                      this.editData[k]=Number(data[k]);
                    }
                  }else{
                    this.editData[k]=1;
                  }

                }
              
                else{
                  this.editData[k]=data[k];
                }

              }

              // this.editData.image=process.env.API_HOST+'showImage?assetID='+this.editData.assetID;

              this.$axios.get(process.env.API_HOST+'showImage?assetID='+this.editData.assetID,{responseType: 'arraybuffer'})
              .then((res)=>{
                // console.log(res)
                    var result='';
                    result= 'data:image/png;base64,' + btoa(
                      new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
                    )

                    this.editData.image=result;
              })
              .catch((err)=>{
                console.log(err)
              })

            }else{
                this.$Message.error({
                    content:"未找到资产信息，将返回上一页!",
                    duration:2,
                })

                setTimeout(()=>{
                  this.$router.go(-1)
                },2000)

            }
      }
  },
  
    created(){

    },
    mounted(){
      
      this.getDataFn();
    },

    beforeDestroy(){
      this.$Loading.finish();
      window.sessionStorage.removeItem('detailData')
    }
}
</script>



<style lang='scss' scoped>
    @import '../../../assets/scss/web/common/data_detail.scss';
    @import '../../../assets/scss/web/common/form_data.scss';
</style>