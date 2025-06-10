package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.hope.domain.Translation;
import cc.mrbird.febs.hope.domain.TranslationResponse;
import cc.mrbird.febs.hope.service.TransService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;


@Service
@Slf4j
public class TransServiceImpl implements TransService {

    private static String key = "0c0e336314a4431dbcc4db137de4082c";

    private static String location = "eastasia";

    Gson gson = new Gson();


    @Autowired
    OkHttpClient okHttpClient;

    @Override
    public String azureTrans(String text, String targetLang) {
        log.info("执行azureTrans,text:[{}],targetLang:[{}]",text,targetLang);
        try{
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    "[{\"Text\": \""+text+"\"}]");
            Request request = new Request.Builder()
                    .url("https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&to="+targetLang)
                    .post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", key)
                    // location required if you're using a multi-service or regional (not global) resource.
                    .addHeader("Ocp-Apim-Subscription-Region", location)
                    .addHeader("Content-type","application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            // 解析 JSON 字符串
            String jsonString = response.body().string();
            log.info("azureTrans执行结果jsonString: {}",jsonString);
            Type listType = new TypeToken<List<TranslationResponse>>(){}.getType();
            List<TranslationResponse> responses = gson.fromJson(jsonString, listType);
            String res = null;
            // 提取 text 的值
            if (!responses.isEmpty()) {
                List<Translation> translations = responses.get(0).getTranslations();
                if (!translations.isEmpty()) {
                    res = translations.get(0).getText();
                }
            }
            return  res;
        }catch (Exception e){
            log.error("执行azureTrans失败，error-msg:[{}]",e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
