var _returnPageTableData = "fragments/usersListTableData";
var _returnPagePageData = "fragments/usersListPageData";
var _selectorByIdTableData = "tListUsers";	
var _selectorByIdPageBehavior = "pageDataListUsers";
var getUrl = window.location;
var baseUrl = getUrl.protocol + "//" + getUrl.host + "/"
			+ getUrl.pathname.split('/')[1];

$(function() {	
	onClickCreateExcelAllUsersTasks();
	registerAllListener();	
});


function registerAllListener(){
	onPaginationChangeData();
	onSortingOrder();
	fillSizePageSelect();
	onChangeSizePageSelect();
	onClickDeleteButton();
}

function onClickDeleteButton(){
	$('.table .delBtn').on('click',function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		$('#deleteModal #delRef').attr('href',href);
		onClickModalDeleteButton();
		$('#deleteModal').modal();		
	});
}

function onClickModalDeleteButton(){
	$('#deleteModal #delRef').on('click',function(event){
		var _href = this.getElementsByTagName('a')[0].href;
		var urlSortingOrder = _href + "&returnPage="
		+ _returnPageTableData;
		
		selectPage(urlSortingOrder, _selectorByIdTableData);
	});
}

function selectPage(str, selector) {

	var xhttp; // deklaracja pustej zmienej
	
	if (str == "") {
		document.getElementById(selector).innerHTML = "";
		return;
	}

	// create object XMLHttpRequest
	if (window.XMLHttpRequest) {
		// code for modern browsers
		xhttp = new XMLHttpRequest();
	} else {
		// code for old IE browsers
		xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}

	// w�a�ciwo�c kt�r� posiada obiekt xhttp
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById(selector).innerHTML = this.responseText;
			registerAllListener();

		}
	};
	console.log(email);
	xhttp.open("GET", str, true);
	// xhttp.setRequestHeader("Content-type",
	// "application/x-www-form-urlencoded");
	xhttp.send();
}

function seleYear(year) {
	selectedYear = year;
}

function onClickCreateExcelAllUsersTasks() {
	$("#createExcelAllUsersTasks").on(
			"click",
			function(event) {
				event.preventDefault()
				var urlcreatePdf = "/createExcelAllUsersTasks?selectedYear="
						+ selectedYear;
				window.location.href = urlcreatePdf;
			});
}

function onSortingOrder() {
	$(".headTableUserList th").on(
			"click",
			function(event) {
				event.preventDefault()
				var _href = this.getElementsByTagName('a')[0].href;								
				//				var getUrl = window.location;
				//				var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];
				sortParam = getUrlVars(_href)["sort"];

				var urlSortingOrder = _href + "&returnPage="
						+ _returnPageTableData + "&size=" + sizeParam;
				selectPage(urlSortingOrder, _selectorByIdTableData);
			});
}

function onPaginationChangeData() {
	$(".pagination li").on(
			"click",
			function(event) {
				event.preventDefault();
				var _href = this.getElementsByTagName('a')[0].href;							
				pageParam = getUrlVars(_href)["page"];	
				
				var urlTableData = baseUrl + "?returnPage="
						+ _returnPageTableData + "&sort=" + sortParam
						+ "&page=" + pageParam + "&size=" + sizeParam;
				var urlPageData = baseUrl + "?returnPage="
						+ _returnPagePageData + "&sort=" + sortParam + "&page="
						+ pageParam + "&size=" + sizeParam;

				selectPage(urlTableData, _selectorByIdTableData);
				selectPage(urlPageData, _selectorByIdPageBehavior);
			});

}

// retrieve param from url.
function getUrlVars(str) {
	var vars = {};
	if (str != null) {
		var parts = str.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key,
				value) {
			vars[key] = value;
		});
	} else {
		var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
				function(m, key, value) {
					vars[key] = value;
				});
	}
	return vars;
}

function onSizeNumberOfRow() {
	//				sortParam = getUrlVars(_href)["sort"];
	var urlPageData = baseUrl + "?returnPage=" + _returnPagePageData
	//					+ "&sort=" + sortParam
	+ "&page=" + 0 + "&size=" + sizeParam;
	var urlSortingOrder = baseUrl + "?returnPage=" + _returnPageTableData
	//					+ "&sort=" + sortParam
	+ "&page=" + 0 + "&size=" + sizeParam;
	selectPage(urlSortingOrder, _selectorByIdTableData);
	selectPage(urlPageData, _selectorByIdPageBehavior);
}

//var _innerText = this.innerText;
// var _innerHTML = this.innerHTML;
// var _textContent = this.textContent;
// console.log(/* _innerText, _innerHTML, _textContent,
// */ _href)

//function selectYear(str) {
//
//	var xhttp; // deklaracja pustej zmienej
//	// if(email == ""){
//	// var email = /*[[${email}]]*/ 'default';
//	// //var email = $('#email').val();
//	// return;
//	// }
//	if (str == "") {
//		document.getElementById("table").innerHTML = "";
//		return;
//	}
//
//	// create object XMLHttpRequest
//	if (window.XMLHttpRequest) {
//		// code for modern browsers
//		xhttp = new XMLHttpRequest();
//	} else {
//		// code for old IE browsers
//		xhttp = new ActiveXObject("Microsoft.XMLHTTP");
//	}
//
//	// w�a�ciwo�c kt�r� posiada obiekt xhttp
//	xhttp.onreadystatechange = function() {
//		if (this.readyState == 4 && this.status == 200) {
//			document.getElementById("table").innerHTML = this.responseText;
//			onPaginationChangeData();
//			onSortingOrder();
//
//		}
//	};
//	xhttp.open("GET", "/profileYear?selectedYear=" + str + "&email=" + email,
//			true);
//	// xhttp.setRequestHeader("Content-type",
//	// "application/x-www-form-urlencoded");
//	xhttp.send();
//}