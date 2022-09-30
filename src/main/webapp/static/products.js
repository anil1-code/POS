const head_name = "Products";

highlightItem(head_name);

fetchAndDisplayProductsAndBrands();

function showBulkUploadDialog() {
    $('#product-bulk-upload-modal').modal('show');
    $("#process-product-data").attr("disabled", true);
    $("#download-errors").attr("disabled", true);
    $('#productFile').val('');
    $('#productFileName').text('Choose file');
    $('#productFile').on("change", function () {
        var file = $('#productFile')[0].files[0].name;
        $('#productFileName').text(file);
        $("#process-product-data").removeAttr("disabled");
    });
}

function processData() {
    var file = $('#productFile')[0].files[0];
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
    $.ajax({
        url: 'http://localhost:8000/pos/api/products/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            showSuccessToast("Successfully added");
            $('#product-bulk-upload-modal').modal('hide');
            fetchAndDisplayProductsAndBrands();
        },
        error: function (response) {
            $('#download-errors').attr('data-error', JSON.parse(response.responseText).message);
            $("#download-errors").removeAttr("disabled");
            showErrorToast("Something went wrong, download errors for more info.");
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

function showProductModal(id, button) {
    $('#edit-product-modal').modal('show');
    var list = $('#brand_cat_id_edit_dropdown');
    list.children('option:not(:first)').remove();
    var data = JSON.parse(button.getAttribute("data-items"));
    for (var i in data) {
        var item = '<option value="' + data[i].id + '">' +
            data[i].brandName + ' , ' + data[i].categoryName +
            '</option>';
        list.append(item);
    }
    $('#product-update').attr('data-id', id);
    $('#inputEditName').val(button.getAttribute('data-bname'));
    $('#inputEditCat').val(button.getAttribute('data-catname'));
}

function productChecker(name, barcode, brandCatId, mrp) {
    if (name.length < 1) {
        showErrorToast("Product name cannot be empty");
        return true;
    }
    if (barcode.length < 1) {
        showErrorToast("Barcode cannot be empty");
        return true;
    }
    if (Number.isNaN(brandCatId) || brandCatId < 0) {
        showErrorToast("Invalid Brand Category");
        return true;
    }
    if (!isNonNegNum(mrp)) {
        showErrorToast("Invalid MRP");
        return true;
    }
    if (name.length > 30) {
        showErrorToast("Name should not exceed 30 chars.")
        return true;
    }
    if (barcode.length > 30) {
        showErrorToast("Barcode should not exceed 30 chars.")
        return true;
    }
    return false;
}

function updateProduct() {
    var name = $('#inputEditName').val();
    var brandCatId = Number($('#brand_cat_id_edit_dropdown').find(":selected").val());
    var mrp = Number($('#inputEditMrp').val());
    if (productChecker(name, "1", brandCatId, mrp)) {
        return;
    }
    var json = JSON.stringify({
        "name": name,
        "barcode": "barcode",
        "brandCategory": brandCatId,
        "mrp": mrp
    });
    $.ajax({
        url: 'http://localhost:8000/pos/api/products/update/' + $('#product-update').attr('data-id'),
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            fetchAndDisplayProductsAndBrands();
            $('#edit-product-modal').modal('hide');
            showSuccessToast('Successfully updated');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function addProduct() {
    var name = $("#inputName").val();
    var barcode = $("#inputBarcode").val();
    var brandCatId = Number($('#brand_cat_id_dropdown').find(":selected").val());
    var mrp = $("#inputMrp").val();
    if (productChecker(name, barcode, brandCatId, mrp)) {
        return;
    }
    var json = JSON.stringify([{
        "name": name,
        "barcode": barcode,
        "brandCategory": brandCatId,
        "mrp": mrp
    }]);
    $.ajax({
        url: 'http://localhost:8000/pos/api/products/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $("#inputName").val('');
            $("#inputBarcode").val('');
            $("#inputMrp").val('');
            showSuccessToast("Successfully added");
            fetchAndDisplayProductsAndBrands();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function fetchAndDisplayProductsAndBrands() {
    var brand_cat_str = "";
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            // refresh the brand category dropdown
            brand_cat_str = JSON.stringify(data);
            var list = $('#brand_cat_id_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].id + '">' +
                    data[i].brandName + ' , ' + data[i].categoryName +
                    '</option>';
                list.append(item);
            }
            $.ajax({
                url: 'http://localhost:8000/pos/api/products',
                type: 'GET',
                data: {},
                dataType: 'json',
                success: function (data) {
                    // now show the data
                    var $tbody = $('#product-table').find('tbody');
                    $tbody.children('tr:not(:first)').remove();
                    for (var i in data) {
                        var e = data[i];
                        var buttonHtml = '<button class="btn btn-warning" data-items =' + brand_cat_str + ' onclick="showProductModal(' + e.id + ', this)">Edit</button>';
                        var row = '<tr>'
                            + '<td>' + parseInt((parseInt(i) + parseInt(1))) + '</td>'
                            + '<td>' + e.name + '</td>'
                            + '<td>' + e.barcode + '</td>'
                            + '<td>' + e.brandName + '</td>'
                            + '<td>' + e.categoryName + '</td>'
                            + '<td>' + e.mrp + '</td>'
                            + '<td>' + buttonHtml + '</td>'
                            + '</tr>';
                        $tbody.append(row);
                    }
                },
                error: function (response) {
                    var response = JSON.parse(response.responseText);
                    showErrorToast("Error while fetching products data");
                }
            });
        },
        error: function (response) {
            showErrorToast("Error while fetching brands data");
        }
    });
}