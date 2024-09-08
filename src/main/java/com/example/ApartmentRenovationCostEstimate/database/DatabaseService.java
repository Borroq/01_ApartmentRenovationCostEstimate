package com.example.ApartmentRenovationCostEstimate.database;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DatabaseService {
    //PROBA STWORZENIA FUNKCJI BACKUP/RESTORE DATABASE -> NARAZIE NIE DZIAŁĄ.
    int performBackup()  throws IOException, InterruptedException;
    int performRestore(String fileName) throws IOException, InterruptedException;
    List<String> getAllBackupsName();
}
