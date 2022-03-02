<template>
  <div class='mobile_assets_detail_wrap'>
      <div class="assets_detail_box">
          <div  class="assets_img_wrap">
              <div v-if="!imgList.length" class="assets_img_info">
                <span>暂无图片</span>
              </div>
              <div v-else class="assets_img_box">
                <div class="img_item_box" v-for="(item,index) in imgList" :key="index" :style="imgCom(item)"  @click="imgPreviewFn(index)"></div>
              </div>
          </div>

          <template v-for="(item,index) in assetsDetailList">
            <Cell v-if="item.key==='status'" title="状态">
              <Tag  size="medium" v-if="assetsDetailData.status==='正常'" :color="statusColor[0]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else-if="assetsDetailData.status==='待维修'" :color="statusColor[1]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else-if="assetsDetailData.status==='维修接单'" :color="statusColor[2]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else-if="assetsDetailData.status==='待报废'" :color="statusColor[3]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else-if="assetsDetailData.status==='报废'" :color="statusColor[4]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else-if="assetsDetailData.status==='外借'" :color="statusColor[5]">{{assetsDetailData.status}}</Tag>
              <Tag  size="medium" v-else color="#f7f7f7" text-color="#515a6e">{{assetsDetailData.status}}</Tag>
            </Cell>
            <Cell v-else :title="item.label" :value="assetsDetailData[item.key] ? assetsDetailData[item.key] : '--'"></Cell>
          </template>
          
          <div class="tools_wrap">
            <Button round class="tools_button" type="warning" @click="toModifyFn">修改</Button>
            <Button round v-if="role==='admin'" class="tools_button" type="danger" @click="toDeleteFn">删除</Button>
          </div>
         
      </div>
  </div>
</template>

