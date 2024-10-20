<template>
    <div class="modal-modal"  @click="handleOutsideClick">
      <div class="modal-box">
        <div class="card-body">
            <form @submit.prevent="doUpdate" enctype="multipart/form-data">
                <div class="mb-3">
                    <select name="category_no" v-model= "data.boardUpdate.category_no">
                        <option v-for="category in totalCategory" :key="category.category_no" :value= "category.category_no" >{{category.category_name}}</option>
                    </select>
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">제목</span>
                    <input name="title" class="form-control" placeholder="Title" v-model="data.boardUpdate.title">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">내용</span>
                    <input name="content" class="form-control" placeholder="Content" v-model="data.boardUpdate.content">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">게시글 비밀번호</span>
                    <input name="board_password" class="form-control" placeholder="Password" v-model="data.boardUpdate.board_password">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">첨부파일</span>
                    <input type="file" name="file" multiple @change="handleFileUpload"><br />
                </div>
                <input type="submit" class="btn btn-primary" value="등록">
                <input type="reset" class="btn btn-secondary" value="초기화">
                <button @click.prevent="$emit('close');">돌아가기</button>
            </form>
        </div>  
      </div>
    </div>
</template>
  
<script setup>
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { defineEmits, defineProps, onMounted, reactive } from 'vue';

    const emit = defineEmits(['close']);
    const store = useMainStore();
    const {totalCategory} = storeToRefs(store);
    const { updateBoard, getPageResponseDTO} = store;
    
    const props = defineProps({
        board: {
        type: Object,
        required: true
        },
    });
    
    const data = reactive({
        boardUpdate: {
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
        
    });

    onMounted(()=>{
        data.boardUpdate = {...props.board};
    });

    const doUpdate = async () =>  {
        try{
            const response = await updateBoard(data.boardUpdate);
            if(response.success) {
                window.alert("수정을 완료하였습니다");
                getPageResponseDTO();
                emit('close');
            }
            else{
                window.alert("수정실패!");
            }
        
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