package tylercode.schedulertyler.tripwizard.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationTitle;
    private String lodging;
    private String startDate;
    private String endDate;

    public Vacation(int vacationID, String vacationTitle, String lodging, String startDate,
                    String endDate) {
        this.vacationID = vacationID;
        this.vacationTitle = vacationTitle;
        this.lodging = lodging;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Vacation() {
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationTitle() {
        return vacationTitle;
    }

    public void setVacationTitle(String vacationTitle) {
        this.vacationTitle = vacationTitle;
    }

    public String getLodging() {
        return lodging;
    }

    public void setLodging(String lodging) {
        this.lodging = lodging;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return vacationID + " " + vacationTitle + " " + lodging + " " +
                startDate + " " + endDate;
    }
}
