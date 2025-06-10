package cc.mrbird.febs.hope.utils;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class AzureTransUtils {
    private static String key = "0c0e336314a4431dbcc4db137de4082c";

    // location, also known as region.
    // required if you're using a multi-service or regional (not global) resource. It can be found in the Azure portal on the Keys and Endpoint page.
    private static String location = "eastasia";


    // Instantiates the OkHttpClient.
    OkHttpClient client = new OkHttpClient();
    public String trans(String text,String type) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"Text\": \""+text+"\"}]");
        Request request = new Request.Builder()
                .url("https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&from=en&to=zh-Hans")
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                // location required if you're using a multi-service or regional (not global) resource.
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type","application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
