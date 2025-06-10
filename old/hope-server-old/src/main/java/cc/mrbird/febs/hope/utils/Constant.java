package cc.mrbird.febs.hope.utils;

/**
 * @author fanta
 */
public class Constant {

    public static final String MESSAGE_SUCCESS="success";
    public static final String MESSAGE_ERROR="error";
    public static final String MESSAGE_BAD_REQUEST="badRequest";

    public static final String VIDEO_NAME_REX = "[^\\u0000-\\uFFFF]";
    public static final String VIDEO_NAME_OR_PATH_REX = "[^\\u0000-\\uFFFF]|[ ]";

    public static final String YOUTUBE_DL_TMP_VIDEO_REX =  "\\.f.+\\.|\\.temp\\.";

    public static final String VIDEO_NAME_OR_PATH_REX_OR_YT_DL_TMP = "[^\\u0000-\\uFFFF]|[ ]|\\.f.+\\.|\\.temp\\.";


}
