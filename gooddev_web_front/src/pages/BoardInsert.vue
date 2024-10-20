<template>
    <div class="modal" @click="handleOutsideClick">
        <div class="modal-box">
            <div class="card-body">
            <form @submit.prevent="submitHandler" enctype="multipart/form-data">
                <div class="mb-3">
                    <select name="category_no" v-model= "data.boardInsert.category_no">
                        <option v-for="category in totalCategory" :key="category.category_no" :value= "category.category_no" >{{category.category_name}}</option>
                    </select>
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">제목</span>
                    <input name="title" class="form-control" placeholder="Title" v-model="data.boardInsert.title">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">내용</span>
                    <input name="content" class="form-control" placeholder="Content" v-model="data.boardInsert.content">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">게시글 비밀번호</span>
                    <input name="board_password" class="form-control" placeholder="Password" v-model="data.boardInsert.board_password">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text">첨부파일</span>
                    <input type="file" name="file" multiple @change="handleFileUpload"><br />
                </div>
                <input type="submit" class="btn btn-primary" value="등록">
                <input type="reset" class="btn btn-secondary" value="초기화">
                <a href="#" @click.prevent="returnPage">돌아가기</a>
            </form>
            </div>  
        </div>
    </div>
</template>

<script setup>
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';

    const store= useMainStore();
    const {loginInfo, totalCategory , pageRequestDTO} = storeToRefs(store);
    const {postBoard,updatePageRequestDTO} = store;
    const router = useRouter(); 
    const data = reactive({
        boardInsert : {
            title : '',
            content : '',
            mid : '',
            board_password : '',
            category_no :pageRequestDTO.value.category_no,
            files : [],
        },
    });

    const submitHandler = async ()=>{
        data.boardInsert.mid = loginInfo.value.mid;
        const result = await postBoard(data.boardInsert);
        if(result) {
            window.alert("등록되었습니다");
            updatePageRequestDTO({
                page : 1,
                size : 10,
                category_no : data.boardInsert.category_no,
                keyword : '',
            });
            router.push("/boards");
        }
        else{
            window.alert("등록에 실패하였습니다");
        }
    }

    const returnPage=()=>{
        router.back();
    }

    const handleOutsideClick = (event) => {
        if (event.target === event.currentTarget) {
            router.back();
        }
    };
   const handleFileUpload = (event) => {
      data.boardInsert.files = Array.from(event.target.files);
    }
</script>
