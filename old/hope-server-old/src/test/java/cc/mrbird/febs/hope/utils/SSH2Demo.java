package cc.mrbird.febs.hope.utils;

import ch.ethz.ssh2.Connection;

public class SSH2Demo {

    public static void main(String[] args) {
        try {
            String host = "hope01";
            String username = "hope";
            String password = "hope";
            Connection connection = SSH2Util.openConnection(host,username,password);
            String cpuInfo = "cat /proc/cpuinfo | grep \"cpu cores\" | uniq"; //服务器核数
            String ls = "ls";
            String rm =  "rm -rf /Volumes/One/despair/塞尔达达人Zeldadaren/【薩爾達傳說︰曠野之息】#23_到處遊逛，找種子、解任務、收集素材、打打人馬或其他.mp4";
            ExecCmdResult cup = SSH2Util.execCommand(connection,rm);
            System.out.println(cup.getResult());
            System.out.println(cup.getFlag());
            connection.close();
        }
        catch (Exception a){
            a.printStackTrace();
        }
    }
}
