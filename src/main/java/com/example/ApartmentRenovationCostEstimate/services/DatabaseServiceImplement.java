package com.example.ApartmentRenovationCostEstimate.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseServiceImplement implements DatabaseService {
    //PROBA STWORZENIA FUNKCJI BACKUP/RESTORE DATABASE -> NARAZIE NIE DZIAŁĄ.

    private static String BACKUP_FOLDER = "C:/Users/Damian/Java/Projects/DATABASE/backups";

    @Override
    public int performBackup() throws IOException, InterruptedException{
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File directory = new File(BACKUP_FOLDER);
        if(!directory.exists()) {
            boolean resultOfCreatingDirectory = directory.mkdirs();
            if (resultOfCreatingDirectory) {
                System.out.println("Folder was created successfully.");
            } else {
                System.out.println("Failed to create folder.");
            }
        }
        //=========================================================================================
        String filePath = BACKUP_FOLDER + "/" + date + "backup.sql";
        String dumpCommand = "mariadb-dump -u root --password='' --add-drop-database -B apartment_renovation -r " + filePath;
        //=========================================================================================
        String dumpCommand2 = "mysqldump -u {root} -p apartment_renovation > backup.sql";
        //=========================================================================================
        String dbUsername = "root";
        String dbPassword = "";
        String dbName = "apartment_renovation";
        String file = "backup.sql";
        String command = String.format("mariadb-dump -u%s -p%s --add-drop-table --databases %s -r %s", dbUsername, dbPassword, dbName, file);
        //=========================================================================================
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(dumpCommand2);
        int processComplete = process.waitFor();

        // Logowanie wyjścia procesu
        InputStream errorStream = process.getInputStream();
        String errors = new BufferedReader(new InputStreamReader(errorStream))
                .lines().collect(Collectors.joining("\n"));
        System.out.println("Error: " + errors);

        if(processComplete == 0){
            System.out.println("Backup created successfully!");
            return processComplete;
        } else {
            System.out.println("Could not create the backup");
            System.out.println("Error: " + errors);
            return processComplete;
        }

    }

    @Override
    public int performRestore(String fileName) throws IOException, InterruptedException {
        String filePath = BACKUP_FOLDER + "/" + fileName;
        String[] restoreCommand = {"mysql", "-u", "root", "-p ", "apartment_renovation", "-e", "source " + filePath};
        Process process = Runtime.getRuntime().exec(restoreCommand);
        return process.waitFor();
    }

    @Override
    public List<String> getAllBackupsName() {
        File folder = new File(BACKUP_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            return new ArrayList<>();
        }
        return Arrays.stream(folder.listFiles())
                .filter(file -> file.isFile() &&file.getName().endsWith(".sql"))
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
