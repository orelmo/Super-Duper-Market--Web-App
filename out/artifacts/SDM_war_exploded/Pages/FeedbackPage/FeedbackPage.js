function onFeedbackSubmit(storeId){
    var rate;
    var message;
    rate = $('input[name=Rate' + storeId + ']:checked').val();
    message = $("#textarea" + storeId).val();

    $.ajax({
        method: 'post',
        url: "submitFeedback",
        data: {
            username: sessionStorage.getItem('username'),
            zName: sessionStorage.getItem('zone'),
            storeId: storeId,
            rate: rate,
            feedbackMessage: message,
            date: sessionStorage.getItem("date")
        },
        success: function (){
            $("#row"+storeId).remove();
            $("#feedbackPlace"+storeId).remove();
            if($("#storesContainer").children().length === 0)
            {
                setTimeout(function (){
                    window.close();
                },2000);
            }
            $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
                "Your feedback has been submitted" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while submitting feedback" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

function enableSubmit(storeId){
    $("#submitButton" + storeId).prop("disabled", false);
}

function addFeedbackPlace(storeId){
    $(".feedbackPlace").empty();
    $("#feedbackPlace" + storeId).append(
        "<div class='container-fluid '>" +
            "<div class='row '>" +
                "<div class='col '>" +
                    "<input name='Rate" + storeId +"'  type='radio' value='1' onclick='enableSubmit(" +storeId +")'>1 </input>" +
                    "<input name='Rate" + storeId +"'  type='radio' value='2' onclick='enableSubmit(" +storeId +")'>2 </input>" +
                    "<input name='Rate" + storeId +"'  type='radio' value='3' onclick='enableSubmit(" +storeId +")'>3 </input>" +
                    "<input name='Rate" + storeId +"'  type='radio' value='4' onclick='enableSubmit(" +storeId +")'>4 </input>" +
                    "<input name='Rate" + storeId +"'  type='radio' value='5' onclick='enableSubmit(" +storeId +")'>5</input>" +
                "</div>" +
            "</div>"+
            "<div class='row '>" +
               "<div class='col '>" +
        "           <label>Feedback Message:</label>" +
        "       </div>"+
                "<div class='col '>" +
                "   <textarea id='textarea" + storeId +"'></textarea>" +
                "</div>"+
        "    </div> " +
        "   <div class='row '>" +
                "<div class='col ' align='center'>" +
            "       <button disabled id='submitButton" + storeId +"' type=\"button\" class=\"btn btn-outline-light\" onclick='onFeedbackSubmit(" + storeId + ")'>Submit</button>"+
        "       </div>"+
        "   </div>"+
        "</div>"
    );
}

function addStoreToFeedbackableStores(index, store) {
    $("#storesContainer").append(
        "<div id='row" + store.id +"' class='row '>" +
        "   <div class='col '>" +
                store.name +
        "   </div>" +
        "   <div class='col '>" +
        "       <button type=\"button\" class=\"btn btn-outline-light\" onclick='addFeedbackPlace(" + store.id + ")'>Feedback</button>" +
        "   </div>" +
        "</div>"+
        "<div class='row  feedbackPlace' id='feedbackPlace" + store.id + "'>" +
        "</div>"
    );
}

$(function (){
    $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
        "Your order has been submitted" +
        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
        "<span aria-hidden=\"true\">&times;</span>" +
        "</button>" +
        "</div>");

    $.ajax({
       url: 'getFeedbackableStores',
       dataType: 'json',
       data: {
           username: sessionStorage.getItem('username'),
           zName: sessionStorage.getItem('zone')
       },
        success: function (storesDTO){
           $.each(storesDTO || [], addStoreToFeedbackableStores);
        },
        error: function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while fetching stores to feedback" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
})