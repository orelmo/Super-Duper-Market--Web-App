$(function (){
    if(sessionStorage.getItem("userType")==="Seller"){
        $("#depositDiv").addClass("invisible");
    }

    getTransactions();
});

function addTransactionToTable(index, transaction) {
    var date = new Date(transaction.transactionDate);
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    $("#transactions").append("<div class=\"row  table-row\">" +
        "                        <div class=\"col \">" + transaction.transactionType + "</div>" +
        "                        <div class=\"col \">" + day + '/' + month + '/' + year + "</div>" +
        "                        <div class=\"col \">" + transaction.transactionAmount + "</div>" +
        "                        <div class=\"col \">" + transaction.preBalance + "</div>" +
        "                        <div class=\"col \">" + transaction.postBalance + "</div>" +
        "                    </div>");

    $("#balanceLabel").text(transaction.postBalance);
}

function getTransactions() {
    $("#transactions").empty();

    $.ajax({
        url: "getTransactions",
        dataType: "json",
        success: function (transactionsDTO) {
            if(transactionsDTO.length === 0){
                $("#transactions").append(
                    "<h4 align='center'>No Transactions So Far</h4>"
                )
            } else {
                $.each(transactionsDTO || [], addTransactionToTable);
            }
        },
        error: function () {
            $("#fetchTransactionsError").empty();

            $("#fetchTransactionsError").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "An error occurred while loading transactions" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}

$(function (){
   setInterval(getTransactions,3000);
});

function submitNewDeposit() {
    $("#depositAmountErrorDiv").empty();
    $("#depositDateErrorDiv").empty();
    if ($("#depositAmount").val() === null || $("#depositAmount").val() <= 0) {
        $("#depositAmountErrorDiv").append("<label class='errorMessage'>Amount must be positive</label><br>");
        $("#balanceRow").removeClass("first-row").addClass("error-row");
        return;
    }
    if ($("#depositDate").val() === "") {
        $("#depositDateErrorDiv").append("<label class='errorMessage'>Date must be entered</label><br>");
        $("#balanceRow").removeClass("first-row").addClass("error-row");
        return;
    }

    $("#balanceRow").removeClass("error-row").addClass("first-row");

    $.ajax({
        method: 'post',
        url: 'submitDeposit',
        data: {
            amount: $("#depositAmount").val(),
            date: $("#depositDate").val()
        },
        success: function () {
            $("#alertPlace").append("<div class='alert alert-success fade show alert-dismissible' style='margin: auto'>" +
                "Deposit Succeeded" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");

            $("#depositAmount").val("");
            $("#depositDate").val("");
        },
        error: function () {
            $("#alertPlace").append("<div class='alert alert-danger fade show alert-dismissible' style='margin: auto'>" +
                "Deposit Failed" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                "<span aria-hidden=\"true\">&times;</span>" +
                "</button>" +
                "</div>");
        }
    });
}