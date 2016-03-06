package com.firstlinesoftware.delivery.calc;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.Product;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.storage.map.Storage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class ImportProductCalcTest {

    @Test
    public void testImportCost(){
        Storage storage  = new Storage();
        storage.open();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProduct(new Product("6815 10 900 9", "Product 1"));
        productInfo.setCost(100d);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setReceiver("ru");
        paymentInfo.setType(PaymentInfo.PaymentType.imp);
        double payment = new ImportProductCalc(storage).calculate(productInfo, paymentInfo);
        System.out.println(payment);
        assertEquals(16d, payment,  1e-6);

    }
}
