package com.example.writelog.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RootExecution {


    public static boolean canRequestRootPermission() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("id\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = inputReader.readLine()) != null) {
                if (line.contains("uid=0")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isRooted() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            outputStream.writeBytes("id\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            String line;
            while ((line = inputReader.readLine()) != null) {
                if (line.contains("uid=0")) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes(command + "\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}