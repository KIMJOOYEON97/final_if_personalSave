<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Start1 | IF Maker Studio</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    
</head>
<body>
    <section>
    <form id="insertMakerInfo">
        <div class ="container p-5">
        	<h2><strong>메이커 정보</strong></h2>
            <p>안녕하세요 메이커님, 본격적으로 프로젝트 작성을 시작하기 전에 간단한 정보를 입력하세요</p>
            
            
			<!--휴대전화 인증-->
            <p class="font-weight-bold">관리자 휴대전화</p>
            <!-- 인증을 완료했을 경우 -->
            <div class="input-group mb-3">
                <!-- 회원정보를 받아와서 value에 휴대전화 번호를 넣을 것 -->
                <input type="text" name="makerPhone" class="form-control" aria-label="Recipient's username" aria-describedby="button-addon2">
                <div class="input-group-append">
                    <!-- 변경을 클릭하면 인증하기로 바뀔 것 | 인증하기를 클릭하면 재전송으로 바뀔것 -->
                    <button class="btn btn-outline-secondary" type="button" id="button-addon2">변경</button>
                </div>
            </div>
            <p class="font-weight-normal">인증을 완료한 회원입니다</p>
            
            
            <!-- 인증하기를 클릭해서 인증할 경우 -->
            <div class="input-group mb-3">
                <input type="text" class="form-control" placeholder="인증번호 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                <div class="input-group-append">
                  <button class="btn btn-outline-secondary" type="button">인증하기</button>
                </div>
            </div>


            <br><br><br>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <input type="checkbox" value aria-label="Checkbox for following text input">
                    </div>
                </div>
                <label type="text" class="form-control" aria-label="Text input with checkbox">(필수) 개인 정보 수집 동의
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend ">
                    <div class="input-group-text ">
                        <input type="checkbox"  aria-label="Checkbox for following text input">
                    </div>
                </div>
                <label type="text" class="form-control" aria-label="Text input with checkbox" >(선택) 와디즈 메이커를 위한 유용한 뉴스레터 받기
            </div>
            <button type="button" class="btn btn-primary btn-lg" onclick="location.href='${pageContext.request.contextPath}/funding/fundingStart2';">시작하기</button>
        </div>
    </form>
    </section>
    
<script>

</script>
</body>
</html>
 
