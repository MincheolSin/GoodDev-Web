<template>
    <div class="header-container">
        <div class="logo-container">
            <router-link to="/">
                <img :src="good" alt="Logo" />
            </router-link>
        </div>
        <searchBar/>
        <miniLoginBar/>
    </div>
</template>

<script setup>
import good from '@/assets/images/good.png';
import miniLoginBar from '@/components/header/MiniLoginBar.vue';
import searchBar from '@/components/header/SearchBar.vue';
</script>
