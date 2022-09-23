const head_name = "Products";

highlightItem(head_name);

fetchAndDisplayProductsAndBrands();

function showProductModal(id) {
    $('#edit-product-modal').modal('show');
    var list = $('#brand_cat_id_edit_dropdown');
    list.children('option:not(:first)').remove();
    for (var i in data) {
        var item = '<option value="' + data[i].id + '">' +
            data[i].brandName + ' , ' + data[i].categoryName +
            '</option>';
        list.append(item);
    }
    window.id = id;
}

function productChecker(name, barcode, brandCatId, mrp) {
    if (name.length < 1) {
        alert("Product name cannot be empty");
        return true;
    }
    if (barcode.length < 1) {
        alert("Barcode cannot be empty");
        return true;
    }
    if (mrp == 0) {
        alert("MRP cannot be 0");
        return true;
    }
    if (name.length > 50) {
        alert("Name should not exceed 50 chars.")
        return true;
    }
    if (barcode.length > 50) {
        alert("Barcode should not exceed 50 chars.")
        return true;
    }
    return false;
}

function updateProduct() {
    var name = $('#inputEditName').val();
    var brandCatId = Number($('#brand_cat_id_edit_dropdown').find(":selected").val());
    var mrp = $('#inputEditMrp').val();
    if (productChecker(name, "!", brandCatId, mrp)) {
        return;
    }
    var json = '{'
        + '"name": "' + name
        + '", "barcode": "' + 0
        + '", "brandCategory": "' + brandCatId
        + '", "mrp": "' + mrp
        + '"}';
    $.ajax({
        url: 'http://localhost:8000/pos/api/products/update/' + id,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee updated " + response);
            fetchAndDisplayProductsAndBrands();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
    $('#edit-product-modal').modal('hide');
}

function addProduct() {
    var name = $("#inputName").val();
    var barcode = $("#inputBarcode").val();
    var brandCatId = Number($('#brand_cat_id_dropdown').find(":selected").val());
    var mrp = $("#inputMrp").val();

    if (productChecker(name, barcode, brandCatId, mrp)) {
        return;
    }
    var json = '[{'
        + '"name": "' + name
        + '", "barcode": "' + barcode
        + '", "brandCategory": "' + brandCatId
        + '", "mrp": "' + mrp
        + '"}]';

    $.ajax({
        url: 'http://localhost:8000/pos/api/products/add',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee added " + response);
            fetchAndDisplayProductsAndBrands();
            $("#inputName").val('');
            $("#inputBarcode").val('');
            $("#inputId").val('');
            $("#inputMrp").val('');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
}

function fetchAndDisplayProductsAndBrands() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/brands',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            // refresh the brand category dropdown
            var list = $('#brand_cat_id_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].id + '">' +
                    data[i].brandName + ' , ' + data[i].categoryName +
                    '</option>';
                list.append(item);
            }     
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            alert(response.message);
        }
    });
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
                var buttonHtml = '<button class="btn btn-danger" onclick="deleteBrand(' + e.id + ')">delete</button>'
                buttonHtml += ' <button class="btn btn-warning" onclick="showProductModal(' + e.id + ')">edit</button>';
                var row = '<tr>'
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
            alert(response.message);
        }
    });
}