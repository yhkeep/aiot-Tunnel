<template>
  <div class='mobile_wrap'>
      <router-view></router-view>
       <Tabbar v-model="active" route>
                <TabbarItem to="/mobile/home" name="mobile_home" icon="home-o">首页</TabbarItem>
                <TabbarItem to="/mobile/assets_manage" name="mobile_assets_create" icon="bar-chart-o">资产管理</TabbarItem>
                <TabbarItem to="/mobile/check_manage" name="mobile_assets_check" icon="certificate">资产盘点</TabbarItem>
                <TabbarItem to="/mobile/device_message" name="mobile_my" icon="more-o"  :info="deviceInfoNum===0 ? '' : deviceInfoNum">设备动态</TabbarItem>
            </Tabbar>
  </div>
</template>

<script>
import { Tabbar, TabbarItem } from 'vant';
import 'vant/lib/tabbar/style';
import 'vant/lib/tabbar-item/style';
export default {
  name:'mobile2_wrap',
  components:{
        Tabbar,
        TabbarItem
  },
  props:['deviceInfoNum'],
  data(){
    return {
        active:'mobile_home',
    }
  },
  watch:{},
  computed:{},
  methods:{},
  created(){},
  mounted(){}
}
</script>
<style scoped>
</style>