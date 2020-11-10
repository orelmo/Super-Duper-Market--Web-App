var zoneName = sessionStorage.getItem("zone");
var itemsCounter = 0;
var popupWindow = null;

function tryUnblockUI(){
    if(popupWindow && popupWindow.closed){
        popupWindow = null;
        $.unblockUI();
    }
}

function addStore(){
    $.blockUI({message:"<h1>Add store window is open...</h1>"});
    popupWindow = window.open('../AddStorePage/AddStorePage.html', 'AddStorePage', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}

function feedbackStore(){
    $.blockUI();
    popupWindow = window.open('../FeedbackPage/FeedbackPage.html', 'FeedbackPage', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}

function getItemCard(storeIndex, item) {
    itemsCounter++;
    return"                                        <div class=\"card\">\n" +
        "                                            <div class=\"card-header\" id=\"itemNumber" + itemsCounter + "\">\n" +
        "                                                <h5 class=\"mb-0\">\n" +
        "                                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#collapseItem" + itemsCounter + "\" aria-expanded=\"false\" aria-controls=\"collapseItem" + itemsCounter + "\">\n" +
        "                                                        " + item.name + "\n" +
        "                                                    </button>\n" +
        "                                                </h5>\n" +
        "                                            </div>\n" +
        "                                            <div id=\"collapseItem" + itemsCounter + "\" class=\"collapse \" aria-labelledby=\"itemNumber" + itemsCounter + "\" data-parent=\"#itemsFromStore" + storeIndex + "\">\n" +
        "                                                <div class=\"card-body\">\n" +
        "                                                    <div>ID: " + item.id + "</div>\n" +
        "                                                    <div>Category: " + item.purchaseCategory + "</div>\n" +
        "                                                    <div>Unit Price: " + item.priceAtStore + "</div>\n" +
        "                                                    <div>Units Sold: " + item.soldCounter + "</div>\n" +
        "                                                </div>\n" +
        "                                            </div>\n" +
        "                                        </div>\n" ;
}

function getItemsCardsFromStore(storeIndex, items){
    var itemsString = String();
    for(var i =0;i<items.length;++i){
        itemsString += getItemCard(storeIndex,items[i]);
    }
    return itemsString;
}

function showStoreOrderHistory(storeId){
    $.blockUI({message:"<h1>Store Order History is Open</h1>"});
    sessionStorage.setItem('storeId',storeId);
    popupWindow = window.open('../StoreOrderHistoryPage/StoreOrderHistoryPage.html', 'StoreOrderHistoryWindow', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}

function addStoreToAccordion(index, store){
    var orderHistoryButton = "";
    if(store.ownerName === sessionStorage.getItem('username')){
        orderHistoryButton = "<button type=\"button\" class=\"btn btn-outline-primary\" onclick=\"showStoreOrderHistory("+ store.id + ")\">Order History</button>";
    }
    $("#storesCards").append(
        "<div class=\"card\">\n" +
        "            <div class=\"card-header\" id=\"heading" + index + "\" >\n" +
        "                <h5 class=\"mb-0\">\n" +
        "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#collapse" + index + "\" aria-expanded=\"false\" aria-controls=\"collapse" + index + "\">\n" +
        "                        " + store.name + "\n" +
        "                    </button>\n" +
        "                </h5>\n" +
        "            </div>\n" +
        "\n" +
        "            <div id=\"collapse" + index + "\" class=\"collapse \" aria-labelledby=\"heading" + index + "\" data-parent=\"#storesAccordion\">\n" +
        "                <div class=\"card-body\">\n" +
        "                    <div>ID: " + store.id + "</div>\n" +
        "                    <div>Owner Name: " + store.ownerName + "</div>\n" +
        "                    <div>Location: {" + store.location.x + ", " + store.location.y + "}" + "</div>\n" +
        "                    <div>Number of Orders: " + store.numberOfOrders + "</div>\n" +
        "                    <div>Income from Items: " + store.itemsIncome + "</div>\n" +
        "                    <div>PPK: " + store.ppk + "</div>\n" +
        "                    <div>Delivery Income: " + store.deliveriesIncome + "</div>\n" +
                             orderHistoryButton +
        "                    <div id=\"itemsAccordion" + index + "\">\n" +
        "                         <div class=\"card\">\n" +
        "                           <div class=\"card-header\" id=\"accordionNumber" + index + "\">\n" +
        "                                <h5 class=\"mb-0\">\n" +
        "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#itemsInsideAccordion" + index + "\" aria-expanded=\"false\" aria-controls=\"itemsInsideAccordion" + index + "\">\n" +
        "                                        Store Items\n" +
        "                                    </button>\n" +
"                                        </h5>\n" +
"                                   </div>\n" +
"\n" +
"                            <div id=\"itemsInsideAccordion" + index + "\" class=\"collapse\" aria-labelledby=\"accordionNumber" + index + "\" data-parent=\"#itemsAccordion" + index + "\">\n" +
"                                <div class=\"card-body\">\n" +
"                                    <div id=\"itemsFromStore" + index + "\">\n" +
                                    getItemsCardsFromStore(index,store.itemsDetails) +
        "                            </div>"+
        "                        </div>"+
        "                    </div>"+
        "                </div>"+
        "            </div>"+
        "</div>");
}

function getZoneStores(){
    $.ajax({
        url: 'getZoneStores',
        dataType: 'json',
        data:{
            zName : zoneName
        } ,
        success:function (zoneStoresDTO){
            $.each(zoneStoresDTO || [], addStoreToAccordion);
        },
        error:function (){
            $("#zoneStoresErrorAlert").empty();
            $("#zoneStoresErrorAlert").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "Unknown error occurred while fetching zone`s stores" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
    getZoneStores();
});

function addItemToTable(index, item) {
    $("#itemsTable").append(
        "<div class=\"row item-row\">\n" +
        "            <div class=\"col\">" + item.id + "</div>\n" +
        "            <div class=\"col \">" + item.name + "</div>\n" +
        "            <div class=\"col \">" + item.purchaseCategory + "</div>\n" +
        "            <div class=\"col \">" + item.storesSellingTheItem + "</div>\n" +
        "            <div class=\"col \">" + item.avgPrice + "</div>\n" +
        "            <div class=\"col \">" + item.soldCounter + "</div>\n" +
        "        </div>"
    );
}

$(function (){
   $.ajax({
       url: 'getZoneItems',
       data:{
           zName : zoneName
       },
       dataType: 'json',
       success: function (zoneItemsDTO){
           $.each(zoneItemsDTO || [], addItemToTable);
       },
       error: function (){
           $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
               "Unknown error occurred while fetching zone's items" +
               "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
               "<span aria-hidden=\"true\">&times;</span>" +
               "</button>" +
               "</div>");
       }
   }) ;
});

$(function (){
    if(sessionStorage.getItem("userType")==="Seller") {
        $("#customerMenu").addClass("invisible");
        if(sessionStorage.getItem("username") !== sessionStorage.getItem("zoneOwnerName")){
            $("#addItemButton").addClass("invisible");
        }
    }else {
        $("#sellerMenu").addClass("invisible");
    }
});

function showFeedbacks(){
    $.blockUI({message: "<h1>Feedbacks Window is Open...</h1>"});
    popupWindow = window.open('../SellerZoneFeedbacksPage/SellerZoneFeedbacksPage.html', 'SellerZoneFeedbacksWindow', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}

function makeOrder() {
    window.location.href = '../OrderPage/OrderPage.html';
}

function showOrderHistory() {
    $.blockUI({message: "<h1>Orders History Window is Open...</h1>"});
    popupWindow = window.open('../OrderHistoryPage/OrderHistoryPage.html', 'OrderHistoryWindow', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}

function showNotification(index, notification) {
    switch (notification.notificationType){
        case "NEW_ORDER":{
            $("#alertPlace").append(
                "<div class=\"alert alert-success\" role=\"alert\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "  <h4 class=\"alert-heading\">New Order Has Been Made From " + notification.storeName + "!</h4>\n" +
                "  <p>A new order with id " + notification.orderId + " made by " + notification.customerUsername + ".</p>\n" +
                "<p>" + notification.customerUsername + " bought " + notification.numberOfDifferentItems + " different items, " +
                " paid " + notification.itemsPrice + " for the items and " + notification.deliveryPrice + " for the delivery.</p>" +
                "  <p>Your income from this order is: " + (notification.itemsPrice + notification.deliveryPrice) + ".</p>\n" +
                "</div>");
            break;
        }
        case "NEW_STORE": {
            $("#alertPlace").append(
                "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "  <h4 class=\"alert-heading\">New Store Has Been Opened In " + notification.zoneName + " Zone!</h4>\n" +
                "  <p>" + notification.newStoreOwnerName + " opened a new store named " +
                notification.newStoreName + "in your zone at location: {" + notification.newStoreLocation.x + ", " + notification.newStoreLocation.y + "}.</p>\n" +
                "  <p>" + notification.newStoreName + " sells " + notification.storeDifferentItems + "/" + notification.zoneDifferentItems + " zone items.</p>\n" +
                "</div>");
            break;
        }
        case "NEW_FEEDBACK":{
            var messageString = "";
            if(notification.message !== ""){
                messageString = "<p>Feedback message: " + notification.message + "</p>\n";
            }

            $("#alertPlace").append(
                "<div class=\"alert alert-primary\" role=\"alert\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "  <h4 class=\"alert-heading\">New Feedback Has Been Submitted!</h4>\n" +
                "  <p>A new feedback has been submitted by " + notification.customerName + ".</p>\n" +
                "  <p>" + notification.customerName + " rated you " + notification.rate + ".</p>\n" +
                messageString +
                "</div>");
            break;
        }
        default:{
            break;
        }
    }
}

function getNotifications() {
    $.ajax({
        url : 'getNewNotifications',
        dataType : 'json',
        data : {
            username : sessionStorage.getItem("username"),
            seenNotifications : sessionStorage.getItem("seenNotifications")
        },
        success : function (notificationsDTO) {
            $.each(notificationsDTO[0] || [], showNotification);
            sessionStorage.setItem("seenNotifications", notificationsDTO[1]);
        },
        error : function (){
            $("#notificationsErrorPlace").empty();
            $("#notificationsErrorPlace").append("<div class='alert alert-warning fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching new notifications" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
    if(sessionStorage.getItem("userType") === "Seller"){
        setInterval(getNotifications, 2000);
    }
});

function addItem(){
    $.blockUI({message: "<h1>Add Item Window is Open...</h1>"});
    popupWindow = window.open('../AddItemPage/AddItemPage.html', 'AddItemWindow', 'width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
}