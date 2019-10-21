package binar.box.rest.payment.smartbill;

import binar.box.rest.payment.smartbill.dto.SmartBillPayRequest;
import binar.box.rest.payment.smartbill.dto.SmartBillPayResponse;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class SmartBillService {
    private String apiURL;
    private String token;
    private String username;

    private static String paymentURL = "/SBORO/api/payment/";

    public SmartBillService(String apiURL, String token, String username) {
        this.apiURL = apiURL;
        this.token = token;
        this.username = username;
    }

    public SmartBillPayResponse sendPaymentToSmartBill(SmartBillPayRequest smartBillPayRequest) throws IOException {

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                    Credentials.basic(username, token));

            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiURL + paymentURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        SmartBillInterface smartBillInterface = retrofit.create(SmartBillInterface.class);
        Call<SmartBillPayResponse> responseCall = smartBillInterface.createPayment(smartBillPayRequest);

        Response<SmartBillPayResponse> response = responseCall.execute();
        return response.body();
    }

}
