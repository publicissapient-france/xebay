var xebay = {
  "bidOfferInfo": {},
  "init": function () {
    $(".registered").hide();
    $.cookie.json = true;
    this.displayBidOffer();
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
  "displayBidOffer": function () {
    $.getJSON("/rest/bidEngine/current",function (bidOfferInfo) {
      xebay.bidOfferInfo = bidOfferInfo;
      xebay.updateTimeToLive();
    }).fail(function () {
      xebay.bidOfferInfo = {};
      setTimeout(xebay.displayBidOffer, 1000);
      $("#current-bid-offer").html(xebay.currentBidOfferTemplate(xebay.bidOfferInfo));
    });
  },
  "updateTimeToLive": function () {
    var timeToLive = Number(xebay.bidOfferInfo["timeToLive"]);
    if (timeToLive < 0) {
      xebay.displayBidOffer();
      return;
    }
    xebay.bidOfferInfo["timeToLiveSeconds"] = twoDigits(Math.floor(timeToLive / 1000));
    xebay.bidOfferInfo["timeToLiveMilliseconds"] = Math.floor((timeToLive - (xebay.bidOfferInfo["timeToLiveSeconds"] * 1000)) / 100);
    $("#current-bid-offer").html(xebay.currentBidOfferTemplate(xebay.bidOfferInfo));
    setTimeout(xebay.updateTimeToLive, 100);
    xebay.bidOfferInfo["timeToLive"] -= 100;
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
  "sendBidDemand": function (increment) {
    $.ajax("/rest/bidEngine/bid", {
      headers: {
        "Authorization": $.cookie("xebay").key
      },
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        itemName: xebay.bidOfferInfo["itemName"],
        curValue: xebay.bidOfferInfo["currentValue"],
        increment: increment
      }),
      success: function () {
        // TODO
      }
    })
  },
  "onBidOffer": function (message) {
    var bidOfferNotification = JSON.parse(message.data);
    xebay.bidOfferInfo = bidOfferNotification; // TODO updateCurrentBidOffer
    $("#bidAnswerLog").prepend(xebay.bidOfferNotificationTemplate(bidOfferNotification));
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
