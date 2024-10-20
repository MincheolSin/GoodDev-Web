<template>
     <div class = "list-table-container">
        <table class="list-table">
            <thead>
                <tr>
                    <th scope="col">번호</th>
                    <th scope="col">제목</th>
                    <th scope="col">작성자</th>
                    <th scope="col">일자</th>
                    <th scope="col">조회수</th>
                    <th scope="col">좋아요</th>
                </tr>
            </thead>
            <tbody id="list_body">
                <tr v-if='boards.length ==0'>
                    <td colspan="6" style="text-align: center;">검색 결과가 없습니다.</td>
                </tr>
                <tr v-if='boards != null' v-for="board in boards" :key="board.bno">
                    <td>{{ board.bno }}</td>
                    <td class="td-link">
                        <a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a>
                    </td>
                    <td>{{ board.mid }}</td>
                    <td>{{ board.formatDate }}</td>
                    <td>{{ board.view_cnt }}</td>
                    <td>{{ board.like_cnt }}</td>
                </tr>
            </tbody>
        </table>
    </div>
    <ReadBoard
      v-if="activeModalBno !== null"
      @close="closeReadModal"
      :bno="activeModalBno"
    />
</template>

<script setup>
import ReadBoard from '@/components/Read.vue';
import { ref } from 'vue';

const activeModalBno =  ref(null);

const openReadModal = (bno) => {
    activeModalBno.value = bno;
};

const closeReadModal = () => {
    activeModalBno.value = null;
};

const props = defineProps({
    boards: {
        type: Array,
        default: () => [],
    },
});



</script>
