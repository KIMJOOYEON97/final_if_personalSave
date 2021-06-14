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
    <title>Save | IF Maker Studio</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <!-- SweetAlert Ver1 , 2아님 -->
	<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>
    <section>
    <!-- <form id="insertMakerInfo"> -->
        <div class ="container p-5">
        	<h2><strong>프로젝트 새로 만들기</strong></h2>
          
            <button class="btn btn-outline-info " type="button" id="sendPhoneNumber" onclick="location.href='${pageContext.request.contextPath}/funding/fundingStart2';">새로 만들기 </button>
          
            
            <br />
           	<hr />
           	<br />
        	<h2><strong>만든 프로젝트 정보</strong></h2>
            
			<div class="card" style="width: 18rem;">
			  <img class="card-img-top" src="..." alt="Card image cap">
			  <div class="card-body">
			    <h5 class="card-title">Card title</h5>
			    <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
			    <button class="btn btn-outline-info " type="button" id="sendPhoneNumber" onclick="location.href='${pageContext.request.contextPath}/funding/fundingStart2';">스튜디오 바로가기 </button>
			  </div>
			</div>
    </section>
    
</body>
</html>
 
