<!-- 리워드를 입력하는 페이지 입니다.  -->
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/views/common/makerNav.jsp" flush="false">
    <jsp:param value="Reward | IF Maker Studio" name="title"/>
</jsp:include>
<style>
/* 모달 배경화면 투명도  */
.modal-backdrop
{
    opacity:0.5 !important;
}

/* 내부 scroll */
/* Important part */
.modal-dialog{
    overflow-y: initial !important
}
.modal-body{
    height: 80vh;
    overflow-y: auto;
}
</style>
<%
	Date nowTime = new Date();
	SimpleDateFormat sf =new SimpleDateFormat("yyyy-MM-dd");
%>
    <div class="container p-5">


        <h1 class="font-weight-bold">리워드 설계</h1>
        <p>서포터에게 제공할 리워드 내용을 입력하세요. 서포터가 쉽게 펀딩할 수 있는 저렴한 리워드부터 서포터의 보다 빠른 펀딩을 유도할 수 있는 얼리버드 리워드까지 다양한 리워드를 구성하세요.</p>
        <br>
        <div class="border p-3">
            <p class="font-weight-bold pt-3 pb-1">리워드 설계 조건</p>
            <table>
                <tr>
                    <td class="pt-2">
                        <p class="text-muted font-weight-bold">정렬 순서</p>
                    </td>
                    <td class="pl-5">
                        <p class="text-muted">
                            혜택이 높은 순으로 나오도록 정렬 순서를 등록하세요.
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="text-muted font-weight-bold">제한 수량</p>
                    </td>
                    <td class="pl-5">
                        <p class="text-muted ">
                            생산 및 제공할 수 있는 리워드의 총 수량으로 해당 수량이 모두 펀딩되면 더 이상 펀딩 불가합니다
                        </p>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p class="text-muted font-weight-bold">발송 시작일</p> 
                    </td>
                    <td class="pl-5">
                        <p class="text-muted ">
                            프로젝트 종료일 이후부터 11개월 이내로 설계 가능합니다.
                        </p>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td class="pl-5">
                        <div class="bg-light text-muted">
                            ※ 설정한 발송 시작일까지 리워드가 발송되지 않을 경우, 서포터가 펀딩금 반환을 신청할 수 있으니 신중하게 선택하세요.
                            <br>
                            ※ 종료일+11개월까지 발송이 시작되지 않은 경우, 미발송 상태인 서포터의 펀딩금 반환이 자동으로 반환(결제취소)되니 유의하세요.
                        </div>
                        <br>

                    </td>
                </tr>
               <!--  <tr>
                    <td>
                        <p class="text-muted font-weight-bold">옵션 조건
                        </p> 
                    </td>
                    <td class="pl-5">
                        <p class="text-muted ">
                            옵션(사이즈, 색상 등)이 필요한 경우, 옵션을 추가하세요.리워드 옵션 2개 이상 설계 방법 알아보기
                        </p>
                    </td>
                </tr> -->

            </table>
        </div>
        <br>
        <button type="button" onclick="makerRewardAdd()" class="btn btn-secondary">
            +추가하기
        </button>
        <br>

        <!-- insert Modal 처리 -->
        <div id="makerRewardInsert" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document" >
            <div class="modal-content">
            <form id="rewardInsertFrm" name="rewardInsertFrm" action="${pageContext.request.contextPath}/funding/insertReward" method="POST"
            	onsubmit="return rewardInsertValidate();">          
                <input type="hidden" name="fundingNo" value="${funding.fundingNo}">
                <div class="modal-header">
                    <p class="font-weight-bold">리워드 추가</p>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <hr>
                <div class="modal-body" data-spy="scroll"  style="height:70vh;">
                    
                    <br>    
                    <h6 class="font-weight-bold"> 금액</h6>
                    <div class="input-group">
                        <input type="number" name="price" min ="1000" max="100000000"  class="form-control" placeholder="목표 금액 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <span class="p-1">원</span>
                    </div>

                    <br>    
                    <h6 class="font-weight-bold">리워드명</h6>
                    <div class="input-group">
                        <textarea id="rewardName" name="title" type="text" class="form-control" maxlength="40" placeholder="타이틀 입력 ex)[얼리버드]기본 리워드"  rows="1" aria-label="Recipient's username" aria-describedby="basic-addon2"></textarea>
                    </div>
                    <p id="rewardName_cnt" class="text-muted ml-1" style="font-size: 12px">60자 남음</p>

                    <h6 class="font-weight-bold">상세설명</h6>
                    <div class="input-group">
                        <textarea id="describe" name="content" class="form-control" aria-label="With textarea" id="basicRequireYes" placeholder="제공하는 리워드가 무엇인지 간략하게 제시해 주세요." cols=""  rows="3" maxlength="400"></textarea>
                    </div>
                        <!-- 몇 글자 남았는지 표시 -->
                        <p id="describe_cnt" class="text-muted ml-1" style="font-size: 12px">400자 남음</p>
                    
                    <!-- <br>
                    <h6>옵션조건</h6>
                    <div class="input-group mb-3">
                        <select class="custom-select" id="makerCategory">
                            <option selected disabled>옵션 조건 유형을 선택하세요</option>
                            <option value="optionNon">옵션 없음</option>
                            <option value="optionChoice">선택 옵션(사이즈, 색상 등)</option>
                            <option value="optionDirect">직접 입력 옵션(각인,메시지 등)</option>
                        </select>
                    </div>
                    선택옵션일 경우
                    <div id="optionChoice">
                        <p class="text-muted">옵션 설정</p>
                        <textarea class="form-control" aria-label="With textarea" id="basicRequireYes" placeholder="옵션 값을 입력하세요. &#13;&#10;옵션값은 엔터로 구분됩니다.&#13;&#10; 예시:블랙,230mm &#13;&#10;         화이트, 240mm" cols=""  rows="5"></textarea>
                    </div>
                    직접 입력 옵션(각인, 메시지등)일 경우
                    <div id="optionDirect">
                        <input type="text" class="form-control" placeholder="옵션 안내 메시지 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <p class="text-muted">예시)한글 10자, 영문 15자 이내 각인 메시지를 입력하세요</p>
                    </div> -->

                    <br>
                    <h6>배송조건</h6>
                    <div class="makerRewardShipPrice ml-1">
                    	<div class="custom-control custom-radio">
							<input type="radio" name="shipPriceYN" id="shipNeed" 
								class="custom-control-input"  onchange="showShipPrice()" checked> 
								<label class="custom-control-label" for="shipNeed">배송이 필요한 리워드 입니다.</label>
						</div>
						    <!-- 배송이 필요한 리워드 일때만 보일 것 -->
						<br>
                        <div id="shipPrice" class="input-group">
                            <label class="p-1 ml-4">배송료&nbsp;</label>
                            <div>
	                            <input name="shippingPrice" type="hidden" value="2500">
                            	<span class="p-1">2500원</span>
                                <p class="text-muted" style="font-size: 12px">(배송비가 없는 경우,0원 입력)</p>
                            </div>                            
                        </div>
						<div class="custom-control custom-radio">
							<input type="radio" name="shipPriceYN" id="noShip"
								class="custom-control-input" onchange="showShipPrice()"> 
								<label class="custom-control-label" for="noShip">배송이 필요 없는 리워드입니다.</label>
						</div>
                    </div>

                    <br>    
                    <h6 class="font-weight-bold">제한수량</h6>
                    <div class="input-group">
                        <input name="limitAmount" type="number" class="form-control" min="1" max="999999999999" placeholder="수량 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <span class="p-1">개</span>
                    </div>
                    
                    <br>
                    <h6 class="font-weight-bold">발송시작일</h6>
                    <div class="input-group-prepend">
                   	<c:if test="${reward.shippingDate== null}">
				        <div class="form-group row">
						    <input class="form-control ml-3" type="date" value="<%=sf.format(nowTime) %>" name="shippingDate">
						</div>
				    </c:if>
			 
			     	<c:if test="${reward.shippingDate != null}">
				        <div class="form-group row">
						    <input class="form-control ml-3" type="date" value="${reward.shippingDate}" name="shippingDate">
						</div>
			     	</c:if>
			   
                    </div>

                    <br><br />
                    <p class="text-muted" style="font-size: 12px">
	                    ※ 설정한 발송 시작일까지 리워드가 발송되지 않을 경우, 서포터가 펀딩금 반환을 신청할 수 있으니 신중하게 선택하세요.
	                   	<br /><br />
	                    <!-- 종료일 동적으로 설정하기 -->
	                    ※ 종료일+11개월까지 리워드 발송이 시작되지 않은 경우, 미발송 상태인 서포터의 펀딩금 반환이 자동으로 완료 처리(결제 취소)되니 유의하세요
                    </p>
                    
                    <div style="text-align:center;">
                        <input type="reset" data-dismiss="modal" value="취소" class="d-inline-flex btn btn-outline-primary btn-lg "/>
                        &nbsp;&nbsp;
                        <input type="submit" value="등록" class="d-inline-flex btn btn-primary btn-lg"></input>
                    </div>
                </div>
            </form>
                
            </div>
            </div>
        </div>

         <!-- update Modal 처리 -->
         <div id="makerRewardUpdate" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document" >
            <div class="modal-content">
            <form id="rewardUpdateFrm" name="rewardUpdateFrm" action="${pageContext.request.contextPath}/funding/updateReward" method="POST"
            onsubmit="return rewardUpdateValidate();">
                <%--  <c:forEach items="${rewardList}" var="reward"> --%>
                 <input type="hidden" name="hiddenRewardInfo"/>
                 <input type="hidden" name="fundingNo"/>
                 <div class="modal-header">
                    <p class="font-weight-bold">리워드 수정</p>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <hr>
                <div class="modal-body" data-spy="scroll"  style="height: 80vh;">
                    
                    <br>
                    <h6 class="font-weight-bold"> 금액</h6>    
                    <div class="input-group">
                        <input name="price" type="number" min ="1000" max="100000000"  class="form-control" placeholder="목표 금액 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <span class="p-1">원</span>
                    </div>

                    <br>    
                    <h6 class="font-weight-bold">리워드명</h6>
                    <div class="input-group">
                        <textarea name="title" id="rewardName" type="text" class="form-control" maxlength="40" placeholder="타이틀 입력 ex)[얼리버드]기본 리워드"  rows="1" aria-label="Recipient's username" aria-describedby="basic-addon2"></textarea>
                    </div>
                    <p id="rewardName_cnt" class="text-muted ml-1" style="font-size: 12px">60자 남음</p>

                    <h6 class="font-weight-bold">상세설명</h6>
                    <div class="input-group">
                        <textarea id="describe" name="content" class="form-control" aria-label="With textarea" id="basicRequireYes" placeholder="제공하는 리워드가 무엇인지 간략하게 제시해 주세요." cols=""  rows="3" maxlength="400"></textarea>
                    </div>
                        <!-- 몇 글자 남았는지 표시 -->
                        <p id="describe_cnt" class="text-muted ml-1" style="font-size: 12px">400자 남음</p>
                    
                   <!--  <br>
                    <h6>옵션조건</h6>
                    <div class="input-group mb-3">
                        <select class="custom-select" id="makerCategory">
                            <option selected disabled>옵션 조건 유형을 선택하세요</option>
                            <option value="optionNon">옵션 없음</option>
                            <option value="optionChoice">선택 옵션(사이즈, 색상 등)</option>
                            <option value="optionDirect">직접 입력 옵션(각인,메시지 등)</option>
                        </select>
                    </div>
                    선택옵션일 경우
                    <div id="optionChoice">
                        <p class="text-muted">옵션 설정</p>
                        <textarea class="form-control" aria-label="With textarea" id="basicRequireYes" placeholder="옵션 값을 입력하세요. &#13;&#10;옵션값은 엔터로 구분됩니다.&#13;&#10; 예시:블랙,230mm &#13;&#10;         화이트, 240mm" cols=""  rows="5"></textarea>
                    </div>
                    직접 입력 옵션(각인, 메시지등)일 경우
                    <div id="optionDirect">
                        <input type="text" class="form-control" placeholder="옵션 안내 메시지 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <p class="text-muted">예시)한글 10자, 영문 15자 이내 각인 메시지를 입력하세요</p>
                    </div> -->

                    <br>
                    <h6>배송조건</h6>
                    <div class="makerRewardShipPrice ml-1">
                    	<div class="custom-control custom-radio">
							<input type="radio" name="shipPriceYN" id="shipNeed2"
								class="custom-control-input"  onchange="showShipPrice2()"> 
								<label class="custom-control-label" for="shipNeed2">배송이 필요한 리워드 입니다.</label>
						</div>
						    <!-- 배송이 필요한 리워드 일때만 보일 것 -->
						<br>
                        <div id="shipPrice2" class="input-group">
                            <label class="p-1 ml-4">배송료&nbsp;</label>
                              <div>
	                            <input name="shippingPrice" type="hidden" value="2500">
                            	<span class="p-1">2500원</span>
                                <p class="text-muted" style="font-size: 12px">(배송비가 없는 경우,0원 입력)</p>
                            </div>                        
                        </div>
						<div class="custom-control custom-radio">
							<input type="radio" name="shipPriceYN" id="noShip2"
								class="custom-control-input" onchange="showShipPrice2()"> <label
								class="custom-control-label" for="noShip2">배송이 필요 없는 리워드입니다.</label>
						</div>
                    </div>

                    <br>    
                    <h6 class="font-weight-bold">제한수량</h6>
                    <div class="input-group">
                        <input name="limitAmount" type="number" class="form-control" maxlength="40" placeholder="수량 입력" aria-label="Recipient's username" aria-describedby="basic-addon2">
                        <span class="p-1">개</span>
                    </div>
                    
                    <br>
                    <h6 class="font-weight-bold">발송시작일</h6>
                    <div class="input-group-prepend">
                   		<c:if test="${reward.shippingDate == null}">
					        <div class="form-group row">
							    <input class="form-control ml-3" type="date" value="<%=sf.format(nowTime) %>" name="shippingDate">
							</div>
				     	</c:if>
				 
				     	<c:if test="${reward.shippingDate != null}">
					        <div class="form-group row">
							    <input class="form-control ml-3" type="date" value="" name="shippingDate">
							</div>
				     	</c:if>
                    </div>

                    <br><br />
                    <p class="text-muted" style="font-size: 12px">
	                    ※ 설정한 발송 시작일까지 리워드가 발송되지 않을 경우, 서포터가 펀딩금 반환을 신청할 수 있으니 신중하게 선택하세요.
	                   	<br /><br />
	                    <!-- 종료일 동적으로 설정하기 -->
	                    ※ 종료일+11개월까지 리워드 발송이 시작되지 않은 경우, 미발송 상태인 서포터의 펀딩금 반환이 자동으로 완료 처리(결제 취소)되니 유의하세요
                    </p>
                    
                    <div style="text-align:center;">
                        <input type="reset" value="취소" data-dismiss="modal" class="d-inline-flex btn btn-outline-primary btn-lg "/>
                        &nbsp;&nbsp;
                        <input type="submit" value="수정" class="d-inline-flex btn btn-primary btn-lg"></input>
                    </div>
                </div>
                <%-- </c:forEach> --%>
            </form>
                
            </div>
            </div>
        </div>

		
			
        <!-- 리워드 추가시 card로 리워드 보이게 하기 -->
        <div class="rewardCardSpace d-flex">
			
			<c:if test="${not empty rewardList}">
			<c:forEach items="${rewardList}" var="reward">
			<form name="rewardUpdateDeleteFrm" method="POST" action="${pageContext.request.contextPath}/funding/deleteReward">
            <input type="hidden" name="rewardNo"/>
            <div class="card d-inline-flex m-2 mt-4" style="width: 300px">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <!-- 가격 불러오기 -->
                        <h5 class="card-title text-muted">${reward.price}원</h5>
                        <!-- 제한 수량 불러오기 -->
                        <button type="button" class="btn btn-primary btn-sm">제한 수량 ${reward.limitAmount}개</button>
                    </div>
                    <hr>
                    <!-- 리워드 명 불러오기 -->
                    <h6 class="font-weight-bold card-subtitle mb-2 text-muted">${reward.title}</h6>
                    <!-- 상세설명 불러오기  -->
                    <p class="card-text text-muted">${content}</p>
                    <!-- 배송비 불러오기 -->
                    <div class="d-flex justify-content-between mb-2">
                        <p class="font-weight-bold text-muted">배송비</p>
                        <p class="card-subtitle text-muted" style="margin-top: unset;">${reward.shippingPrice}원</p>
                    </div>
                    <!-- 리워드 발송 시작일 불러오기 -->
                    <div class="d-flex justify-content-between mb-2">
                        <p class="font-weight-bold text-muted">리워드 발송 시작일</p>
                        <p class="card-subtitle text-muted" style="margin-top: unset;">${reward.shippingDate}</p>
                    </div>
                    
                    <div class="d-flex justify-content-end">
                        <!-- 매개변수로 reward no 넘겨줄것 -->
                        <button type="button" class="btn btn-outline-secondary mr-3" 
                        data-target="#makerRewardUpdate" data-toggle="modal" onclick="sendRewardInfo(${reward.rewardNo})">편집</button>
                        <button type="button" class="btn btn-outline-secondary" onclick="deleteReward(${reward.rewardNo})">삭제</a></button>
                    </div>
                </div>
            </div>
            </form>
			</c:forEach>
			</c:if>

        </div>


        <br><br>
        <button type="button" class="btn btn-primary btn-lg" style="width: 200px;" onclick="rewardSave()">저장하기</button>
        
        
    </div>
