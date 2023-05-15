package android.schedulertyler.tripwizard.dao;

import android.schedulertyler.tripwizard.entities.Vacation;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface vacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);
    @Update
    void update(Vacation vacation);
    @Delete
    void delete(Vacation vacation);
    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();
}
