package com.firstlinesoftware.delivery.storage.map;

import com.firstlinesoftware.delivery.dto.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * User: Legohuman
 * Date: 06/03/16
 */
public class Storage {

    Logger log = LogManager.getLogger(Storage.class);

    public static final String dataFolder = "data";
    public static final String dbFileName = "delivery";

    private DB db = null;
    private Maps maps = null;

    public Maps maps() {
        if (!opened()) {
            throw new IllegalStateException(String.format("Can not get maps from unopened database %s", getDbFile().getAbsolutePath()));
        }
        return maps;
    }

    public void open() {
        if (opened()) {
            throw new IllegalStateException(String.format("Can not open opened database %s", getDbFile().getAbsolutePath()));
        }
        db = DBMaker.fileDB(getDbFile())
                .closeOnJvmShutdown()
                .make();
        maps = new Maps();
    }

    public void close() {
        if (!opened()) {
            throw new IllegalStateException(String.format("Can not close unopened database %s", getDbFile().getAbsolutePath()));
        }
        db.close();
        db = null;
    }

    public void remove() {
        if (opened()) {
            throw new IllegalStateException(String.format("Can not remove opened database %s", getDbFile().getAbsolutePath()));
        }
        File dbFile = getDbFile();
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    public void commit() {
        if (!opened()) {
            throw new IllegalStateException(String.format("Can not commit in unopened database %s", getDbFile().getAbsolutePath()));
        }
        db.commit();
    }

    public void rollback() {
        if (!opened()) {
            throw new IllegalStateException(String.format("Can not rollback in unopened database %s", getDbFile().getAbsolutePath()));
        }
        db.rollback();
    }

    public void doInTransaction(Runnable action) {
        if (!opened()) {
            throw new IllegalStateException(String.format("Can not rollback in unopened database %s", getDbFile().getAbsolutePath()));
        }
        try {
            action.run();
            db.commit();
        } catch (Exception e) {
            log.error("Error during database transaction", e);
            db.rollback();
        }
    }

    public boolean exists() {
        return getDbFile().exists();
    }

    public boolean opened() {
        return db != null;
    }

    private File getDbFile() {
        return new File(dataFolder, dbFileName);
    }

    public enum MapNames {
        product,
        importRates,
        transportRates,
        transportFunctions,
        importPayments,
        cities,
        segments,
        functions
    }

    public class Maps {

        public ConcurrentNavigableMap<String, Product> products() {
            return db.treeMap(MapNames.product.name());
        }

        public ConcurrentNavigableMap<ImportRateKey, Double> importRates() {
            return db.treeMap(MapNames.importRates.name());
        }

        public ConcurrentNavigableMap<TransportRateKey, Double> transportRates() {
            return db.treeMap(MapNames.transportRates.name());
        }

        public ConcurrentNavigableMap<TransportFnKey, String> transportFunctions() {
            return db.treeMap(MapNames.transportFunctions.name());
        }

        public ConcurrentNavigableMap<ImportPaymentKey, String> importPaymentRules() {
            return db.treeMap(MapNames.importPayments.name());
        }

        public ConcurrentNavigableMap<Integer, City> cities() {
            return db.treeMap(MapNames.cities.name());
        }

        public ConcurrentNavigableMap<Integer, SegmentVal> segments() {
            return db.treeMap(MapNames.segments.name());
        }

        public ConcurrentNavigableMap<String, String> functions() {
            return db.treeMap(MapNames.functions.name());
        }

        private void checkDbIsOpen() {
            if (db == null) {
                throw new IllegalStateException(String.format("Can not access to map in unopened database %s", getDbFile().getAbsolutePath()));
            }
        }
    }
}
