package com.ruoyi.system.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ChannelConfig {

    @Value("${hope.channel.xigua.titleLimit}")
    private String xiguaTitleLimit;

    @Value("${hope.channel.xigua.loginUrl}")
    private String xiguaLoginUrl;

    @Value("${hope.channel.xigua.homeUrl}")
    private String xiguaHomeUrl;

    @Value("${hope.channel.xigua.cookieDomain}")
    private String xiguaCookieDomain;

    // Bili 配置
    @Value("${hope.channel.bili.titleLimit}")
    private String biliTitleLimit;

    @Value("${hope.channel.bili.loginUrl}")
    private String biliLoginUrl;

    @Value("${hope.channel.bili.homeUrl}")
    private String biliHomeUrl;

    @Value("${hope.channel.bili.cookieDomain}")
    private String biliCookieDomain;

    // YouTube 配置
    @Value("${hope.channel.youtube.titleLimit}")
    private String youtubeTitleLimit;

    @Value("${hope.channel.youtube.loginUrl}")
    private String youtubeLoginUrl;

    @Value("${hope.channel.youtube.homeUrl}")
    private String youtubeHomeUrl;

    @Value("${hope.channel.youtube.cookieDomain}")
    private String youtubeCookieDomain;

    // Getter 和 Setter 方法
    public String getXiguaTitleLimit() {
        return xiguaTitleLimit;
    }

    public void setXiguaTitleLimit(String xiguaTitleLimit) {
        this.xiguaTitleLimit = xiguaTitleLimit;
    }

    public String getXiguaLoginUrl() {
        return xiguaLoginUrl;
    }

    public void setXiguaLoginUrl(String xiguaLoginUrl) {
        this.xiguaLoginUrl = xiguaLoginUrl;
    }

    public String getXiguaHomeUrl() {
        return xiguaHomeUrl;
    }

    public void setXiguaHomeUrl(String xiguaHomeUrl) {
        this.xiguaHomeUrl = xiguaHomeUrl;
    }

    public String getXiguaCookieDomain() {
        return xiguaCookieDomain;
    }

    public void setXiguaCookieDomain(String xiguaCookieDomain) {
        this.xiguaCookieDomain = xiguaCookieDomain;
    }

    public String getBiliTitleLimit() {
        return biliTitleLimit;
    }

    public void setBiliTitleLimit(String biliTitleLimit) {
        this.biliTitleLimit = biliTitleLimit;
    }

    public String getBiliLoginUrl() {
        return biliLoginUrl;
    }

    public void setBiliLoginUrl(String biliLoginUrl) {
        this.biliLoginUrl = biliLoginUrl;
    }

    public String getBiliHomeUrl() {
        return biliHomeUrl;
    }

    public void setBiliHomeUrl(String biliHomeUrl) {
        this.biliHomeUrl = biliHomeUrl;
    }

    public String getBiliCookieDomain() {
        return biliCookieDomain;
    }

    public void setBiliCookieDomain(String biliCookieDomain) {
        this.biliCookieDomain = biliCookieDomain;
    }

    public String getYoutubeTitleLimit() {
        return youtubeTitleLimit;
    }

    public void setYoutubeTitleLimit(String youtubeTitleLimit) {
        this.youtubeTitleLimit = youtubeTitleLimit;
    }

    public String getYoutubeLoginUrl() {
        return youtubeLoginUrl;
    }

    public void setYoutubeLoginUrl(String youtubeLoginUrl) {
        this.youtubeLoginUrl = youtubeLoginUrl;
    }

    public String getYoutubeHomeUrl() {
        return youtubeHomeUrl;
    }

    public void setYoutubeHomeUrl(String youtubeHomeUrl) {
        this.youtubeHomeUrl = youtubeHomeUrl;
    }

    public String getYoutubeCookieDomain() {
        return youtubeCookieDomain;
    }

    public void setYoutubeCookieDomain(String youtubeCookieDomain) {
        this.youtubeCookieDomain = youtubeCookieDomain;
    }
}