package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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
import android.widget.ScrollView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        editTitle = findViewById(R.id.excursion_title);
        editDate = findViewById(R.id.date);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String d = sdf.format(new Date());
        id = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        editDate.setText(d);
        vacationId = getIntent().getIntExtra("vacation_id", -1);
        editTitle.setText(title);
        repository = new Repository(getApplication());
        /*RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == id) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);*/

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editDate.getText().toString();
                if (info.equals(""))info= String.valueOf(new Date());
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
                    finish();

                } else {
                    excursion = new Excursion(id, editTitle.getText().toString(),
                            editDate.getText().toString(), vacationId);
                    repository.update(excursion);
                    finish();
                }
            }
        });
    }
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.share) {
                Intent sendIntent=new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String t=editTitle.getText().toString();
                String d=editDate.getText().toString();
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Excursion Name: "+t+
                        " Date: "+d);
                sendIntent.putExtra(Intent.EXTRA_TITLE, t);
                sendIntent.setType("text/plain");
                Intent shareIntent=Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
}