
var xebay = {
  "bidOffer": {},
  "init": function () {
    $(".registered").hide();
    $("#register-message-display").hide();
    $.cookie.json = true;
    this.getCurrentBidOffer();
    var cookie = $.cookie("xebay");
    if (cookie) {
      this.signinWith(cookie.key);
    }
  },
  "signin": function () {
    this.signinWith($("#key").val());
  },
  "signinWith": function (key) {
    $.ajax("/rest/users/info", {
      "headers": {"Authorization": key},
      "success": function (data) {
        xebay.signedin(data, key);
      },
      "error": function () {
        $("#register-message-display").text("Please provide a valid API key").show();
      }
    });
  },
  "signedin": function (user, key) {
    $("#register-message-display").text("").hide();
    $("#name-display").text(user.name);
    $("#key-display").text(key);
    $(".registered").show();
    $(".unregistered").hide();
    $("#user-display").html(xebay.userTemplate(user));
    $.cookie("xebay", {"key": key});
    this.connect();
  },
  "signout": function () {
    $("#key-display").text("");
    $(".unregistered").show();
    $(".registered").hide();
    $("#user-display").text("");
    $.removeCookie("xebay");
  },
  "register": function() {
    var name = $("#name").val()
    $.ajax("/rest/users/register", {
      "headers": {"Authorization": $.cookie("xebay").key},
      "data": {"name": $("#name").val() },
      "success": function (key) {
        $("#users").append(xebay.adminUserTemplate({name: name, key: key}));
      },
      "error": function (jqxhr) {
        console.warn("User " + name + " was not registered because " + jqxhr.responseText + ".");
      }
    });
  },
  "unregister": function(key) {
    var name = $("#name").val()
    $.ajax("/rest/users/unregister?key=" + key, {
      "type":"DELETE",
      "headers": {"Authorization": $.cookie("xebay").key},
      "success": function () {
        $("#users").find("tr[id=" + key + "]").remove();
      },
      "error": function (jqxhr) {
        console.warn("User " + key + " was not unregistered because " + jqxhr.responseText + ".");
      }
    });
  },
  "getCurrentBidOffer": function () {
    $.ajax("/rest/bidEngine/current", {
      "dataType": "json",
      "success": function (currentBidOffer) {
        xebay.updateCurrentBidOffer(currentBidOffer);
      },
      "error": function (jqxhr) {
        xebay.updateCurrentBidOffer({});
        setTimeout(xebay.getCurrentBidOffer, 5000);
      }
    });
  },
  "updateCurrentBidOffer": function (currentBidOffer) {
    xebay.bidOffer = currentBidOffer;
    $("#current-bid-offer").html(xebay.currentBidOfferTemplate(xebay.bidOffer));
    if (xebay.bidOffer.timeToLive && !xebay.timeout) {
      xebay.updateTimeToLive();
    }
  },
  "updateTimeToLive": function () {
    var timeToLive = xebay.bidOffer.timeToLive;
    if (timeToLive < 0) {
      xebay.timeout = null;
      xebay.getCurrentBidOffer();
      return;
    }
    var timeToLiveSeconds = twoDigits(Math.floor(timeToLive / 1000));
    var timeToLiveMilliseconds = Math.floor((timeToLive - (timeToLiveSeconds * 1000)) / 100);
    $("#timeToLiveSeconds").html(timeToLiveSeconds);
    $("#timeToLiveMilliseconds").html(timeToLiveMilliseconds);
    xebay.timeout = setTimeout(xebay.updateTimeToLive, 100);
    xebay.bidOffer.timeToLive -= 100;
  },
  "sendBidDemand": function (increment) {
    if (!increment) {
      increment = $("#increment").val();
    }
    var bidDemand = {
      "itemName": xebay.bidOffer.item.name,
      "value": xebay.bidOffer.item.value + increment
    };
    $.ajax("/rest/bidEngine/bid", {
      "type": "POST",
      "headers": { "Authorization": $.cookie("xebay").key },
      "contentType": "application/json",
      "data": JSON.stringify(bidDemand)
    }).done(function () {
      $("#console").prepend(xebay.bidSuccessTemplate(bidDemand));
    }).fail(function (jqxhr) {
      $("#console").prepend(xebay.bidFailTemplate({ cause: jqxhr.responseText }));
    });
  },
  "offer": function (name, value, buttonId) {
    $.ajax("/rest/bidEngine/offer", {
      "type": "POST",
      "headers": { "Authorization": $.cookie("xebay").key },
      "contentType": "application/json",
      "data": JSON.stringify({"itemName": name, "value": value}),
      "success": function () {
        $("#" + buttonId).text("");
      }
    });
  },
  "connect": function () {
    this.socket = new WebSocket("ws://" + window.location.host + "/socket/bidEngine");
    this.socket.onmessage = this.onBidOffer;
    this.socket.onopen = this.connected;
    this.socket.onclose = this.disconnected;
    this.socket.onerror = this.disconnected;
  },
  "connected": function () {
    $(".connected").show();
    $(".disconnected").hide();
  },
  "disconnect": function () {
    this.socket.close();
    this.clearLogs();
  },
  "disconnected": function () {
    $(".connected").hide();
    $(".disconnected").show();
  },
  "onBidOffer": function (message) {
    var currentBidOffer = JSON.parse(message.data);
    xebay.updateCurrentBidOffer(currentBidOffer);
    $("#console").prepend(xebay.bidOfferTemplate(currentBidOffer));
  },
  "clearLogs": function () {
    $("#console").find("p").remove();
  }
};

function twoDigits(value) {
  if (value < 10) {
    return "0" + value;
  }
  return value;
}
