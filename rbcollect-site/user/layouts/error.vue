<template>
  <div>
    {{ error }}
  </div>
</template>

<script>
// 默认的错误页面存放在 /layouts/error.vue,一旦系统出错，自动跳转到这
export default {
  props: ['error'], //一旦有错误信息就被该属性捕获，像java的catch;props和data定义属性区别是,props定义属性的值可以从外面传进来
}
</script>
