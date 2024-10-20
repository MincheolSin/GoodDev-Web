<template>
    <div class = "main">
        <advertise/>
        <div class="main-content">
            <div class = "board-list">
                <boardsItem :boards="pageResponseDTO.list"/>
                <router-link to="/board/insert">글쓰기</router-link>
                <pageNavBar @page-nav="pagination" v-if="pageResponseDTO.start != null && pageResponseDTO.page > 0" :pageResponseDTO="pageResponseDTO"/>
                <div class="boards-search-wrapper">
                    <span class="search-label">제목+내용</span>
                    <div class="search-container">
                        <form @submit.prevent="doSearch" method="get" class="search-form">
                            <input name="keyword" id="keyword" class="search-input" :placeholder="pageRequestDTO.keyword || '검색어를 입력해주세요'" v-model="keyword">
                            <button type="submit" class="search-button">검색</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <advertise/>
    </div>
    <router-view/>
</template>

<script setup>
import boardsItem from '@/components/BoardsItem.vue';
import pageNavBar from "@/components/PageNavBar.vue";
import advertise from '@/components/SideAd.vue';
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from "pinia";
import { ref, watch } from 'vue';
import { onBeforeRouteLeave, useRoute } from 'vue-router';

const store = useMainStore();
const {pageRequestDTO,pageResponseDTO} = storeToRefs(store);
const { updatePageRequestDTO } = store;
const route = useRoute();

const keyword = ref('');

const pagination = (page)=>{
    updatePageRequestDTO({
      page: page,
    });
}

watch(() => route.query, (newQuery) => {
    const { category_no } = newQuery;
    if(category_no || keyword){
        if (category_no && category_no != pageRequestDTO.value.category_no) {
            updatePageRequestDTO({
                page: 1,
                size: 10,
                category_no: category_no || undefined,
            });
        }
    }
}, { immediate: true });


const doSearch = () => {
    updatePageRequestDTO({
      page: 1,
      size: 10,
      keyword: keyword.value || '',
    });
};

onBeforeRouteLeave(()=>{
    updatePageRequestDTO({
        page : 1,
        size : 10,
        keyword : ''
    });
});


</script>