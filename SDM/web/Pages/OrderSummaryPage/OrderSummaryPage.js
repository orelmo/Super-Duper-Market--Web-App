function getStoreDetailsLabels(orderDTO, i) {
    var labelsString ="";
    labelsString += "<label>Id: " + orderDTO.boughtItemsStoresIds[i] + "</label></br>";
    labelsString += "<label>PPK: " + orderDTO.boughtItems[i].PPK + "</label></br>";
    labelsString += "<label>Distance From Client: " + orderDTO.boughtItems[i].distanceFromClient + "</label></br>";
    labelsString += "<label>Delivery Price: " + orderDTO.boughtItems[i].deliveryPrice + "</label>";
    return labelsString;
}

function getItemCard(orderDTO, storeIndex, itemIndex){
    var cardString="";
    cardString +=  "              <div class=\"card\">\n" +
        "                           <div class=\"card-header\" id=\"itemNumber" + itemIndex + storeIndex + "\">\n" +
        "                                <h5 class=\"mb-0\">\n" +
        "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#itemDetails" + itemIndex+ storeIndex + "\" aria-expanded=\"false\" aria-controls=\"itemDetails" + itemIndex+ storeIndex + "\">\n" +
        "                                        " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].name + "\n" +
        "                                    </button>\n" +
        "                                </h5>\n" +
        "                           </div>\n" +
        "\n" +
        "                            <div id=\"itemDetails" + itemIndex+ storeIndex + "\" class=\"collapse\" aria-labelledby=\"itemNumber" + itemIndex + storeIndex + "\" data-parent=\"#items" + storeIndex + "\">\n" +
        "                                <div class=\"card-body\">\n" +
        "                                   <label>Id: " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].id + "</label><br/>" +
        "                                   <label>Purchase Category: " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].purchaseCategory + "</label><br/>" +
        "                                   <label>Amount: " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].amount + "</label><br/>" +
        "                                   <label>Price Per Unit: " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].priceAtStore + "</label><br/>" +
        "                                   <label>Total Price: " + orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].priceAtStore
                                                        * orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].amount + "</label><br/>" +
        "                                   <label>Is Part Of Deal: No</label>" +
        "                                </div>"+
        "                            </div>"+
        "                         </div>"
    return cardString;
}

function getItemsCards(orderDTO, storeIndex) {
    var cardsString="";
    for(var itemIndex = 0; itemIndex < orderDTO.boughtItems[storeIndex].itemsDetailsContainer.itemsDetailsContainer.length; ++itemIndex){
        cardsString += getItemCard(orderDTO, storeIndex, itemIndex);
    }
    return cardsString;
}

function fillBoughtItems(orderDTO) {
    for(var i = 0; i < orderDTO.boughtItemsStoresIds.length; ++i){
        $("#storesAccordion").append(
            "<div class=\"card\">\n" +
            "            <div class=\"card-header\" id=\"heading" + i + "\" >\n" +
            "                <h5 class=\"mb-0\">\n" +
            "                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#collapse" + i + "\" aria-expanded=\"false\" aria-controls=\"collapse" + i + "\">\n" +
            "                        " + orderDTO.boughtItemsStoresNames[i] + "\n" +
            "                    </button>\n" +
            "                </h5>\n" +
            "            </div>\n" +
            "\n" +


            "            <div id=\"collapse" + i + "\" class=\"collapse \" aria-labelledby=\"heading" + i + "\" data-parent=\"#storesAccordion\">\n" +
            "                <div class=\"card-body\">\n" +
            "                    <div id=\"storeAccordion" + i + "\">\n" +
            "                         <div class=\"card\">\n" +
            "                           <div class=\"card-header\" id=\"accordionNumber" + i + "\">\n" +
            "                                <h5 class=\"mb-0\">\n" +
            "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#storeDetails" + i + "\" aria-expanded=\"false\" aria-controls=\"storeDetails" + i + "\">\n" +
            "                                        Details\n" +
            "                                    </button>\n" +
            "                                </h5>\n" +
            "                           </div>\n" +
            "\n" +
            "                            <div id=\"storeDetails" + i + "\" class=\"collapse\" aria-labelledby=\"accordionNumber" + i + "\" data-parent=\"#storeAccordion" + i + "\">\n" +
            "                                <div class=\"card-body\">\n" +
            "                                    <div id=\"storeDetails" + i + "\">" +
                                                        getStoreDetailsLabels(orderDTO,i) +
            "                                    </div>"+
            "                                </div>"+
            "                            </div>"+
            "                         </div>"+


            "                         <div class=\"card\">\n" +
            "                           <div class=\"card-header\" id=\"itemsAccordion" + i + "\">\n" +
            "                                <h5 class=\"mb-0\">\n" +
            "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#items" + i + "\" aria-expanded=\"false\" aria-controls=\"items" + i + "\">\n" +
            "                                        Items\n" +
            "                                    </button>\n" +
            "                                </h5>\n" +
            "                           </div>\n" +
            "\n" +
            "                            <div id=\"items" + i + "\" class=\"collapse\" aria-labelledby=\"itemsAccordion" + i + "\" data-parent=\"#storeAccordion" + i + "\">\n" +
            "                                <div class=\"card-body\">\n" +
            "                                    <div id=\"itemsCards" + orderDTO.boughtItemsStoresIds[i] + "\">" +
                                                        getItemsCards(orderDTO,i) +
            "                                    </div>"+
            "                                </div>"+
            "                            </div>"+
            "                         </div>"+
            "                      </div>"+
            "                   </div>" +
            "               </div>" +
            "            </div>");
    }
}

