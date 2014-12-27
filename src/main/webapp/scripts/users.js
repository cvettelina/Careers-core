function getURLParameter(name) {
	return decodeURIComponent((new RegExp('[?|&]' + name + '='
			+ '([^&;]+?)(&|#|;|$)').exec(location.search) || [ , "" ])[1]
			.replace(/\+/g, '%20'))
			|| null;
}

function ViewModel() {
	var self = this;
	self.username = ko.observable();
	self.active = ko.observable();
	self.userId = ko.observable();
};

var viewModel = new ViewModel();
ko.applyBindings(viewModel);

(function getUser() {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var url = "services/users".concat('/' + auth[1]);
	var authorizationToken = "Basic " + auth[0];
	$.ajax({
		type : 'GET',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		global : false,
		success : function(user) {
			viewModel.username(user.username);
			viewModel.active(user.active);
			viewModel.userId(user.id);
		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
				return;
			}
			if (xhr.status == 400) {
				$(".error").text(xhr.responseText);
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
	});
})();

function logout() {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var url = "services/authenticate".concat('/' + auth[1]).concat("/logout");
	var authorizationToken = "Basic " + auth[0];
	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		global : false,
		success : function() {
			window.location = "login.html";
		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
				return;
			}
			if (xhr.status == 400) {
				$(".error").text(xhr.responseText);
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
		complete : function() {

		},
	});
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for ( var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0)
			return c.substring(nameEQ.length, c.length);
	}
	return null;
}