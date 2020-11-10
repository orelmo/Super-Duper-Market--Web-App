$(function (){
    $.ajax({
        url: "login",
        data: {
          isFirstTry : true
        },
        dataType: 'json',
        success:function(loginDTO){
            if(loginDTO.isError === false){
                window.location.href = "../ZonesPage/ZonesPage.html";
            }else{
                $("html").removeClass("invisible");
            }
        },
        error : function (){
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while login" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
});

$(document).ready(function (){
    $("#loginForm").submit(function(){
        if($("#userName").val().trim() === ""){
            $("#errorMessage").text("No User Name Entered").css({
                'color': 'darkred',
                "background-color": 'lightcoral'
            });
        }else if($('input[name=userType]:checked').length === 0){
            $("#errorMessage").text("User Type Was Not Chosen").css({
                'color': 'darkred',
                "background-color": 'lightcoral'
            });
        }else {
            $.ajax({
                data: $(this).serialize(),
                url: this.action,
                dataType: 'json',
                success: function (loginDTO) {
                    if (loginDTO.isError === true) {
                        $("#userName").text("");
                        $("#errorMessage").text(loginDTO.errorMessage).css({
                            'color': 'darkred',
                            "background-color": 'lightcoral'
                        });
                    } else {
                        sessionStorage.setItem("userType", $('input[name=userType]:checked').val());
                        sessionStorage.setItem("username", $("#userName").val().trim());
                        window.location.href = "../ZonesPage/ZonesPage.html";
                    }
                },
                error : function (){
                    $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                        "An error occurred while login" +
                        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                        "<span aria-hidden=\"true\">&times;</span>" +
                        "</button>" +
                        "</div>");
                }
            });
        }

        return false;
    });
});

function onCustomerClicked(){
    $("#customerRadio").trigger('click');

}function onSellerClicked(){
    $("#sellerRadio").trigger('click');
}