package com.txtled.gp_a012.bean.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.txtled.gp_a012.bean.Song;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SONG".
*/
public class SongDao extends AbstractDao<Song, Long> {

    public static final String TABLENAME = "SONG";

    /**
     * Properties of entity Song.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SongId = new Property(1, int.class, "songId", false, "SONG_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Singer = new Property(3, String.class, "singer", false, "SINGER");
        public final static Property Url = new Property(4, String.class, "url", false, "URL");
        public final static Property Uri = new Property(5, String.class, "uri", false, "URI");
        public final static Property Duration = new Property(6, int.class, "duration", false, "DURATION");
    }


    public SongDao(DaoConfig config) {
        super(config);
    }
    
    public SongDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SONG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SONG_ID\" INTEGER NOT NULL ," + // 1: songId
                "\"NAME\" TEXT," + // 2: name
                "\"SINGER\" TEXT," + // 3: singer
                "\"URL\" TEXT," + // 4: url
                "\"URI\" TEXT," + // 5: uri
                "\"DURATION\" INTEGER NOT NULL );"); // 6: duration
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SONG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Song entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSongId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(4, singer);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(5, url);
        }
 
        String uri = entity.getUri();
        if (uri != null) {
            stmt.bindString(6, uri);
        }
        stmt.bindLong(7, entity.getDuration());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Song entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSongId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(4, singer);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(5, url);
        }
 
        String uri = entity.getUri();
        if (uri != null) {
            stmt.bindString(6, uri);
        }
        stmt.bindLong(7, entity.getDuration());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Song readEntity(Cursor cursor, int offset) {
        Song entity = new Song( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // songId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // singer
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // url
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // uri
            cursor.getInt(offset + 6) // duration
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Song entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSongId(cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSinger(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUri(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDuration(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Song entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Song entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Song entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
