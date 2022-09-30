const head_name = "Brands";

highlightItem(head_name);

fetchAndDisplayBrands();

function showBulkUploadDialog() {
    $('#brand-bulk-upload-modal').modal('show');
    $("#process-brand-data").attr("disabled", true);
    $("#download-errors").attr("disabled", true);
    $('#brandFile').val('');
    $('#brandFileName').text('Choose file');
    $('#brandFile').on("change", function () {
        var file = $('#brandFile')[0].files[0].name;
        $('#brandFileName').text(file);
        $("#process-brand-data").removeAttr("disabled");
    });
}

function showBrandModal(button) {
    $('#edit-brand-modal').modal('show');
    $('#brand-update').attr('data-id', button.getAttribute('data-id'));
    $('#inputEditName').val(button.getAttribute('data-bname'));
    $('#inputEditCat').val(button.getAttribute('data-catname'));
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
        if (x == 0) {
            x++;
        } else {
            json += ", ";
        }
        json += JSON.stringify(element);
    });
    json += "]";
    // Make ajax call
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            showSuccessToast("Successfully added");
            $('#brand-bulk-upload-modal').modal('hide');
            fetchAndDisplayBrands();
        },
        error: function (response) {
            $('#download-errors').attr('data-error', JSON.parse(response.responseText).message);
            $("#download-errors").removeAttr("disabled");
            showErrorToast("Something went wrong, download errors for more info.")
        }
    });
}

function downloadErrors(btn) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(btn.getAttribute('data-error')));
    element.setAttribute('download', "errors.txt");

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}

function brandCatChecker(brand, category) {
    if (brand.length < 1) {
        showErrorToast("Brand cannot be empty");
        return true;
    }
    if (category.length < 1) {
        showErrorToast("Category cannot be empty");
        return true;
    }
    if (brand.length > MAX_LENGTH) {
        showErrorToast("Brand cannot have more than " + MAX_LENGTH + " chars");
        return true;
    }
    if (category.length > MAX_LENGTH) {
        showErrorToast("Category cannot have more than " + MAX_LENGTH + " chars");
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
        url: 'http://localhost:8000/pos/api/brands/update/' + $('#brand-update').attr('data-id'),
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            fetchAndDisplayBrands();
            $('#edit-brand-modal').modal('hide');
            showSuccessToast('Successfully updated');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function addBrand() {
    var brand = $("#inputName").val();
    var category = $("#inputCat").val();
    if (brandCatChecker(brand, category)) {
        return;
    }
    var json = JSON.stringify([{
        "brandName": brand,
        "categoryName": category
    }]);
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $("#inputName").val('');
            $("#inputCat").val('');
            showSuccessToast("Successfully added");
            fetchAndDisplayBrands();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
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
                var buttonHtml = '<button class="btn btn-warning" onclick="showBrandModal(this)" data-id="' + e.id + '" data-bname="' + e.brandName + '" data-catname="' + e.categoryName + '">Edit</button>';
                var row = '<tr>'
                    + '<td>' + parseInt((parseInt(i) + parseInt(1))) + '</td>'
                    + '<td>' + e.brandName + '</td>'
                    + '<td>' + e.categoryName + '</td>'
                    + '<td>' + buttonHtml + '</td>'
                    + '</tr>';
                $tbody.append(row);
            }
        },
        error: function (response) {
            showErrorToast("Error while fetching data");
        }
    });
}