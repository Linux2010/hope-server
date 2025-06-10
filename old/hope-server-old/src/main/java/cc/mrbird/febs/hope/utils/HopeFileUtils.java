package cc.mrbird.febs.hope.utils;

import cc.mrbird.febs.hope.domain.DownloadInfo;
import cc.mrbird.febs.hope.domain.FileInfo;
import cc.mrbird.febs.hope.domain.VideoInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class HopeFileUtils {


    /**
     * true:该文件修改日期小于日历日期
     * false：该文件修改日期大于日历日期
     * @param filePath
     * @param calendar
     */
    public static boolean checkFileDate(String filePath , Calendar calendar){
        File file = new File(filePath);
        Date fileCreateDate = getFileCreateDate(file);
        return fileCreateDate.before(calendar.getTime());
    }
    public static Date getFileCreateDate(File file){
        BasicFileAttributes attr = null;
        try {
            Path path =  file.toPath();
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建时间
        Instant instant = attr.creationTime().toInstant();
        return new Date(instant.toEpochMilli());
    }


    public static List<FileInfo> getOtherFileList(String path, int deep){

        ArrayList<FileInfo> list = new ArrayList<FileInfo>();
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        if (array ==null) {
            return list;
        }
        for(int i=0;i<array.length;i++) {
            //如果是文件
            if(array[i].isFile())
            {
                FileInfo vi = new FileInfo();
                String fileName = array[i].getName();
                String filePath = array[i].getPath();
                Integer type = FileTypeUtil.getType(fileName);
                if(5==type){
                    vi.setName(fileName.substring(0,fileName.lastIndexOf(".")));
                    vi.setPath(filePath);
                    list.add(vi);
                }
            }
        }
        return list;
    }


    public static List<FileInfo> getIllegalFileList(String path, int deep){

        ArrayList<FileInfo> list = new ArrayList<FileInfo>();
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for(int i=0;i<array.length;i++) {
            //如果是文件
            if(array[i].isFile()) {
                FileInfo vi = new FileInfo();
                String fileName = array[i].getName();
                String filePath = array[i].getPath();
                Integer type = FileTypeUtil.getType(fileName);
                // 非无效文件 && 匹配非法字符串和空格 && 不匹配.fxxxxx./.temp.的youtube-dl临时文件 && 文件size大于0
                if(5!=type  && !HopeFileUtils.rexMatchFind(Constant.YOUTUBE_DL_TMP_VIDEO_REX,filePath) && HopeFileUtils.rexMatchFind(Constant.VIDEO_NAME_OR_PATH_REX,filePath) && HopeFileUtils.getFileSize(filePath)>0 ){
                    vi.setName(fileName.substring(0,fileName.lastIndexOf(".")));
                    vi.setPath(filePath);
                    list.add(vi);
                }
            }
        }
        return list;
    }


    public static List<FileInfo> getFileInfoList(String path, int deep){

        ArrayList<FileInfo> list = new ArrayList<FileInfo>();
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        if(null == array) {
            return list;
        }
        for(int i=0;i<array.length;i++) {
            //如果是文件
            if(array[i].isFile()) {
                FileInfo vi = new FileInfo();
                String fileName = array[i].getName();
                String filePath = array[i].getPath();
                vi.setName(StringUtils.substringBeforeLast(fileName,"."));
                vi.setPath(filePath);
                list.add(vi);
            }
        }
        return list;
    }

    /**
     * 该方法获取所有size》指定大小的视频且符合title规范的视频，适用于入库视频和上传视频.
     * @param basePath
     * @param skipVideoSize
     * @return
     */
    public static List<VideoInfo> getVideoInfoList(String basePath, Integer skipVideoSize){

        ArrayList<VideoInfo> list = new ArrayList<VideoInfo>();
        // 获得指定文件对象
        File file = new File(basePath);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        if(null == array) {
            return list;
        }
        for(int i=0;i<array.length;i++) {
            //如果是文件
            if(array[i].isFile())
            {
                VideoInfo vi = new VideoInfo();
                String fileName = array[i].getName();
                String filePath = array[i].getPath();
                Integer type = FileTypeUtil.getType(fileName);
                if(3==type && !HopeFileUtils.rexMatchFind(Constant.VIDEO_NAME_OR_PATH_REX_OR_YT_DL_TMP,filePath)&& HopeFileUtils.getFileSize(filePath)>skipVideoSize){
                    vi.setTitle(fileName.substring(0,fileName.lastIndexOf(".")));
                    vi.setThumbnailPath(filePath.substring(0,filePath.lastIndexOf("."))+".webp");
                    vi.setPath(filePath);
                    vi.setOkPath(filePath+".ok");
                    vi.setFileName(fileName);
                    vi.setFileType(fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()));
                    list.add(vi);
                }
            }
        }
        return list;
    }



    /**
     * 该方法获取所有size》0的视频，适用于文件清理
     * @param downloadInfo
     * @param deep
     * @return
     */
    public static List<VideoInfo> getVideoInfoList(DownloadInfo downloadInfo, int deep){

        ArrayList<VideoInfo> list = new ArrayList<VideoInfo>();
        // 获得指定文件对象
        File file = new File(downloadInfo.getDownloadPath());
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        if(null == array) {
            return list;
        }
        for(int i=0;i<array.length;i++) {
            if(array[i].isFile())//如果是文件
            {
                VideoInfo vi = new VideoInfo();
                String fileName = array[i].getName();
                String filePath = array[i].getPath();
                Integer type = FileTypeUtil.getType(fileName);
                if(3==type&& HopeFileUtils.getFileSize(filePath)>0){
                    vi.setTitle(fileName.substring(0,fileName.lastIndexOf(".")));
                    vi.setThumbnailPath(filePath.substring(0,filePath.lastIndexOf("."))+".webp");
                    vi.setPath(filePath);
                    vi.setOkPath(filePath+".ok");
                    list.add(vi);
                }
            }
        }
        return list;
    }


    public static List<String> getFilePath(String path, int deep){

        ArrayList<String> list = new ArrayList<String>();
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        for(int i=0;i<array.length;i++)
        {
            //如果是文件
            if(array[i].isFile()) {
                //path
                list.add(array[i].getPath());
            }
        }
        return list;
    }


    public static   File createFile(String fileName, String data) {
        //参数校验
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        File newFile = new File(fileName);
        try {
            if (!newFile.exists()) {
                //创建文件
                boolean b = newFile.createNewFile();
                if(b){
                    Writer out = new FileWriter(newFile);
                    out.write(data);
                    out.close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return newFile;
    }


    //检查该视频是否可上传
    public static boolean checkExist(String path){
        File file= new File(path);
        return file.exists();
    }
    public static Boolean reName(String oldPath ,String newPath){
        File file = new File(oldPath);
        return file.renameTo(new File(newPath));
    }
    public static long getFileSize(String path){
        File file = new File(path);
        return file.length();
    }

    public static Boolean delete(String path){
        File file = new File(path);
        return file.delete();
    }

    public static String replaceByRexMatch(String rex,String s,String replacement){
        return Pattern.compile(rex).matcher(s).replaceAll(replacement);
    }

    public static Boolean rexMatchFind(String rex ,String s){
        return Pattern.compile(rex).matcher(s).find();
    }

}
