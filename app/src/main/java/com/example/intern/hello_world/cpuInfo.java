package com.example.intern.hello_world;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by intern on 16/07/26.
 */
public class CpuInfo {//クラス名は大文字から メソッド名は小文字から
    public int base[] = new int[16];
    public String fileRead() {
        String ondo = heat();
        return ondo + "℃";
    }

    public String[] cpuRead(){
        String [] cmdArgs = {"/system/bin/cat","/proc/stat"};
        ProcessBuilder cmd = new ProcessBuilder(cmdArgs);
        StringBuffer cpuBuffer    = new StringBuffer();
        cpuBuffer = bufferRead(cmd,cpuBuffer);
        String cputext[] = cutOff(cpuBuffer);
        String[] s = calcu(cputext);
        return s;
    }
    private String heat(){
        String ondo = "";
        try {
        File file = new File("/sys/class/thermal/thermal_zone0/temp");
        FileReader filereader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(filereader);
        String line;
        while((line = bufferedReader.readLine())!=null) {
            ondo = line;
        }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ondo;
    }
    private StringBuffer bufferRead(ProcessBuilder cmd,StringBuffer cpuBuffer ){
        try {
            Process process = cmd.start();
            InputStream in  = process.getInputStream();
            byte[] lineBytes = new byte[1024];
            while(in.read(lineBytes) != -1 ) {
                cpuBuffer.append(new String(lineBytes));
            }
            in.close();
        }catch (IOException e) {
        }
        return cpuBuffer;
    }
    private String[] calcu(String cpu[]){
        int after[] = new int[16];
        int result[] = new int[16];
        int sum = 0;
        String[] text = new String[2];
        text[0] = "";
        for (int i = 2; i < cpu.length-1; i++){//2からとるのはcpu  85869 2265 272756 16906192 986 6671 1435 0 0 0　のような数値が入っているため
            after[i] = Integer.parseInt(cpu[i]);
            sum += base[i] - after[i];
            //Log.i("CPU"+i,a[i]);
        }
        for (int i = 2; i < cpu.length-1; i++){
            if(sum==0){
                result[i] = 0;
            }
            else{
                result[i] = 100 * (base[i] - after[i]) / sum;
            }

            text[0] = text[0] + String.valueOf(result[i]) + "%:";
            base[i] = after[i];
        }
        text[1] = String.valueOf(100 - result[5]);
        return text;
    }
    private String[] cutOff(StringBuffer cpuBuffer){
        //Log.i("CPU_VALUES_LINE", String.valueOf(cpuBuffer)+"1");
        String cpuLine = cpuBuffer.toString();
        int start = cpuLine.indexOf("cpu");
        int end = cpuLine.indexOf("cpu0");
        cpuLine = cpuLine.substring(start, end);
        String a[] = cpuLine.split(" ");
        return a;
    }
}
