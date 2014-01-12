$(document).ready(function () {
  $.getJSON("/rest/bidEngine",function (currentBidOffer) {
    console.log(currentBidOffer);
    $("#current-bid-offer").html("" +
        "<p>" + currentBidOffer["item"]["name"] + "</p>" +
        "<p>current value: " + currentBidOffer["currentValue"] + " bid points</p>");
  }).fail(function () {
        $("#current-bid-offer").html("" +
            "<p>There is no bid offer.</p>");
      });
});