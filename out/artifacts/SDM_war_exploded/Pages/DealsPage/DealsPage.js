var allDeals;
var selectedDeals= Array();

function radioButtonClicked(index){
    var checkBoxName = "checkBox" + index;
    $("#" + checkBoxName).prop('disabled', false);
}

function addDealToTable(index, deal) {
    var disabledString = "";
    var dealItemsString = "";
    if (deal.benefit.operator === "ONE_OF") {
        disabledString = "disabled";
        for (var i = 0; i < deal.benefit.optionalBenefitItems.offers.length; ++i) {
            if (i !== 0) {
                dealItemsString += "<br/>";
            }

            dealItemsString += "<input onclick='radioButtonClicked(" + index + ")' type=\"radio\" name=\"dealGroup" + index + "\">  Get " +
                deal.benefit.optionalBenefitItems.offers[i].quantity + " " + deal.benefit.optionalBenefitItems.offers[i].itemName +
                " for " + deal.benefit.optionalBenefitItems.offers[i].quantity * deal.benefit.optionalBenefitItems.offers[i].additionalPrice +
                "</input>";
        }
    } else if (deal.benefit.operator === "ALL_OR_NOTHING") {
        var offerPrice = 0;
        dealItemsString = "<label>Get ";
        for (var i = 0; i < deal.benefit.optionalBenefitItems.offers.length; ++i) {
            if (i !== 0) {
                dealItemsString += " and ";
            }

            dealItemsString += deal.benefit.optionalBenefitItems.offers[i].quantity + " " +
                deal.benefit.optionalBenefitItems.offers[i].itemName;
            offerPrice += deal.benefit.optionalBenefitItems.offers[i].quantity *
                deal.benefit.optionalBenefitItems.offers[i].additionalPrice;
        }

        dealItemsString += " for " + offerPrice + "</label>";
    } else {
        dealItemsString = "<label>Get " + deal.benefit.optionalBenefitItems.offers[0].quantity + " " +
            deal.benefit.optionalBenefitItems.offers[0].itemName + " for " +
            deal.benefit.optionalBenefitItems.offers[0].quantity * deal.benefit.optionalBenefitItems.offers[0].additionalPrice + "</label>";
    }
    var tableString =
        "       <div class=\"row \">\n" +
        "            <div class=\"col \">\n" +
        "                <label>" + deal.name + "</label><br/>\n" +
        dealItemsString +
        "</div>\n" +
        "            <div class=\"col \" align=\"center\">\n" +
        "                <label>Choose Deal</label><br/>\n" +
        "                <input " + disabledString + " type=\"checkbox\" id=\"checkBox" + index + "\"/>\n" +
        "            </div>\n" +
        "        </div>";

    $("#dealsTable").append(tableString);
}

function analyzeSelectedDeals() {
    for (var i = 0; i < allDeals.length; ++i) {
        var dealDetails = {};
        if ($("#checkBox" + i).is(":checked")) {
            if (allDeals[i].benefit.operator === "IRRELEVANT") {
                dealDetails.itemId = allDeals[i].benefit.optionalBenefitItems.offers[0].itemId;
                dealDetails.itemAmount = allDeals[i].benefit.optionalBenefitItems.offers[0].quantity;
                dealDetails.additionalPrice = allDeals[i].benefit.optionalBenefitItems.offers[0].additionalPrice;
                dealDetails.storeId = allDeals[i].storeId;
            } else if (allDeals[i].benefit.operator === "ALL_OR_NOTHING") {
                var additionalPrice = 0;
                for (var j = 0; j < allDeals[i].benefit.optionalBenefitItems.offers.length; ++j) {
                    dealDetails.itemId[j] = allDeals[i].benefit.optionalBenefitItems.offers[j].itemId;
                    dealDetails.itemAmount[j] = allDeals[i].benefit.optionalBenefitItems.offers[j].quantity;
                    additionalPrice += allDeals[i].benefit.optionalBenefitItems.offers[j].additionalPrice;
                }
                dealDetails.additionalPrice = additionalPrice;
                dealDetails.storeId = allDeals[i].storeId;
            } else {
                for (var j = 0; j < (($("#dealsTable").children()[0].children[0].children.length)-1)/2; ++j) {
                    if ($("#dealsTable").children()[i].children[0].children[(j+1)*2].checked) {
                        dealDetails.itemId =  allDeals[i].benefit.optionalBenefitItems.offers[j].itemId;
                        dealDetails.itemAmount = allDeals[i].benefit.optionalBenefitItems.offers[j].quantity;
                        dealDetails.additionalPrice = allDeals[i].benefit.optionalBenefitItems.offers[j].additionalPrice;
                        dealDetails.storeId = allDeals[i].storeId;
                        break;
                    }
                }
            }
            selectedDeals.push(dealDetails);
        }
    }
}

