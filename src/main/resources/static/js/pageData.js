/**
 * 
 */

function selectYear(str) {
		
		var xhttp; //deklaracja pustej zmienej
// 		if(email == ""){
// 		var email = /*[[${email}]]*/ 'default';
// 		//var email = $('#email').val();
// 		return;
// 		}
		if (str == "") {
	    document.getElementById("table").innerHTML = "";
	    return;
	  	}
	
		//create object XMLHttpRequest
		if (window.XMLHttpRequest) {
		   // code for modern browsers
		   xhttp = new XMLHttpRequest();
		 } else {
		   // code for old IE browsers
		   xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	  
		//w�a�ciwo�c kt�r� posiada obiekt xhttp 
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
	      document.getElementById("table").innerHTML =
	      this.responseText;
	    }
	  };
	  xhttp.open("GET", "/profileYear?selectedYear="+str+"&email=" + email , true);
	  //xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	  xhttp.send(); 
}

function selectPage(str,selector) {
	
	var xhttp; //deklaracja pustej zmienej
	//var email = /*[[${email}]]*/ 'default';
	if (str == "") {
    document.getElementById(selector).innerHTML = "";
    return;
  	}

	//create object XMLHttpRequest
	if (window.XMLHttpRequest) {
	   // code for modern browsers
	   xhttp = new XMLHttpRequest();
	 } else {
	   // code for old IE browsers
	   xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
  
	//w�a�ciwo�c kt�r� posiada obiekt xhttp 
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById(selector).innerHTML =
      this.responseText;
      onSortingOrder()
      onPaginationChangeData();
      
    }
  };
  console.log(email);
  xhttp.open("GET", str ,true);
  //xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send(); 
}

  

$(function() {
	//	$(window).on('load', function () {             
	//	    var email= email;
	//	    alert(email);  });	
	onSortingOrder()
	onPaginationChangeData();
	
});

function selectPage1(obiekt) {
	console.log(obiekt.href);
}

function onSortingOrder(){
	$(".headTable th").on("click",function(event){
		 event.preventDefault()		 
		 var _href = this.getElementsByTagName('a')[0].href;
		 var _selectorByIdTableData = "table";
		 var _returnPageProfileTableData = "profileTableData";
		 //"http://localhost:8090/profileYear?email=6John@mail.com&returnPage=profileTableData&sort=startDate,desc"
		 if (!_href.includes("email=")){
			 var urlSortingOrder = _href + "&email=" + email + "&returnPage=profileTableData";
		 }else{
			 urlSortingOrder = _href;
		 }
		 sortParam = getUrlVars(_href)["sort"];	
		 selectPage(urlSortingOrder,_selectorByIdTableData);		 
	});
}

function onPaginationChangeData() {
	  $(".pagination li").on("click",function(event) {
		  event.preventDefault()
		  var _href = this.getElementsByTagName('a')[0].href;
//		  var email = email;
//		  var email = /*[[${email}]]*/ 'default';
		  var sort = getUrlVars()["sort"];
		  var _selectorByIdTableData = "table";
		  var _selectorByIdPageBehavior = "pageData";
		  var _divPageData = "";
		  var _returnPageProfileTableData = "profileTableData";
		  var _returnPagePageData = "pageData";
		  
		  
//		  var _innerText = this.innerText;
//		  var _innerHTML = this.innerHTML;
//		  var _textContent = this.textContent;		  
//		  console.log(/* _innerText, _innerHTML, _textContent, */ _href)
		var urlProfileTableData = _href +"&email=" + email + "&returnPage=profileTableData" + "&sort=" + sortParam;
		var urlPageData = _href +"&email=" + email + "&returnPage=pageData" + "&sort=" + sortParam;
		selectPage(urlProfileTableData,_selectorByIdTableData);
		selectPage(urlPageData,_selectorByIdPageBehavior);
	  });
	
}

function getUrlVars(str) {
    var vars = {};
    if(str !=null){
    	 var parts = str.replace(/[?&]+([^=&]+)=([^&]*)/gi,function(m,key,value) {
    	      vars[key] = value;
    	    });
    }else
    {
    	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,function(m,key,value) {
    	      vars[key] = value;
        });
    }    
    return vars;
  }