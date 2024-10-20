<template>
    <nav>
        <div class="menu-item">
            <router-link :to="{ path: '/boards', query: { category_no:10 } }" class="navbar a">공지사항</router-link>
        </div>
        <div class = "menu-item">
            <p class="navbar a">게시판</p>
            <div class="dropdown">
                <div class="dropdown-item">
                    <router-link :to="{ path: '/boards', query: {category_no:20 } }" class="navbar a">MSA5차</router-link>
                </div>
                <div class="dropdown-item">
                    <router-link :to="{ path: '/boards', query: {category_no:30 } }" class="navbar a">MSA6차</router-link>
                </div>
                <div class="dropdown-item">
                    <router-link :to="{ path: '/boards', query: {category_no:40 } }" class="navbar a">MSA7차</router-link>
                </div>
            </div>
        </div>
        <div class="menu-item">
            <router-link :to="{ path: '/gallery', query: {category_no:50 } }" class="navbar a">갤러리</router-link>
        </div>
        <div class="menu-item">
            <router-link to="/curicurum" class="navbar a">커리큘럼</router-link>
        </div>
        <div class="menu-item">
            <a href="https://www.sw.or.kr/site/sw/edu/selectEduListGallery.do" >교육신청</a>
        </div>
    </nav>
</template>

<script setup>
   
</script>