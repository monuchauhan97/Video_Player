package plugin.adsdk.service.api;

import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

@Obfuscate
public interface AppConfigApi {

    @POST("/get-app.php")
    @FormUrlEncoded
    Call<ListModel> getApp(@Field("pkg_name") String packageName);
}
