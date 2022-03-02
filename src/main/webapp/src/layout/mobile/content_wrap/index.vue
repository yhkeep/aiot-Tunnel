<template>
  <div class='mobile_content_wrap'>
    <Header :title="title"></Header>
    <div class="mobile_content_box">
        <router-view @setTitle="setTitleFn"></router-view>
    </div>
  </div>
</template>

<script>
import Header from "../../../components/mobile/Header"
export default {
  name:'mobile_content',
  components:{Header},
  props:{},
  data(){
    return {
      title:'',
    }
  },
  watch:{},
  computed:{},
  methods:{
    setTitleFn(val){
      this.title=val
    }
  },
  created(){},
  mounted(){}
}
</script>
<style scoped>

</style>