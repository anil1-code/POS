const head_name = "Place Order";

highlightItem(head_name);

function getOrderId() {
    console.log(window.location.href);
    var arr = window.location.href.split('/');
    return arr.at(-1);
}

fetchAndDisplayOrderItemsAndProducts();

function fetchAndDisplayOrderItemsAndProducts() {
    $('#inputQuantity').val('');
    $('#inputMrp').val('');
    $.ajax({
        url: 'http://localhost:8000/pos/api/products',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            var list = $('#product_id_dropdown');
            list.children('option:not(:first)').remove();
            for (var i in data) {
                var item = '<option value="' + data[i].id + '">' +
                    data[i].name + '(' + data[i].barcode +
                    ')</option>';
                list.append(item);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
    $.ajax({
        url: 'http://localhost:8000/pos/api/order-items/order/' + getOrderId(),
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            var $tbody = $('#order-item-table').find('tbody');
            $tbody.children('tr:not(:first)').remove();
            for (var i in data) {
                var e = data[i];
                var row = '<tr>'
                    + '<td>' + parseInt((parseInt(i) + parseInt(1))) + '</td>'
                    + '<td scope="col">' + e.productName + '</td>'
                    + '<td scope="col">' + e.barcode + '</td>'
                    + '<td scope="col">' + e.quantity + '</td>'
                    + '<td scope="col">' + e.sellingPrice + '</td>'
                    + '<td scope="col"><button type="button" class="btn btn-warning" onclick=showOrderItemModal(this) data-name="' + e.productName + '" data-oi-id="' + e.orderItemId + '" data-quant="' + e.quantity + '" data-sp="' + e.sellingPrice + '" data-pid="' + e.productId + '">Edit</button>'
                    + '&nbsp;<button type="button" class="btn btn-danger" onclick=deleteOrderItem(' + e.orderItemId + ')>Delete</button></td>'
                    + '</tr>'
                $tbody.append(row);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function showOrderItemModal(button) {
    $('#edit-order-item-modal').modal('show');
    $('#inputEditProduct').prop("readonly", false);
    $('#inputEditProduct').val(button.getAttribute('data-name'));
    $('#inputEditProduct').prop("readonly", true);
    $('#order-item-update').attr("data-oi-id", button.getAttribute('data-oi-id'));
    $('#order-item-update').attr("data-pid", button.getAttribute('data-pid'));
    $('#inputEditQuantity').val(button.getAttribute('data-quant'));
    $('#inputEditSP').val(button.getAttribute('data-sp'));
}

function updateOrderItem(button) {
    var quantity = $('#inputEditQuantity').val();
    var oiid = button.getAttribute('data-oi-id');
    var pid = button.getAttribute('data-pid');
    var sp = $("#inputEditSP").val();
    if (orderItemChecker(pid, quantity, sp)) {
        return;
    }
    $.ajax({
        url: 'http://localhost:8000/pos/api/order-items/update/' + oiid,
        type: 'PUT',
        data: JSON.stringify({
            "orderId": 0, // any dummy value
            "productId": pid, // any dummy value
            "quantity": quantity,
            "sellingPrice": sp
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            $('#edit-order-item-modal').modal('hide');
            fetchAndDisplayOrderItemsAndProducts();
            showSuccessToast('Successfully updated');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function deleteOrderItem(oiid) {
    $.ajax({
        url: 'http://localhost:8000/pos/api/order-items/delete/' + oiid,
        type: 'DELETE',
        success: function (data) {
            fetchAndDisplayOrderItemsAndProducts();
            showSuccessToast('Successfully deleted');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function clearAll() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/order-items/delete/order/' + getOrderId(),
        type: 'DELETE',
        success: function (data) {
            fetchAndDisplayOrderItemsAndProducts();
            showSuccessToast('Successfully Cleared');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function orderItemChecker(pid, quantity, sp) {
    // TODO: to be implemented
    if (!isNonNegInt(pid)) {
        showErrorToast('Invalid Product ID');
        return true;
    }
    if (!isNonNegInt(quantity)) {
        showErrorToast('Invalid Product Quantity');
        return true;
    }
    if (!isNonNegNum(sp)) {
        showErrorToast('Invalid Selling Price');
        return true;
    }
    return false;
}

function addOrderItem() {
    var pid = Number($('#product_id_dropdown').find(":selected").val());
    var quantity = $("#inputQuantity").val();
    var sp = $("#inputSP").val();
    if (orderItemChecker(pid, quantity, sp)) {
        return;
    }
    $.ajax({
        url: 'http://localhost:8000/pos/api/order-items/add',
        type: 'POST',
        data: JSON.stringify({
            "orderId": getOrderId(),
            "productId": pid,
            "quantity": quantity,
            "sellingPrice": sp
        }),
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            fetchAndDisplayOrderItemsAndProducts();
            showSuccessToast('Successfully Added');
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function placeOrder() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/orders/place/' + getOrderId(),
        type: 'PUT',
        data: "",
        headers: {
            'Content-Type': 'application/json'
        },
        success: function (response) {
            // clear the table
            showSuccessToast('Successfully Placed');
            window.location = base_url + "/view-orders";
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}