<template>
    <div class="login-body">
        <div class="login-container">
            <h2>굿 로그인</h2>
            <div>
                <form @submit.prevent="handleSubmit" id="loginForm" method="post">
                    <input type="text" class="form-control" placeholder="아이디" name="mid" v-model="data.loginInfo.mid" required>
                    <input type="password" class="form-control" placeholder="비밀번호" name="password" v-model="data.loginInfo.password" required>
                    <button type="submit" class="login-button">로그인하기</button>
                    <label for="autoLogin">자동 로그인</label><input type="checkbox" id="autoLogin" name="auto_login_check">
                </form>
            </div>
            <div class="navigation-links">
                <router-link to="/findID" class="nav-link">아이디 찾기</router-link>
                <router-link to="/findPWD" class="nav-link">비밀번호 찾기</router-link>
                <router-link to="/register" class="nav-link">회원가입</router-link>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useMainStore } from '@/store/mainStore';
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const store = useMainStore();
const { doLogin } = store;

const data = reactive({
    loginInfo : {
        mid: '',
        password : '',
    }
});

const router = useRouter();
const route = useRoute();

const handleSubmit = async ()=>{
    const loginResult = await doLogin(data.loginInfo);
    if(loginResult){
        const redirect = route.params.redirect;
        console.log(redirect)
        router.push({ path: decodeURIComponent(redirect)});
    }
    else{
        window.alert("로그인이 실패하였습니다.");
    }
}

</script>