package plugin.adsdk.service.para;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ParanoidInterceptor implements Interceptor {

    //private static final String TAG = "ParanoidInterceptor";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        //Log.d(TAG, "intercept: ===============PARANOID RESPONSE===============");

        Response response = chain.proceed(chain.request());
        if (response.isSuccessful()) {
            Response.Builder newResponse = response.newBuilder();
            String contentType = response.header("Content-Type");
            if (contentType == null || contentType.isEmpty()) contentType = "application/json";

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Null Body While Paranoid Parsing");
            }

            String responseString = body.string();
            String decryptedString = "";
            try {
                decryptedString = Paranoid.decrypt(responseString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Log.d(TAG, "intercept: Response string => " + responseString);
            //Log.d(TAG, "intercept: PARANOID BODY=> " + decryptedString);

            newResponse.body(ResponseBody.create(decryptedString, MediaType.parse(contentType)));
            return newResponse.build();
        }
        return response;
    }
}