<template>
    <div class="main-content">
        <div class="register-form">
            <h2>회원가입</h2>
            <form @submit.prevent="register" id="registerForm" method="post" enctype="application/x-www-form-urlencoded">
                <div class="form-group">
                    <label for="mid">아이디:</label>
                    <input type="text" id="mid" name="mid" v-model="data.memberDTO.mid" required>
                    <button type="button" id="checkIdBtn">중복 체크</button>
                    <span id="idCheckMessage"></span>
                </div>

                <div class="form-group">
                    <label for="password">비밀번호:</label>
                    <input type="password" id="password" name="password" v-model="data.memberDTO.password" required>
                    <span id="passwordCheckMessage"></span>
                </div>

                <div class="form-group">
                    <label for="member_Name">이름:</label>
                    <input type="text" id="member_Name" name="member_name" v-model="data.memberDTO.member_name" required>
                </div>

                <div class="form-group">
                    <label for="nickname">닉네임:</label>
                    <input type="text" id="nickname" name="nickname" v-model="data.memberDTO.nickname" required>
                </div>

                <div class="form-group">
                    <label for="email">이메일:</label>
                    <input type="email" id="email" name="email" v-model="data.memberDTO.email" required>
                    <button type="button" id="checkEmailBtn">중복 체크</button>
                    <span id="emailCheckMessage"></span>
                </div>

                <div class="form-group">
                    <label for="phone">전화번호:</label>
                    <input type="tel" id="phone" name="phone" v-model="data.memberDTO.phone" required pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}" placeholder="000-0000-0000">
                </div>

                <button type="submit" name="register" value="register" id="registerBtn" class="submit-btn">가입하기</button>
            </form>
        </div>
    </div>
</template>

<script setup>
import { useMainStore } from '@/store/mainStore';
import { onMounted, reactive, toRaw } from 'vue';
import { useRouter } from 'vue-router';
const store = useMainStore();
const{idCheck,emailCheck,postMember} = store;
const data = reactive({
    confirm : {
        isValidId : false,
        isEmailValid :false,
        isPasswordValid : false,
    },
    memberDTO : {
        mid : '',
        password : '',
        member_name : '',
        nickname : '',
        email : '',
        phone : '',
    }
});

const router = useRouter();

const memberDTO = toRaw(data.memberDTO);

const register = async () => {
    if (!data.confirm.isValidId) {
        document.getElementById('idCheckMessage').textContent = "아이디 중복 확인을 해주세요.";
        return;
    }
    
    if (!data.confirm.isEmailValid) {
        document.getElementById('emailCheckMessage').textContent = "유효한 이메일을 입력해주세요.";
        return;
    }
    
    if (!data.confirm.isPasswordValid) {
        document.getElementById('passwordCheckMessage').textContent = "비밀번호를 확인해주세요.";
        return;
    }
    const response = await postMember(data.memberDTO);
    if(response.success){
        window.alert("회원가입에 성공하였습니다");
        router.replace('/');
    }
    else{
        window.alert("회원가입에 실패하였습니다");
    }
}

onMounted(() => {
    // 아이디 중복 확인
    document.getElementById('checkIdBtn').addEventListener('click', async function() {
        if (data.memberDTO.mid.length==0) {
            document.getElementById('idCheckMessage').textContent = "아이디를 입력해주세요.";
        }
        else {
            const response = await idCheck(data.memberDTO.mid);
            if(response == "invalid"){
                document.getElementById('idCheckMessage').textContent = "아이디 형식이 맞지 않습니다.";
            }
            else if(response == "duplicate"){
                document.getElementById('idCheckMessage').textContent = "중복된 아이디입니다.";
            }
            else if(response == "available"){
                document.getElementById('idCheckMessage').textContent = "사용 가능한 아이디입니다.";
                data.confirm.isValidId = true;
            }
            else{
                document.getElementById('idCheckMessage').textContent = "서버오류입니다 다시 시도해주세요";
            }
        }

    });

    document.getElementById('mid').addEventListener('blur', function() {
        data.confirm.isValidId = false;
        document.getElementById('idCheckMessage').textContent = "아이디 중복 확인을 해주세요.";
    });

    // 이메일 중복 및 유효성 확인
    document.getElementById('checkEmailBtn').addEventListener('click', async function() {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (data.memberDTO.email.length==0) {
            document.getElementById('emailCheckMessage').textContent = "이메일을 입력해주세요.";
            data.confirm.isEmailValid = false;
            return;
        }

        if (!emailRegex.test(data.memberDTO.email)) {
            document.getElementById('emailCheckMessage').textContent = "유효하지 않은 이메일 형식입니다.";
            data.confirm.isEmailValid = false;
            return;
        }
        
        else {
            const response = await emailCheck(data.memberDTO.email);
            if(response == "invalid"){
                document.getElementById('emailCheckMessage').textContent = "이메일 형식이 맞지 않습니다.";
            }
            else if(response == "duplicate"){
                document.getElementById('emailCheckMessage').textContent = "중복된 이메일입니다.";
            }
            else if(response == "available"){
                document.getElementById('emailCheckMessage').textContent = "사용 가능한 이메일입니다.";
                data.confirm.isEmailValid = true;
            }
            else{
                document.getElementById('emailCheckMessage').textContent = "서버오류입니다 다시 시도해주세요";
            }
        }
    });

    document.getElementById('email').addEventListener('blur', function() {
        data.confirm.isEmailValid = false;
        document.getElementById('emailCheckMessage').textContent = "이메일 중복 확인을 해주세요.";
    });

    // 비밀번호 유효성 검사
    document.getElementById('password').addEventListener('blur', function() {
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/;
        if (passwordRegex.test(data.memberDTO.password)) {
            document.getElementById('passwordCheckMessage').textContent = "유효한 비밀번호 형식입니다.";
            data.confirm.isPasswordValid = true;
        } else {
            document.getElementById('passwordCheckMessage').textContent = "비밀번호는 6자 ~ 최대 15자 이며, 영문자와 숫자를 포함해야 합니다.";
            data.confirm.isPasswordValid = false;
        }
    });
   
});
</script>