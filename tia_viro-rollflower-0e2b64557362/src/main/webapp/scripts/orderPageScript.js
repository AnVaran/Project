$("document").ready(function() {
	var isValidInput = false;

	$(".submit-order-form").submit(function() {
		$.ajax({
			type : "POST",
			url : "/rollflower/order/submit",
			data : {
				"type" : "submit",
				"address" : $(".address-input").val(),
				"phone" : $(".phone-input").val(),
				"commentary" : $(".commentary-text").text(),
			},
			dataType : "html",
			success : function(data) {
				$(".modal-content").empty();
				$(".modal-content").append(data);
				$(".modal").modal({
					backdrop : 'static',
					keyboard : false
				});
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("Status: " + textStatus);
				console.log("Error: " + errorThrown);
			}
		});
		return false;
	})

	$(".search-order").submit(function() {
		$.ajax({
			type : "POST",
			url : "/rollflower/order/search",
			data : {
				"type" : "search",
				"order" : $(".search-order .form-control").val(),
			},
			dataType : "html",
			success : function(data) {
				$(".result-table-container").empty();
				$(".result-table-container").append(data);
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log("Status: " + textStatus);
				console.log("Error: " + errorTgrown);
			}
		});
		return false;
	})
})

function redirectFRomModal() {
	window.location.href = "/rollflower";
}

function hideModal() {
	$(".modal").modal("hide");
}