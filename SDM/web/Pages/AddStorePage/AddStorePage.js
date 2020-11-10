function getItemsList(){
    var itemsList = [];
    $.each($(".price") || [] , function (index, price) {
        if (price.value !== "0" && $("#checkbox" + index).is(":checked")) {
            var item = Object();
            item.id = $("#itemId" + index).text();
            item.price = price.value;
            itemsList.push(item);
        }
    });

        return itemsList;
}

function onOKClicked(){
    $("#locationError").text("");
    $.ajax({
       url: 'addStore',
        method: 'post',
        dataType: 'json',
       data: {
           zName: sessionStorage.getItem('zone'),
           username: sessionStorage.getItem('username'),
           storeName: $("#storeName").val().trim(),
           xCoord: $("#Xcoord").val(),
           yCoord:  $("#Ycoord").val(),
           PPK: $("#PPK").val(),
           items: JSON.stringify(getItemsList())
       },
        success:function (message) {
            if (message !== "success") {
                $("#locationError").text(message);
                $("#Xcoord").val("");
                $("#Ycoord").val("");
                window.location.href ="#";
            } else {
                $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
                    "Your store added successfully" +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button>" +
                    "</div>");
                window.location.href ="#";

                setTimeout(function () {
                   window.close();
                }, 2000);
            }
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "Unknown error occurred while adding new store" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
            window.location.href ="#";

            setTimeout(function (){
               window.close();
            }, 2000);
        }
    });
}

function checkOKButtonDisability(){
    var isLegalStore = false;
    var isInvalidItem = false;
    if($("#storeName").val().trim() !== "" && $("#Xcoord").val() !=="" && $("#Xcoord").val() !=="0" &&
        $("#Ycoord").val() !== "" && $("#Ycoord").val()!=="0"&& $("#PPK").val() !== ""){
        var prices = $(".price");
        $.each(prices || [] , function (index, price){
            if((price.value === "0" || price.value === "") && $("#checkbox" + index).is(":checked")){
                isInvalidItem = true;
            } else if(price.value !== "0" && price.value !== "" && $("#checkbox" + index).is(":checked")){
                isLegalStore = true;
            }
        });
    }
    if(isLegalStore && isInvalidItem === false){
        $("#OKButton").prop("disabled", false);
    }else{
        $("#OKButton").prop("disabled", true);
    }
}

function onPriceTyped(event) {
    if(event.path[0].value.split('.').length>1 ){
        event.path[0].value = event.path[0].value.split('.')[0];
    }
    if (event.path[0].value === "0" || event.path[0].value === "") {
        event.path[0].value = "";
    }
    if (event.which === 110) {
        var temp = event.path[0].value;
        event.path[0].value = "";
        event.path[0].value = temp;
    }
}

function onPPKTyped(event){
    if(event.path[0].value.split('.').length>1 ){
        event.path[0].value = event.path[0].value.split('.')[0];
    }
    if(event.path[0].value === ""){
        event.path[0].value = "";
    }
    if (event.which === 110) {
        var temp = event.path[0].value;
        event.path[0].value = "";
        event.path[0].value = temp;
    }
}

function onCoordTyped(event) {
    if(event.path[0].value.split('.').length>1 ){
        event.path[0].value = event.path[0].value.split('.')[0];
    }
    if(event.path[0].value === "0" || event.path[0].value === ""){
        event.path[0].value = "";
    }
    if (event.which === 110) {
        var temp = event.path[0].value;
        event.path[0].value = "";
        event.path[0].value = temp;
    }
    if(event.path[0].value > 50){
        event.path[0].value = "";
    }
}

function addItemToTable(index, item) {
    $("#detailsContainer").append(
        "<div class=\"row \">\n" +
        "            <div class=\"col smallCol \" id='itemId" + index + "'>" + item.id + "</div>\n" +
        "            <div class=\"col regularCol \">" + item.name + "</div>\n" +
        "            <div class=\"col regularCol \">" + item.purchaseCategory + "</div>\n" +
        "            <div class=\"col regularCol \">" + item.storesSellingTheItem + "</div>\n" +
        "            <div class=\"col regularCol \">" + item.avgPrice + "</div>\n" +
        "            <div class=\"col regularCol \">" + item.soldCounter + "</div>\n" +
        "            <div class=\"col bigCol \"><input class='price' onkeyup='onPriceTyped(event)' id='price" + index + "' type='number' min=\"1\" onchange='checkOKButtonDisability()'></div>\n" +
        "            <div class=\"col smallCol \"><input id='checkbox" + index + "' type='checkbox' name='items' onchange='checkOKButtonDisability()'></div>\n" +
        "</div>"
    );
}

$(function (){
    $.ajax({
        url: 'getZoneItems',
        data:{
            zName : sessionStorage.getItem('zone')
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

            setTimeout(function (){
                window.close();
            }, 2000);
        }
    }) ;
});