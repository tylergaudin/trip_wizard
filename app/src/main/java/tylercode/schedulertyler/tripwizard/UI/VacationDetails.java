package tylercode.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import tylercode.schedulertyler.tripwizard.R;
import tylercode.schedulertyler.tripwizard.database.Repository;
import tylercode.schedulertyler.tripwizard.entities.Excursion;
import tylercode.schedulertyler.tripwizard.entities.Vacation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editLodging;
    TextView editStart;
    TextView editEnd;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendar = Calendar.getInstance();
    String title;
    String lodging;
    String start;
    String end;
    int id;
    Vacation vacation;
    Vacation currentVacation;
    int numExcursions;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        editTitle = findViewById(R.id.vacation_title);
        editLodging = findViewById(R.id.lodging);
        editStart = findViewById(R.id.startdate);
        editEnd = findViewById(R.id.enddate);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String sd = sdf.format(new Date());
        String ed = sdf.format(new Date());
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        lodging = getIntent().getStringExtra("lodging");
        start = getIntent().getStringExtra("start_date");
        end = getIntent().getStringExtra("end_date");

        if (start == null) {
            start = sd;
        }

        if (end == null) {
            end = ed;
        }
        editTitle.setText(start);
        editLodging.setText(end);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == id) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editStart.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editEnd.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateEditTextStart();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            private void updateEditTextStart() throws ParseException {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String info = editEnd.getText().toString();
                if (myCalendar.getTime().after(sdf.parse(info))) {
                    Toast.makeText(VacationDetails.this,
                            "Start date must be before end date.",
                            Toast.LENGTH_LONG).show();
                } else {
                    editStart.setText(sdf.format(myCalendar.getTime()));
                }
            }
        };

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    updateEditTextEnd();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            private void updateEditTextEnd() throws ParseException {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String info = editStart.getText().toString();
                if (myCalendar.getTime().before(sdf.parse(info))) {
                    Toast.makeText(VacationDetails.this,
                            "End date must be after start date.",
                            Toast.LENGTH_LONG).show();
                } else {
                    editEnd.setText(sdf.format(myCalendar.getTime()));
                }
            }
        };
        Button button = findViewById(R.id.savevacation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (id == -1) {
                    vacation = new Vacation(0, editTitle.getText().toString(),
                            editLodging.getText().toString(), editStart.getText().toString(),
                            editEnd.getText().toString());
                    repository.insert(vacation);

                } else {
                    vacation = new Vacation(id, editTitle.getText().toString(),
                            editLodging.getText().toString(), editStart.getText().toString(),
                            editEnd.getText().toString());
                    repository.update(vacation);

                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent
                        (VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacation_id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        editTitle = findViewById(R.id.vacation_title);
        editLodging = findViewById(R.id.lodging);
        editStart = findViewById(R.id.startdate);
        editEnd = findViewById(R.id.enddate);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String sd = sdf.format(new Date());
        String ed = sdf.format(new Date());
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        lodging = getIntent().getStringExtra("lodging");
        start = getIntent().getStringExtra("start_date");
        end = getIntent().getStringExtra("end_date");
        if (start == null) {
            start = sd;
        }

        if (end == null) {
            end = ed;
        }

        editStart.setText(start);
        editEnd.setText(end);
        editTitle.setText(title);
        editLodging.setText(lodging);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == id) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int numVacations = 0;
        if (item.getItemId() == R.id.delete_vacation) {
            for (Vacation vac : repository.getAllVacations()) {
                if (vac.getVacationID() == id){
                    currentVacation = vac;
                    numVacations++;
                }
                else {Toast.makeText(VacationDetails.this,
                        "Can't delete when there is no vacation scheduled.",
                        Toast.LENGTH_LONG).show();}
            }

            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationID() == id) ++numExcursions;
            }

            if (numExcursions == 0 && numVacations > 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle()
                        + " was deleted.", Toast.LENGTH_LONG).show();
            }
            else if (numVacations < 1){
                Toast.makeText(VacationDetails.this,
                        "No vacation to delete.",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(VacationDetails.this,
                        "Can't delete a vacation with excursions scheduled.",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String t = editTitle.getText().toString();
            String h = editLodging.getText().toString();
            String s = editStart.getText().toString();
            String e = editEnd.getText().toString();
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Vacation Name: " + t + " Lodging: " + h +
                    " From " + s + " to " + e);
            sendIntent.putExtra(Intent.EXTRA_TITLE, t);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        } else if (item.getItemId() == R.id.notify_start) {
            String dateFromScreen = editStart.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key", "Your " + title + " vacation starts today.");
            PendingIntent sender = PendingIntent.getBroadcast
                    (VacationDetails.this,
                            ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        } else if (item.getItemId() == R.id.notify_end) {
            String dateFromScreen = editEnd.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key", "Your " + title + " vacation ends today.");
            PendingIntent sender = PendingIntent.getBroadcast
                    (VacationDetails.this,
                            ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}