package org.hydra.gui.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.hydra.util.Log;
import org.hydra.util.Utils;

public class Database {

    private static Config config = new Config();
    private static DB db;

    static {
        db = DB.load(config.getUploadPath());
        
    }

    public static void save(String type, String id, Serializable obj) {
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

    public static class Record implements Serializable {
        private static final long serialVersionUID = -8089946506185400983L;
        private String id, type;
        private Serializable obj;

        public Record(String id, String type, Serializable obj) {
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

        public Serializable getObj() {
            return obj;
        }
    }

    public static class Util {
        public static String nextId() {
            return UUID.randomUUID().toString();
        }
    }

    private static class DB {

        private static String dbFile = "simpledb.bin";

        private String path;

        /**
         * id -> Record
         */
        private Map<String, Record> db = new ConcurrentHashMap<String, Record>();
        /**
         * type -> list of ids
         */
        private Map<String, List<String>> index = new ConcurrentHashMap<String, List<String>>();

        public DB(String path2) {
            this.path = path2;
        }

        public void save(String type, String id, Serializable obj) {
            List<String> indexByType = this.index.get(type);
            if (indexByType == null) {
                indexByType = new ArrayList<String>();
                index.put(type, indexByType);
            }
            if (indexByType.contains(id) == false) {
                indexByType.add(id);
            }
            db.put(id, new Record(id, type, obj));
            store();
        }

        private void store() {
            File f = new File(path, dbFile);
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(f));

                oos.writeObject(this.db);
                oos.writeObject(this.index);
                oos.flush();
            } catch (Exception e) {
                Log.error(e.toString());
                Utils.close(oos);
            }
        }

        @SuppressWarnings("unchecked")
		public static DB load(String path) {
            File f = new File(path, dbFile);
            if (f.exists()) {
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(new FileInputStream(f));
                    DB db = new DB(path);
                    db.db = (Map<String, Record>) ois.readObject();
                    db.index = (Map<String, List<String>>) ois.readObject();
                    return db;
                } catch (Exception e) {
                    Log.error(e.toString());
                    Utils.close(ois);
                }
            }
            return new DB(path);
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
            store();
        }

    }
}
