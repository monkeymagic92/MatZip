<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  
<div id="sectionContainerCenter">
	<div>
		<form id="frm" action="/restaurant/restRegProc" method="post" onsubmit="return chkFrm()">
			<div><input type="text" name="nm" placeholder="가게명"></div>
			<div>
				<input type="text" name="addr" placeholder="주소" onkeyup="changeAddr()" style="width: 200px;">
				<button type="button" onclick="getLatLng()">좌표가져오기</button><span id="resultGetLatLng"></span>
			</div>
			<input type="hidden" name="lat" value="0">
			<input type="hidden" name="lag" value="0">
			<div>
				카테고리 : 
				<select name="cd_category">
					<option value="0">--선택--</option>
					<c:forEach items="${categoryList}" var="item">
						<option value="${item.cd}">${item.val}</option>
					</c:forEach>
				</select>
			</div>
			<div><input type="submit" value="등록"></div>
		</form>
		
	</div>
	<!-- 중간 쿼리스트링값 중에 appkey는 절대 이런식으로 대놓고 노출하면안됨 
		여기서 사용한 이유는 단지 ajax 통신 만들기 귀찮아서 일단 스겜뛰기위해
	-->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=9996836ad8617fab6206b5bcc9625c1f&libraries=services"></script>
	<script>
	
		function chkFrm() {
			if(frm.nm.value.length == 0) {
				alert('가게명을 입력해 주세요');
				frm.nm.focus();
				
			} else if(frm.addr.value.length < 9) {
				alert('주소를 확인해 주세요');
				frm.addr.focus();
				return false;
				
			} else if(frm.lat.value == '0' || frm.lag.value == '0') {
				alert('좌표값을 가져와 주세요');
				return false;
			} else if(frm.cd_category.value == '0') {
				alert("카테고리를 선택해 주세요")
				frm.cd_category.focus();
				return false;
			}			
		}
	
		function changeAddr() {
			resultGetLatLng.innerText = ''; // 옆에 주소값 등록안되어있으면 빈값 
			frm.lat.value = '0'
			frm.lag.value = '0'
		}
	
		const geocoder = new kakao.maps.services.Geocoder();
	
		function getLatLng() {
			const addrStr = frm.addr.value
			
			if(addrStr.length < 9) {
				alert('주소를 확인해 주세요')
				frm.addr.focus()
				return
			}
			
			geocoder.addressSearch(addrStr, function(result, status) {
				if (status === kakao.maps.services.Status.OK) {
			        console.log(result[0]);
			        
			        if(result.length > 0) {
			        	resultGetLatLng.innerText = 'V'	// 옆에 주소값 추가되면 v 나타내기
			        	frm.lat.value = result[0].y
			        	frm.lag.value = result[0].x
			        }
			    }	
			});			
		}
	</script>
</div>