package com.example.ApartmentRenovationCostEstimate.database;

import java.io.IOException;
import java.util.List;


public interface DatabaseService {

    List<String> getAllBackupsName();

    /**
     * Performs a backup of the database by:
     * <ul>
     *   <li>Creating the necessary backup directory if it doesn't exist</li>
     *   <li>Executing the database dump command</li>
     * </ul>
     *
     * The method returns an appropriate exit code based on the success or failure of the backup process:
     * <ul>
     *   <li>Returns {@code EXIT_CODE_DIRECTORY_CREATION_ERROR} if the directory could not be created</li>
     *   <li>Returns {@code EXIT_CODE_SUCCESS} if the backup process completes successfully</li>
     *   <li>Returns {@code EXIT_CODE_BACKUP_DUMP_PROCESS_ERROR} if there is an error during the backup dump process</li>
     * </ul>
     *
     * @return an integer exit code representing the outcome of the backup process
     */
    int performBackup()  throws IOException, InterruptedException;

    /**
     * Performs a restore of the database using a specified backup file.
     * <p>
     * The method invokes a command to restore the database using a MySQL backup file
     * located in the backup directory. It returns an exit code based on the result
     * of the restore process:
     * <ul>
     *   <li>Returns {@code EXIT_CODE_SUCCESS} if the restore process completes successfully</li>
     *   <li>Returns {@code EXIT_CODE_RESTORE_BACKUP_PROCESS_ERROR} if there is an error during the restore process</li>
     * </ul>
     *
     * @param fileName the name of the backup file to restore
     * @return an integer exit code representing the outcome of the restore process
     */
    int performRestore(String fileName) throws IOException, InterruptedException;
}
