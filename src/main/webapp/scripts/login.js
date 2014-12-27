function login() {
	var credentials = {};
	credentials["username"] = $("#username").val();
	credentials["password"] = $("#password").val();
	
	var data = ko.toJSON(credentials);
	$.ajax({
		type : 'POST',
		url : 'services/authenticate',
		dataType : "json",
		data : data,
		global : false,
		contentType : "application/json",
		success : function(response) {
			createCookie("SESSION", response.token + ":" + response.userId, 1);
			window.location = "users.html";
		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
				return;
			}
			if (xhr.status == 400) {
				$(".error").text("Wrong username or password!");
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
	});
}

function changePassword() {
	var credentials = {};
	credentials["oldPassword"] = $("#old-password").val();
	credentials["newPassword"] = $("#password").val();
	
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var url = "services/users".concat('/' + auth[1]);
	var authorizationToken = "Basic " + auth[0];
	
	var data = ko.toJSON(credentials);
	$.ajax({
		type : 'POST',
		url : url,
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		dataType : "json",
		data : data,
		global : false,
		contentType : "application/json",
		success : function() {
			window.location = "users.html";
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

function createCookie(name,value,days) {
	var expires;
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		expires = "; expires="+date.toGMTString();
	}
	else expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
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
