package com.ruoyi.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class DownloadConfig {

    @Value("${hope.download.bili.shellPath}")
    private String biliShellPath;

    @Value("${hope.download.bili.afterShellPath}")
    private String biliAfterShellPath;

    @Value("${hope.download.youtube.shellPath}")
    private String youtubeShellPath;

    @Value("${hope.download.youtube.afterShellPath}")
    private String youtubeAfterShellPath;

    // Getter 和 Setter 方法
    public String getBiliShellPath() {
        return biliShellPath;
    }

    public void setBiliShellPath(String biliShellPath) {
        this.biliShellPath = biliShellPath;
    }

    public String getBiliAfterShellPath() {
        return biliAfterShellPath;
    }

    public void setBiliAfterShellPath(String biliAfterShellPath) {
        this.biliAfterShellPath = biliAfterShellPath;
    }

    public String getYoutubeShellPath() {
        return youtubeShellPath;
    }

    public void setYoutubeShellPath(String youtubeShellPath) {
        this.youtubeShellPath = youtubeShellPath;
    }

    public String getYoutubeAfterShellPath() {
        return youtubeAfterShellPath;
    }

    public void setYoutubeAfterShellPath(String youtubeAfterShellPath) {
        this.youtubeAfterShellPath = youtubeAfterShellPath;
    }
}