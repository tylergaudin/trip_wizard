package tylercode.schedulertyler.tripwizard.database;

import android.content.Context;
import tylercode.schedulertyler.tripwizard.dao.ExcursionDAO;
import tylercode.schedulertyler.tripwizard.dao.VacationDAO;
import tylercode.schedulertyler.tripwizard.entities.Excursion;
import tylercode.schedulertyler.tripwizard.entities.Vacation;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Vacation.class, Excursion.class}, version = 2, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();

    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabaseBuilder.class, "TripWizardDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
