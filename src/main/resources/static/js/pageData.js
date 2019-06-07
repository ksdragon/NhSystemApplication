var _selectorByIdTableData = "table";
var _returnPageTableData = "fragments/profileTableData";	

$(function() {
	registerAllListenerInProfile();
	onClickPdfDownloadRaportTasksUser();

});

function registerAllListenerInProfile(){
	onSortingOrderInProfile();
	onPaginationChangeDataInProfile();
	fillSizePageSelectInProfile();
	onChangeSizePageSelectInProfile();
}

function selectPageInProfile(str, selector) {

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
			registerAllListenerInProfile();

		}
	};
	console.log(email);
	xhttp.open("GET", str, true);
	// xhttp.setRequestHeader("Content-type",
	// "application/x-www-form-urlencoded");
	xhttp.send();
}

function seleYearInProfile(year){
	var urlSelectedYear = "/profile?selectedYear=" + year 
						+ "&email=" + email
						+ "&returnPage=" + _returnPageTableData 
						+ "&sort=" + sortParam
						+ "&size=" + sizeParam;
//	var urlPageData = "/profile?selectedYear=" + year + "&email=" + email
//					+ "&returnPage=views/profilePageData" + "&sort=" + sortParam;
	selectedYear = year;
	selectPageInProfile(urlSelectedYear,'table');
}

function onClickPdfDownloadRaportTasksUser(){
	$("#pdfRaportTasksUser").on(
			"click",
			function(event) {
			event.preventDefault()
			var urlcreatePdf = "/createPdf?selectedYear=" + selectedYear + "&email=" + email + "&sort=" + sortParam;
			window.location.href = urlcreatePdf;
			});
}

function onSortingOrderInProfile() {
	$(".headTable th").on(
			"click",
			function(event) {
				event.preventDefault()
				var _href = this.getElementsByTagName('a')[0].href;					
				// "http://localhost:8090/profileYear?email=6John@mail.com&returnPage=profileTableData&sort=startDate,desc"
				if (!_href.includes("email=")) {
					var urlSortingOrder = _href + "&email=" + email
							+ "&returnPage=" + _returnPageTableData
							+ '&selectedYear=' + selectedYear
							+ "&sort=" + sortParam 
							+ "&size=" + sizeParam;
				} else {
					var urlSortingOrder = _href + '&selectedYear=' + selectedYear;
				}
				sortParam = getUrlVars(_href)["sort"];
				selectPageInProfile(urlSortingOrder, _selectorByIdTableData);
			});
}

function onPaginationChangeDataInProfile() {
	$(".pagination li")
			.on(
					"click",
					function(event) {
						event.preventDefault()
						var _href = this.getElementsByTagName('a')[0].href;							
						var urlProfileTableData = _href 
								+ "&email=" + email
								+ "&returnPage=" + _returnPageTableData 
								+ "&selectedYear="	+ selectedYear 
								+ "&sort=" + sortParam 
								+ "&size=" + sizeParam;
						selectPageInProfile(urlProfileTableData, _selectorByIdTableData);
					});

}

function onSizeNumberOfRowInProfile() {
	
	let sActionName = "profile"
	var getUrl = window.location;	
	var baseUrl = getUrl.protocol + "//" + getUrl.host + "/"
			+ sActionName;
	var urlSortingOrder = baseUrl 
		+ "?returnPage=" + _returnPageTableData
		+ "&email=" + email
		+ "&sort=" + sortParam
		+ "&selectedYear="	+ selectedYear
		+ "&page=" + 0 
		+ "&size=" + sizeParam;
	selectPageInProfile(urlSortingOrder, _selectorByIdTableData);
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

function fillSizePageSelectInProfile() {
	var options = [ 1, 2, 5, 10, 15, 20, 50, 100 ];
	$('#sizePageSelect').empty();
	$.each(options, function(i, p) {
		$('#sizePageSelect').append($('<option></option>').val(p).html(p));
	});
	$('#sizePageSelect').val(sizeParam);
}

function onChangeSizePageSelectInProfile() {
	$("#sizePageSelect").on(
			"change",
			function(event){
				event.preventDefault();
				let selOptionValue = this.options[this.selectedIndex].value;
				sizeParam = selOptionValue;
				onSizeNumberOfRowInProfile()
			});
}



function selectPageInProfile1(obiekt) {
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
//			onPaginationChangeDataInProfile();
//			onSortingOrderInProfile();
//
//		}
//	};
//	xhttp.open("GET", "/profileYear?selectedYear=" + str + "&email=" + email,
//			true);
//	// xhttp.setRequestHeader("Content-type",
//	// "application/x-www-form-urlencoded");
//	xhttp.send();
//}