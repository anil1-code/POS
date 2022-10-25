package com.increff.pos;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.exception.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Transactional
public class ReportDtoTest extends AbstractUnitTest {
    @Autowired
    private ReportDto reportDto;

    @Test
    public void testInventoryReport() {
        reportDto.inventoryReport();
    }

    @Test(expected = ApiException.class)
    public void testSalesReport() throws ApiException {
        try {
            reportDto.salesReport("2023-09-15T16:05:00+05:30[Asia/Kolkata]", "2022-09-15T16:05:00+05:30[Asia/Kolkata]", "b", "c");
        } catch (ApiException e) {
            assertEquals(e.getMessage(), "Start date can't be after end date");
            throw e;
        }
    }

    @Test
    public void testSalesReportValid() {
        try {
            reportDto.salesReport("2021-09-15T16:05:00+05:30[Asia/Kolkata]", "2022-09-15T16:05:00+05:30[Asia/Kolkata]", "b", "c");
        } catch (ApiException e) {
            fail();
        }
    }

}