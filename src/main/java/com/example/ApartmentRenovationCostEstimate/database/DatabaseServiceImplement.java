package com.example.ApartmentRenovationCostEstimate.database;

import com.example.ApartmentRenovationCostEstimate.database.DatabaseService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.err;
import static java.lang.System.out;


@Service
public class DatabaseServiceImplement implements DatabaseService {

    private static String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static String BACKUP_DIRECTORY = "C:\\Users\\Damian\\Java\\Projects\\DATABASE\\backups";
    private static String MYSQLDUMP = "C:\\xampp\\mysql\\bin\\mysqldump ";
    private static String MYSQL_RESTORE = "C:\\xampp\\mysql\\bin\\mysql ";
    private static String MYSQLDUMP_PARAMETERS = "-u root -p --add-drop-database -B apartment_renovation > ";
    private static String MYSQLRESTORE_PARAMETERS = "-u root -p --database=apartment_renovation < ";
    private static final int EXIT_ERROR_CODE = 1; // Directory creation error code
    private static final int EXIT_SUCCESS_CODE = 0; // Directory creation success code

    @Override
    public int performBackup(){
        String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        String filePath = BACKUP_DIRECTORY + "\\" + date + "_arcea.sql";

        //===================================================
        StringBuilder sb = new StringBuilder();
        sb.append("cmd /c start cmd /c \"");
        sb.append(MYSQLDUMP);
        sb.append(MYSQLDUMP_PARAMETERS);
        sb.append(filePath);
        sb.append("\"");

        String dumpCommand = sb.toString();
        //===================================================

        if (createDirectory() == false) {
            err.println("Not possible to create [ " + filePath + " ], backup progress was terminated.");
            return EXIT_ERROR_CODE;
        }

        try {
            executeDataBaseDump(dumpCommand);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // todo To use Logger instead
            out.println("\nBackup process terminated.");
        }
        return EXIT_SUCCESS_CODE;
    }

    @Override
    public int performRestore(String fileName) {
        String filePath = BACKUP_DIRECTORY + "/" + fileName;

        //===================================================
        StringBuilder sb = new StringBuilder();
        sb.append("cmd /c start cmd /c \"");
        sb.append(MYSQL_RESTORE);
        sb.append(MYSQLRESTORE_PARAMETERS);
        sb.append(filePath);
        sb.append("\"");

        String restoreCommand = sb.toString();
        //===================================================

        try {
            executeDatabaseRestore(restoreCommand);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            out.println("\nRestore process terminated.");
        }
        return EXIT_SUCCESS_CODE;
    }

    private boolean createDirectory() {
        File directory = new File(BACKUP_DIRECTORY);

        if(!directory.exists()) {
            boolean resultOfCreatingDirectory = directory.mkdirs();
            if (resultOfCreatingDirectory) {
                out.println("Folder was created successfully.");
                return true;
            } else {
                err.println("Failed to create folder.");
                return false;
            }
        } else {
            return true;
        }
    }

    private int executeDataBaseDump(String dumpCommand) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", dumpCommand);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        int resultStatus = process.waitFor();

        if(resultStatus == 0){
            System.out.println("Backup created successfully.");
            return resultStatus;
        } else {
            System.out.println("Backup creation failed!");
            return resultStatus;
        }
    }

    private int executeDatabaseRestore(String restoreCommand) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", restoreCommand);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        int resultStatus = process.waitFor();

        if(resultStatus == 0){
            System.out.println("Successfully restored database.");
            return resultStatus;
        } else {
            System.out.println("Restore database failed!");
            return resultStatus;
        }
    }

    @Override
    public List<String> getAllBackupsName() {
        File folder = new File(BACKUP_DIRECTORY);
        if (!folder.exists() || !folder.isDirectory()) {
            return new ArrayList<>();
        }
        return Arrays.stream(folder.listFiles())
                .filter(file -> file.isFile() && file.getName().endsWith(".sql"))
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
