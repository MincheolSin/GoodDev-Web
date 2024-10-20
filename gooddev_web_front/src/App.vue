<template>
  <div class="container">
    <Header v-if="!isHidden"/>
    <Navigator v-if="!isHidden"/>
    <router-view></router-view>
    <Footer v-if="!isHidden"/>
  </div>
</template>

<script setup>
import Header from '@/components/header/MainHeader.vue';
import Footer from '@/components/MainFooter.vue';
import Navigator from '@/components/Navigator.vue';
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const hiddenPaths = ['/login', '/register','/findID','findPWD']; 
const isHidden = computed(() => hiddenPaths.some(path => route.path.startsWith(path) || route.path === path));
</script>