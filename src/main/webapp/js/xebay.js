var xebay = {
  "bidOfferInfo": {},
  "timeout": null,
  "init": function () {
    $(".registered").hide();
    $.cookie.json = true;
    this.getCurrentBidOffer();
    var cookie = $.cookie("xebay");
    if (cookie) {
      this.signinWith(cookie.email, cookie.key);
    }
  },
  "signup": function () {
    this.signupWith($("#email").val());
  },
  "signupWith": function (email) {
    $.get("/rest/users/register", {"email": email}, function (key) {
      xebay.signedin(email, key);
    });
  },
  "signin": function () {
    this.signinWith($("#email").val(), $("#key").val());
  },
  "signinWith": function (email, key) {
    $.ajax("/rest/users/info", {
      "headers": {"Authorization": key},
      "data": {"email": email},
      "success": function () {
        xebay.signedin(email, key);
      }
    });
  },
  "signout": function () {
    var cookie = $.cookie("xebay");
    this.signoutWith(cookie.key);
  },
  "signoutWith": function (key) {
    $.ajax("/rest/users/unregister", {
      "headers": {"Authorization": key},
      "type": "DELETE",
      "success": function () {
        xebay.signedout();
      }
    });
  },
  "signedin": function (email, key) {
    $("#email-display").text(email);
    $("#key-display").text(key);
    $(".registered").show();
    $(".unregistered").hide();
    $.cookie("xebay", {"email": email, "key": key});
    this.connect();
  },
  "signedout": function () {
    $("#key-display").text("");
    $(".unregistered").show();
    $(".registered").hide();
    $.removeCookie("xebay");
  },
  "getCurrentBidOffer": function () {
    $.getJSON("/rest/bidEngine/current").done(function(currentBidOffer) {
      xebay.updateCurrentBidOffer(currentBidOffer);
    }).fail(function (jqxhr) {
      xebay.updateCurrentBidOffer({});
      setTimeout(xebay.getCurrentBidOffer, 5000);
    });
  },
  "updateCurrentBidOffer": function (currentBidOffer) {
    xebay.bidOfferInfo = currentBidOffer;
    $("#current-bid-offer").html(xebay.currentBidOfferTemplate(xebay.bidOfferInfo));
    if (xebay.bidOfferInfo.timeToLive && !xebay.timeout) {
      xebay.updateTimeToLive();
    }
  },
  "updateTimeToLive": function () {
    var timeToLive = xebay.bidOfferInfo.timeToLive;
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
    xebay.bidOfferInfo.timeToLive -= 100;
  },
  "sendBidDemand": function (increment) {
    var bidDemand = {
      itemName: xebay.bidOfferInfo["itemName"],
      curValue: xebay.bidOfferInfo["currentValue"],
      increment: increment
    };
    $.ajax("/rest/bidEngine/bid", {
      type: "POST",
      headers: { "Authorization": $.cookie("xebay").key },
      contentType : "application/json",
      data: JSON.stringify(bidDemand)
    }).done(function () {
        $("#bidAnswerLog").prepend(xebay.bidSuccessTemplate(bidDemand));
    }).fail(function(jqxhr) {
        $("#bidAnswerLog").prepend(xebay.bidFailTemplate({ cause: jqxhr.responseText }));
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
    $("#bidAnswerLog").prepend(xebay.bidOfferTemplate(currentBidOffer));
    xebay.clearLogs(5);
  },
  "clearLogs": function (count) {
    if (!count) {
      count = 0;
    }
    $("#bidAnswerLog p").slice(count).remove();
  }
};

function twoDigits(value) {
  if (value < 10) {
    return "0" + value;
  }
  return value;
}
