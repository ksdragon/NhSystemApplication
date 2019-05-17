$(function() {
	registerAllListener();
	onClickPdfDownloadRaportTasksUser();

});

function registerAllListener(){
	onSortingOrder();
	onPaginationChangeData();
	fillSizePageSelect();
	onChangeSizePageSelect();
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

function seleYear(year){
	var urlSelectedYear = "/profileYear?selectedYear=" + year + "&email=" + email
						+ "&returnPage=profileTableData" + "&sort=" + sortParam;
	var urlPageData = "/profileYear?selectedYear=" + year + "&email=" + email
					+ "&returnPage=pageData" + "&sort=" + sortParam;
	selectedYear = year;
	selectPage(urlSelectedYear,'table');
	selectPage(urlPageData, 'pageData');
}

function selectPage1(obiekt) {
	console.log(obiekt.href);
}

//function onClickPdfDownloadRaportTasksUser1(){	
//			event.preventDefault()
//			var urlcreatePdf = "/createPdf?selectedYear=" + year + "&email=" + email
//			 + "&sort=" + sortParam;
//			window.location.href = urlcreatePdf;
//			};
//}
//

function onClickPdfDownloadRaportTasksUser(){
	$("#pdfRaportTasksUser").on(
			"click",
			function(event) {
			event.preventDefault()
			var urlcreatePdf = "/createPdf?selectedYear=" + selectedYear + "&email=" + email + "&sort=" + sortParam;
			window.location.href = urlcreatePdf;
			});
}

function onSortingOrder() {
	$(".headTable th").on(
			"click",
			function(event) {
				event.preventDefault()
				var _href = this.getElementsByTagName('a')[0].href;
				var _selectorByIdTableData = "table";
				var _returnPageProfileTableData = "profileTableData";
				// "http://localhost:8090/profileYear?email=6John@mail.com&returnPage=profileTableData&sort=startDate,desc"
				if (!_href.includes("email=")) {
					var urlSortingOrder = _href + "&email=" + email
							+ "&returnPage=profileTableData" 
							+ '&selectedYear=' + selectedYear;
				} else {
					var urlSortingOrder = _href + '&selectedYear=' + selectedYear;
				}
				sortParam = getUrlVars(_href)["sort"];
				selectPage(urlSortingOrder, _selectorByIdTableData);
			});
}

function onPaginationChangeData() {
	$(".pagination li")
			.on(
					"click",
					function(event) {
						event.preventDefault()
						var _href = this.getElementsByTagName('a')[0].href;
						var _selectorByIdTableData = "table";
						var _selectorByIdPageBehavior = "pageData";
						var _returnPageProfileTableData = "profileTableData";
						var _returnPagePageData = "pageData";
						
						var urlProfileTableData = _href 
								+ "&email=" + email
								+ "&returnPage=profileTableData" 
								+ "&selectedYear="	+ selectedYear 
								+ "&sort=" + sortParam 
								+ "&size=" + sizeParam;
						var urlPageData = _href 
								+ "&email=" + email
								+ "&returnPage=pageData" 
								+ "&selectedYear=" 	+ selectedYear 
								+ "&sort=" + sortParam
								+ "&size=" + sizeParam;
						selectPage(urlProfileTableData, _selectorByIdTableData);
						selectPage(urlPageData, _selectorByIdPageBehavior);
					});

}

function onSizeNumberOfRow() {	
	var _selectorByIdTableData = "table";
	var _returnPageTableData = "profileTableData";
	var _returnPagePageData = "pageData";
	var _selectorByIdPageBehavior = "pageData";
	let sActionName = "profileYear"
	var getUrl = window.location;	
	var baseUrl = getUrl.protocol + "//" + getUrl.host + "/"
			+ sActionName;
	var urlPageData = baseUrl 
		+ "?returnPage=" + _returnPagePageData
		+ "&email=" + email
		+ "&sort=" + sortParam
		+ "&page=" + 0 
		+ "&size=" + sizeParam;
	var urlSortingOrder = baseUrl 
		+ "?returnPage=" + _returnPageTableData
		+ "&email=" + email
		+ "&sort=" + sortParam
		+ "&page=" + 0 
		+ "&size=" + sizeParam;
	selectPage(urlSortingOrder, _selectorByIdTableData);
	selectPage(urlPageData, _selectorByIdPageBehavior);
	//			});
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