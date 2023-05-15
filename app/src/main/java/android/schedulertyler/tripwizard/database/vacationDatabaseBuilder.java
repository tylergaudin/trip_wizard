package android.schedulertyler.tripwizard.database;

import android.content.Context;
import android.schedulertyler.tripwizard.dao.excursionDAO;
import android.schedulertyler.tripwizard.dao.vacationDAO;
import android.schedulertyler.tripwizard.entities.Excursion;
import android.schedulertyler.tripwizard.entities.Vacation;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vacation.class, Excursion.class}, version=1, exportSchema = false)
public abstract class vacationDatabaseBuilder extends RoomDatabase {
    public abstract vacationDAO vacationDAO();
    public abstract excursionDAO excursionDAO();

    private static volatile vacationDatabaseBuilder INSTANCE;

    static vacationDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (vacationDatabaseBuilder.class){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            vacationDatabaseBuilder.class, "TripWizardDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
