package com.example.hw2p2_netowrks1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private String fileName;

    FileHandler() {
        fileName = "logfile.txt";
    }


    public String writeToFile(String line) {
        String result = "";
        try {
            BufferedWriter file = new BufferedWriter(new FileWriter(fileName, true));
            file.write(line + "\n");
            result = "200";
            file.close();
        } catch (IOException e) {
            return "Something Wrong with write to file!";
        }

        return result;

    }
    List<String> findAllLines() {
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = file.readLine()) != null) {
                lines.add(line);
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void updateOrDeleteLineInFile(String username, char op, String record) {
        try {
            List<String> lines = findAllLines();
            BufferedWriter file = new BufferedWriter(new FileWriter(fileName));
            for (String line : lines) {
                String UName = line.split(",")[0];
                if (!UName.equals(username)) {
                    file.write(line + "\n");
                } else if (op == 'u' || op == 'U') {
                    file.write(record + "\n");
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String findRecord(String username) {
        String result = "404";
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = file.readLine()) != null) {
                String uName = line.split(",")[0];
                if (username.equalsIgnoreCase(uName)) {
                    return line;
                }
            }
            file.close();
        } catch (IOException e) {
            return "Something Wrong while find record by username from file!";
        }
        return result;
    }
}