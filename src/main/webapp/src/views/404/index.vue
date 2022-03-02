<template>
  <div id='fault_wrap'>
      <div class="message_box">
        <img src="../../assets/img/error.jpg" alt="">
        <button @click="logoutFn" class="fault_page_logout">重新登录</button>
      </div>
  </div>
</template>

<script>
import { exitFn } from "../../api/logout.js"
export default {
  name:'fault',
  components:{},
  props:{},
  data(){
    return {

    }
  },
  watch:{},
  computed:{},
  methods:{
    logoutFn(){
      exitFn()
    }
  },
  created(){},
  mounted(){}
}
</script>
<style lang='scss' scoped>
  @import "../../assets/scss/404.scss"
</style>f