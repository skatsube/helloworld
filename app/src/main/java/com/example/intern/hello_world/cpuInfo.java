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
public class cpuInfo {
    public int base[] = new int[15];
    public String fileRead() {
        String ondo ="";
        ondo = Heat(ondo);
        return ondo + "℃";
    }

    public String cpuRead(int num){
        String [] cmdArgs = {"/system/bin/cat","/proc/stat"};
        ProcessBuilder cmd = new ProcessBuilder(cmdArgs);
        StringBuffer cpuBuffer    = new StringBuffer();
        cpuBuffer = BufferRead(cmd,cpuBuffer);
        String a[] = CutOff(cpuBuffer);
        String s = calcu(a,num);
        return s;
    }
    private String Heat(String ondo){
        try {
        File file = new File("/sys/class/thermal/thermal_zone0/temp");
        FileReader filereader = null;
        filereader = new FileReader(file);
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
    private StringBuffer BufferRead(ProcessBuilder cmd,StringBuffer cpuBuffer ){
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
    private String calcu(String a[],int num){
        int b[] = new int[15];
        int c[] = new int[15];
        int sum = 0;
        String s = "";
        for (int i = 2; i < a.length-1; i++){
            b[i] = Integer.parseInt(a[i]);
            sum += base[i] - b[i];
            //Log.i("CPU"+i,a[i]);
        }
        for (int i = 2; i < a.length-1; i++){
            if(sum==0){
                c[i] = 0;
            }
            else{
                c[i] = 100 * (base[i] - b[i]) / sum;
            }

            s = s + String.valueOf(c[i]) + "%:";
            base[i] = b[i];
        }
        if (num == 0){
            return s + "," + String.valueOf(100 - c[5]);
        }
        else {
            //Log.i("CPU--",String.valueOf(c[5]));
            //Log.i("C",s);
            return String.valueOf(100 - c[5]);
        }
    }
    private String[] CutOff(StringBuffer cpuBuffer){
        String cpuLine    =  "";
        cpuLine = cpuBuffer.toString();
        int start = cpuLine.indexOf("cpu");
        int end = cpuLine.indexOf("cpu0");
        cpuLine = cpuLine.substring(start, end);
        String a[] = cpuLine.split(" ");
        return a;
    }
}
