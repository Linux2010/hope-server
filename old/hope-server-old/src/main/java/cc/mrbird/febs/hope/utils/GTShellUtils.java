package cc.mrbird.febs.hope.utils;

public class GTShellUtils {

    public static String translateText(String text, String targetLang,String sourceLang) throws Exception{
        String homePath=System.getenv("HOPE_HOME");
        String cmdTmp = homePath+"/hope-engine/shell/translate.sh %s %s %s";
        String cmd = String.format(cmdTmp, text, targetLang, sourceLang);
        Process process = Runtime.getRuntime().exec(cmd);
        String[] resArr = ShellUtils.execCmdWithResInfo(process);
        if (process.waitFor() == 0) { //成功更新
            return resArr[0];
        }else {
            throw new Exception(String.format("翻译失败.脚本返回内容:\n%s", resArr[0]+resArr[1]));
        }
    }
}

