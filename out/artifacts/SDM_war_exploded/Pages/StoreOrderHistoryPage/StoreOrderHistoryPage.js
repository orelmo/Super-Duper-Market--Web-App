
function fillBoughtItems(orderDTO, index) {
    for (var i = 0; i < orderDTO.boughtItemsStoresIds.length; ++i) {
        for (var j = 0; j < orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer.length; ++j) {
            $("#orderItemsAccordion" + index).append(
                "<div class=\"card\">\n" +
                "            <div class=\"card-header\" id=\"heading" + i + index + j + "\" >\n" +
                "                <h5 class=\"mb-0\">\n" +
                "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#collapse" + i + j + "\" aria-expanded=\"false\" aria-controls=\"collapse" + i + j + "\">\n" +
                "                        " + orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].name + "\n" +
                "                    </button>\n" +
                "                </h5>\n" +
                "            </div>\n" +
                "\n" +


                "            <div id=\"collapse" + i + j + "\" class=\"collapse \" aria-labelledby=\"heading" + i + j + "\" data-parent=\"#orderItemsAccordion" + index + "\">\n" +
                "                <div class=\"card-body\">" +
                "                   <p>Id: " + orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].id + "</p>" +
                "                   <p>Purchase Category: " + orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].purchaseCategory + "</p>" +
                "                   <p>Amount: " + orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].amount + "</p>" +
                "                   <p>Price Per Unit: " + orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].priceAtStore + "</p>" +
                "                   <p>Total Price: " + (orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].priceAtStore * orderDTO.boughtItems[i].itemsDetailsContainer.itemsDetailsContainer[j].amount) + "</p>" +
                "                   <p>Is Part Of Deal: No</p>" +
                "                </div>" +
                "           </div>" +
                "      </div>");
        }
    }
}

function getDealItemsCards(orderDTO,dealIndex){
    var dealItemsCards = "";
    for(var itemIndex = 0; itemIndex < orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer.length; itemIndex++){
        dealItemsCards +=  getDealItemCard(orderDTO, dealIndex, itemIndex);
    }
    return dealItemsCards;
}

function getStoreName(orderDTO, dealItemsStoreId) {
    var storeName;
    for(var i =0 ; i<orderDTO.boughtItemsStoresIds.length; ++i){
        if(orderDTO.boughtItemsStoresIds[i] === dealItemsStoreId){
            storeName = orderDTO.boughtItemsStoresNames[i];
        }
    }
    return storeName;
}



function fillDealsItems(orderDTO, index){
    for (var i = 0; i < orderDTO.dealItemsStoreIds.length; ++i) {
        var storeName = getStoreName(orderDTO,orderDTO.dealItemsStoreIds[i]);
        for (var j = 0; j < orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer.length; ++j) {
            $("#orderItemsAccordion" + index).append(
                "<div class=\"card\">\n" +
                "            <div class=\"card-header\" id=\"dealHeading" + i + index + j + "\" >\n" +
                "                <h5 class=\"mb-0\">\n" +
                "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#dealCollapse" + i + j + "\" aria-expanded=\"false\" aria-controls=\"dealCollapse" + i + j + "\">\n" +
                "                        " + orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].name + "\n" +
                "                    </button>\n" +
                "                </h5>\n" +
                "            </div>\n" +
                "\n" +


                "            <div id=\"dealCollapse" + i + j + "\" class=\"collapse \" aria-labelledby=\"dealHeading" + i + index + j + "\" data-parent=\"#orderItemsAccordion" + index + "\">\n" +
                "                <div class=\"card-body\">" +
                "                   <p>Id: " + orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].id + "</p>" +
                "                   <p>Purchase Category: " + orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].purchaseCategory + "</p>" +
                "                   <p>Amount: " + orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].amount + "</p>" +
                "                   <p>Price Per Unit: " + orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].priceAtStore + "</p>" +
                "                   <p>Total Price: " + (orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].priceAtStore * orderDTO.deals[i].itemsDetailsContainer.itemsDetailsContainer[j].amount) + "</p>" +
                "                   <p>Is Part Of Deal: Yes</p>" +
                "                </div>" +
                "           </div>" +
                "      </div>");
        }
    }
}

