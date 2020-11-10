var isOwnStores = false;

function onQuantityClicked(){
    $("#dropdownMenuButton").text("Quantity");
    checkSubmitButtonDisability();
}

function onWeightClicked(){
    $("#dropdownMenuButton").text("Weight");
    checkSubmitButtonDisability();
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

    checkSubmitButtonDisability();
}

function addStoreToTable(index, store) {
    if (sessionStorage.getItem("username") === store.ownerName) {
        isOwnStores = true;
        $("#stores").append(
            "<div class='row'>" +
            "<div class='col' id='store" + index + "'>" +
            store.name + ", Id: " + store.id +
            "</div>" +
            "<div class='col'>" +
            "<input id='" + index + "' onchange='checkSubmitButtonDisability()' type='checkbox' name='storesCheckbox'>" +
            "</div>" +
            "</div>"
        );
    }
}

function checkSubmitButtonDisability(){
    if($("#itemName").val().trim() !== "" && $("#dropdownMenuButton").text().trim() !== "Purchase Category" && $("#price").val() !== "" &&
        $('input[name=storesCheckbox]:checked').length > 0){
        $("#submitButton").prop('disabled', false);
    } else {
        $("#submitButton").prop('disabled', true);
    }
}

$(function (){
   $.ajax({
       url: 'getSellerZoneStores',
       dataType: 'json',
       data: {
           zName: sessionStorage.getItem("zone")
       },
       success: function (storesDTO){
           $.each(storesDTO || [], addStoreToTable);
           if(isOwnStores === false){
               $("#stores").append(
                   "<h4 align='center'>You Don't Own Stores In This Zone</h4>"
               );
           }
       },
       error: function (){
           $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
               "An error occurred while fetching seller's zone stores" +
               "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
               "<span aria-hidden=\"true\">&times;</span>" +
               "</button>" +
               "</div>");
       }
   });
});

function onSubmitButtonClicked(){
    var selectedStoresIds = [];
    $.each($('input[name=storesCheckbox]:checked') || [], function (index, checkBox){
        var rowIndex = checkBox.id;
        var storeId = $("#store" + rowIndex).text().split(', Id: ')[1];
        selectedStoresIds.push(storeId);
    });

    $.ajax({
        method: 'post',
        url: 'addNewItem',
        data: {
            zName: sessionStorage.getItem("zone"),
            selectedStoresIds: JSON.stringify(selectedStoresIds),
            itemName: $("#itemName").val().trim(),
            itemPrice: $("#price").val(),
            purchaseCategory: $("#dropdownMenuButton").text()
        },
        success: function (){
            $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while adding new item" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while adding new item" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });

    setTimeout(function(){window.close();}, 2000);
}