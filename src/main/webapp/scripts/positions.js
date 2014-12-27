function ViewModel() {
	var self = this;
	self.data = ko.observableArray();
};

var viewModel = new ViewModel();
ko.applyBindings(viewModel);

(function getPositions() {

	$.ajax({
		type : 'GET',
		url : 'services/positions',
		dataType : "json",
		global : false,
		success : function(positions) {
			viewModel.data(positions);
		},
		error : function(xhr) {
			if (xhr.status == 400) {
				$(".error").text(xhr.responseText);
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
		complete : function() {
			setTimeout(getPositions, 50000);
		},
	});
})();
