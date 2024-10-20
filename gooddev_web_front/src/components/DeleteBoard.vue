<template>
    <div class="modal-modal" @click="handleOutsideClick">
      <div class="modal-box">
        <span class="input-group-text">삭제비밀번호를 입력해주세요</span>
        <form @submit.prevent="doDelete">
                <div class="input-group mb-3">
                    <span class="input-group-text">제목</span>
                    <input type="password" class="form-control" placeholder="password" v-model="data.board_password">
                </div>
                <input type="submit" class="btn btn-primary" value="삭제">
                <button @click.prevent="$emit('close');">돌아가기</button>
            </form>
      </div>
    </div>
</template>
  
<script setup>
import { useMainStore } from '@/store/mainStore';
import { defineEmits, defineProps, reactive } from 'vue';
    const emit = defineEmits(['close']);
    const store = useMainStore();
    const { deleteBoard , getPageResponseDTO} = store;
    
    const props = defineProps({
        bno: {
        type: String,
        required: true
        },
    });
    
    const data = reactive({
        board_password : '',
    });
    
    const doDelete = async () =>  {
        try{
            const response = await deleteBoard(props.bno,data.board_password);
            if(response.success) {
                window.alert("삭제되었습니다");
                getPageResponseDTO();
                emit('close');
            }
            else{
                window.alert("삭제실패!");
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