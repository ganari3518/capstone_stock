package com.syu.capstone_stock.util;

import org.zeroturnaround.exec.ProcessExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PythonExec {
    /**
     *
     * @param filename 파이썬 파일명
     * @param args 첫 번째는 인자는 주식 코드
     * @return 출력된 모든 값을 List&lt;String&gt; 값으로 반환
     * 파이썬에서 인자를 받을 때 argv[1:]을 해야하는 이유는 첫 번째 인자가 파일 경로이기 때문임.
     */
    public static List<String> exec(String filename, String ... args) {
        List<String> cmd = new ArrayList<>();
        cmd.add("python");
        cmd.add(System.getProperty("user.dir") + "/src/main/resources/python/" + filename);
        if(args != null){
            Collections.addAll(cmd, args);
        }
        try{
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);

            Process p = pb.start();
            int exitCode = p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

            List<String> res = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
            if(exitCode != 0){
                res.add("exit : "+exitCode);
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ProcessBuilder의 출력 스트림 제한으로 인해 자식 프로세스가 교착 상태에 빠지는걸 방지하기 위한 메소드임.
     * @param filename 파이썬 파일명
     * @param args 매개변수
     * @return 이 메소드는 종료 코드를 반환하지 않고 UTF-8의 JSON출력 값을 반환함.
     */
    public static String execByzt(String filename, String ... args){
        List<String> cmd = new ArrayList<>();
        cmd.add("python");
        cmd.add(System.getProperty("user.dir") + "/src/main/resources/python/" + filename);
        if(args != null){
            Collections.addAll(cmd, args);
        }
        try{
            return new ProcessExecutor().command(cmd)
                    .readOutput(true).execute()
                    .outputUTF8();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(List<String> res){
        for(String s : res){
            System.out.println(s);
        }
    }
}
