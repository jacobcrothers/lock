package binar.box.rest.payment.smartbill;

import binar.box.rest.payment.smartbill.dto.SmartBillPayRequest;
import binar.box.rest.payment.smartbill.dto.SmartBillPayResponse;
import retrofit2.Call;
import retrofit2.http.*;


public interface SmartBillInterface {
    @POST("/SBORO/api/payment")
    Call<SmartBillPayResponse> createPayment(@Body SmartBillPayRequest smartBillPayRequest);
}
