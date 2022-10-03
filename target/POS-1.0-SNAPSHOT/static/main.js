function highlightItem(head_name) {
    let elements = document.getElementsByClassName('nav-link');
    Array.from(elements).forEach(element => {
        console.log(element.className);
        console.log(element);
        if (element.text == head_name) {
            element.classList.add("active");
        } else {
            element.classList.remove("active");
        }
    });
}

var base_url = "http://localhost:8000/pos";
var MAX_LENGTH = 30;

function createNewOrder() {
    // create an ajax call to create new order
    $.ajax({
        url: 'http://localhost:8000/pos/api/orders/add',
        type: 'POST',
        data: "",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            window.location = base_url + "/place-order/" + response.id;
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function showSuccessToast(message) {
    $('#infoToast').toast({ autohide: true, delay: 3000 });
    $("#infoToast").css('background-color', 'lightgreen');
    $("#infoToast .toast-body span").text(message);
    $("#infoToast .toast-body span").css("fontSize", "20px");
    $('#infoToast').toast('show');
}

function showErrorToast(message) {
    $('#infoToast').toast({ autohide: false });
    $("#infoToast").css('background-color', 'red');
    $("#infoToast .toast-body span").text(message);
    $("#infoToast .toast-body span").css("fontSize", "16px");
    $('#infoToast').toast('show');
}

function isNonNegInt(num) {
    num = String(num);
    const DIGIT_EXPRESSION = /^\d$/;
    const isDigit = (character) => {
        return character && DIGIT_EXPRESSION.test(character);
    };
    for (var i = 0; i < num.length; i++) {
        if (!isDigit(num.charAt(i))) {
            return false;
        }
    }
    return true;
}


function isNonNegNum(num) {
    num = String(num);
    var cnt = 0;
    const DIGIT_EXPRESSION = /^\d$/;
    const isDigit = (character) => {
        return character && DIGIT_EXPRESSION.test(character);
    };
    for (var i = 0; i < num.length; i++) {
        var ch = num.charAt(i);
        if (ch == '.') {
            cnt++;
        } else if (!isDigit(ch)) {
            return false;
        }
    }
    return cnt < 2;
}

function brandReport() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands',
        type: 'GET',
        success: function (data) {
            var tsv_content = 'Brand\tCategory\n';
            data.forEach(row => {
                tsv_content += row.brandName + '\t' + row.categoryName + '\n';
            });
            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/tsv;charset=utf-8,' + encodeURIComponent(tsv_content));
            element.setAttribute('download', "brandReport.tsv");
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function inventoryReport() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/reports/inventory',
        type: 'GET',
        success: function (data) {
            var tsv_content = 'Brand\tCategory\tQuantity\n';
            data.inventoryReportSingleRowDataList.forEach(row => {
                tsv_content += row.brand + '\t' + row.category + '\t' + row.quantity + '\n';
            });
            var element = document.createElement('a');
            element.setAttribute('href', 'data:text/tsv;charset=utf-8,' + encodeURIComponent(tsv_content));
            element.setAttribute('download', "inventoryReport.tsv");
            element.style.display = 'none';
            document.body.appendChild(element);
            element.click();
            document.body.removeChild(element);
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function salesReport() {
    console.log("sales");

}