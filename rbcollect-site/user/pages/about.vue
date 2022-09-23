<template>
  <!--关于我们-->
  <div class="bg">
    <div class="contain" id="tabs" style="margin:0 auto;">
      <div class="text-nav">
        <ul class="clearfix">
          <li :class="{ selected: $route.fullPath === '/about' }">
            <NuxtLink class="text-link" to="/about">联系我们</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/report' }">
            <NuxtLink class="text-link" to="/about/report">反馈中心</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/profile' }">
            <NuxtLink class="text-link" to="/about/profile">公司简介</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/leader' }">
            <NuxtLink class="text-link" to="/about/leader">管理团队</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/team' }">
            <NuxtLink class="text-link" to="/about/team">团队风采</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/price' }">
            <NuxtLink class="text-link" to="/about/price">资费说明</NuxtLink>
          </li>
          <li :class="{ selected: $route.fullPath === '/about/job' }">
            <NuxtLink class="text-link" to="/about/job">招贤纳士</NuxtLink>
          </li>
        </ul>
      </div>
      <NuxtChild />
    </div>
  </div>
</template>

<script>
import '~/assets/css/about.css'

export default {}
</script>
