package cc.mrbird.febs.hope.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoInfo  implements Serializable {
    private static final long serialVersionUID = 639649551831707956L;

    private String title;
    private String path;
    private String thumbnailPath;
    private String okPath;
    private String fileName;
    private String fileType;
}
