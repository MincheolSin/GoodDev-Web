<template>
    <div class="search-wrapper">
        <span class="search-label">통합검색</span>
        <div class="search-container">
            <form @submit.prevent="handleSubmit" class="search-form">
                    <input v-model="keyword" name="keyword" id="keyword" class="search-input" :placeholder="searchKeyword || '검색어를 입력해주세요'">
                    <button type="submit" class="search-button">검색</button>
            </form>
        </div>
    </div>
</template>

<script setup>
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const store = useMainStore();
const router = useRouter();
const route = useRoute();

const {searchKeyword} = storeToRefs(store);
const {updateSearchKeyword,getSearch} = store;
const keyword = ref('');

const handleSubmit = ()=>{
    updateSearchKeyword(keyword);
    getSearch();
    router.push({
        path: '/search',
    });
}


</script>
