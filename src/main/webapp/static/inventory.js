const head_name = "Inventory";

highlightItem(head_name);

fetchAndDisplayInventoryAndProducts();

function showBulkUploadDialog() {
    $('#inventory-bulk-upload-modal').modal('show');
    $("#process-inventory-data").attr("disabled", true);
    $("#download-errors").attr("disabled", true);
    $('#inventoryFile').val('');
    $('#inventoryFileName').text('Choose file');
    $('#inventoryFile').on("change", function () {
        var file = $('#inventoryFile')[0].files[0].name;
        $('#inventoryFileName').text(file);
        $("#process-inventory-data").removeAttr("disabled");
    });
}

function showInventoryModal(button) {
    $('#edit-inventory-modal').modal('show');
    $('#inputEditProduct').prop("readonly", false);
    $('#inputEditProduct').val(button.getAttribute('data-name'));
    $('#inputEditProduct').prop("readonly", true);
    $('#inputEditQuantity').val(button.getAttribute('data-quant'));
    $('#update-inventory').attr('data-id', button.getAttribute('data-id'));
}

function processData() {
    var file = $('#inventoryFile')[0].files[0];
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
        if (x == 0) x++;
        else json += ", ";
        json += JSON.stringify(element);
    });
    json += "]";
    // make ajax call
    $.ajax({
        url: 'http://localhost:8000/pos/api/inventory/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            showSuccessToast("Successfully added");
            $('#inventory-bulk-upload-modal').modal('hide');
            fetchAndDisplayInventoryAndProducts();
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

function inventoryChecker(prod_id, quant) {
    if (!isNonNegInt(prod_id)) {
        showErrorToast('Invalid product id');
        return true;
    }
    if (!isNonNegInt(quant)) {
        showErrorToast('Invalid product quantity');
        return true;
    }
    return false;
}

function updateInventory(btn) {
    var quantity = $("#inputEditQuantity").val();
    if (inventoryChecker(btn.getAttribute('data-id', quantity))) {
        return;
    }
    $.ajax({
        url: 'http://localhost:8000/pos/api/inventory/update/' + btn.getAttribute('data-id'),
        type: 'PUT',
        data: JSON.stringify({
            "productId": btn.getAttribute('data-id'),
            "quantity": quantity
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            fetchAndDisplayInventoryAndProducts();
            $('#edit-inventory-modal').modal('hide');
            showSuccessToast('Successfully updated');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function addInventory() {
    var productId = Number($('#product_id_dropdown').find(":selected").val());
    var quantity = $("#inputQuantity").val();
    if (inventoryChecker(productId, quantity)) {
        return;
    }
    $.ajax({
        url: 'http://localhost:8000/pos/api/inventory/add',
        type: 'POST',
        data: JSON.stringify([{
            "productId": productId,
            "quantity": quantity
        }]),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $("#inputQuantity").val('');
            showSuccessToast("Successfully added");
            fetchAndDisplayInventoryAndProducts();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function fetchAndDisplayInventoryAndProducts() {
    // now fetch the data
    $('#inputQuantity').val('');
    $.ajax({
        url: 'http://localhost:8000/pos/api/products',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            // now show the data
            var list = $('#product_id_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].id + '">' +
                    data[i].name
                '</option>';
                list.append(item);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    })
    $.ajax({
        url: 'http://localhost:8000/pos/api/inventory',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            // now show the data
            var $tbody = $('#inventory-table').find('tbody');
            $tbody.children('tr:not(:first)').remove();
            for (var i in data) {
                var e = data[i];
                var buttonHtml = '<button class="btn btn-warning" onclick="showInventoryModal(this)" ' + 'data-id="' + e.productId + '" data-name="' + e.productName + '" data-quant="' + e.quantity + '">edit</button>';
                var row = '<tr>'
                    + '<td>' + parseInt((parseInt(i) + parseInt(1))) + '</td>'
                    + '<td>' + e.productName + '</td>'
                    + '<td>' + e.barcode + '</td>'
                    + '<td>' + e.quantity + '</td>'
                    + '<td>' + buttonHtml + '</td>'
                    + '</tr>';
                $tbody.append(row);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}