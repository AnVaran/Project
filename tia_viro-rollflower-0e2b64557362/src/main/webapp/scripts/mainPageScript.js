$('document').ready(function() {
	var flowers = [];
	var bouquets = [];
	var order = {};
	var flowerPrice= 0;
	
	$('.flowerSelect').change(function() {
		price = $(".flowerSelect option:selected").attr("data-flower-price")
		$('.flowerQuantity').val(1);
		$('.flowerPriceLabel').text('Flower price: ' + price);
		$('.flowerPriceLabel').attr("data-current-price", price)
		$('.flowerCompose').attr("hidden", false);
	});
	
	$('.decorationSelect').change(function() {
		if ($(".decorationSelect option:selected").attr("value") != 0){
			var price = $(".decorationSelect option:selected").attr("data-decoration-price")
			$('.decorationPriceLabel').text('Decoration price: ' + price);
			$('.decorationPriceLabel').attr("hidden", false);
		} else {
			$('.decorationPriceLabel').attr("hidden", true);
		}
	});
	
	$('.wrapperSelect').change(function() {
		if ($(".wrapperSelect option:selected").attr("value") != 0){
			var price = $(".wrapperSelect option:selected").attr("data-wrapper-price")
			$('.wrapperPriceLabel').text('Wrapper price: ' + price);
			$('.wrapperPriceLabel').attr("hidden", false);
		} else {
			$('.wrapperPriceLabel').attr("hidden", true);
		}
	});
	
	$('.flowerQuantity').change(function(){
		var quantity = $('.flowerQuantity').val();
		$('.flowerPriceLabel').text('Flower price: ' + (price*quantity));
	});
	
	$('.addFlowerTOBouquetButton').click(function(){
		var flowerJSON = {"flowerId" : $(".flowerSelect option:selected").val(), "flowerQuantity" : $('.flowerQuantity').val()};
		flowers.push(flowerJSON);
		$('.addBouquetButton').attr("disabled", false);
	});
	
	$('.addBouquetButton').click(function(){
		var bouquetJSON = {"wrapperId" : $(".wrapperSelect option:selected").attr("value"), "decorationId" : $(".decorationSelect option:selected").attr("value"), "flowers" : flowers};
		bouquets.push(bouquetJSON);
		flowers = [];
		var orderCount = Number($('.order-badge').text());
		$('.order-badge').text(orderCount+1);
		$('.order-badge').attr("hidden", false);
		$('.alert-success').attr("hidden", false);
		$('.flowerCompose').attr("hidden", true);
		$('.addBouquetButton').attr("disabled", true);
	});
	
	$('.orderMenuButton').click(function(){
		order = {"bouquets" : bouquets};
		var orderJSON = JSON.stringify(order);
		order = [];
		if(bouquets.length > 0){
			var form = document.createElement("FORM");
			form.method = "POST";
			form.style.display = "none";
			form.action = "/rollflower/order"
			document.body.appendChild(form);
			var input = document.createElement("INPUT");
			input.type = "hidden";
			input.name = "order";
			input.value = orderJSON;
			form.appendChild(input);
			form.submit();
		} else {
			window.location.href = "http://localhost:8080/rollflower/order";
		}
	});
	
	$('.homeMenuButton').click(function(){
		window.location.href = "http://localhost:8080/rollflower";
	});
})