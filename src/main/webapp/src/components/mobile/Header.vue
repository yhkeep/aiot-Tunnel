<template>
  <div class='mobile_header_wrap'>
        <NavBar
            :title="title"
            left-text="返回"
            left-arrow
            @click-left="onClickLeft"
        />
  </div>
</template>

<script>
import { NavBar } from 'vant';
import 'vant/lib/nav-bar/style';
export default {
  name:'mobile_header',
  components:{
      NavBar
  },
  props:['title'],
  data(){
    return {
        
    }
  },
  watch:{},
  computed:{},
  methods:{
        onClickLeft() {
            this.$router.go(-1)
        },
       
  },
  created(){},
  mounted(){}
}
</script>
<style scoped>
</style>