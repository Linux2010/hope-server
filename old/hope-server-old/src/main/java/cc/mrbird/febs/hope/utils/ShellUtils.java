package cc.mrbird.febs.hope.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {

    private static final Logger log = LoggerFactory.getLogger(ShellUtils.class);

    public static boolean execCmd(String cmd){
        try {
            log.info("cmd info:{}" , cmd);
            Process	process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = stdout.readLine()) != null) {
                log.info(line);
            }
            while ((line = stderr.readLine()) != null) {
                log.info("errors:"+line);
            }

            if(process.waitFor() == 0){
                //成功更新
                return true;

            }else {
                //失败更新
                return false;
            }
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
    }


    public static String execCmdWithResInfo(String cmd) {
        try {
            //执行上传命令并阻塞监听
            Process	process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder info = new StringBuilder();
            StringBuilder error = new StringBuilder();
            while ((line = stdout.readLine()) != null) {
                info.append(line);
                log.info("info:{}", line);
            }
            while ((line = stderr.readLine()) != null) {
                error.append(line);
                error.append("\n");
                log.error("error:{}",line);
            }
            return  info.toString();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public static String[] execCmdWithResInfo(Process process) throws IOException {
        String[] strings = new String[2];
        //执行上传命令并阻塞监听
        BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        StringBuilder info = new StringBuilder();
        StringBuilder error = new StringBuilder();
        while ((line = stdout.readLine()) != null) {
            info.append(line);
            info.append("\n");
            log.info("info:{}", line);
        }
        while ((line = stderr.readLine()) != null) {
            error.append(line);
            error.append("\n");
            log.error("error:{}",line);
        }
        strings[0] = info.toString();
        strings[1] = error.toString();
        return  strings;
    }

}
