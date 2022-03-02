<template>
  <div id='mobile_my_wrap' class="custom_flex_column_wrap">
        <div class="user_wrap">
            <div class="user_box">
                    <img src="../../../assets/img/users.jpg" alt="">
                    <h2>{{user}}</h2>
                    <Tag  class="role_tag" size="medium" plain type="primary">{{department}}</Tag><Tag class="role_tag" size="medium" :type="role==='admin' ? 'success' : 'warning'">{{role==='admin'? '管理员' :'操作员'}}</Tag>
            </div>
        </div>
        <div class="menu_wrap">
           
        </div>

        <div class="logout_wap">
            <CellGroup :border=false>
                <Cell :border=false>
                    <Button class="logout_button" type="danger"  @click="logoutFn">立即退出</Button>
                </Cell>
            </CellGroup>
        </div>
            
             
        
      </div>
         
          
  </div>
</template>

<script>
import {CellGroup,Cell,Button,Notify,Tag} from 'vant';
import 'vant/lib/cell-group/style';
import 'vant/lib/cell/style';
import 'vant/lib/button/style';
import 'vant/lib/notify/style';
import 'vant/lib/tag/style';
import {exitFn} from '../../../api/logout.js'
export default {
  name:'mobile_my',
  components:{
      Button,
      CellGroup,
      Cell,
      Tag
  },
  props:{},
  data(){
    return {
        user:this.$store.state.user,
        role:this.$store.state.role,
        department:this.$store.state.queryData.department,
    }
  },
  watch:{},
  computed:{},
  methods:{
      logoutFn(){
          exitFn();
      }
  },
  created(){},
  mounted(){}
}
</script>
<style lang="scss" scoped>
    // @import "../../../assets/scss/mobile/common/default.scss";
    @import "../../../assets/scss/mobile/my.scss";
</style>