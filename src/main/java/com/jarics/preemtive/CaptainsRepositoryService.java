package com.jarics.preemtive;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

import java.io.IOException;
import java.util.UUID;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.WriteResult;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptainsRepositoryService {

    Nitrite db;
    ObjectRepository<Captain> repository;

    @Autowired
    public CaptainsRepositoryService(@Value("${nitrite.dir}") final String nitriteDir,
                                    @Value("${nitrite.file.name}") final String nitriteFileName) {
        //java initialization
        try {
            FileUtils.prepareDir(nitriteDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String aPath = nitriteDir + nitriteFileName;
        if (System.getProperty("testing") != null) {
            aPath = aPath + "_" + UUID.randomUUID();
        }
        db = Nitrite.builder()
                .compressed()
                .filePath(aPath)
                .openOrCreate("system", "admin");
        repository = db.getRepository(Captain.class);
    }


    /**
     * Make sure you get athlete before updating.
     */
    public Captain update(Captain captain) throws Exception {
        Captain captain1 = null;
        captain1 = getById(captain.getId());

        if (captain1 == null){
            captain.setId(NitriteId.newId().getIdValue());
            captain.setVersion(1);
            WriteResult result = repository.insert(captain);
            return getFromResult(captain, result);
        }

        if (captain1.getVersion() != captain.getVersion())
            throw new Exception("Concurrency Exception");
        captain.setVersion(captain1.getVersion()+1);
        WriteResult writeResult = repository.update(captain);
        return getFromResult(captain1, writeResult);
    }

    public Captain removeCaptain(long id) {
        Captain captain = getById(id);
        WriteResult writeResult = repository.remove(captain);
        return captain;
    }

    private Captain getFromResult(Captain captain, WriteResult writeResult) {
        for (NitriteId id : writeResult) {
            return repository.getById(id);
        }
        return null;
    }

    public Captain getById(long id) {
        org.dizitart.no2.objects.Cursor<Captain> wCursor = repository.find(eq("id", id));
        for (Captain captain1 : wCursor) {
            return captain1;
        }
        return null;
    }






}
