package com.example.ApartmentRenovationCostEstimate.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    //PROBA STWORZENIA FUNKCJI BACKUP/RESTORE DATABASE -> NARAZIE NIE DZIAŁĄ.

    private DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseBackupService) {
        this.databaseService = databaseBackupService;
    }

    @PostMapping("/backup")
    public ResponseEntity<DatabaseResponse> backupDatabase() {
        try {
            int result = databaseService.performBackup();
            if (result == 0) {
                return new ResponseEntity<>(new DatabaseResponse("SUCCESS", "Backup created successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DatabaseResponse("FAILURE", "Could not create the backup"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new DatabaseResponse("ERROR", "Error while creating backup: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/restore/{fileName}")
    public ResponseEntity<DatabaseResponse> restoreBackup(@PathVariable("fileName") String fileName) {
        try {
            int result = databaseService.performRestore(fileName);
            if (result == 0) {
                return new ResponseEntity<>(new DatabaseResponse("SUCCESS", "Database restored successfully!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DatabaseResponse("FAILURE", "Backup cannot be restored"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new DatabaseResponse("FAILURE", "Error during restore backup: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/backups")
    public ResponseEntity<List<String>> getBackupFileNames() {
        List<String> backupFileNames = databaseService.getAllBackupsName();
        if (backupFileNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(backupFileNames, HttpStatus.OK);
        }
    }

}
