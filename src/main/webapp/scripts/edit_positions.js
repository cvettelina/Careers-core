function ViewModel() {
	var self = this;
	self.positions = ko.observableArray();
	self.username = ko.observable();
	self.active = ko.observable();
	self.userId = ko.observable();
};

var viewModel = new ViewModel();
ko.applyBindings(viewModel);

(function getPositions() {

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
		complete : function() {
		},
	});

	$.ajax({
		type : 'GET',
		url : 'services/positions',
		dataType : "json",
		global : false,
		success : function(positions) {
			viewModel.positions(positions);
		},
		error : function(xhr) {
			if (xhr.status == 400) {
				$(".error").text(xhr.responseText);
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
	});
})();

(function($) {
	$.fn.changeElementType = function(newType) {
		var attrs = {};

		$.each(this[0].attributes, function(idx, attr) {
			attrs[attr.nodeName] = attr.nodeValue;
		});

		this.replaceWith(function() {
			return $("<" + newType + "/>", attrs).append($(this).contents());
		});
	}
})(jQuery);

function enableEdit(index) {
	var titleId = 'title' + index;
	var descId = 'desc' + index;
	var saveBut = 'save' + index;
	var editBut = 'edit' + index;

	$("#" + titleId).attr('readonly', false);
	$("#" + descId).attr('readonly', false);
	$("#" + saveBut).show();
	$("#" + editBut).hide();

}

function save(index, edit) {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];

	var url = 'services/positions';

	if (edit) {
		url = url.concat('/' + index);
	}
	var title = $('#title' + index).val();
	var desc = $('#desc' + index).val();

	var data = {};
	data["title"] = title;
	data["description"] = desc;
	var jsonData = ko.toJSON(data);

	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		data : jsonData,
		global : false,
		contentType : "application/json",
		success : function(positions) {
			location.reload(true);
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
	var saveBut = 'save' + index;
	var editBut = 'edit' + index;

	$("#" + saveBut).hide();
	$("#" + editBut).show();
}

function del(index) {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];

	var url = 'services/positions/' + index + '/delete';

	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		global : false,
		contentType : "application/json",
		success : function() {
			location.reload(true);
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
}

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
