package org.hydra.gui.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    private static Config config = new Config();
    private static DB db = new DB();

    public static void save(String type, String id, Object obj) {
        db.save(type, id, obj);
    }

    public static void delete(String id) {
        db.delete(id);
    }

    public static Record get(String id) {
        return db.get(id);
    }

    public static List<Record> list(String type) {
        return db.list(type);
    }

    public static Config getConfig() {
        return config;
    }

    public static class Config {
        public String getUploadPath() {
            String home = System.getProperty("user.home");
            File workdir = new File(home, ".bf_workdir");
            if (workdir.exists() == false) {
                workdir.mkdirs();
            }
            try {
                return workdir.getCanonicalPath();
            } catch (IOException e) {
                return home;
            }
        }
    }

    public static class Record {
        private String id, type;
        private Object obj;

        public Record(String id, String type, Object obj) {
            this.id = id;
            this.type = type;
            this.obj = obj;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public Object getObj() {
            return obj;
        }

    }

    public static class Util {
        public static String nextId() {
            return UUID.randomUUID().toString();
        }
    }

    private static class DB {
        /**
         * id -> Record
         */
        private Map<String, Record> db = new ConcurrentHashMap<String, Record>();
        /**
         * type -> list of ids
         */
        private Map<String, List<String>> index = new ConcurrentHashMap<String, List<String>>();

        public void save(String type, String id, Object obj) {
            List<String> indexByType = this.index.get(type);
            if (indexByType == null) {
                indexByType = new ArrayList<String>();
                index.put(type, indexByType);
            }
            if (indexByType.contains(id) == false) {
                indexByType.add(id);
            }
            db.put(id, new Record(id, type, obj));
        }

        public Record get(String id) {
            return db.get(id);
        }

        public List<Record> list(String type) {
            List<String> indexByType = this.index.get(type);
            if (indexByType == null) {
                return null;
            } else {
                List<Record> result = new ArrayList<Record>(indexByType.size());
                for (String id : indexByType) {
                    result.add(db.get(id));
                }
                return result;
            }
        }

        public void delete(String id) {
            Record rec = db.get(id);
            List<String> indexByType = this.index.get(rec.getType());
            if (indexByType != null) {
                indexByType.remove(id);
            }
            db.remove(id);
        }

    }
}
