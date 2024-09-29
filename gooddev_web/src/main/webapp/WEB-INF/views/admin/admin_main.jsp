<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 - goodDev</title>

		<!-- Bootstrap 5를 위한 외부 라이브러리 설정 -->
		<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
		crossorigin="anonymous">
		<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
		
		<!-- external css -->
		<link rel="stylesheet" href="<c:url value='/resources/css/my_page.css'/>">
		
</head>
<body>
	<div>
		<h1>여기가 공지사항이 들어갈 부분</h1>
		<div class="container wrap" style="width:100%; height: 100%; padding-top:100px; padding-bottom: 300px;">
			<div class="d-flex">
		        <!-- 관리자용 메뉴 바 -->
		        <div id="my_box" style="width:24%; height: 80%; padding: 0px 30px;">
		            <h2 style="padding-bottom: 60px; width:15rem;"><a href="mypage" style="text-decoration-line: none; color:black;"><b>관리자페이지</b></a></h2> 
		            <ul class="my_menu">
		                <li id="menu1" style="height: 50%;">  
		                    <a class="menu_depth01" href="#">관리자 정보</a> 
		                    <ul class="menu_depth02">
		                    	<li id="update"><a href="memberList">전체 회원목록 관리</a></li>
		                        <li id="update"><a href="noticeList">전체 공지사항 관리</a></li> 
		                        <li id="myBoardList"><a href="boardList">전체 게시물 관리</a></li> 
		                    </ul>
		                </li>
		            </ul>
		        </div>
			<div class="content" style="width:80%; padding:0px 30px;">
				<div class="profile">
					<div class="user_info">
						<span class="name" id="spanNickname">관리자{$써야함} 님</span>
					</div>
				</div>
				<div class="wrapper">
				<!-- contents -->
				<div class="main p-3">
					<div class="section_block">
						<div class="container mt-3">
							  <!-- 페이지네이션 -->
							  <div class="float-end" >
							    <ul class="pagination flex-wrap" >
							        <c:if test="${not empty pageResponseDTO && not empty pageRequestDTO}">
							            <c:if test="${pageResponseDTO.prev}">
							                <li class="page-item" ><a class="page-link" href="#" data-param="${pageRequestDTO.getParam(pageResponseDTO.start-1)}">이전</a></li>
							            </c:if>
							            <c:forEach var="num" begin="${pageResponseDTO.start}" end="${pageResponseDTO.end}">
							                <li class="page-item ${pageResponseDTO.page == num ? 'active' : ''}">
							                    <a class="page-link" href="?page=${num}&size=${pageResponseDTO.size}" data-param="${pageRequestDTO.getParam(num)}">${num}</a>
							                </li>		
							            </c:forEach>
							            <c:if test="${pageResponseDTO.next}">
							                <li class="page-item"><a class="page-link"href="#" data-param="${pageRequestDTO.getParam(pageResponseDTO.end+1)}">다음</a></li>
							            </c:if>
							        </c:if>
							    </ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			</div>
		</div>
	</div>
	<script>
		//페이지네이션 자바스크립트 (게시물목록)	
		document.querySelectorAll(".page-link").forEach(item => {
		    item.addEventListener('click', e => {
		        e.preventDefault();
		        e.stopPropagation();
		        
		        const param = e.target.getAttribute("data-param");
	            self.location = "boardList?" + param; // 경로
		    });
		});
	</script>
	</div>
</body>
</html>