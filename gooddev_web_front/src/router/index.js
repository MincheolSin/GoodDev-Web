import { createRouter, createWebHistory } from 'vue-router';

import Read from '@/components/Read.vue';
import BoardInsert from '@/pages/BoardInsert.vue';
import Boards from '@/pages/Boards.vue';
import FindID from '@/pages/FindID.vue';
import FindPWD from '@/pages/FindPWD.vue';
import Gallery from '@/pages/Gallery.vue';
import Login from '@/pages/Login.vue';
import Main from '@/pages/Main.vue';
import MyPage from '@/pages/MyPage.vue';
import Register from '@/pages/Register.vue';
import Search from '@/pages/Search.vue';
import { useMainStore } from '@/store/mainStore';
import { storeToRefs } from 'pinia';

const loginGuard = (to,from,next) =>{
    const store= useMainStore();
    const {loginInfo} = storeToRefs(store);
    let isAuthenticated = false;
    if(loginInfo.value.mid != ''){
        isAuthenticated = true;
    }
    if (!isAuthenticated) {
        next({
            path: '/login/'+encodeURIComponent(from.fullPath),
            });
    } else {
        next();
    }
}

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Main },
        { path: '/gallery', component: Gallery },
        { path : '/search', component : Search},
        { path: '/login/:redirect', component: Login },
        { path: '/register', component: Register },
        { path: '/myPage', component: MyPage,
            beforeEnter: (to, from, next) => {
                loginGuard(to,from,next);
            }
        },
        { path: '/findID', component: FindID },
        { path: '/findPWD', component: FindPWD },
        { 
            path: '/boards',
            component: Boards,
            children: [
                { path: 'read/:bno', component: Read },

                {
                    path: '/board/insert', component: BoardInsert,
                    beforeEnter: (to, from, next) => {
                        loginGuard(to,from,next);
                    }
                }
            ]
        },
    ]
});

export default router;