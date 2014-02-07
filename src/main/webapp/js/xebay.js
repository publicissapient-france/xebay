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
    var cookie = $.cookie("xebay");
    this.socket = new WebSocket("ws://" + window.location.host + "/socket/bidEngine/" + cookie.key);
    this.socket.onmessage = this.readBidAnswer;
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
  "sendBidCall": function (increment) {
    this.socket.send(JSON.stringify({
      itemName: xebay.bidOfferInfo["itemName"],
      curValue: xebay.bidOfferInfo["currentValue"],
      increment: increment
    }));
  },
  "readBidAnswer": function (message) {
    var bidAnswer = JSON.parse(message.data);
    if (bidAnswer.type === "ACCEPTED") {
      xebay.bidOfferInfo.currentValue = bidAnswer.value;
      xebay.bidOfferInfo.futureBuyerEmail = bidAnswer.futureBuyerEmail;
      xebay.bidOfferInfo.timeToLive = bidAnswer.timeToLive;
    }
    $("#bidAnswerLog").prepend(xebay.bidAnswerTemplate[bidAnswer.type](bidAnswer));
    xebay.clearLogs(3);
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
