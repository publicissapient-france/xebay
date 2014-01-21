var xebay = {
  "init": function () {
    $("#unregister").hide();
    this.initCurrentBidOffer();
  },
  "email": "",
  "key": "",
  "signup": function () {
    var email = $("#email").val();
    $.get("/rest/users/register", {"email": email}, function (key) {
      xebay.signedin(email, key);
    });
  },
  "signin": function () {
    var email = $("#email").val();
    var key = $("#key").val();
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
    this.email = email;
    this.key = key;
    $("#email-display").text(email);
    $("#key-display").text(key);
    $("#unregister").show();
    $("#register").hide();
  },
  "signedout": function () {
    email = "";
    key = "";
    $("#key-display").text("");
    $("#register").show();
    $("#unregister").hide();
  },
  "initCurrentBidOffer": function () {
    $.getJSON("/rest/bidEngine",function (currentBidOffer) {
      $("#current-bid-offer").html("" +
          "<p>" + currentBidOffer["itemName"] + "</p>" +
          "<p>current value: " + currentBidOffer["currentValue"] + " bid points</p>");
    }).fail(function () {
          $("#current-bid-offer").html("" +
              "<p>There is no bid offer.</p>");
        });
  }
};
