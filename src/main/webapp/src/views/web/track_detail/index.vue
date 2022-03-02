<template>
  <div class='track_detail_wrap'>
      {{assetID}}
  </div>
</template>

<script>
export default {
  name:'track_detail_wrap',
  components:{},
  props:{},
  data(){
    return {
        assetID:"",
    }
  },
  watch:{},
  computed:{},
  methods:{},
  created(){
      this.assetID=this.$route.params.id;
      console.log(this.assetID)
  },
  mounted(){}
}
</script>
<style scoped>
</style>