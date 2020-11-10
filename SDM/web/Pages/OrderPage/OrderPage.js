var globalZoneStoresDTO;
var dynamicOrderSuppliers;
var popupWindow = null;

function onStaticOrderSelected(){
    $("#suppliers").empty();
    $("#orderTypeDropdown").text("Static Order");
    $("#itemsTable").empty();
    $("#storesComboBox").removeClass("invisible");
    updateSubmitButtonDisability();
}

function checkAmountAreasDisability() {
    if($("#Xcoord").val() !== "" && $("#Xcoord").val() !=="0" && $("#Ycoord").val() !== "" && $("#Ycoord").val() !=="0"){
        $(".amountArea").prop("disabled", false);
    }else{
        $(".amountArea").prop("disabled", true);
    }
}

function onCoordChanged(){
    updateSubmitButtonDisability();
    checkAmountAreasDisability()
}

function offerDeals() {
    var index = 0;
    var itemsIds = new Array();
    var itemsAmounts = new Array();
    $.each($(".itemId") || [], function (){
        var amountId = "amount" + this.innerText;
        if($("#" + amountId).val() !== "0"){
            itemsIds[index] =this.innerText;
            itemsAmounts[index] =$("#" + amountId).val() ;
            ++index;
        }
    });

    sessionStorage.setItem("itemsIds", JSON.stringify(itemsIds));
    sessionStorage.setItem("itemsAmounts", JSON.stringify(itemsAmounts));
    sessionStorage.setItem("orderType", $("#orderTypeDropdown").text());
    sessionStorage.setItem("selectedStoreId", $("#storesComboBox").text().split(", Id: ")[1]);

    $.blockUI({message: "<h1>Order in Progress...</h1>"});
    popupWindow = window.open('../DealsPage/DealsPage.html','DealsWindow','width=750, height=600,top=' +(screen.height - 750) / 2+', left=' + (screen.width - 600) / 2 +"'");

    return false;
}

function tryUnblockUI(){
    if(popupWindow && popupWindow.closed){
        popupWindow = null;
        $.unblockUI();
        clearPage();
    }
}

function clearPage() {
    $("#orderTypeDropdown")[0].innerText = "Order Type";
    $("#storesComboBox")[0].innerText = "Select Store";
    $("#storesComboBox").addClass("invisible");
    $("#Xcoord").val("");
    $("#Ycoord").val("");
    $("#arrivalDate").val("");
    $("#itemsTable").empty();
    $("#suppliers").empty();
    $("#submitButton").prop('disabled', true);
}

function onSubmitPressed() {
    if (checkCustomerLocationTaken() === false) {
        return;
    }

    sessionStorage.setItem("Xcoord",$("#Xcoord").val());
    sessionStorage.setItem("Ycoord",$("#Ycoord").val());
    var date = new Date();
    date.setDate(parseInt($("#arrivalDate").val().split("-")[2]));
    date.setMonth(parseInt($("#arrivalDate").val().split("-")[1])-1);
    date.setFullYear(parseInt($("#arrivalDate").val().split("-")[0]));

    sessionStorage.setItem("date",JSON.stringify(date));

    offerDeals();
}

function updateSubmitButtonDisability() {
    var counter = 0;

    if ((($("#orderTypeDropdown")[0].innerText === "Static Order" && $("#storesComboBox")[0].innerText !== "Select Store")
        || $("#orderTypeDropdown")[0].innerText === "Dynamic Order") && ($("#Xcoord").val() !== "" && $("#Xcoord").val() !== "0") && ($("#Ycoord").val() !== "" && $("#Ycoord").val() !== "0")
        && $("#arrivalDate").val() !== "") {
        var amountList = $(".amountArea");
        $.each(amountList || [], function (index, amountTextfield) {
            if (amountTextfield.value !== "0") {
                counter++;
            }
        });
        if (counter > 0) {
            $("#submitButton").prop("disabled", false);
        } else {
            $("#submitButton").prop("disabled", true);
        }
    } else {
        $("#submitButton").prop("disabled", true);
    }
}

function onAmountChanged() {
    $("#suppliersAccordion").empty();
    updateSubmitButtonDisability();
    if ($("#orderTypeDropdown").text() === "Dynamic Order") {
        getSuppliersAccordion();
    }
}