function getOrderDetailsLabels(orderDTO){
    var date = new Date(orderDTO.arrivalDate);
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    return "<p>Arrival Date: " + day + '-' + month+ '-' + year + "</p>" +
        "<p>Customer: " + orderDTO.customerUsername +"</p>"+
        "<p>Arrival Location: {" + orderDTO.arrivalLocation.x + ", " + orderDTO.arrivalLocation.y + "}</p>" +
        "<p>Number of Units: " + orderDTO.totalUnits + "</p>" +
        "<p>Items Price: " + orderDTO.itemsPrice + "</p>" +
        "<p>Delivery Price: " + orderDTO.deliveryPrice + "</p>" +
        "<p>Total Price: " + (orderDTO.deliveryPrice + orderDTO.itemsPrice) + "</p>" ;
}

function showOrder(index, orderDTO) {
    $("#ordersAccordion").append(
    "<div class=\"card\">\n" +
    "            <div class=\"card-header\" id=\"orderHeading" + index + "\" >\n" +
    "                <h5 class=\"mb-0\">\n" +
    "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#orderCollapse" + index + "\" aria-expanded=\"false\" aria-controls=\"orderCollapse" + index + "\">\n" +
    "                       Order Id: " + orderDTO.orderId + "\n" +
    "                    </button>\n" +
    "                </h5>\n" +
    "            </div>\n" +
    "\n" +
    "            <div id=\"orderCollapse" + index + "\" class=\"collapse \" aria-labelledby=\"orderHeading" + index + "\" data-parent=\"#ordersAccordion\">\n" +
    "                <div class=\"card-body\">\n" +
    "                    <div id=\"orderAccordion" + index + "\">\n" +
    "                         <div class=\"card\">\n" +
    "                           <div class=\"card-header\" id=\"orderAccordionNumber" + index + "\">\n" +
    "                                <h5 class=\"mb-0\">\n" +
    "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#orderDetails" + index + "\" aria-expanded=\"false\" aria-controls=\"orderDetails" + index + "\">\n" +
    "                                        Details\n" +
    "                                    </button>\n" +
    "                                </h5>\n" +
    "                           </div>\n" +
    "\n" +
    "                            <div id=\"orderDetails" + index + "\" class=\"collapse\" aria-labelledby=\"orderAccordionNumber" + index + "\" data-parent=\"#orderAccordion" + index + "\">\n" +
    "                                <div class=\"card-body\">\n" +
    "                                    <div id=\"orderDetailsLabels" + index + "\">" +
                                             getOrderDetailsLabels(orderDTO) +
    "                                    </div>"+
    "                                </div>"+
    "                            </div>"+
    "                         </div>"+

        "                         <div class=\"card\">\n" +
        "                           <div class=\"card-header\" id=\"orderItemsAccordionNumber" + index + "\">\n" +
        "                                <h5 class=\"mb-0\">\n" +
        "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#orderItemsDetails" + index + "\" aria-expanded=\"false\" aria-controls=\"orderItemsDetails" + index + "\">\n" +
        "                                        Items\n" +
        "                                    </button>\n" +
        "                                </h5>\n" +
        "                           </div>\n" +
        "\n" +
        "                            <div id=\"orderItemsDetails" + index + "\" class=\"collapse\" aria-labelledby=\"orderItemsAccordionNumber" + index + "\" data-parent=\"#orderAccordion" + index + "\">\n" +
        "                                <div class=\"card-body\">\n" +
        "                                    <div id=\"orderItemsAccordion" + index + "\">" +
                                                     /// filled by both bottom methods
        "                                    </div>"+
        "                                </div>"+
        "                            </div>"+
        "                         </div>"
    );
    fillBoughtItems(orderDTO,index);
    fillDealsItems(orderDTO, index);
}

$(function (){
    $.ajax({
       url: 'getStoreOrderHistory',
        dataType: 'json',
        data: {
            zName: sessionStorage.getItem('zone'),
            storeId: sessionStorage.getItem('storeId')
        },
        success: function (ordersDTO){
           if(ordersDTO.length === 0)
           {
               $("#ordersAccordion").append("<h4 align='center'>No Order Was Made From This Store</h4>")
           }else{
               $.each(ordersDTO || [],showOrder);
           }
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching order history" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" onclick='{window.close();}'>" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
});