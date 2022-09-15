package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.exception.ApiException;
import com.increff.pos.model.data.InvoiceData;
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
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

    public OrderPojo add(OrderPojo orderPojo) throws ApiException {
        orderDao.add(orderPojo);
        return orderPojo;
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getAll() {
        System.out.println("order service");
        return orderDao.getAll();
    }

    public OrderPojo getById(int orderId) {
        return orderDao.getById(orderId);
    }

    public List<OrderPojo> getOrdersBetweenDates(String startDate, String endDate) {
//        System.out.println("Order Service ");
        return orderDao.getOrdersBetweenDates(ZonedDateTime.parse(startDate), ZonedDateTime.parse(endDate));
    }

    @Transactional(readOnly = true)
    public void getOrderInvoice(int orderId, InvoiceData invoiceData) throws ApiException {
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
