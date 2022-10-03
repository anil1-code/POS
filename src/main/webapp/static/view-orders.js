const head_name = "View Orders";

highlightItem(head_name);

fetchAndDisplayOrders();

function fetchAndDisplayOrders() {
    $.ajax({
        url: 'http://localhost:8000/pos/api/orders',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (data) {
            var $tbody = $('#order-table').find('tbody');
            $tbody.children().remove();
            for (var i in data) {
                var e = data[i];
                var items = JSON.stringify(e.orderItemDataList);
                var order_row = '<tr>'
                    + '<td>' + parseInt(parseInt(i) + parseInt(1)) + '</td>'
                    + '<td>' + e.id + '</td>'
                    + '<td>' + e.dateTime + '</td>';
                if (e.dateTime.charAt(0) != 'N') {
                    order_row += '<td><button type="button" class="btn btn-outline-primary" id="generate-invoice" onclick = "generateInvoice(' + e.id + ')">Download Invoice</button></td>'
                } else {
                    order_row += '<td>'
                        + '<button type="button" class="btn btn-warning" id="edit-order" onclick = "editOrder(' + e.id + ')">Edit</button>'
                        + '&nbsp;'
                        + '<button type="button" class="btn btn-danger" id="delete-order" onclick = "deleteOrder(' + e.id + ')">Delete</button>'
                        + '</td>'
                }
                order_row += '<td><button id="' + e.id + '" class="btn" onclick = showOrderItems(this) data-items=' + items + '><i class="bi bi-eye"></i></button></td>'
                    + '</tr>';
                $tbody.append(order_row);
            }
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function deleteOrder(oid) {
    $.ajax({
        url: 'http://localhost:8000/pos/api/orders/delete/' + oid,
        type: 'DELETE',
        success: function (data) {
            showSuccessToast('Successfully Deleted');
            fetchAndDisplayOrders();
        },
        error: function (response) {
            var response = JSON.parse(response.responseText);
            showErrorToast(response.message);
        }
    });
}

function showOrderItems(btn) {
    var items = btn.getAttribute("data-items");
    var items_jsn = JSON.parse(items);
    $('#show-order-items-modal').modal('show');
    var $tbody = $('#show-order-items-table').find('tbody');
    $tbody.children().remove();
    var total = 0;
    for (var i in items_jsn) {
        var e = items_jsn[i];
        // e.productName
        var row = '<tr>'
            + '<td>' + parseInt(parseInt(i) + parseInt(1)) + '</td>'
            + '<td>' + e.productName + '</td>'
            + '<td>' + e.barcode + '</td>'
            + '<td>' + e.quantity + '</td>'
            + '<td>' + e.sellingPrice + '</td>'
            + '</tr>';
        $tbody.append(row);
        total += e.quantity * e.sellingPrice;
    }
    var row = '<tr>'
        + '<td></td>'
        + '<td></td>'
        + '<td></td>'
        + '<td>Total:</td>'
        + '<td>' + total + '</td>'
        + '</tr>';
    $tbody.append(row);
}

function generateInvoice(id) {
    window.open(
        'http://localhost:8000/pos/api/orders/invoice/' + id,
        '_blank' // <- This is what makes it open in a new window.
    );
    // $.ajax({
    //     url: 'http://localhost:8000/pos/api/orders/invoice/' + id,
    //     contentType: 'application/pdf',
    //     type: 'GET',
    //     success: function (data) {
    //         console.log(data);
    //         showSuccessToast('Successfully generated');
    //     },
    //     error: function (response) {
    //         var response = JSON.parse(response.responseText);
    //         showErrorToast(response.message);
    //     }
    // });
}

function editOrder(id) {
    window.location = base_url + "/place-order/" + id;
}

// TODO: change modal style a bit
// TODO: change place order to edit order