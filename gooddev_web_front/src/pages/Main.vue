<template>
    <div class = "main">
        <advertise/>
        <div class="main-content">
            <!-- Content (Notice, Boards, Gallery) -->
            <div class="content">
                <!-- Notice Board -->
                    <div class="main-notice-board">
                        <h2>공지사항</h2>
                        <div class="td-link" v-if='data.noticeList != null' v-for="board in data.noticeList">
                            <a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a>
                        </div>
                    </div>
                <!-- 게시판 Section -->
                <div class="main-boards">
                    <div class="main-boards-list" v-if='data.mainMap != null'  v-for="(value, key) in data.mainMap" :key="key">
                        <!-- key를 출력 -->
                        <h2>{{key}}</h2>
                        <div class="board" id="board1" v-for="board in value">
                            <div class="td-link">
                                <a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Gallery Section -->
                <div class="gallery-container">
                    <h2>갤러리</h2>
                    <main class="main-gallery">
                        <div class="gallery-item" v-if='data.galleryList != null' v-for="board in data.galleryList" :key="board.bno">
                            <div class="gallery-item" v-for="boardFile in board.boardFileDTOList" :key="boardFile.fid">
                                <img :src="('/api/board/download/' + boardFile.fid)" alt="이미지"/>
                            </div>
                            <div class="td-link">
                                <a @click.stop.prevent="openReadModal(board.bno)">{{ board.title }}</a>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <div>
                <!-- 로그인 Section -->
                <div v-if="loginInfo.mid.length ==0" class="main-login-container">
                    <div>
                        <div class="login-btn">
                            <router-link :to="'/login/' + data.encodedCurrentPath" class="mini-login-link">로그인하기</router-link>
                        </div>
                        <div class="login-links">
                            <router-link to="/register" class="mini-login-link">아이디 찾기</router-link>
                            <router-link to="/register" class="mini-login-link">비밀번호 찾기</router-link>
                            <router-link to="/register" class="mini-login-link">회원가입</router-link>
                        </div>
                    </div>
                </div>
                <div v-if="loginInfo.mid.length >0" class="main-login-container" >
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
                <Top10List v-if="data.topTenList != null" :topTenList="data.topTenList" @topTenRead="(bno) => openReadModal(bno)"/>
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
import Top10List from '@/components/Top10List.vue';
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';
import { onMounted, reactive, ref, watch } from 'vue';
import { useRoute } from 'vue-router';

const activeModalBno =  ref(null);

const openReadModal = (bno) => {
    activeModalBno.value = bno;
};

const closeReadModal = () => {
    activeModalBno.value = null;
};

const route = useRoute();
const store = useMainStore();
const {loginInfo} = storeToRefs(store);
const {doLogout, getMainPage} = store;

const data = reactive({
    noticeList : {},
    mainMap : {},
    galleryList : {},
    topTenList : {},
    encodedCurrentPath : encodeURIComponent(route.fullPath)
});

onMounted(async ()=>{
    const response = await getMainPage();
    data.galleryList = response.galleryList;
    data.mainMap = response.mainMap;
    data.noticeList = response.noticeList;
    data.topTenList = response.topTenList;
});

watch(
  () => route.path,
  async (newPath) => {
    if (newPath === '/main') { // '/main'은 메인 페이지의 경로
      const response = await getMainPage();
      data.galleryList = response.galleryList;
      data.mainMap = response.mainMap;
      data.noticeList = response.noticeList;
      data.top10List = response.topTenList;
    }
  }
);

</script>

