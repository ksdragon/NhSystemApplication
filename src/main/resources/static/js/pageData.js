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

function selectPage(str,email,selector,redirectPage) {
	
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
    }
  };
  console.log(email);
  xhttp.open("GET", str+"&email=" + email + "&returnPage=" + redirectPage, true);
  //xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
  xhttp.send(); 
}

  

$(function() {
//	$(window).on('load', function () {             
//	    var email= email;
//	    alert(email);  }); 
	  $(".pagination li").on("click",function(event) {
		  event.preventDefault()
		  var _href = this.getElementsByTagName('a')[0].href
//		  var email = email;
//		  var email = /*[[${email}]]*/ 'default';
		  var _selectorByIdTableData = "table";
		  var _selectorByIdPageBehavior = "pageData";
		  var _divPageData = "";
		  var _returnPageProfileTableData = "profileTableData";
		  var _returnPagePageData = "pageData";
		  
		  
//		  var _innerText = this.innerText;
//		  var _innerHTML = this.innerHTML;
//		  var _textContent = this.textContent;
		  
		  console.log(/* _innerText, _innerHTML, _textContent, */ _href)
//		selectPage(_href,email,_selectorByIdTableData,_returnPageProfileTableData);
		selectPage(_href,email,_selectorByIdPageBehavior,_returnPagePageData);
	  });
	});
	
	function selectPage1(obiekt) {
	console.log(obiekt.href);
}