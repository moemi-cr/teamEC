function checkValue() {
	var checkList = document.getElementsByClassName("checkList");
	var checkFlag = 0;
	for (var i = 0;  i<checkList.length;  i++) {

		if(checkList[i].checked) {
			checkFlag = 1;
			break;
		}
	}
	if (checkFlag == 1) {
    	document.getElementById('delete').disabled="";
	} else {
		document.getElementById('delete').disabled="true";
	}
}
function goDeleteCartAction() {
	document.getElementById("cart").action = "DeleteCartAction";
}
function goSettlementConfirmAction() {
	document.getElementById("cart").action = "SettlementConfirmAction";
}