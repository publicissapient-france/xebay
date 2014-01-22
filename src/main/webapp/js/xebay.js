var xebay = {
  "init": function () {
    $("#unregister").hide();
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
      "success": function (user) {
        xebay.signedin(email, key);
      }
    });
  },
  "signout": function () {
    this.signedout();
  },
  "unregister": function () {
    $.get("/rest/users/unregister", {"email": this.email, "key": this.key}, function () {
      xebay.signedout();
    });
  },
  "signedin": function (email, key) {
    $("#email-display").text(email);
    $("#key-display").text(key);
    $("#unregister").show();
    $("#register").hide();
    $.cookie("xebay", {"email": email, "key": key});
  },
  "signedout": function () {
    $("#key-display").text("");
    $("#register").show();
    $("#unregister").hide();
    $.removeCookie("xebay");
  },
  "displayBidOffer": function () {
    $.getJSON("/rest/bidEngine",function (currentBidOffer) {
      $("#current-bid-offer").html("" +
          "<p>" + currentBidOffer["itemName"] + "</p>" +
          "<p>current value: " + currentBidOffer["currentValue"] + " bid points</p>");
      setTimeout(xebay.displayBidOffer, currentBidOffer["timeToLive"]);
    }).fail(function () {
          $("#current-bid-offer").html("" +
              "<p>There is no bid offer.</p>");
        });
  }
};
