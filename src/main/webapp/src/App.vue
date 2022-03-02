<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
}
</script>

<style>
  html,body,#app{
    width:100%;
    height:100%;
    overflow: hidden;
  }

  body{
    font-family:"Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    -webkit-font-smoothing:antialiased;
    -moz-osx-font-smoothing:grayscale;
    color:#323846;
  }

  html,body,h1,h2,h3,h4,h5,p{
      margin:0;
      padding:0;
  }

  .ivu-table-wrapper{
    border:none!important;
  }

  .ivu-table td, .ivu-table th{
    border:none!important;
  }

  .ivu-table:before ,.ivu-table:after{
    display: none!important;
  }

  .ivu-drawer-header{
    /* padding:0!important; */
    border:none;
  }

  .ivu-drawer-body{
    padding-top:0!important;
  }

  .ivu-menu-vertical.ivu-menu-light:after{
    display: none!important;
  }

  .ivu-checkbox-group-item{
    margin-bottom:10px!important;
  }

  .ivu-auto-complete.ivu-select-dropdown{
    max-height:200px!important;
    overflow: auto!important;
  }

  .ivu-split-horizontal>.ivu-split-trigger-con{
    width:auto!important;
  }

  .ivu-table-wrapper>.ivu-spin-fix{
    border:none!important;
  }

  .van-cell:not(:last-child)::after{
      left:0!important;
      right:0!important;
  }

  .van-popover[data-popper-placement=bottom-end] .van-popover__arrow{
    right:0!important
  }

  .van-button--small{
    padding:0 10px!important;
  }

</style>
