<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - 굿이야</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">
        var contextPath = "${pageContext.request.contextPath}";
        document.addEventListener('DOMContentLoaded', function() {
            let registerLink = document.getElementById('registerBtn');
            registerLink.addEventListener('click', function(event) {
                event.preventDefault(); // 기본 제출 동작을 막음
                let originalHref = registerLink.getAttribute('formaction');
                const redirectParam = window.location.href;
                registerLink.setAttribute('formaction', originalHref + "?redirect=" + encodeURIComponent(redirectParam));
                document.getElementById('registerForm').submit(); // 폼 제출
            });

            let isValidId = false;
            let isEmailValid = false;
            let isPasswordValid = false;

            // 아이디 중복 확인
            $("#checkIdBtn").click(function() {
                const checkMid = $("#mid").val();
                if (!checkMid) {
                    $("#idCheckMessage").text("아이디를 입력해주세요.");
                    isValidId = false;
                    return;
                }

                $.ajax({
                    url: contextPath + '/member/checkIdDuplicate',
                    type: 'POST',
                    data: { mid: checkMid },
                    success: function(response) {
                        if (response === "invalid") {
                            $("#idCheckMessage").text("유효하지 않은 아이디 형식입니다.");
                            isValidId = false;
                        } else if (response === "duplicate") {
                            $("#idCheckMessage").text("이미 사용 중인 아이디입니다.");
                            isValidId = false;
                        } else if (response === "available") {
                            $("#idCheckMessage").text("사용 가능한 아이디입니다.");
                            isValidId = true;
                        }
                    },
                    error: function() {
                        $("#idCheckMessage").text("서버 오류가 발생했습니다. 다시 시도해주세요.");
                        isValidId = false;
                    }
                });
            });

            // 비밀번호 유효성 검사
            $("#password").blur(function() {
                const password = $(this).val();
                const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
                if (passwordRegex.test(password)) {
                    $("#passwordCheckMessage").text("유효한 비밀번호 형식입니다.");
                    isPasswordValid = true;
                } else {
                    $("#passwordCheckMessage").text("비밀번호는 8자 이상이며, 영문자와 숫자를 포함해야 합니다.");
                    isPasswordValid = false;
                }
            });

            // 이메일 유효성 검사
            $("#email").blur(function() {
                const email = $(this).val();
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (emailRegex.test(email)) {
                    $("#emailCheckMessage").text("유효한 이메일 형식입니다.");
                    isEmailValid = true;
                } else {
                    $("#emailCheckMessage").text("유효하지 않은 이메일 형식입니다.");
                    isEmailValid = false;
                }
            });

            // 폼 제출 전 유효성 검사
            $("#registerForm").submit(function(e) {
                e.preventDefault(); // 기본 동작을 막음

                if (!isValidId) {
                    $("#idCheckMessage").text("아이디 중복 확인을 해주세요.");
                } else if (!isEmailValid) {
                    $("#emailCheckMessage").text("유효한 이메일을 입력해주세요.");
                } else if (!isPasswordValid) {
                    $("#passwordCheckMessage").text("비밀번호를 확인해주세요.");
                } else {
                    this.submit(); // 모든 유효성 검사가 통과되면 폼을 제출
                }
            });
        });
    </script>
</head>
<body>
    <div class="container">
        <%@ include file="/WEB-INF/views/commons/header.jsp" %>
        <%@ include file="/WEB-INF/views/commons/nav.jsp" %>
        <div class="main">
            <%@ include file="/WEB-INF/views/commons/advertisement.jsp" %>
            <div class="main-content">
                <div class="register-form">
                    <h2>회원가입</h2>
                    <form id="registerForm" action="<c:url value='/member/register'/>" method="post" enctype="application/x-www-form-urlencoded">
                        <div class="form-group">
                            <label for="mid">아이디:</label>
                            <input type="text" id="mid" name="mid" value="${memberDTO.mid}" required>
                            <button type="button" id="checkIdBtn">중복 체크</button>
                            <span id="idCheckMessage"></span>
                        </div>

                        <div class="form-group">
                            <label for="password">비밀번호:</label>
                            <input type="password" id="password" name="password" required>
                            <span id="passwordCheckMessage"></span>
                        </div>

                        <div class="form-group">
                            <label for="member_Name">이름:</label>
                            <input type="text" id="member_Name" name="member_Name" value="${memberDTO.member_Name}" required>
                        </div>

                        <div class="form-group">
                            <label for="nickname">닉네임:</label>
                            <input type="text" id="nickname" name="nickname" value="${memberDTO.nickname}" required>
                        </div>

                        <div class="form-group">
                            <label for="email">이메일:</label>
                            <input type="email" id="email" name="email" value="${memberDTO.email}" required>
                            <span id="emailCheckMessage"></span>
                        </div>

                        <div class="form-group">
                            <label for="phone">전화번호:</label>
                            <input type="tel" id="phone" name="phone" value="${memberDTO.phone}" required pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}" placeholder="000-0000-0000">
                        </div>

                        <button type="submit" name="register" value="register" id="registerBtn" formaction="<c:url value='/member/register'/>" class="submit-btn">가입하기</button>
                    </form>
                </div>
            </div>

            <%@ include file="/WEB-INF/views/commons/advertisement.jsp" %>
        </div>

        <%@ include file="/WEB-INF/views/commons/footer.jsp" %>
    </div>
</body>
</html>
