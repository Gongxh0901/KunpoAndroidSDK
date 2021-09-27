package com.kunpo.kunposdk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kunpo.kunposdk.utils.Utils;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * 数据库当前版本为 1
 */
class UserDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "kunpo_account.db";
    private static final String USER_TABLE_NAME = "userinfo";
    private static final int version = 1;

    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    public void onCreate(SQLiteDatabase database) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS userinfo (" +
                    "openid CHAR(64)," +
                    "uid CHAR(64)," +
                    "token CHAR(512)," +
                    "user_type INT," +
                    "sex INT," +
                    "status INT," +
                    "nick_name CHAR(64)," +
                    "mail CHAR(64)," +
                    "avatar CHAR(512)," +
                    "phone_number CHAR(32)," +
                    "expire_time INT" +
                    ")";
            database.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否存在
     * @param userInfo
     * @return
     */
    public boolean exist(UserInfo userInfo) {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] columns = {"open_id"};
            String[] values = {userInfo.openid.trim()};
            try {
                return db.query(USER_TABLE_NAME, columns, "openid=?", values, null, null, null).moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除数据
     * @param userInfo
     * @return
     */
    public int delete(UserInfo userInfo) {
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            try {
                return db.delete(USER_TABLE_NAME, "openid=?", new String[]{userInfo.openid.trim()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 查询数量
     * @return {count} 数量
     */
    public int count() {
        SQLiteDatabase tmpDatabase = getReadableDatabase();
        if (tmpDatabase != null) {
            String[] columns = {"open_id"};
            try {
                Cursor cursor = tmpDatabase.query(USER_TABLE_NAME, columns, null, null, null, null, null);
                return cursor.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 查询并返回所有数据
     * @return
     */
    public UserInfo[] query() {
        TreeMap<Integer, UserInfo> treeMap = new TreeMap<>(new Comparator<Integer>() {
            public int compare(Integer left, Integer right) {
                return left < right ? 1 : (left == right ? 0 : -1);
            }
        });

        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] columns = {"openid", "uid", "token", "user_type", "sex", "status", "nick_name", "mail", "avatar", "phone_number","expire_time"};
            Cursor cursor = null;
            try {
                cursor = db.query(USER_TABLE_NAME, columns, null, null, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (cursor.moveToNext()) {
                UserInfo userInfo = new UserInfo();
                userInfo.openid = decrypt(cursor.getString(0));
                userInfo.uid = decrypt(cursor.getString(1));
                userInfo.token = decrypt(cursor.getString(2));
                userInfo.user_type = cursor.getInt(3);
                userInfo.sex = cursor.getInt(4);
                userInfo.status = cursor.getInt(5);
                userInfo.nick_name = cursor.getString(6);
                userInfo.mail = cursor.getString(7);
                userInfo.avatar = cursor.getString(8);
                userInfo.phone_number = decrypt(cursor.getString(9));
                userInfo.expire_time = cursor.getInt(10);
                treeMap.put(userInfo.expire_time, userInfo);
            }
        }
        return treeMap.values().toArray(new UserInfo[0]);
    }

    /**
     * 根据openid查询数据
     * @param openid
     * @return
     */
    public UserInfo queryWithOpenID(String openid) {
        if (Utils.isNullOrEmpty(openid)) {
            return null;
        }
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            String[] columns = {"openid", "uid", "token", "user_type", "sex", "status", "nick_name", "mail", "avatar", "phone_number","expire_time"};
            Cursor cursor = null;
            try {
                cursor = db.query(USER_TABLE_NAME, columns, "openid=?", new String[]{openid}, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor.moveToFirst()) {
                UserInfo userInfo = new UserInfo();
                userInfo.openid = decrypt(cursor.getString(0));
                userInfo.uid = decrypt(cursor.getString(1));
                userInfo.token = decrypt(cursor.getString(2));
                userInfo.user_type = cursor.getInt(3);
                userInfo.sex = cursor.getInt(4);
                userInfo.status = cursor.getInt(5);
                userInfo.nick_name = cursor.getString(6);
                userInfo.mail = cursor.getString(7);
                userInfo.avatar = cursor.getString(8);
                userInfo.phone_number = decrypt(cursor.getString(9));
                userInfo.expire_time = cursor.getInt(10);
                return userInfo;
            }
        }
        return null;
    }

    /**
     * 插入或更新数据
     * @param userInfo
     */
    public void update(UserInfo userInfo) {
        if (exist(userInfo)) {
            delete(userInfo);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("openid", encrypt(userInfo.openid));
        contentValues.put("uid", encrypt(userInfo.uid));
        contentValues.put("token", encrypt(userInfo.token));
        contentValues.put("user_type", userInfo.user_type);
        contentValues.put("sex", userInfo.sex);
        contentValues.put("status", userInfo.status);
        contentValues.put("nick_name", userInfo.nick_name);
        contentValues.put("mail", userInfo.mail);
        contentValues.put("avatar", userInfo.avatar);
        contentValues.put("phone_number", encrypt(userInfo.phone_number));
        contentValues.put("expire_time", userInfo.expire_time);
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            try {
                db.insert(USER_TABLE_NAME, null, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 升级数据库
     * @param db
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "alter table " + USER_TABLE_NAME + " add openid CHAR(64)";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param value
     * @return
     */
    private String encrypt(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim();
        }
    }

    /**
     * @param value
     * @return
     */
    private String decrypt(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim();
        }
    }
}
