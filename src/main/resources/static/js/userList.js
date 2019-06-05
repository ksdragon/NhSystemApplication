var returnViewUsersList = "fragments/usersListTableData";
var currentUrlInUsersList = window.location; 
var getUrlInUsersList = window.location;
var baseUrl = getUrlInUsersList.protocol + "//" + getUrlInUsersList.host + "/"
			+ getUrlInUsersList.pathname.split('/')[1];

$(function() {	
	onClickCreateExcelAllUsersTasks();
	registerAllListenerInUsersList();	
	onClickModalDeleteButtonInUsersList();
	
});

function registerAllListenerInUsersList(){	
	onPaginationChangeDataInUsersList();	
	onChangeSizePageSelectInUsersList();
	fillSizePageSelectInUsersList();
	onClickDeleteButtonInUsersList();	
	onSortingOrderInUsersList();
	
}

function onClickDeleteButtonInUsersList(){
	$('#usersTableData .table .delBtn').on('click',function(event){
		event.preventDefault();		
		var href = $(this).attr('href');
		$('#deleteModal #delRef').attr('href',href);			
		$('#deleteModal').modal();		
	});
}

function onClickModalDeleteButtonInUsersList(){
	$('#deleteModal #delRef').on('click',function(event){
		event.preventDefault();
		var href = $(this).attr('href');
		$.get(href,"returnPage=" + returnViewUsersList, function(data){
			$("#usersTableData").html(data);			
			registerAllListenerInUsersList();
		});
		$('#deleteModal').modal('hide');
	});
}


function selectPageInUsersList(str, selector) {

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
			registerAllListenerInUsersList();
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

function onSortingOrderInUsersList() {
	$("#usersTableData .headTableUserList th").on(
			"click",
			function(event) {
				event.preventDefault()
				var _href = this.getElementsByTagName('a')[0].href;				
				sortParam = getUrlVars(_href)["sort"];
				var urlSortingOrder = _href 
					+ "&returnPage=" + returnViewUsersList 
					+ "&size=" + sizeParam;
				selectPageInUsersList(urlSortingOrder, "usersTableData");
				currentUrlInUsersList = urlSortingOrder;
			});
}

function onPaginationChangeDataInUsersList() {
	$("#usersTableData .pagination li").on(
			"click",
			function(event) {
				event.preventDefault();
				var _href = this.getElementsByTagName('a')[0].href;							
				pageParam = getUrlVars(_href)["page"];	
				
				var urlTableData = baseUrl 
						+ "?returnPage=" + returnViewUsersList 
						+ "&sort=" + sortParam
						+ "&page=" + pageParam 
						+ "&size=" + sizeParam;
				selectPageInUsersList(urlTableData, "usersTableData");
				currentUrlInUsersList = urlTableData;
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
function fillSizePageSelectInUsersList() {
	var options = [ 1, 2, 5, 10, 15, 20, 50, 100 ];
	$('#sizePageSelect').empty();
	$.each(options, function(i, p) {
		$('#sizePageSelect').append($('<option></option>').val(p).html(p));
	});
	$('#sizePageSelect').val(sizeParam);
}

function onChangeSizePageSelectInUsersList() {
	$("#usersTableData #sizePageSelect").on(
			"change",
			function(event){
				event.preventDefault();
				let selOptionValue = this.options[this.selectedIndex].value;
				sizeParam = selOptionValue;
				
				var urlSortingOrder = baseUrl + "?returnPage=" + returnViewUsersList
				//					+ "&sort=" + sortParam
				+ "&page=" + 0 + "&size=" + sizeParam;
				selectPageInUsersList(urlSortingOrder, "usersTableData");
			});
}

