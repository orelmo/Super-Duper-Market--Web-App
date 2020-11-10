$(function (){
    if(sessionStorage.getItem("userType") === "Customer"){
        $("#XMLButton").addClass("invisible");
    }
});

function moveToStoresPage(div) {
    sessionStorage.setItem("zone", div.children[1].innerText);
    sessionStorage.setItem("zoneOwnerName", div.children[0].innerHTML);
    window.location.href = "../StoresPage/StoresPage.html";
}

function updateZonesTable() {
    $("#zonesTable").empty();

    function addZoneToTable(index, zone) {
        $("#zonesTable").append("<div class=\"row  table-row\" onclick='moveToStoresPage(this)'>" +
            "                        <div class=\"col \">" + zone.ownerName + "</div>" +
            "                        <div class=\"col \">" + zone.zoneName + "</div>" +
            "                        <div class=\"col \">" + zone.numberOfDifferentItems + "</div>" +
            "                        <div class=\"col \">" + zone.numberOfStores + "</div>" +
            "                        <div class=\"col \">" + zone.numberOfOrders + "</div>" +
            "                        <div class=\"col \">" + zone.avgOrderItemsPrice + "</div>" +
            "                    </div>");
    }

    $.ajax({
        url: 'getZones',
        dataType: 'json',
        success: function (zones){
            $.each(zones || [], addZoneToTable);
        },
        error: function (){
            $("#zoneErrorPlace").empty();
            $("#zoneErrorPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "Unknown error occurred while updating zones table" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
    setInterval(updateZonesTable, 2000);
});

function pullOnlineUsers() {
    $("#onlineUsers").empty();

    $.ajax({
        url: "onlineUsers",
        dataType: "json",
        success: function (usersDTO) {
            var users = usersDTO.users;
            $.each(users || [], function (userName, userType) {
                $('<li>' + userType + ": " + userName + '</li>').appendTo($("#onlineUsers"));
            });
        },
        error: function () {
            $("#onlineUsersErrorPlace").empty();
            $("#onlineUsersErrorPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "Error occurred while loading online users" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
    setInterval(pullOnlineUsers, 2000);
})

function loadXMLFile(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    reader.readAsText(file);

    reader.onload = function (){
        var content = reader.result;

        $.ajax({
            url: "loadXML",
            data: {fileContent : content, username : sessionStorage.getItem("username")},
            dataType: 'json',
            success: function (loadXMLDTO) {
                if (loadXMLDTO.isValid) {
                    $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
                        "The file was loaded successfully!" +
                        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                        "<span aria-hidden=\"true\">&times;</span>" +
                        "</button>" +
                        "</div>");
                } else {
                    $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                        loadXMLDTO.errorMessage +
                        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                        "<span aria-hidden=\"true\">&times;</span>" +
                        "</button>" +
                        "</div>");
                }

                $('#inputGroupFile01').empty();
                $(".custom-file-label").text("Choose File");
            },
            error: function () {
                $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                    "Unknown error occurred while loading XML file" +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button>" +
                    "</div>");
            }
        });
    }
}

function checkFileType(event){
    if($('#inputGroupFile01').val() === null){
        return;
    }
    var ext = $('#inputGroupFile01').val().split('.').pop().toLowerCase();
    if(ext !== 'xml') {
        $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
            "Please upload xml file only" +
            "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
            "<span aria-hidden=\"true\">&times;</span>" +
            "</button>" +
            "</div>");
        return false;
    }
    else{
        $(".custom-file-label").text($("#inputGroupFile01").val().split('\\').pop());
        loadXMLFile(event);
    }
}

function openTransactionsWindow(){
    window.open('../FinancialPage/FinancialPage.html','FinancialWindow','width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");
    return false;
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
        case "NEW_STORE":{
            $("#alertPlace").append(
                "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "  <h4 class=\"alert-heading\">New Store Has Been Opened In " + notification.zoneName +" Zone!</h4>\n" +
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

function getChatHistory() {
    var seenMessages = sessionStorage.getItem("seenMessages");

    $.ajax({
        url: 'getChatHistory',
        dataType: 'json',
        data: {
            seenMessages: seenMessages
        },
        success: function (messagesDTO){
            $.each(messagesDTO || [], function (index, message){
               $("#chatSpace").append("<p>" + message + "</p>");
            });
            sessionStorage.setItem("seenMessages", (seenMessages - '0' + messagesDTO.length));
            var scroller = $("#chatSpace");
            var height = scroller[0].scrollHeight - $(scroller).height();
            $(scroller).stop().animate({ scrollTop: height }, "slow");
        },
        error: function (){
            $("#chatErrorPlace").empty();
            $("#chatErrorPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching chat history" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
    sessionStorage.setItem("seenMessages", "0");

   getChatHistory();

   setInterval(getChatHistory, 1000);
});

function sendChatMessage() {
    var newMessage = sessionStorage.getItem("username") + ">> " + $("#messageArea").val().trim();

    $("#messageArea").val("");

    $.ajax({
        method: 'post',
        url: 'sendChatMessage',
        data: {
            newMessage: newMessage
        },
        error: function () {
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while sending chat message" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

function checkSendButtonDisability(){
    if($("#messageArea").val().trim() !== ""){
        $("#sendButton").prop('disabled', false);
    } else {
        $("#sendButton").prop('disabled', true);
    }
}