function showErrorMessage(message) {
    freezeScreen();

    $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
        message +
        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" onclick='{window.close();}'>" +
        "<span aria-hidden=\"true\">&times;</span>" +
        "</button>" +
        "</div>");
}

function onApplyButtonClicked(){
    analyzeSelectedDeals();
    $.ajax({
        url: "postSelectedDeals",
        method: 'post',
        data: {
            itemsIds : sessionStorage.getItem("itemsIds"),
            itemsAmounts : sessionStorage.getItem("itemsAmounts"),
            orderType : sessionStorage.getItem("orderType"),
            storeId : sessionStorage.getItem("selectedStoreId"),
            selectedDeals: JSON.stringify(selectedDeals),
            username : sessionStorage.getItem("username"),
            xCoord : sessionStorage.getItem("Xcoord"),
            yCoord : sessionStorage.getItem("Ycoord"),
            date : sessionStorage.getItem("date"),
            zName : sessionStorage.getItem("zone")
        },
        success: function () {
            window.location.href = "../OrderSummaryPage/OrderSummaryPage.html" ;
        },
        error: function () {
            showErrorMessage("An error occurred while posting deals details");
        }

    });
}

function addApplyButton() {
    $("#dealsTable").append("<div class=\"row \">" +
                                "<div class='col ' align='center'>" +
    "<input type=\"button\" value=\"Apply\" onclick='onApplyButtonClicked()'/>" +
                                "</div>" +
                            "</div>");
}

function getEmptyDealsMessage() {
    var message = String();
    message += "<div class=\"row \">" +
        "<div class='col ' align='center'>" +
        "<h5 align='center'>There are No Deals to Offer</h5>" +
        "</div>" +
        "</div>"
    return message;
}

function freezeScreen() {
    var childNodes = document.getElementsByTagName('button');
    for (var node of childNodes) {
        node.disabled = true;
    }
    var childNodes = document.getElementsByTagName('input');
    for (var node of childNodes) {
        node.disabled = true;
    }
}

$(function (){
    var itemsIds = sessionStorage.getItem("itemsIds");
    var itemsAmounts = sessionStorage.getItem("itemsAmounts");
    var orderType = sessionStorage.getItem("orderType");
    var selectedStoreId = sessionStorage.getItem("selectedStoreId");

    $.ajax({
        url : 'getDeals',
        dataType : 'json',
        data : {
            zName : sessionStorage.getItem("zone"),
            itemsIds : itemsIds,
            itemsAmounts : itemsAmounts,
            orderType : orderType,
            storeId : selectedStoreId
        },
        success : function (dealsDTO){
            allDeals = dealsDTO;
            $.each(dealsDTO || [], addDealToTable);
            if(dealsDTO.length === 0){
                $("#dealsTable").append(getEmptyDealsMessage());
            }
            addApplyButton();
        } ,
        error : function (){
            showErrorMessage("An error occurred while fetching deals details");
        }
    });
});