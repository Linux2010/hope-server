package cc.mrbird.febs.hope.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class SSH2Util {

    //指定默认编码
    private static String DEFAULT_CHARSET = "UTF-8";

    /**
     * 建立SSH2连接
     * @param host 主机地址
     * @param username 用户名
     * @param password 密码
     * @return Connection
     */
    public static Connection openConnection(String host,String username,String password) {
        try {
            Connection connection = new Connection(host);
            //建立ssh2连接
            connection.connect();
            //检验用户名
            boolean login = connection.authenticateWithPassword(username,password);
            if (login){
                log.info(host + " 连接成功");
                return connection;
            }else {
                throw new RuntimeException(host + " 用户名密码不正确");
            }
        } catch (Exception e) {
            throw new RuntimeException(host +" "+ e);
        }
    }

    /**
     * 执行命令
     * @param connection ssh2连接对象
     * @param command 命令语句
     * @return 执行结果, 封装执行状态和执行结果
     */
    public static ExecCmdResult execCommand(Connection connection,String command){
        ExecCmdResult execCmdResult = new ExecCmdResult();
        Session session = null;
        try{
            session = connection.openSession();
            //执行命令
            session.execCommand(command);
            //解析结果
            String result = parseResult(session.getStdout());
            //解析结果为空，则表示执行命令发生了错误，尝试解析错误出输出
            if (result == null||result.length()==0){
                execCmdResult.setFlag(false);
                result = parseResult(session.getStderr());
            }else {
                execCmdResult.setFlag(true);
            }
            //设置响应结果
            execCmdResult.setResult(result);
            log.info(command + " ==>> " +execCmdResult.getResult());
            return execCmdResult;

        }catch (Exception e){
            log.error("error:{}",e.getMessage(),e);
        }finally {
            if (null != session) session.close();
        }
        return null;
    }

    /**
     * 执行命令
     * @param connection ssh2连接对象
     * @param command 命令语句
     * @return 执行结果, 封装执行状态和执行结果
     */
    public static boolean execCheckNetCommand(Connection connection,String command){
        Session session = null;
        try{
            session = connection.openSession();
            //执行命令
            session.execCommand(command);
            //解析结果
            String result = parseResult(session.getStdout());
            log.info("[{}] ==>> [{}]",command,result);
            return StringUtils.contains(result, "希望");
        }catch (Exception e){
            log.info("[{}] ==>> [{}]",command,e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            if (null != session) session.close();
        }
    }


    /**
     * 执行命令
     * @param connection ssh2连接对象
     * @param command 命令语句
     * @return 执行结果, 封装执行状态和执行结果
     */
    public static boolean execCmdWithStatus(Connection connection,String command){
        Session session = null;
        try{
            session = connection.openSession();
            //执行命令
            session.execCommand(command);
            //解析结果
            String result = parseResult(session.getStdout());
            log.info("[{}] ==>> [{}]",command,result);
            return StringUtils.contains(result, "True");
        }catch (Exception e){
            log.info("[{}] ==>> [{}]",command,e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            if (null != session) session.close();
        }
    }

    public static String parseResult(InputStream inputStream) throws IOException{
        //读取输出流内容
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,DEFAULT_CHARSET));
        StringBuffer resultSB = new StringBuffer();
        String line;
        while((line = br.readLine()) != null){
            resultSB.append(line+"\n");
        }
        //替换换行符
        String result = resultSB.toString().replaceAll("[\\t\\n\\r]", "");
        return result;

    }
}

