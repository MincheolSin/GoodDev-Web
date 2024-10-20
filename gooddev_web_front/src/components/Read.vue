<template>
  <div class="modal" @click="handleOutsideClick">
    <div class="modal-box">
      <div class="board-read-container">
        <div class="board-read-header">
          <h1 class="board-read-title">{{data.board.title}}</h1>
          <div class="board-read-meta">
            <span class="board-read-author">작성자: {{data.board.mid}}</span>
            <span class="board-read-date">일자: {{data.board.formatDate}}</span>
          </div>
        </div>
        <div class="board-read-content">
					<p>{{data.board.content}}</p>
				</div>
        <a class="button-link" @click.prevent="openUpdateModal" v-if="loginInfo.mid==data.board.mid">수정</a>
        <updateBoard v-if="isUpdateModalOpen" @close="closeUpdateModal":board="data.board"/>
        <a class="button-link" @click.prevent="openDeleteModal" v-if="loginInfo.mid==data.board.mid">삭제</a>
        <deleteBoard v-if="isDeleteModalOpen" @close="closeDeleteModal":bno="bno"/>
        <div class = "comment-section" id = "comment-section">
          <div class="comment" v-for="comment in data.comments" :key="comment.cno" id="comment">
              <div class="comment-header">
                  <span class="comment-author">{{comment.mid}}</span>
              </div>
              <div class="comment-content">
                  {{comment.comment_content}}
              </div>
          </div>
      </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import deleteBoard from '@/components/DeleteBoard.vue';
import updateBoard from '@/components/UpdateBoard.vue';
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { defineEmits, defineProps, onMounted, reactive, ref } from 'vue';


const isUpdateModalOpen = ref(false);
const isDeleteModalOpen = ref(false);

const emit = defineEmits(['close']);
const store = useMainStore();
const {loginInfo} = storeToRefs(store);
const { getBoard} = store;

onMounted(() => {
    
    requestAPI();
});

const openUpdateModal = () => {
  isUpdateModalOpen.value = true;
};

const closeUpdateModal = () => {
  requestAPI();
  isUpdateModalOpen.value = false;
};

const openDeleteModal = () => {
  isDeleteModalOpen.value = true;
};

const closeDeleteModal = () => {
  emit('close');
};

const props = defineProps({
  bno: {
    type: String,
    required: true
  },
});

const data = reactive({
  board : {
    bno : '',
    title : '',
    content : '',
    mid : '',
    board_password : '',
    category_no : '',
    view_cnt : '',
    insert_date : '',
    parent_bno : '',
    deleteYn : '',
    like_cnt : '',
    hate_cnt : '',
  },
  comments : [],
  
});

const requestAPI = async () =>  {
    try{
        const response = await getBoard(props.bno);
        data.board = response.board;
        data.comments = response.comments;
    
    }
    catch (error) {
        console.error("API 요청 중 오류 발생:", error);
     }
};


const handleOutsideClick = (event) => {
  if (event.target === event.currentTarget) {
    emit('close');
  }
};

</script>