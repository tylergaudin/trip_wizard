package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.schedulertyler.tripwizard.R;
import android.schedulertyler.tripwizard.database.Repository;
import android.schedulertyler.tripwizard.entities.Excursion;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    EditText editTitle;
    EditText editDate;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendar = Calendar.getInstance();
    String title;
    String date;
    int vacationId;
    int id;
    Excursion excursion;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        editTitle = findViewById(R.id.vacation_title);
        editDate = findViewById(R.id.date);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String d = sdf.format(new Date());
        editDate.setText(d);
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        vacationId = getIntent().getIntExtra("vacation_id", -1);
        editTitle.setText(title);
        repository = new Repository(getApplication());
        //repository = new Repository(getApplication());
        //final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        /*recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == id) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);*/

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = editDate.getText().toString();
                try {
                    myCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, excursionDate,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        excursionDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText();
            }

            private void updateEditText() {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        Button button = findViewById(R.id.saveexcursion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == -1) {
                    excursion = new Excursion(0, editTitle.getText().toString(),
                            editDate.getText().toString(), vacationId);
                    repository.insert(excursion);

                } else {
                    excursion = new Excursion(id, editTitle.getText().toString(),
                            editDate.getText().toString(), vacationId);
                    repository.update(excursion);
                }
            }
        });
    }
}