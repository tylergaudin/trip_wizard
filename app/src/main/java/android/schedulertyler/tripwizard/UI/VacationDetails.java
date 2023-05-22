package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.schedulertyler.tripwizard.R;
import android.schedulertyler.tripwizard.database.Repository;
import android.schedulertyler.tripwizard.entities.Excursion;
import android.schedulertyler.tripwizard.entities.Vacation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    EditText editStart;
    EditText editEnd;
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
        /*String sd = sdf.format(new Date());
        String ed = sdf.format(new Date());*/
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        lodging = getIntent().getStringExtra("lodging");
        start = getIntent().getStringExtra("start_date");
        end = getIntent().getStringExtra("end_date");
        editTitle.setText(title);
        editLodging.setText(lodging);
        editStart.setText(start);
        editEnd.setText(end);
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
                updateEditTextStart();
            }

            private void updateEditTextStart() {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editStart.setText(sdf.format(myCalendar.getTime()));
            }
        };

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditTextEnd();
            }

            private void updateEditTextEnd() {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editEnd.setText(sdf.format(myCalendar.getTime()));
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
        editStart.setText(sd);
        editEnd.setText(ed);
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        lodging = getIntent().getStringExtra("lodging");
        start = getIntent().getStringExtra("start_date");
        end = getIntent().getStringExtra("end_date");
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
        getMenuInflater().inflate(R.menu.deletevacation, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deletevacation) {
            for (Vacation vac : repository.getAllVacations()) {
                if (vac.getVacationID() == id) currentVacation = vac;
            }

            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationID() == id) ++numExcursions;
            }

            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle()
                        + " was deleted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VacationDetails.this,
                        "Can't delete a vacation with excursions scheduled.",
                        Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}