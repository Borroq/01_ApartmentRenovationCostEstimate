package com.example.ApartmentRenovationCostEstimate.database;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

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


    /** Application exit code for Directory creation error */
    private static final int EXIT_CODE_DIRECTORY_CREATION_ERROR = 1;

    /** Application exit code for Directory creation success */
    private static final int EXIT_CODE_SUCCESS = 0;

    /** Application exit code for database dump process error */
    private static final int EXIT_CODE_BACKUP_DUMP_PROCESS_ERROR = 2;

    private static final int EXIT_CODE_RESTORE_BACKUP_PROCESS_ERROR = 3;

    /** Timestamp format for database file name */
    private static String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    /** Database dump path */
    private static String BACKUP_DIRECTORY = "C:\\Users\\Damian\\Java\\Projects\\DATABASE\\backups";

    /** Database dump application */
    private static String MYSQLDUMP = "C:\\xampp\\mysql\\bin\\mysqldump ";

    /** Database restore application */
    private static String MYSQL_RESTORE = "C:\\xampp\\mysql\\bin\\mysql ";

    /** Database dump application parameters */
    private static String MYSQLDUMP_PARAMETERS = "-u root -p --add-drop-database -B apartment_renovation > ";

    /** Database restore application parameters */
    private static String MYSQLRESTORE_PARAMETERS = "-u root -p --database=apartment_renovation < ";


    @Override
    public int performBackup() {
        if (createDirectory() == false) {
            return EXIT_CODE_DIRECTORY_CREATION_ERROR;
        }

        if (executeDataBaseDump() == 0) {
            return EXIT_CODE_SUCCESS;
        } else {
            return EXIT_CODE_BACKUP_DUMP_PROCESS_ERROR;
        }
    }

    @Override
    public int performRestore(String fileName) {
        if (executeDatabaseRestore(fileName) == 0) {
            return EXIT_CODE_SUCCESS;
        } else {
            return EXIT_CODE_RESTORE_BACKUP_PROCESS_ERROR;
        }
    }

    /** Creates a directory in the appropriate path */
    private boolean createDirectory() {
        File directory = new File(BACKUP_DIRECTORY);

        if (!directory.exists()) {
            boolean resultOfCreatingDirectory = directory.mkdirs();
            if (resultOfCreatingDirectory) {
                out.println("Folder was created successfully.");
                return true;
            } else {
                err.println("Not possible to create directory:\n [ " + BACKUP_DIRECTORY + " ]\n, backup process was terminated.");
                return false;
            }
        } else {
            return true;
        }
    }

    /** Invokes a command in the cmd.exe console to dump the database */
    private int executeDataBaseDump() {
        int resultStatus = -1;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", this.getDumpCommand());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            resultStatus = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); //fixme Use logger instead
            err.println("Backup process failed!");
        }

        if(resultStatus == 0) {
            out.println("Backup created successfully.");
            return resultStatus;
        } else {
            err.println("Backup creation failed!");
            return resultStatus;
        }
    }

    /** Invokes a command in the cmd.exe console to restore the database */
    private int executeDatabaseRestore(String restoreCommand) {
        int resultStatus = -1;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", this.getRestoreCommand(restoreCommand));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            resultStatus = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); //fixme Use logger instead
            err.println("Restore process failed!");
        }

        if (resultStatus == 0) {
            out.println("Successfully restored database.");
            return resultStatus;
        } else {
            err.println("Restore database failed!");
            return resultStatus;
        }
    }

    /**
     * The method returns a full command containing:<br />
     * <ul>
     * <li>the application performing the database backup</li>
     * <li>the parameters to execute</li>
     * <li>the full path to the backup file</li>
     * <li>the name of the backup file.</li>
     * </ul>
     * The file name contains a prefix that is the date and time the backup was created.
     *
     * @return full command for database dump
     */
    private static String getDumpCommand() {
        StringBuilder dumpCommand = new StringBuilder("cmd /c start cmd /c \"");
        dumpCommand.append(MYSQLDUMP);
        dumpCommand.append(MYSQLDUMP_PARAMETERS);
        dumpCommand.append(BACKUP_DIRECTORY).append("\\");
        String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        dumpCommand.append(date).append("_arcea.sql");
        dumpCommand.append("\"");

        return dumpCommand.toString();
    }

    /**
     * The method returns a full command containing:<br />
     * <ul>
     * <li>the application performing the database restore</li>
     * <li>the parameters to execute</li>
     * <li>the full path to the backup file</li>
     * <li>Read the name of the backup file.</li>
     * </ul>
     * The file name contains a prefix that is the date and time the backup was created.
     *
     * @return full command for database restore
     */
    private static String getRestoreCommand(String fileName) {
        StringBuilder restoreCommand = new StringBuilder("cmd /c start cmd /c \"");
        restoreCommand.append(MYSQL_RESTORE);
        restoreCommand.append(MYSQLRESTORE_PARAMETERS);
        restoreCommand.append(BACKUP_DIRECTORY).append("\\");
        restoreCommand.append(fileName);
        restoreCommand.append("\"");

        return restoreCommand.toString();
    }

    /** The method returns a complete list of backup files existing in the backup directory */
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