</section> 
<script>

	function rewardSave(){

		const msg = "리워드를 저장했습니다.";
		location.href = `${pageContext.request.contextPath}/funding/ready1Funding/\${msg}`;
		
	}

	var fundingNo;
	var price;
	var title;
	var content;
	var shippingPrice;
	var limitAmount;
	var shippingDate;


   function makerRewardAdd(){
        $("#makerRewardInsert").modal()
    };

    function sendRewardInfo(rewardNo){

		console.log(rewardNo);

		$.ajax({
			url:`${pageContext.request.contextPath}/funding/selectOneReward/\${rewardNo}`,
			contentType: "application/json; charset=utf-8",
			async:false,
			method: "GET",
			success(data){
				
				const {chReward} = data;

				fundingNo = chReward.fundingNo;
				price = chReward.price;
				title = chReward.title;
				content = chReward.content;
				shippingPrice = chReward.shippingPrice;
				limitAmount = chReward.limitAmount;
				shippingDate = chReward.shippingDate;

				
			},
			error: console.log
			});

        
/*     	var reward =$(btn).data("reward");
    	console.log(typeof reward);
		
		rewardInfo = reward.split("[()]");
			
		console.log(reward[0]);
		console.log(reward);
		console.log(rewardInfo);
		console.log(rewardInfo.title);
		console.log(rewardInfo[1]); */
		
    };

	function formatDate(date) {
		 var d = new Date(date),
		 month = '' + (d.getMonth() + 1),
		 day = '' + d.getDate(),
		 year = d.getFullYear();
		 if (month.length < 2) month = '0' + month; 
		 if (day.length < 2) day = '0' + day; 
		 return [year, month, day].join('-'); }
    
    
    $('#makerRewardUpdate').on('show.bs.modal', function(e) {

        
        	console.log("작동");
        	console.log(fundingNo);
    		const $frm = $("[name=rewardUpdateFrm]",e.target);
	    	$frm.find("[name=fundingNo]").val(fundingNo);
			$frm.find("[name=price]").val(price);
			$frm.find("[name=title]").val(title);
			$frm.find("[name=content]").val(content);
			if(shippingPrice > 0 && shippingPrice != null){
				$frm.find("[id=shipNeed2]").prop("checked",true);
				$frm.find("[name=shippingPrice]").val(shippingPrice);
			}else{
				$frm.find("[id=noShip2]").prop("checked",true);
				$("#shipPrice2").hide();
			}
			$frm.find("[name=limitAmount]").val(limitAmount);
			$frm.find("[name=shippingDate]").val(formatDate(shippingDate));
    	});
	

    //유효성 검사
    function rewardInsertValidate(){

    	var $frm =  $('#rewardInsertFrm');
       
    	console.log($frm);
    	const $price = $frm.find("[name=price]").val();
    	const $title = $frm.find("[name=title]").val();
    	const $content = $frm.find("[name=content]").val();
    	const $shippingPrice = $frm.find("[name=shippingPrice]").val();
    	const $limitAmount = $frm.find("[name=limitAmount]").val();
    	const $shippingDate = $frm.find("[name=shippingDate]").val();
    	console.log($content);

    	if(/^(.|\n)+$/.test($price) == false){
    		swal("리워드 금액을 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($title) == false){
    		swal("리워드명을 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($content) == false){
    		swal("상세설명을 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($shippingPrice) == false){
    		swal("배송비를 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($limitAmount) == false){
    		swal("제한수량을 입력하세요","","warning")
    		return false;
    	}
   

    	return true;
    }
    
    function rewardUpdateValidate(){

    	var $frm =  $('#rewardUpdateFrm');
     
    	console.log($frm);
    	const $price = $frm.find("[name=price]").val();
    	const $title = $frm.find("[name=title]").val();
    	const $content = $frm.find("[name=content]").val();
    	/* const $shippingPrice = $frm.find("[name=shippingPrice]").val(); */
    	const $limitAmount = $frm.find("[name=limitAmount]").val();
    	//배송시작일을 default값이 있으므로 필요가 없음
    	console.log($content);

    	if(/^(.|\n)+$/.test($price) == false){
    		swal("리워드 금액을 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($title) == false){
    		swal("리워드명을 입력하세요","","warning")
    		return false;
    	}
    	if(/^(.|\n)+$/.test($content) == false){
    		swal("상세설명을 입력하세요","","warning")
    		return false;
    	}
    	/* 배송비 fix로 필요 없어짐
    	if(/^(.|\n)+$/.test($shippingPrice) == false){
    		swal("배송비를 입력하세요","","warning")
    		return false;
    	} */
    	if(/^(.|\n)+$/.test($limitAmount) == false){
    		swal("제한수량을 입력하세요","","warning")
    		return false;
    	}
    
    	return true;
    }

    
    // 배송비 선택시 보이게 안보이게 하기
    function showShipPrice(){
        if($('input:radio[id=shipNeed]').is(':checked')){
            console.log("1보임");
            $('#shipPrice').show();
        }else{
            console.log("안보임");
            $('#shipPrice').hide();

        }
    };
    
    // 배송비 선택시 보이게 안보이게 하기
    function showShipPrice2(){
       if($('input:radio[id=shipNeed2]').is(':checked')){
        	console.log("2보임");
            $('#shipPrice2').show();
        }else{
            console.log("안보임");
            $('#shipPrice2').hide();
        }
    };

    //옵션 선택시 보이게 안보이게 하기 -> 옵션 일단 뺌
    $(document).ready(function() {
       /*      //처음에는 둘 다 안보이게
            $('#optionDirect').hide();
            $('#optionChoice').hide();

        $('#makerCategory').change(function() {

            var result = $('#makerCategory option:selected').val();
           
            if (result == 'optionDirect') {
                $('#optionDirect').show();
                $('#optionChoice').hide();
            }else {
                $('#optionChoice').show();
                $('#optionDirect').hide();

            }

        });  */
         
	    //남은 글자 수 표시하기
        $('#rewardName').on('keyup', function() {
            $('#rewardName_cnt').html( 60 - $(this).val().length+"자 남음");
 
            if($(this).val().length > 60) {
                $(this).val($(this).val().substring(0, 60));
                
            }
        });
        $('#describe').on('keyup', function() {
            $('#describe_cnt').html(400 - $(this).val().length+"자 남음");
 
            if($(this).val().length > 400) {
                $(this).val($(this).val().substring(0, 400));
                
            }
        });
        
    }); 

    function deleteReward(rewardNo){

    	
    	swal({
    		  title: "리워드를 삭제하겠습니까?",
    		  text: "",
    		  icon: "warning",
    		  buttons: true,
    		  dangerMode: true,
    		})
    		.then(function(){
    		  var $frm = $(document.rewardUpdateDeleteFrm);
              $frm.find("[name = rewardNo]").val(rewardNo);
              $frm.submit();
            
    	});
		
     
    }

</script>
</body>
</html>