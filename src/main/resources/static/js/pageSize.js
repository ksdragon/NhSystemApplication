function fillSizePageSelect() {
	var options = [ 1, 2, 5, 10, 15, 20, 50, 100 ];
	$('#sizePageSelect').empty();
	$.each(options, function(i, p) {
		$('#sizePageSelect').append($('<option></option>').val(p).html(p));
	});
	$('#sizePageSelect').val(sizeParam);
}

function onChangeSizePageSelect() {
	$("#sizePageSelect").on(
			"change",
			function(event){
				event.preventDefault();
				let selOptionValue = this.options[this.selectedIndex].value;
				sizeParam = selOptionValue;
				onSizeNumberOfRow()
			});
}