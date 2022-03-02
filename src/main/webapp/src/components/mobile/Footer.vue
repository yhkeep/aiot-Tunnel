<template>
  <div class='mobile_footer_wrap' >
            <Tabbar v-model="active" route active-color="#1d50a2" inactive-color="#868686">
                <TabbarItem to="/mobile/home" name="mobile_home" icon="wap-home">资产首页</TabbarItem>
                <TabbarItem to="/mobile/assets_manage" name="mobile_assets_manage" icon="card">资产管理</TabbarItem>
                <TabbarItem to="/mobile/check_manage" name="mobile_check_manage" icon="eye">资产盘点</TabbarItem>
                <TabbarItem to="/mobile/device_message" name="mobile_device_message" icon="bell"  :info="deviceInfoNum===0 ? '' : deviceInfoNum">设备动态</TabbarItem>
            </Tabbar>
  </div>
</template>

<script>
import { Tabbar, TabbarItem } from 'vant';
import 'vant/lib/tabbar/style';
import 'vant/lib/tabbar-item/style';
export default {
  name:'mobile_footer',
  components:{
        Tabbar,
        TabbarItem
  },
  props:['deviceInfoNum'],
  data(){
    return {
       active:'mobile_home',
      //  deviceInfoNum:5,
    }
  },
  watch:{},
  computed:{
    
  },
  methods:{},
  created(){},
  mounted(){}
}
</script>
<style lang="scss">

    // .van-tabbar-item{
    //     font-size:14px;
    //     color:#868686;
    // }

    // .van-tabbar-item.van-tabbar-item--active{
    //     color:#1d50a2;
    // }

    .van-tabbar-item--active>.van-tabbar-item__icon>.van-icon{
      animation:tabbarAnimate .5s 1;
    }

    @keyframes tabbarAnimate
    {
      0% {transform:scale(1)}
      50% {transform:scale(1.3)}
      100% {transform:scale(1)}
    }



</style>