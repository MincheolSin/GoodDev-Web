<template>
    <div class="pagination-container">
        <ul class="pagination">
            <li>
                <button @click.prevent="pagination" v-if="pageResponseDTO.prev" id="prev" class="page-button" :data-page="pageResponseDTO.start-1">이전</button>
            </li>
            <li v-for="page in pageRange" :key="page" class="page-item">
                <button :class="{ active: pageResponseDTO.page === page }"  @click.prevent="pagination" class="page-button" :data-page="page">{{ page }}</button>
            </li>
            <li>
                <button @click.prevent="pagination" v-if="pageResponseDTO.next" id="next" class="page-button" :data-page="pageResponseDTO.end+1">다음</button>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
    pageResponseDTO: {
        type: Object,
    },
});

const emits = defineEmits(['pageNav']);

const pagination = (e) => {
    emits("pageNav",e.target.dataset.page)
};

const pageRange = computed(() => {
    const start = props.pageResponseDTO.start;
    const end = props.pageResponseDTO.end;
    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
});
</script>