<template>
  <div class='mobile_wrap' id="mobile_wrap" ref="mobileWrap">
      <!-- <div class="mobile_box"> -->
        <!-- <router-view></router-view> -->
      <!-- </div> -->
        <keep-alive>
            <router-view  v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
  </div>
</template>

<script>

export default {
  name:'mobile',
  components:{
   
  },
  props:{},
  data(){
    return {
    
    }
  },
  watch:{
   
  },
  computed:{
   
  },
  methods:{
    getScrollFn(){
      let scrollTop=this.$refs.mobileWrap.scrollTop;
      this.$store.commit('setMobileScroll',scrollTop)
    }
  },
  created(){
   
  },
  mounted(){
    this.$refs.mobileWrap.addEventListener('scroll',this.getScrollFn)
  },

  beforeDestroy(){
    this.$refs.mobileWrap.removeEventListener('scroll',this.getScrollFn)
  }
 
}
</script>

<style lang="scss" scoped>
  @import "../../assets/scss/mobile/layout.scss";

</style>