<script>
import {Cell,Button,Notify,Tag,ImagePreview,Dialog,Toast} from 'vant';
import 'vant/lib/cell/style';
import 'vant/lib/button/style';
import 'vant/lib/notify/style';
import 'vant/lib/tag/style';
import 'vant/lib/image-preview/style';
import 'vant/lib/dialog/style';
import 'vant/lib/toast/style';
import NET_PORT from "../../../api/port.js"
import statusColor from "../../../assets/data/statusColor.js"
export default {
  name:'mobile_assets_detail',
  components:{
      Button,
      Cell,
      Tag,
      ImagePreview,
      Dialog,
      Toast
  },
  props:{},
  data(){
    return {
      role:this.$store.state.role,
      statusColor:statusColor,
      assetID:'',
      showRepair:false,
      showScrap:false,

      imgList:[],

      assetsDetailData:{
            assetID: "",
            generalName: "",
            assetName: "",
            location:"",
            specification:"",
            type: "",
            placeoforigin:"",
            brand: "",
            departmentcode:"",
            departmentroom:"",
            homeofficenumber:"",
            homeofficename:"",
            isentrance:"",
            suppliername:"",
            generatebusinessname:"",
            money: null,
            buyDate:"",
            locDept:"",
            status: "",
            recorder:"",
            mac: "",
            amount: null,
            unit: "",
            applyoddnumbers:"",
            oneclassify:"",
            secondclassify:"",
            threeclassify:"",
            fourclassify:"",
            sn:"",
            architecture:"",
            academy:"",
            remark:"",
      },

      assetsDetailList:[
        {
          label:'资产编号',
          key:'assetID',
        },
        {
          label:'资产名称',
          key:'assetName',
        },
        {
          label:'通用名称',
          key:'generalName',
        },
        {
          label:'存放地点',
          key:'location',
        },
        {
          label:'规格',
          key:'specification',
        },
        {
          label:'型号',
          key:'type',
        },
        
        {
          label:'产地',
          key:'placeoforigin',
        },
        {
          label:'品牌',
          key:'brand',
        },
        {
          label:'部门编码',
          key:'departmentcode',
        },
        {
          label:'部门名称',
          key:'departmentroom',
        },
        {
          label:'大科室编码',
          key:'homeofficenumber',
        },
        {
          label:'大科室名称',
          key:'homeofficename',
        },
        {
          label:'所在科室名称',
          key:'locDept',
        },
        
        {
          label:'供应商名称',
          key:'suppliername',
        },
        {
          label:'生产厂商名称',
          key:'generatebusinessname',
        },
        {
          label:'是否进口',
          key:'isentrance',
        },
       
        {
          label:'原值',
          key:'money',
        },
        {
          label:'入库时间',
          key:'buyDate',
        },
        {
          label:'状态',
          key:'status',
        },
        
        {
          label:'录入人员',
          key:'recorder',
        },
        {
          label:'一级分类',
          key:'oneclassify',
        },
        {
          label:'二级分类',
          key:'secondclassify',
        },
        {
          label:'三级分类',
          key:'threeclassify',
        },
        {
          label:'四级分类',
          key:'fourclassify',
        },
        {
          label:'标签mac',
          key:'mac',
        },
        {
          label:'数量',
          key:'amount',
        },
        {
          label:'单位',
          key:'unit',
        },
        {
          label:'电量',
          key:'electric',
        },
        {
          label:'处置申请单号',
          key:'applyoddnumbers',
        },
        {
          label:'SN',
          key:'sn',
        },
        {
          label:'建筑',
          key:'architecture',
        },
        {
          label:'院区',
          key:'academy',
        },
        
        {
          label:'备注',
          key:'remark',
        },
         
      ]
    }
  },
  watch:{},
  computed:{

  },
  methods:{

    imgPreviewFn(index){
        ImagePreview({
            images:this.imgList,
            startPosition: index,
        });
    },
    imgCom(src){
        return 'background-image:url('+src+')';
    },

    toModifyFn(){

      this.$router.push('/mobile/assets_modify/'+this.assetID)
    },

    toDeleteFn(){
      Dialog.confirm({
        title: '提示！',
        message: '<span style="color:red;">你确定要删除此资产吗？</span>',
        confirmButtonColor:'red',
      })
      .then(() => {
          var deleteLoading=Toast.loading({
              message: '正在删除...',
              forbidClick: true,
              duration:0
          });
          let list=[];
          list.push(this.assetID)
          this.$axios.get(NET_PORT.assetsDelete+list)
          .then((res)=>{

              if(res.data.msg==='ok'){
                deleteLoading.clear();
                Toast.success('删除成功！');
                setTimeout(()=>{
                  this.$router.go(-1)
                },1000)
                
              }else if(res.data.msg==='failed'){
                  deleteLoading.clear();
                  Toast.fail('删除失败！');
              }

          })
          .catch((err)=>{
              deleteLoading.clear();
              Toast.fail('删除失败！');
          })
      })
      .catch(() => {

      });
      
    },

    getDataFn(){
          this.$axios.post(NET_PORT.assetsQuery+'?currentPage=1'+'&assetID='+this.assetID)
          .then((res)=>{
              if(res.data[0].assetID){
                  let data=res.data[0];
                  let normalData={

                      assetID: data.assetID,
                      generalName: data.generalName? data.generalName : '',
                      assetName: data.assetName ? data.assetName : '',
                      location:data.location ? data.location : '',
                      specification:data.specification ? data.specification : '',
                      type: data.type ? data.type : '',
                      placeoforigin:data.placeoforigin ? data.placeoforigin : '',
                      brand: data.brand ? data.brand : '',
                      departmentcode:data.departmentcode ? data.departmentcode :'',
                      departmentroom:data.departmentroom ? data.departmentroom : '',
                      homeofficenumber:data.homeofficenumber ? data.homeofficenumber : '',
                      homeofficename:data.homeofficename ? data.homeofficename : '',
                      isentrance:data.isentrance ? data.isentrance : '',
                      suppliername:data.suppliername ? data.suppliername : '',
                      generatebusinessname:data.generatebusinessname ? data.generatebusinessname : '',
                      money: data.money ? Number(data.money) :0,
                      buyDate:data.buyDate ? data.buyDate : '',
                      locDept:data.locDept ? data.locDept : '',
                      status: data.status ? data.status : '',
                      recorder:data.recorder?data.recorder : this.$store.state.user,
                      mac: data.mac ? data.mac : '',
                      amount: data.amount ? Number(data.amount) : 1,
                      unit: data.unit ? data.unit : '',
                      applyoddnumbers:data.applyoddnumbers ? data.applyoddnumbers : '',
                      oneclassify:data.oneclassify ? data.oneclassify : '',
                      secondclassify:data.secondclassify ? data.secondclassify : '',
                      threeclassify:data.threeclassify ? data.threeclassify : '',
                      fourclassify:data.fourclassify ? data.fourclassify : '',
                      sn:data.sn ? data.sn : '',
                      architecture:data.architecture ? data.architecture : '',
                      academy:data.academy ? data.academy : '',
                      remark:data.remark ? data.remark : '',
                  }


                  this.buyTime=normalData.buyDate;

                  this.assetsDetailData=normalData;

                  this.getImgFn('imgShow')
                  this.getImgFn('imgShowLeft')
                  this.getImgFn('imgShowAllround')
                  this.getImgFn('imgShowAbove')
                  this.getImgFn('imgShowRight')
                  this.getImgFn('imgShowPaperlabel')
                  this.getImgFn('imgShowOnecodelable')

              }else{
                Notify({ type: 'danger', message: '未找到资产数据！' });
                setTimeout(()=>{
                  this.$router.go(-1)
                },1000)
              }
              
          })
          .catch((err)=>{

          })  
    },


    getImgFn(key){
        this.$axios.get(NET_PORT[key]+this.assetID,{responseType: 'arraybuffer'})
        .then((res)=>{
            let result='';
            result= 'data:image/png;base64,' + btoa(
                new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')
            )

            if(result==='data:image/png;base64,'){

            }else{

              this.imgList.push(result)
                
            }
            
        })
        .catch((err)=>{
        // console.log(err)
        })
    },

  },

  beforeRouteEnter(to, from, next) {
    to.meta.needFresh=false,
    next();
  },

  beforeRouteLeave(to, from, next) {
    from.meta.needFresh=false;
    next();
  },

  created(){
    this.$emit('setTitle','资产明细');
    this.assetID=this.$route.params.id
  },
  mounted(){
    this.getDataFn();
  },

     
}
</script>
<style lang="scss" scoped>
  @import "../../../assets/scss/mobile/assets_detail.scss";

  
</style>