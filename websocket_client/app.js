var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS("http://localhost:8080/websocket-endpoint");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/alerts", function (alert) {
            showGreeting(JSON.parse(alert.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showGreeting(message) {
    $("#alerts_table").append("<tr><td>Pair: " + message.currencyPair + " above " + message.limit + " on " + message.time + "</td></tr>");
}

function submitAlert() {
    var pair = $("#add_pair").val();
    var limit = $("#add_limit").val();

    $.ajax({
      url: "http://localhost:8080/alert?pair=" + pair + "&limit=" + limit,
      type: "PUT",
      beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      },
      success: function(data) {
        alert("Alert added.");
      }
    });
}

function deleteAlert() {
    var pair = $("#add_pair").val();
    var limit = $("#add_limit").val();

    $.ajax({
      url: "http://localhost:8080/alert?pair=" + pair + "&limit=" + limit,
      type: "DELETE",
      beforeSend: function (xhr) {
          xhr.setRequestHeader("Authorization", "Basic " + btoa("ubs:ubs_passwd"));
          xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      },
      success: function(data) {
        alert("Alert removed.");
      }
    });
}

$(function () {
    $("form").on("submit", function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#submit_alert" ).click(function() { submitAlert(); } );
    $( "#delete_alert" ).click(function() { deleteAlert(); } );
});