package com.example.todoList;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import android.arch.persistence.room.Room;
import static android.content.ContentValues.TAG;

@Database(entities = {Item.class},version = 3,exportSchema = false)
@TypeConverters({DateConverter.class})


public abstract class ItemDatabase extends RoomDatabase {

    private static ItemDatabase instance;

    public abstract ItemDao itemDao();

    public static synchronized ItemDatabase getInstance(Context context) {
        if (instance==null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),ItemDatabase.class, "test_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask().execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {



        private PopulateDbAsyncTask() {

        }

        @Override
        protected Void doInBackground(Void... voids) {

            //itemDao.insert(new Item("Name 1","Description 1",16));
            //itemDao.insert(new Item("Name 2","Description 2",17));
            //itemDao.insert(new Item("Name 3","Description 3",18));
            Log.d(TAG,"inserted");

            return null;
        }
    }

}
