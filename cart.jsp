<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/button.css">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/horizonList.css">
<link rel="stylesheet" href="./css/message.css">
<link rel="stylesheet" href="./css/title.css">

<title>カート</title>
</head>
<body>
	<script src="./js/cart.js"></script>
	<jsp:include page="header.jsp" />
	<div id="contents">
		<h1>カート画面</h1>
		<!-- 		カートリストがnullかつ空でなければ一覧表示			 -->
		<s:if test="cartList!=null && cartList.size()>0">
			<s:form class="form" id="cart">
				<table class="horizon_list">
					<tr>
						<th><s:label value="#" /></th>
						<th><s:label value="商品名" /></th>
						<th><s:label value="商品名ふりがな" /></th>
						<th><s:label value="商品画像" /></th>
						<th><s:label value="値段" /></th>
						<th><s:label value="発売会社名" /></th>
						<th><s:label value="発売年月日" /></th>
						<th><s:label value="購入個数" /></th>
						<th><s:label value="合計金額" /></th>
					</tr>
					<tbody>
						<s:iterator value="cartList">
							<tr>
								<td><input type="checkbox" name="checkList" class="checkList" value='<s:property value="productId" />' onchange="checkValue()"></td>
								<td><s:property value="productName" /></td>
								<td><s:property value="productNameKana" /></td>
								<td><img
									src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>'
									width="50px" height="50px" /></td>
								<td><s:property value="price" />円</td>
								<td><s:property value="releaseCompany" /></td>
								<td><s:property value="releaseDate" /></td>
								<td><s:property value="productCount" /></td>
								<td><s:property value="subTotal" />円</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
				<h4>
					<label>カート合計金額 :</label>
					<s:property value="totalPrice" />
					円
				</h4>

				<br>
					<s:if test='#session.logined == 1'>
						<!-- 			ログインしていたら確認画面に遷移 -->
						<s:submit value="決済" onclick="goSettlementConfirmAction()" class="submit_btn_box"/>
					</s:if>
					<!-- 			ログインせずに決済ボタンを押したらログイン画面に遷移(カートフラグ=1を渡す)  -->
					<s:else>
						<s:submit value="決済" onclick="goGoLoginAction(1)" class="submit_btn_box" >
							<s:hidden name="cartFlag" value="1" />
						</s:submit>
					</s:else>
					<s:submit value="削除"  class="submit_btn_box" id="delete" onclick="goDeleteCartAction()" disabled="true" />
			</s:form>
		</s:if>
		<!-- 		カートリストがnullならメッセージ表示 -->
		<s:else>
		<div class="message_blue">
			<p>カート情報がありません。</p>
		</div>
		</s:else>
	</div>
</body>
</html>