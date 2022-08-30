var id = 0;
function showBrandModal(id) {
    $('#edit-brand-modal').modal('show')
    window.id = id;
}
function showProductModal(id) {
    $('#edit-product-modal').modal('show')
    window.id = id;
}
function updateProduct() {
    var name = $('#inputEditName').val();
    var brandCatId = $('inputEditBcId').val();
    var mrp = $('inputEditMrp').val();

    var json = '[{'
        + '"name": "' + name
        + '", "barcode": "' + 0
        + '", "brandCategory": "' + brandCatId
        + '", "mrp": "' + mrp
        + '"}]';
    $.ajax({
        url: 'http://localhost:8000/pos/api/products/update/' + id,
        type: 'PUT',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee updated " + response);
            fetchAndDisplayProducts();
        },
        error: function () {
            alert("An error has occurred");
        }
    });
    $('#edit-product-modal').modal('hide');
}
function updateBrand() {
    var brand = $("#inputEditName").val();
    var category = $("#inputEditCat").val();
    var json = '{'
        + '"brandName": "' + brand
        + '", "categoryName": "' + category
        + '"}';
    console.log(json);
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
        error: function () {
            alert("An error has occurred");
      }
    });
    $('#edit-brand-modal').modal('hide');
}
function addBrand() {
    var brand = $("#inputName").val();
    var category = $("#inputCat").val();

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
        },
        error: function () {
            alert("An error has occurred");
        }
    });
}
function addProduct() {
    var name = $("#inputName").val();
    var barcode = $("#inputBarcode").val();
    var brandCatId = $("#inputId").val();
    var mrp = $("#inputMrp").val();

    var json = '[{'
        + '"name": "' + name
        + '", "barcode": "' + barcode
        + '", "brandCategory": "' + brandCatId
        + '", "mrp": "' + mrp
        + '"}]';

    $.ajax({
        url: 'http://localhost:8000/pos/api/products/',
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            console.log("Employee added " + response);
            fetchAndDisplayProducts();
        },
        error: function () {
            alert("An error has occurred");
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
                var buttonHtml = '<button class="btn btn-danger" onclick="deleteBrand(' + e.id + ')">delete</button>'
                buttonHtml += ' <button class="btn btn-warning" onclick="showBrandModal(' + e.id + ')">edit</button>'
                var row = '<tr>'
                    + '<td>' + e.brandName + '</td>'
                    + '<td>' + e.categoryName + '</td>'
                    + '<td>' + buttonHtml + '</td>'
                    + '</tr>';
                $tbody.append(row);
            }
        },
        error: function (request, error) {
            alert("Request: " + JSON.stringify(error));
        }
    });
}
function fetchAndDisplayProducts() {
    // now fetch the data
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
                buttonHtml += ' <button class="btn btn-warning" onclick="showProductModal(' + e.id + ')">edit</button>'
                var row = '<tr>'
                    + '<td>' + e.name + '</td>'
                    + '<td>' + e.barcode + '</td>'
                    + '<td>' + e.brandCategory + '</td>'
                    + '<td>' + e.mrp + '</td>'
                    + '<td>' + buttonHtml + '</td>'
                    + '</tr>';
                $tbody.append(row);
            }
        },
        error: function (request, error) {
            alert("Request: " + JSON.stringify(error));
        }
    });
}
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