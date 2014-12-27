function getURLParameter(name) {
	return decodeURIComponent((new RegExp('[?|&]' + name + '='
			+ '([^&;]+?)(&|#|;|$)').exec(location.search) || [ , "" ])[1]
			.replace(/\+/g, '%20'))
			|| null;
}


function apply() {
	var data = ko.toJSON(buildJsonRequest());
	$.ajax({
		type : 'POST',
		url : 'services/applications',
		dataType : "json",
		data : data,
		global : false,
		contentType : "application/json",
		success : function(positions) {
			window.location = "success.html";
		},
		error : function(xhr) {
			if (xhr.status == 400) {
				$(".error").text(xhr.responseText);
				return;
			}
			$(".error").text("Ooops! Something went wrong");
		},
	});
}

function buildJsonRequest() {
	var jsonRequest = {};
	var personalInformation = buildPersonalInformation();
	jsonRequest["personalInformation"] = personalInformation;
	jsonRequest["positionId"] = getURLParameter("position");
	jsonRequest["technicalSkills"] = $("#technicalSkills").val();
	jsonRequest["personalSkills"] = $("#socialSkills").val();
	jsonRequest["education"] = buildEducation();
	jsonRequest["employment"] = buildEmployment();
	return jsonRequest;
}

function buildPersonalInformation() {
	var personalInformation = {};
	personalInformation["firstName"] = $("#firstName").val();
	personalInformation["lastName"] = $("#lastName").val();
	personalInformation["gender"] = $('[name="gender"]').val();
	personalInformation["dateOfBirth"] = $("#birthDate").val();
	personalInformation["email"] = $("#email").val();
	personalInformation["address"] = buildAddress();
	return personalInformation;
}

function buildAddress() {
	var address = {};
	address["country"] = $("#country").val();
	address["city"] = $("#city").val();
	address["addressLine"] = $("#addressLine").val();
	address["phoneNumber"] = $("#phone").val();
	return address;
}

function buildEducation() {

	var educations = new Array();

	for ( var i = 1; i <= 5; i++) {
		var education = {};
		var duration = {};
		if ($("#education" + i).is(':visible')) {
			duration["startYear"] = $("#educationStartDate" + i).val();
			duration["endYear"] = $("#educationEndDate" + i).val();
			education["duration"] = duration;
			education["city"] = $("#educationCity" + i).val();
			education["country"] = $("#educationCountry" + i).val();
			education["schoolType"] = $("#schoolType" + i).val();
			education["schoolName"] = $("#schoolName" + i).val();
			education["degree"] = $("#degree" + i).val();
			educations[i - 1] = education;
		}
	}

	return educations;
}

function buildEmployment() {

	var employments = new Array();

	for ( var i = 1; i <= 7; i++) {
		var employment = {};
		var duration = {};
		if ($("#employment" + i).is(':visible')) {
			duration["startYear"] = $("#employmentStartDate" + i).val();
			duration["endYear"] = $("#employmentEndDate" + i).val();
			employment["duration"] = duration;
			employment["city"] = $("#employmentCity" + i).val();
			employment["country"] = $("#employmentCountry" + i).val();
			employment["position"] = $("#position" + i).val();
			employment["companyName"] = $("#companyName" + i).val();
			employment["description"] = $("#description" + i).val();
			employments[i - 1] = employment;
		}
	}

	return employments;
}

function hide(index, currentDiv, nextDiv) {
	if ($("#" + nextDiv).is(':visible')) {
		return;
	}
	$("#" + currentDiv).hide();
	$("#" + index).show();
	
}

function show(index, nextDiv, currentDiv) {
	validateDiv(index);
	$("#" + nextDiv).show();
	$("#" + index).hide();
}

function validateDiv(index){
	
}

var SimpleListModel = function(items) {
    this.items = ko.observableArray(items);
    this.itemToAdd = ko.observable("");
    this.addItem = function() {
        if (this.itemToAdd() != "") {
            this.items.push(this.itemToAdd());
            this.itemToAdd(""); 
        }
    }.bind(this);
};
 
ko.applyBindings(new SimpleListModel(["United States", "United Kingdom", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda",
                                      "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",
                                      "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",
                                      "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, The Democratic Republic of The", "Cook Islands", "Costa Rica", "Cote D'ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
                                      "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam",
                                      "Guatemala", "Guinea", "Guinea-bissau", "Guyana", "Haiti", "Heard Island and Mcdonald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran, Islamic Republic of", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
                                      "Kazakhstan", "Kenya", "Kiribati", "Korea", "Kuwait", "Kyrgyzstan", "Lao People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali",
                                      "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand",
                                      "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory, Occupied", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda"
                                      , "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon", "Saint Vincent and The Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia and Montenegro", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                                      "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands",
                                      "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "Uzbekistan", "Uruguay", "Vanuatu", "Venezuela", "Viet Nam", "Virgin Islands", "Wallis and Futuna", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"]));