<template>
    <div v-if="loginInfo.mid == ''" class="mini-login-container">
        <div class="login-links">
            <router-link :to="'/login/' + data.encodedCurrentPath" class="mini-login-link">로그인하기</router-link>
        </div>
        <div class="login-links">
            <router-link to="/register" class="mini-login-link">회원가입</router-link>
        </div>
    </div>
    <div v-if="loginInfo.mid != ''" class="mini-login-container">
        <div>
            <label class="login-message">{{loginInfo.nickname}}님 환영합니다</label>
        </div>
        <div class="login-links">
            <div class="login-links">
                <router-link to="/myPage" class="mini-login-link">마이페이지</router-link>
            </div>
            <div class="login-links">
                <a  class="mini-login-link" @click.prevent="doLogout">로그아웃</a>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { reactive } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const store = useMainStore();
const {loginInfo} = storeToRefs(store);
const {doLogout} = store;

const data = reactive({
    encodedCurrentPath : encodeURIComponent(route.fullPath),
});

</script>
