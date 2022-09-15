const head_name = "Brands";

highlightItem(head_name);

fetchAndDisplayBrands();

function showBulkUploadDialog() {
    $('#brand-bulk-upload-modal').modal('show');
}

function showBrandModal(id) {
    $('#edit-brand-modal').modal('show');
    window.id = id;
}

function processData() {
    var file = $('#brandFile')[0].files[0];
    readFileData(file);
}
function readFileData(file) {
    var config = {
        header: true,
        delimiter: "\t",
        skipEmptyLines: "greedy",
        complete: function (results) {
            uploadRows(results.data);
        }
    }
    Papa.parse(file, config);
}
function uploadRows(fileData) {
    var json = "[";
    var x = 0;
    fileData.forEach(element => {
        if(x == 0) {
            x++;
        } else {
            json += ", ";
        }
        json += JSON.stringify(element);
    });
    json += "]";
    // Make ajax call
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands/',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("data uploaded successfully")
        },
        error: function (response) {
            error_text = JSON.parse(response.responseText).message;
            alert("Something went wrong, download errors for more info.")
        }
    });
}
var error_text = "";
function downloadErrors() {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(error_text));
    element.setAttribute('download', "errors.txt");

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}
function brandCatChecker(brand, category) {
    if (brand.length < 1) {
        alert("Brand cannot be empty");
        return true;
    }
    if (category.length < 1) {
        alert("Category cannot be empty");
        return true;
    }
    if (brand.length > 50) {
        alert("Brand cannot have more than 50 chars");
        return true;
    }
    if (category.length > 50) {
        alert("Category cannot have more than 50 chars");
        return true;
    }
    return false;
}

function updateBrand() {
    var brand = $("#inputEditName").val();
    var category = $("#inputEditCat").val();
    if (brandCatChecker(brand, category)) {
        return;
    }
    var json = '{'
        + '"brandName": "' + brand
        + '", "categoryName": "' + category
        + '"}';
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands/update/' + id,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee update " + response);
            fetchAndDisplayBrands();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
    $('#edit-brand-modal').modal('hide');
}

function addBrand() {
    var brand = $("#inputName").val();
    var category = $("#inputCat").val();
    if (brandCatChecker(brand, category)) {
        return;
    }
    var json = '[{'
        + '"brandName": "' + brand
        + '", "categoryName": "' + category
        + '"}]';
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands/',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee added " + response);
            fetchAndDisplayBrands();
            $("#inputName").val('');
            $("#inputCat").val('');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
}
function fetchAndDisplayBrands() {
    // now fetch the data
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            // now show the data
            var $tbody = $('#brand-table').find('tbody');
            $tbody.children('tr:not(:first)').remove();
            for (var i in data) {
                var e = data[i];
                var buttonHtml = '<button class="btn btn-danger" onclick="deleteBrand(' + e.id + ')">delete</button>';
                buttonHtml += ' <button class="btn btn-warning" onclick="showBrandModal(' + e.id + ')">edit</button>';
                var row = '<tr>'
                    + '<td>' + e.brandName + '</td>'
                    + '<td>' + e.categoryName + '</td>'
                    + '<td>' + buttonHtml + '</td>'
                    + '</tr>';
                $tbody.append(row);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
}