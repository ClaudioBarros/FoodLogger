package com.example.foodlogger;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class USDADatabaseHelper extends SQLiteOpenHelper {

    public static String TAG = "USDADatabaseHelper"; //TAG for debugging on the logcat window
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usda.db";

    private final String DATABASE_PATH;
    private SQLiteDatabase mDatabase;
    private final Context mContext;

    public USDADatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        assert context != null;
        mContext = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).toString();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            this.createDatabase();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    @Override
    public synchronized void close(){
        if(mDatabase != null){
            mDatabase.close();
        }
        super.close();
    }

    //check if database already exists to avoid re-copy on every app opening.
    private boolean checkDatabase(){
       SQLiteDatabase checkDB = null;
       try{
           String dbPath = String.valueOf(mContext.getDatabasePath(DATABASE_NAME));
           checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
       } catch (SQLException e) {
           //database doesn't exist yet
           Log.e("LOG","" + e.toString());
       }
       if(checkDB != null) {
          checkDB.close();
       }
       return checkDB != null;
    }

    public void createDatabase() throws IOException{
        boolean dbExists = checkDatabase();

        if(!dbExists){

            //empty database will be created and filled with data
            this.getWritableDatabase();

            try {
                //Copy database from assets folder
                copyDatabase();
            } catch (IOException e){
                throw new Error("ERROR copying USDA Database:" + e.toString() );
            }
        }
    }

    private void copyDatabase() throws IOException {
        InputStream inStream = mContext.getAssets().open(DATABASE_NAME);

        //output file will be the newly created and empty database file
        OutputStream outStream = new FileOutputStream(DATABASE_PATH);

        //transfer bytes
        byte[] buffer = new byte[1024];
        int length = 0;
        while((length = inStream.read(buffer)) > 0){
           outStream.write(buffer, 0, length);
        }

        //close streams
        outStream.flush();
        outStream.close();
        inStream.close();
    }

    public void openDatabase() throws SQLException{
        //Open the database
        mDatabase = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }
}
