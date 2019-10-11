package binar.box.rest.payment;

import binar.box.dto.payment.SmartBillDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SmartBillInterface {
    @GET("user/repos")
    Call<List<SmartBillDTO>> listRepos(@Header("Authorization") String accessToken,
                                       @Header("Accept") String apiVersionSpec);

    @DELETE("repos/{owner}/{repo}")
    Call<SmartBillDTO> deleteRepo(@Header("Authorization") String accessToken, @Header("Accept") String apiVersionSpec,
                                  @Path("repo") String repo, @Path("owner") String owner);

    @POST("user/repos")
    Call<SmartBillDTO> createRepo(@Body SmartBillDTO repo, @Header("Authorization") String accessToken,
                                  @Header("Accept") String apiVersionSpec,
                                  @Header("Content-Type") String contentType);
}
