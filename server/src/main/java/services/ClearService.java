package services;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import responses.ClearResponse;

public class ClearService {
    public ClearService() {
    }

    public ClearResponse clearData() {
        DataAccess dao = new MemoryDataAccess();
        try {
            dao.clearAll();
            return new ClearResponse(null);
        } catch (DataAccessException e) {
            return new ClearResponse("Error: description");
        }
    }
}
