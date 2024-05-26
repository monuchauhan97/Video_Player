package plugin.adsdk.service.api;

import io.michaelrocks.paranoid.Obfuscate;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import plugin.adsdk.BuildConfig;
import plugin.adsdk.service.para.ParanoidInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Obfuscate
public class AppConfigApiService {

    private final AppConfigApi appConfigApi;

    public AppConfigApiService(String baseUrl) {

        Interceptor paranoid = new ParanoidInterceptor();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient configClient = new OkHttpClient.Builder()
                .addInterceptor(paranoid)
                .addInterceptor(logging)
                .build();

        appConfigApi = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(configClient)
                .build()
                .create(AppConfigApi.class);
    }

    public Call<ListModel> getApp(String pkgName) {
        return appConfigApi.getApp(pkgName);
    }
}
