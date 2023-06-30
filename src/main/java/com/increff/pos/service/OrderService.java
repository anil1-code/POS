package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dto.helper.OrderItemDtoHelper;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.Pair;
import org.apache.fop.apps.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo add(OrderPojo orderPojo) {
        orderDao.add(orderPojo);
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public Pair<List<OrderPojo>, Pair<List<List<OrderItemPojo>>, List<List<ProductPojo>>>> getAll() {
        List<OrderPojo> orderPojoList = orderDao.getAll();
        List<List<OrderItemPojo>> orderItemPojoList = new ArrayList<>();
        List<List<ProductPojo>> productPojoList = new ArrayList<>();
        for (OrderPojo orderPojo : orderPojoList) {
            Pair<List<OrderItemPojo>, List<ProductPojo>> pairedLists = null;
            try {
                pairedLists = orderItemService.getByOrderId(orderPojo.getId());
            } catch (ApiException e) {
                // can never happen
            }
            orderItemPojoList.add(pairedLists.fst);
            productPojoList.add(pairedLists.snd);
        }
        return new Pair<>(orderPojoList, new Pair<>(orderItemPojoList, productPojoList));
    }

    @Transactional(rollbackFor = ApiException.class)
    public void delete(int id) throws ApiException {
        if (getById(id).getZonedDateTime() == null) {
            orderItemService.deleteByOrderId(id);
            orderDao.delete(id);
        } else {
            // order already placed not cant be deleted
            throw new ApiException("Order already placed and can't be deleted.\n");
        }
    }

    @Transactional(rollbackFor = ApiException.class)
    public void placeOrder(int id) throws ApiException {
        OrderPojo existingPojo = getById(id);
        if (existingPojo == null) {
            // order doesn't exist
            throw new ApiException("No order exists for this ID: " + id);
        }
        if (existingPojo.getZonedDateTime() != null) {
            // order already placed
            throw new ApiException("Order with given ID is already placed");
        }
        Pair<List<OrderItemPojo>, List<ProductPojo>> pairedPojoList = orderItemService.getByOrderId(id);
        if (pairedPojoList.fst.isEmpty()) {
            // empty order cant be placed
            throw new ApiException("Empty order can't be placed");
        }
        int i = 0;
        // reduce the inventory
        StringBuilder errorMsg = new StringBuilder();
        for (OrderItemPojo orderItemPojo : pairedPojoList.fst) {
            InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemPojo.getProductId());
            if (inventoryPojo == null) {
                errorMsg.append("Inventory not added for ").append(pairedPojoList.snd.get(i).getName()).append(".\n");
            } else if (inventoryPojo.getQuantity() < orderItemPojo.getQuantity()) {
                errorMsg.append("Insufficient Inventory for ").append(pairedPojoList.snd.get(i).getName()).append(".\n");
            } else {
                inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            }
            i++;
        }
        if (errorMsg.length() != 0) {
            throw new ApiException(errorMsg.toString());
        }
        existingPojo.setZonedDateTime(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Transactional(readOnly = true)
    public File getOrderInvoice(int orderId) throws ApiException {
        OrderPojo orderPojo = getById(orderId);
        if (orderPojo == null) {
            throw new ApiException("Order doesn't exist.\n");
        }
        ZonedDateTime time = orderPojo.getZonedDateTime();
        if (time == null) {
            throw new ApiException("Order isn't placed yet.\n");
        }
        double total = 0;
        Pair<List<OrderItemPojo>, List<ProductPojo>> pairedPojoList = orderItemService.getByOrderId(orderId);
        for (OrderItemPojo orderItemPojo : pairedPojoList.fst) {
            total += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
        }
        InvoiceData invoiceData = new InvoiceData(OrderItemDtoHelper.convertPojoListToDataList(pairedPojoList.fst, pairedPojoList.snd), time, total, orderId);
        String invoice = "main/resources/Invoice/invoice" + orderId + ".pdf";
        String xml = jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "main/resources/com/increff/pos/invoice.xml");
        File pdfFile = new File("src", invoice);
        try {
            convertToPDF(xsltFile, pdfFile, xml);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
        return pdfFile;
    }

    // HELPER methods, these are supposed to be called from a transactional method
    private static String jaxbObjectToXML(InvoiceData invoiceData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(invoiceData, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void convertToPDF(File xslt, File pdf, String xml) throws ApiException, IOException {
        OutputStream out = null;
        try {
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            out = Files.newOutputStream(pdf.toPath());
            out = new java.io.BufferedOutputStream(out);
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (FOPException e) {

            throw new ApiException(e.getMessage());
        } catch (TransformerException | IOException e) {
            throw new ApiException(e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    public OrderPojo getById(int orderId) {
        return orderDao.getById(orderId);
    }

    public List<OrderPojo> getOrdersBetweenDates(ZonedDateTime start, ZonedDateTime end) {
        return orderDao.getOrdersBetweenDates(start, end);
    }

}
