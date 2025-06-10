package cc.mrbird.febs.job.service.impl;

import cc.mrbird.febs.job.service.WechatAlertService;
import cc.mrbird.febs.job.util.OkHttpClientUtils;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class WechatAlertServiceImpl implements WechatAlertService {

    private final String URL="https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    @Autowired
    private OkHttpClientUtils okHttpClientUtils;


    @Override
    public String sendAsText(String key, String content) {
        try{
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("msgtype","text");
            JsonObject contentJson = new JsonObject();
            contentJson.addProperty("content",content);
            jsonObject.add("text", contentJson);

            return okHttpClientUtils.postJson(String.format(URL, key),jsonObject.toString());
        }catch (Exception e){
            log.error("wechat sendAsText error :[{}]",e.getMessage());
            return null;
        }

    }
}