function getStoreIndexInBoughtItems(dealItemsStoreId, orderDTO) {
    for(var i = 0; i< orderDTO.boughtItemsStoresIds.length; ++i){
        if(orderDTO.boughtItemsStoresIds[i] === dealItemsStoreId){
            return i;
        }
    }
    return undefined;
}

function getDealItemCard(orderDTO, dealIndex, itemIndex) {
    var dealCard = "";
    dealCard +=  "              <div class=\"card\">\n" +
        "                           <div class=\"card-header\" id=\"dealNumber" + dealIndex + itemIndex + "\">\n" +
        "                                <h5 class=\"mb-0\">\n" +
        "                                    <button class=\"btn btn-link\" data-toggle=\"collapse\" data-target=\"#itemDetails" + dealIndex + itemIndex + 1 + "\" aria-expanded=\"false\" aria-controls=\"itemDetails" + dealIndex + itemIndex + 1 +"\">\n" +
        "                                        " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].name + "\n" +
        "                                    </button>\n" +
        "                                </h5>\n" +
        "                           </div>\n" +
        "\n" +
        "                            <div id=\"itemDetails" + dealIndex + itemIndex + 1 + "\" class=\"collapse\" aria-labelledby=\"dealNumber" + dealIndex + itemIndex + "\" data-parent=\"#items" + getStoreIndexInBoughtItems(orderDTO.dealItemsStoreIds[dealIndex], orderDTO) + "\">\n" +
        "                                <div class=\"card-body\">\n" +
        "                                   <label>Id: " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].id + "</label><br/>" +
        "                                   <label>Purchase Category: " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].purchaseCategory + "</label><br/>" +
        "                                   <label>Amount: " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].amount + "</label><br/>" +
        "                                   <label>Price Per Unit: " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].priceAtStore + "</label><br/>" +
        "                                   <label>Total Price: " + orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].priceAtStore
                                                     * orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer[itemIndex].amount + "</label><br/>" +
        "                                   <label>Is Part Of Deal: Yes</label>" +
        "                                </div>"+
        "                            </div>"+
        "                         </div>"
    return dealCard;
}

function getDealItemsCards(orderDTO, dealIndex) {
    var dealItemsCards = "";
    for(var itemIndex = 0; itemIndex < orderDTO.deals[dealIndex].itemsDetailsContainer.itemsDetailsContainer.length; itemIndex++){
        dealItemsCards +=  getDealItemCard(orderDTO, dealIndex, itemIndex);
    }
    return dealItemsCards;
}

function fillDealsItems(orderDTO) {
    for(var dealIndex = 0; dealIndex<orderDTO.dealItemsStoreIds.length; ++dealIndex){
        $("#itemsCards" + orderDTO.dealItemsStoreIds[dealIndex]).append(getDealItemsCards(orderDTO,dealIndex));
    }
}

function fillStoresAccordion(orderDTO) {
    fillBoughtItems(orderDTO);
    fillDealsItems(orderDTO);
}

function onCancelClicked(){
    window.close();
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

function onOKClicked(){
    $.ajax({
       method: 'post',
       url: 'executeOrder',
        data: {
            username : sessionStorage.getItem("username"),
            zName : sessionStorage.getItem("zone")
        },
        success: function (){
           window.location.href = '../FeedbackPage/FeedbackPage.html';
        },
        error: function (){
            freezeScreen();

            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while execute the order" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" onclick='{window.close();}'>" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
   $.ajax({
       url : "getCurrentOrder",
       dataType : 'json',
       data : {
           username : sessionStorage.getItem("username"),
           zName : sessionStorage.getItem("zone")
       },
       success : function (orderDTO){
           fillStoresAccordion(orderDTO);

           $("#itemsPriceLabel").text(orderDTO.itemsPrice);
           $("#deliveryPriceLabel").text(orderDTO.deliveryPrice);
           $("#totalPriceLabel").text(orderDTO.totalOrderPrice);
       },
       error : function (){
           freezeScreen();

           $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
               "An error occurred while fetching user current order" +
               "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\" onclick='{window.close();}'>" +
               "<span aria-hidden=\"true\">&times;</span>" +
               "</button>" +
               "</div>");
       }
   });
});