<template>
    <div class = "main">
        <advertise/>
        <div class="main-content">
            <div class="search-results-container">
                <p v-if="Object.keys(searchData).length === 0" colspan="6" style="text-align: center;">검색 결과가 없습니다.</p>
                <section v-for="(value,key) in searchData" class="search-board">
                    <h2>{{key}}</h2>
                    <ul class="search-board-list" v-for="(board,index) in value">
                            <li class="search-board-item"><a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a></li>
                        <li v v-if="index === value.length - 1" class="search-board-item more-link">
                            <a class="search-plus-link" @click.stop.prevent="goToList(board.category_no)">해당 게시판의 결과 더보기</a>
                        </li>
                    </ul>
                </section>
            </div>
        </div>
        <advertise/>
    </div>
    <ReadBoard
      v-if="activeModalBno !== null"
      @close="closeReadModal"
      :bno="activeModalBno"
    />
</template>

<script setup>
import ReadBoard from '@/components/Read.vue';
import advertise from '@/components/SideAd.vue';
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { onBeforeRouteLeave, useRouter } from 'vue-router';

const activeModalBno =  ref(null);

const openReadModal = (bno) => {
    activeModalBno.value = bno;
};

const closeReadModal = () => {
    activeModalBno.value = null;
};

const store = useMainStore();
const router = useRouter();

const {updatePageRequestDTO, updateSearchKeyword} = store;
const {searchData , searchKeyword}  = storeToRefs(store);

const goToList = (category_no) =>{
    updatePageRequestDTO({
        page : 1,
        size : 10,
        category_no : category_no,
        keyword : searchKeyword
    });
    router.push({
        path : '/boards'
    });
}

onBeforeRouteLeave(()=>{
    updateSearchKeyword('');
});

</script>