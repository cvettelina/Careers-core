function ViewModel() {
	var self = this;
	self.applications = ko.observableArray();
	self.positions = ko.observableArray();
	self.username = ko.observable();
	self.active = ko.observable();
	self.userId = ko.observable();
};


(function getPositions() {

	$.ajax({
		type : 'GET',
		url : 'services/positions',
		dataType : "json",
		global : false,
		success : function(positions) {
			viewModel.positions(positions);
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
			setTimeout(getPositions, 150000);
		},
	});
})();

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
	
	$.ajax({
		type : 'GET',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : 'services/applications?status=NOT_CHECKED',
		dataType : "json",
		global : false,
		success : function(applications) {
			viewModel.applications(applications);
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

var viewModel = new ViewModel();
ko.applyBindings(viewModel);

function filterApplications() {

	var filterGender = $("#gender").val();
	var filterPosition = $("#position").val();
	var filterStatus = $("#status").val();
	var filterQualification = $("#qualification").val();
	var filterExperinece = $("#experience").val();

	var url = 'services/applications?a=1';

	if (filterGender != null && filterGender != "") {
		url = url.concat("&gender=" + gender);
	}
	if (filterPosition != null && filterPosition != "") {
		url = url.concat("&position=" + filterPosition);
	}
	if (filterStatus != null && filterStatus != "") {
		url = url.concat("&status=" + filterStatus);
	}
	if (filterQualification != null && filterQualification != "") {
		url = url.concat("&degree=" + filterQualification);
	}

	if (filterExperinece != null && filterExperinece != "") {
		url = url.concat("&period=" + filterExperinece);
	}

	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];
	$.ajax({
		type : 'GET',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		global : false,
		success : function(applications) {
			var bla = ko.toJSON(viewModel);
			viewModel.applications.removeAll();
			bla = ko.toJSON(viewModel);
			viewModel.applications(applications);
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

function changeStatus(status, button) {
	var data = {};
	data["status"] = status;
	data["userId"] = 1;
	var jsonData = ko.toJSON(data);
	var url = 'services/applications/'.concat(button.value).concat('/status');
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];
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
		success : function() {
			window.location.reload();
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