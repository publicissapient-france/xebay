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
        "type":"DELETE",
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
    this.connect(key);
  },
  "signedout": function () {
    $("#key-display").text("");
    $(".unregistered").show();
    $(".registered").hide();
    $.removeCookie("xebay");
  },
  "displayBidOffer": function () {
    $.getJSON("/rest/bidEngine",function (bidOfferInfo) {
      xebay.bidOfferInfo = bidOfferInfo;
      $("#current-bid-offer").html("" +
          "<p>" + bidOfferInfo["itemName"] + "</p>" +
          "<p>current value: " + bidOfferInfo["currentValue"] + " bid points</p>");
      setTimeout(xebay.displayBidOffer, bidOfferInfo["timeToLive"]);
    }).fail(function () {
          xebay.bidOfferInfo = {};
          $("#current-bid-offer").html("" +
              "<p>There is no bid offer.</p>");
    });
  },
  "connect": function (key) {
      this.socket = new WebSocket("ws://" + window.location.host + "/socket/bidEngine/" + key);
      this.socket.onmessage = this.listenBidOffer;
      this.socket.onopen = this.connected;
      this.socket.onclose = this.disconnected;
      this.socket.onerror = this.disconnected;
  },
  "connected" : function() {
    $(".connected").show();
    $(".disconnected").hide();
  },
  "disconnected" : function() {
    $(".connected").hide();
    $(".disconnected").show();
  },
  "sendBidCall" : function (increment) {
      this.socket.send(JSON.stringify({
      itemName: xebay.bidOfferInfo["itemName"],
      curValue: xebay.bidOfferInfo["currentValue"],
      increment: increment
      }));
  },
  "listenBidOffer": function (message) {
      var bidOffer = JSON.parse(message.data);
      console.log("received bidOffer: ", bidOffer);
  }
};
