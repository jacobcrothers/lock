package binar.box.rest.payment.smartbill.dto;

import lombok.Data;

import java.util.List;

@Data
public class SmartBillPayRequest {
    private String companyVatCode;
    SmartBillClient client;
    private String issueDate;
    private String currency;
    private String language;
    private float exchangeRate;
    private float precision;
    private float value;
    private String type;
    private boolean isCash;
    private boolean useInvoiceDetails;
    private List<Invoice> invoicesList;
}

