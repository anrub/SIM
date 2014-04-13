$(function() {
	"use strict";

	var header = $('#header');
	var content = $('#content');
	var input = $('#input');
	var status = $('#status');
	var author = null;
	var logged = false;
	var socket = atmosphere;
	var subSocket;
	var transport = 'websocket';

	// We are now ready to cut the request
	var request = {
		url : document.location.toString() + 'chat',
		contentType : "application/json",
		logLevel : 'debug',
		transport : transport,
		trackMessageLength : true,
		reconnectInterval : 5000,
		enableXDR : true,
		timeout : 60000
	};

	request.onOpen = function(response) {
		content.html($('<p>', {
			text : 'Atmosphere connected using ' + response.transport
		}));
		input.removeAttr('disabled').focus();
		status.text('Input');
		transport = response.transport;

		// Carry the UUID. This is required if you want to call
		// subscribe(request) again.
		request.uuid = response.request.uuid;

		getUserlist();
	};

	request.onClientTimeout = function(r) {
		content
				.html($(
						'<p>',
						{
							text : 'Client closed the connection after a timeout. Reconnecting in '
									+ request.reconnectInterval
						}));
		// input.attr('disabled', 'disabled');
		setTimeout(function() {
			subSocket = socket.subscribe(request);
		}, request.reconnectInterval);
	};

	request.onReopen = function(response) {
		input.removeAttr('disabled').focus();
		content.html($('<p>', {
			text : 'Atmosphere re-connected using ' + response.transport
		}));
	};

	// For demonstration of how you can customize the fallbackTransport using
	// the onTransportFailure function
	request.onTransportFailure = function(errorMsg, request) {
		atmosphere.util.info(errorMsg);
		request.fallbackTransport = "long-polling";
		header
				.html($(
						'<h3>',
						{
							text : 'Atmosphere Chat. Default transport is WebSocket, fallback is '
									+ request.fallbackTransport
						}));
	};

	request.onMessage = function(response) {

		var message = response.responseBody;
		try {
			var json = atmosphere.util.parseJSON(message);
		} catch (e) {
			console.log('This doesn\'t look like a valid JSON: ', message);
			return;
		}

		if (json.type == "GetUserlistResponse") {
			var userlist = "";

			$
					.each(
							json.users,
							function(index, value) {
								var user = "";
								var checked = false;

								$
										.each(
												$(".userCheckbox"),
												function(index, val) {
													if (val.checked
															&& val.value == value.name) {
														checked = true;
													}
												});

								if (checked) {
									user += "<input class=\"userCheckbox\" type=\"checkbox\" name=\"user\" value=\""
											+ value.name
											+ "\" checked=\"checked\" />";
									user += value.name;
									user += "<br />";
								} else {
									user += "<input class=\"userCheckbox\" type=\"checkbox\" name=\"user\" value=\""
											+ value.name + "\" />";
									user += value.name;
									user += "<br />";
								}

								userlist += user;
							});
			$("#userlist").html(userlist);

		} else if (json.type == "NewMessage" || json.type == "SendMessage") {
			input.removeAttr('disabled').focus();
			addMessage(json, 'blue', new Date());
		}else if ( json.type == "GetUserlist") {
			
		}else if (json.type =="FileSendRequest") {
			var Check = confirm("Möchten Sie die Datei annehmen?");
			if (Check == true) {
				sendFileAccept(json);
			}else {
				sendFileReject(json);
			}
		
		} else {
			addMessageDebug(json.type + ": " + json.text);
		}
	};

	request.onClose = function(response) {
		content.html($('<p>', {
			text : 'Server closed the connection after a timeout'
		}));
		// input.attr('disabled', 'disabled');
	};

	request.onError = function(response) {
		content.html($('<p>', {
			text : 'Sorry, but there\'s some problem with your '
					+ 'socket or the server is down'
		}));
		logged = false;
	};

	request.onReconnect = function(request, response) {
		content.html($('<p>', {
			text : 'Connection lost, trying to reconnect. Trying to reconnect '
					+ request.reconnectInterval
		}));
		// input.attr('disabled', 'disabled');
	};

	subSocket = socket.subscribe(request);

	input.keydown(function(e) {
		if (e.keyCode === 13) {
			var msg = $(this).val();

			subSocket.push(msg);
			$(this).val('');
		}
	});
	$("#clearButton").click(function(e) {
		content.html('');
	});
	$("#getUserlistButton").click(function(e) {
		getUserlist();
	});
	$("#sendMessageButton").click(function(e) {
		sendMessageToStream();
		$("#input").val('');
	});
	$("#sendMessageRawButton").click(function(e) {
		subSocket.push($(input).val());
		$("#input").val('');
	});
	$("#getContentButton").click(function(e) {
		getContent($(input).val());
	});

	$("#busyButton").click(function(e) {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "UserUpdateNotice",
			"update" : "BUSY"
		});
		subSocket.push(msg);
	});

	$("#sendMessageSelectedButton").click(function(e) {
		$.each($(".userCheckbox"), function(index, val) {
			if (val.checked) {
				sendMessage($(val).val());
			}

		});
		$("#input").val('');
	});

	function addMessage(message, color, datetime) {
		content
				.append('<p><span style="color:'
						+ color
						+ '">'
						+ message.sender
						+ " &gt; "
						+ message.receiver
						+ '</span> @ '
						+ +(datetime.getHours() < 10 ? '0'
								+ datetime.getHours() : datetime.getHours())
						+ ':'
						+ (datetime.getMinutes() < 10 ? '0'
								+ datetime.getMinutes() : datetime.getMinutes())
						+ ': ' + message.text + '</p>');
		scrollDown('conversations');
	}

	function scrollDown(id) {
		var elem = document.getElementById(id);
		elem.scrollTop = elem.scrollHeight;
	}

	function addMessageDebug(message) {
		$("#debug").append('<p>' + message + '</p>');
		scrollDown('debug');
	}

	function getUserlist() {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "GetUserlist"
		});
		subSocket.push(msg);
	}

	function sendMessageToStream() {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "SendMessage",
			"receiver" : "Stream",
			"text" : $(input).val()
		});
		subSocket.push(msg);
	}

	function sendMessage(toUser) {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "SendMessage",
			"receiver" : toUser,
			"text" : $(input).val()
		});
		subSocket.push(msg);
	}

	function getContent(id) {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "GetContent",
			"id" : id
		});
		subSocket.push(msg);
	}
	
	function sendFileAccept(json) {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "FileSendAccept",
			"receiver" : json.sender,
			"id" : json.id
		});
		subSocket.push(msg);
	}
	
	function sendFileReject(json) {
		var msg = atmosphere.util.stringifyJSON({
			"type" : "FileSendReject",
			"receiver" : json.sender,
			"id" : json.id
		});
		subSocket.push(msg);
	}

	window.setInterval(function() {
		getUserlist();
	}, 50000);
});