function onAmountKeyPress(event){

    if(event.path[0].value[0] === "0" && event.which !== 110 && event.which !== 8 && event.which !== 46){
        event.path[0].value = event.key;
    }
    if(event.path[0].value === ""){
        event.path[0].value = 0;
    }
    if(event.path[2].children[2].innerText === "Quantity"){
        if(event.path[0].value.split('.').length>1 ){
            event.path[0].value = event.path[0].value.split('.')[0];
        }
        if(event.which === 110){
            var temp = event.path[0].value;
            event.path[0].value ="";
            event.path[0].value = temp;
        }
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

function addItemToTable(index, itemDTO){
    var itemRow = String();
    itemRow += "<div class=\"row \">" +
       "<div class=\"col \" align='center'>" +itemDTO.name + "</div>" +
        "<div class=\"col  itemId\" align='center'>" + itemDTO.id + "</div>" +
        "<div class=\"col \" align='center'>" + itemDTO.purchaseCategory + "</div>";
    if($("#orderTypeDropdown")[0].innerText === "Static Order"){
        itemRow += "<div class=\"col \" align='center'>" + itemDTO.priceAtStore + "</div>";
    }
    itemRow += "<div class=\"col \" align='center'>" + "<input disabled class='amountArea' onkeyup='onAmountKeyPress(event)' onchange='onAmountChanged()' id='amount" + itemDTO.id + "' type='number' min=\"0\" value='0'> " + "</div>";

    $("#itemsTable").append(itemRow);
    checkAmountAreasDisability();
}

function onStoreSelected(a){
    updateSubmitButtonDisability();
    $("#storesComboBox").text(a.innerText);
    $("#itemsTable").empty();

    $.ajax({
        url : 'getOrderItems',
        dataType: 'json',
        data: {
            zName: sessionStorage.getItem("zone"),
            orderType: $("#orderTypeDropdown")[0].innerText,
            storeId: a.innerText.split(", Id: ")[1]
        },
        success: function (itemsDTO){
            $("#itemsTable").empty();
            $("#itemsTable").append(staticOrderItemsHeaders());
            $.each(itemsDTO || [], addItemToTable);
        },
        error: function (){
                $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                    "An error occurred while fetching items" +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button>" +
                    "</div>");
        }
    });
}

function buildSuppliersAccordionCards(suppliersDTO) {
    $.each(suppliersDTO || [], function buildSupplierCard(index, store) {
        $("#suppliersAccordion").append(
            "<div class=\"card\">\n" +
            "            <div class=\"card-header\" id=\"heading" + index + "\" >\n" +
            "                <h5 class=\"mb-0\">\n" +
            "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#collapse" + index + "\" aria-expanded=\"false\" aria-controls=\"collapse" + index + "\">\n" +
            "                        " + store.name + "\n" +
            "                    </button>\n" +
            "                </h5>\n" +
            "            </div>\n" +
            "\n" +
            "            <div id=\"collapse" + index + "\" class=\"collapse \" aria-labelledby=\"heading" + index + "\" data-parent=\"#suppliersAccordion\">\n" +
            "                <div class=\"card-body\">\n" +
            "                    <div>ID: " + store.id + "</div>\n" +
            "                    <div>Owner Name: " + store.ownerName + "</div>\n" +
            "                    <div>Location: {" + store.location.x + ", " + store.location.y + "}" + "</div>\n" +
            "                    <div>Distance From Customer: " + store.distanceFromCustomer + "</div>\n" +
            "                    <div>PPK: " + store.ppk + "</div>\n" +
            "                    <div>Delivery Price: " + store.deliveryPrice + "</div>\n" +
            "                    <div>Number Of Different Items: " + store.numberOfDifferentItems + "</div>\n" +
            "                    <div>Items Price: " + store.itemsIncome + "</div>\n" +
            "                </div>" +
            "            </div>" +
            "</div>"
        );
    });
}

function getSuppliersAccordion() {
    var index =0;
    var itemsIds = new Array();
    var amounts = new Array();
    $.each($(".itemId") || [], function (){
        var amountId = "amount" + this.innerText;
        if($("#" + amountId).val() !== "0"){
            itemsIds[index] =this.innerText;
            amounts[index] =$("#" + amountId).val() ;
            ++index;
        }
    });

    $("#suppliers").append("<div id='suppliersAccordion'></div>");

    $.ajax({
        url: 'getOrderSuppliers',
        dataType: 'json',
        data: {
            zName : sessionStorage.getItem("zone"),
            xCoord : $("#Xcoord").val(),
            yCoord : $("#Ycoord").val(),
            itemsIds : JSON.stringify(itemsIds),
            amounts : JSON.stringify(amounts)
        },
        success: function (suppliersDTO){
            dynamicOrderSuppliers = suppliersDTO;
            buildSuppliersAccordionCards(suppliersDTO);
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching order suppliers" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

function buildSuppliersTable() {
    $("#suppliers").empty();

    var suppliersString = String();

    suppliersString =
        "<div class='col '>" +
            "<div class='container-fluid '>" +
                "<div class='row '>" +
                    "<div class='col '>" +
                        "<h4>Suppliers</h4>" +
                    "</div>" +
                "</div>" +
                "<div class='row '>" +
                    "<div class='col ' id='suppliersAccordion'>" +
                    "</div>" +
                "</div>" +
            "</div>" +
        "</div>";

    $("#suppliers").append(suppliersString);
}

function onDynamicOrderSelected(){
    $("#orderTypeDropdown").text("Dynamic Order");
    $("#itemsTable").empty();
    $("#storesComboBox").text("Select Store");
    $("#storesComboBox").addClass("invisible");
    updateSubmitButtonDisability();
    buildSuppliersTable();

    $.ajax({
        url : 'getOrderItems',
        dataType: 'json',
        data: {
            zName: sessionStorage.getItem("zone"),
            orderType: $("#orderType").val()
        },
        success: function (itemsDTO){
            $("#itemsTable").empty();
            $("#itemsTable").append(dynamicOrderItemsHeaders());
            $.each(itemsDTO || [], addItemToTable);
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching items" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

function staticOrderItemsHeaders(){
   return "<div class='row '>" +
       "<div class=\"col \" align='center'>Name</div> "+
   "<div class=\"col \" align='center'>ID</div>" +
   " <div class=\"col \" align='center'>Category</div>" +
   " <div class=\"col \" align='center'>Price</div>" +
   " <div class=\"col \" align='center'>Amount</div>" +
    "</div>";
}

function dynamicOrderItemsHeaders(){
    return "<div class='row '>" +
    "<div class=\"col \" align='center'>Name</div> "+
        "<div class=\"col \" align='center'>ID</div>" +
        " <div class=\"col \" align='center'>Category</div>" +
        " <div class=\"col \" align='center'>Amount</div>" +
    "</div>";
}

function addStoreToComboBox(index, store) {
    $("#stores").append("<a class=\"dropdown-item\" href=\"#\" onclick=\"" +
        "onStoreSelected(this)\" >"
        + store.name + ", Id: " + store.id + "</a>");
}

function checkCustomerLocationTaken() {
    for (var i = 0; i < globalZoneStoresDTO.length; ++i) {
        if (globalZoneStoresDTO[i].location.x.toString() === $("#Xcoord").val() &&
            globalZoneStoresDTO[i].location.y.toString() === $("#Ycoord").val()) {
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "The location you entered is already taken by a store" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
            $("#Xcoord").val("");
            $("#Ycoord").val("");
            return false
        }
    }
    return true;
}

$(function() {
    $.ajax({
        url: "getZoneStores",
        dataType: 'json',
        data: {
            zName: sessionStorage.getItem("zone")
        },
        success: function (zoneStoresDTO) {
            globalZoneStoresDTO = zoneStoresDTO;
            $.each(zoneStoresDTO || [], addStoreToComboBox);
        },
        error: function () {
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching zone's stores" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
});

function updateStoresComboBox() {
    $("#stores").empty();

    $.ajax({
        url: "getZoneStores",
        dataType: 'json',
        data: {
            zName: sessionStorage.getItem("zone")
        },
        success: function (zoneStoresDTO) {
            globalZoneStoresDTO = zoneStoresDTO;
            $.each(zoneStoresDTO || [], addStoreToComboBox);
        },
        error: function () {
            $("#zonesStoresAlert").empty();
            $("#zonesStoresAlert").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching zone's stores" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
   setInterval(updateStoresComboBox, 5000);
});