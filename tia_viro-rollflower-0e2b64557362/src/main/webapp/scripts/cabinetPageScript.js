$("document").ready(function() {
	$(".delete-button").click(function() {
		$(".modal").modal({
			backdrop : 'static',
			keyboard : false
		});
	})
	$(".cancel-delete").click(function() {
		$(".modal").modal("hide");
	})

	$(".confirm-delete").click(function() {
		$.ajax({
			type : "POST",
			url : "/rollflower/cabinet/ac_delete",
			data : {
				"confirmPassword" : $(".delete-password").val(),
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
	})
})