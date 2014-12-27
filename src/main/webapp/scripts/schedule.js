function ViewModel() {
	var self = this;
	self.applications = ko.observableArray();
	self.positions = ko.observableArray();
	self.username = ko.observable();
	self.active = ko.observable();
	self.userId = ko.observable();
};

var viewModel = new ViewModel();
ko.applyBindings(viewModel);

(function getApplications() {

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
		url : 'services/applications?status=APPROVED',
		dataType : "json",
		global : false,
		success : function(applications) {
			viewModel.applications(applications);
		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
			}
		},
		complete : function() {
			$('div.external-event').each(function() {
				// create an Event Object
				var eventObject = {
					title : $.trim($(this).text()),
					id : $(this).attr('id'),
				};

				// store the Event Object in the DOM element so we can get to it
				// later
				$(this).data('eventObject', eventObject);

				// make the event draggable using jQuery UI
				$(this).draggable({
					zIndex : 999,
					revert : true, // will cause the event to go back to its
					revertDuration : 0
				// original position after the drag
				});

			});
		},
	});
})();

function createEvent(object) {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];

	var interview = {};
	interview["userId"] = auth[1];
	interview["personId"] = object.id;
	interview["startDate"] = object.start;
	interview["endDate"] = object.end;

	var jsonObject = ko.toJSON(interview);

	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : 'services/interviews',
		dataType : "json",
		contentType : "application/json",
		data : jsonObject,
		global : false,
		success : function() {

		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
			}
		},
	});
}

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
		error : function() {
		},
		complete : function() {
		},
	});
})();

function loadInterviews(start, end, callback) {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];
	var url = 'services/interviews?userId=' + auth[1];
	$
			.ajax({
				type : 'GET',
				beforeSend : function(request) {
					request.setRequestHeader("Authorization",
							authorizationToken);
				},
				url : url,
				dataType : "json",
				global : false,
				success : function(interviews) {
					var events = [];
					for ( var i = 0; i < interviews.length; i++) {
						events
								.push({
									title : "[ "
											+ interviews[i].application.id
											+ " ] "
											+ interviews[i].application.personalInformation.firstName
											+ " "
											+ interviews[i].application.personalInformation.lastName,
									start : interviews[i].startDate,
									end : interviews[i].endDate,
									id: interviews[i].id,
									allDay: false,
									description : interviews[i].application.personalInformation.firstName
								});
					}
					callback(events);
				},
				error : function(xhr) {
					if (xhr.status == 401) {
						window.location = "login.html";
					}
				},
				complete : function() {
				},
			});

}

function editEvent(startDate, endDate, id) {
	var cookie = readCookie("SESSION");
	if (cookie == null || cookie == "") {
		window.location = "login.html";
	}
	var auth = cookie.split(":");
	var authorizationToken = "Basic " + auth[0];

	var interviewDuration = {};
	interviewDuration["startDate"] = startDate;
	interviewDuration["endDate"] = endDate;

	var jsonObject = ko.toJSON(interviewDuration);
	var url = 'services/interviews/' + id;
	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader("Authorization", authorizationToken);
		},
		url : url,
		dataType : "json",
		contentType : "application/json",
		data : jsonObject,
		global : false,
		success : function() {

		},
		error : function(xhr) {
			if (xhr.status == 401) {
				window.location = "login.html";
			}
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
		error : function() {
		},
		complete : function() {
		},
	});
}