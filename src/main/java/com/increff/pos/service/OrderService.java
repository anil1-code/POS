package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
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
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemService orderItemService;
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo add(OrderPojo orderPojo, List<Integer> orderItemIds) throws ApiException {
        orderDao.add(orderPojo);
        for (Integer id : orderItemIds) {
            orderItemService.updateOrderId(id, orderPojo.getId());
        }
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getAll() {
        return orderDao.getAll();
    }

    public OrderPojo getById(int orderId) {
        return orderDao.getById(orderId);
    }

    @Transactional(readOnly = true)
    public void getOrderInvoice(int orderId) throws ApiException {
        List<OrderItemData> orderItemDataList = orderItemService.getByOrderId(orderId);
        ZonedDateTime time = getById(orderId).getZonedDateTime();
        double total = 0.;
        for (OrderItemData itemData : orderItemDataList) {
            total += itemData.getQuantity() * itemData.getSellingPrice();
        }
        InvoiceData invoiceData = new InvoiceData(orderItemDataList, time, total, orderId);
        String invoice = "main/resources/Invoice/invoice" + orderId + ".pdf";
        String xml = jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "main/resources/com/increff/pos/invoice.xml");
        File pdfFile = new File("src", invoice);
        try {
            convertToPDF(xsltFile, pdfFile, xml);
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

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
            System.out.println("FOP Exception");
            throw new ApiException(e.getMessage());
        } catch (TransformerException e) {
            System.out.println("Transformer Exception");
            throw new ApiException(e.getMessage());
        } catch (IOException e) {
            throw new ApiException(e.getMessage());
        } finally {
            out.close();
        }
    }
}
