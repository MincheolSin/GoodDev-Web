<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>굿이야 로그인</title>
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
        crossorigin="anonymous">
    <style>
        .login-body {
            background-color: #f8f9fa; /* 배경 색상 */
            height: 100vh; /* 전체 화면 높이 */
            display: flex; /* Flexbox 활성화 */
            justify-content: center; /* 수평 중앙 정렬 */
            align-items: center; /* 수직 중앙 정렬 */
            margin: 0; /* 기본 여백 제거 */
        }
        .login-container {
            max-width: 600px; /* 최대 너비 */
            height: 400px;
            background-color: #fff; /* 배경 색상 */
            padding: 20px; /* 패딩 */
            border: 1px solid #ddd; /* 테두리 */
            border-radius: 5px; /* 모서리 둥글게 */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
            text-align: center; /* 텍스트 중앙 정렬 */
        }
        .login-button {
            width: 100%; /* 버튼 너비를 가득 채움 */
            padding: 15px; /* 버튼 패딩 */
            font-size: 18px; /* 버튼 글자 크기 */
            background-color: #0eb4fc; /* 파란색으로 변경 */
            color: white; /* 글자 색상 */
            border: none; /* 테두리 제거 */
            border-radius: 5px; /* 모서리 둥글게 */
            cursor: pointer; /* 커서 변경 */
        }
        .login-button:hover {
            background-color: #0c9ce0; /* 호버 시 색상 변경 */
        }
        .form-control {
            margin-bottom: 15px; /* 입력창 사이 여백 */
        }
    </style>
</head>
<body class="login-body">
    <div class="login-container">
        <h2>굿 로그인</h2>
        <div>
           <form id="loginForm" action="login" method="post">
                <input type="text" class="form-control" placeholder="아이디" name="mid" required>
                <input type="password" class="form-control" placeholder="비밀번호" name="password" required>
                <button type="submit" class="login-button">로그인하기</button>
                <label for="autoLogin">자동 로그인</label><input type="checkbox" id="autoLogin" name="auto_login_check">
                <input type="hidden" name="redirect" value="<%= request.getParameter("redirect") %>">
            </form>
        </div>
        <div>
            <a href="findid" >아이디찾기</a>
            <a href="findpwd" >비밀번호찾기</a>
            <a href="register" >회원가입</a>
        </div>
    </div>
</body>
</html>