package binar.box.rest.payment.smartbill;

public enum SmartBillPaymentType {
    CARD("Card"), CEC("CEC"), BILET("Bilet ordin"), ORDIN("Ordin plata"), MANDAT("Mandat postal"), ALTELE("Alta incasare");

    private final String name;

    SmartBillPaymentType(String name) {
        this.name = name;
    }
}
