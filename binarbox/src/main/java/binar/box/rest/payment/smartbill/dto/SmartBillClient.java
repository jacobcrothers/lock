package binar.box.rest.payment.smartbill.dto;

import lombok.Data;

@Data
public class SmartBillClient {
    private String name;
    private String vatCode;
    private boolean isTaxPayer;
    private String address;
    private String city;
    private String country;
    private String email;
}
