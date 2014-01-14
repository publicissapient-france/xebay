var xebay = {
  "register": function () {
    console.log('register');
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
