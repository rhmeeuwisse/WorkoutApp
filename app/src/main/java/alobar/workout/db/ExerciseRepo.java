package alobar.workout.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import alobar.util.Assert;
import alobar.workout.data.Exercise;

/**
 * Repository for {@link Exercise} entities
 */
@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class ExerciseRepo {

    private final SQLiteDatabase db;

    public ExerciseRepo(SQLiteDatabase db) {
        this.db = db;
    }

    public List<Exercise> all() {
        Cursor cursor = db.rawQuery("select * from " + DatabaseContract.Exercise.tableName, null);
        try {
            final int idIndex = cursor.getColumnIndexOrThrow(DatabaseContract.Exercise._ID);
            final int nameIndex = cursor.getColumnIndexOrThrow(DatabaseContract.Exercise.NAME);
            final int weightIndex = cursor.getColumnIndexOrThrow(DatabaseContract.Exercise.WEIGHT);
            List<Exercise> result = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                result.add(new Exercise(
                        cursor.getLong(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getDouble(weightIndex)
                ));
            }
            return result;
        } finally {
            cursor.close();
        }
    }

    public Exercise findById(long id) {
        final String query = "select * from " + DatabaseContract.Exercise.tableName +
                " where " + DatabaseContract.Exercise._ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{Long.toString(id)});
        try {
            if (cursor.moveToNext())
                return new Exercise(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Exercise._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Exercise.NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Exercise.WEIGHT))
                );
            else return null;
        } finally {
            cursor.close();
        }
    }

    public void save(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Exercise.NAME, exercise.name);
        values.put(DatabaseContract.Exercise.WEIGHT, exercise.weight);
        if (exercise._id == 0) {
            long _id = db.insert(DatabaseContract.Exercise.tableName, null, values);
            Assert.check(_id != 0, "Exercise insert failed");
        } else {
            int affected = db.update(DatabaseContract.Exercise.tableName, values, DatabaseContract.Exercise._ID + " = ?", new String[]{Long.toString(exercise._id)});
            Assert.check(affected == 1, "Exercise update failed");
        }
    }

    public void deleteById(long _id) {
        int affected = db.delete(DatabaseContract.Exercise.tableName, DatabaseContract.Exercise._ID + " = ?", new String[]{Long.toString(_id)});
        Assert.check(affected == 0, "Exercise delete failed");
    }
}
