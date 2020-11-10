function getRateImages(rate) {
    var starsString = "<p>";
    for(var i = 0; i < rate; ++i){
        starsString += "<img width='25px' src='../../Common/Icons/FullStarIcon.png'>   ";
    }

    for(var i = 0; i < 5 - rate; ++i){
        starsString += "<img width='25px' src='../../Common/Icons/EmptyStarIcon.png'>   ";
    }

    starsString += "</p>";

    return starsString;
}

function addFeedback(index, feedback) {
    var feedbackMessage = "";
    if(feedback.message.length !== 0){
        feedbackMessage = "<p align='left' class='feedbackMessage'>Message: " + feedback.message + "</p>";
    }

    var date = new Date(feedback.date);
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    
    $("#feedbacksContainer").append(
        "<div class=\"row \">\n" +
        "            <div class=\"col \" align=\"center\">" + feedback.customerName + "</div>\n" +
        "            <div class=\"col \" align=\"center\">" + day + "-" + month + "-" + year + "</div>\n" +
        "            <div class=\"col \" align=\"center\">" + feedback.storeName + "</div>\n" +
        "            <div class=\"col \" align=\"center\">"+
        "            <p>Rate: " + getRateImages(feedback.rate) + "</p>" +
                     feedbackMessage +
        "            </div>\n" +
        "</div>"
    );
}

$(function (){
   $.ajax({
       url: 'getSellerZoneFeedbacks',
       dataType: 'json',
       data: {
         username: sessionStorage.getItem("username"),
         zName: sessionStorage.getItem("zone")
       },
       success: function (feedbacksDTO){
           if(feedbacksDTO.length === 0){
               $("#feedbacksContainer").empty();
               $("#feedbacksContainer").append("<h4 align='center'>No feedbacks registered</h4>");
           } else {
               $.each(feedbacksDTO || [], addFeedback);
           }
       },
       error: function (){
           $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
               "An error occurred while fetching feedbacks" +
               "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
               "<span aria-hidden=\"true\">&times;</span>" +
               "</button>" +
               "</div>");

           setTimeout(function (){window.close();}, 2000);
       }
   });
});