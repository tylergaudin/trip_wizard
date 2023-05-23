package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.schedulertyler.tripwizard.R;
import android.schedulertyler.tripwizard.database.Repository;
import android.schedulertyler.tripwizard.entities.Excursion;
import android.schedulertyler.tripwizard.entities.Vacation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    EditText editTitle;
    TextView editDate;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendar = Calendar.getInstance();
    String title;
    String date;
    int vacationId;
    int id;
    Excursion excursion;
    Excursion currentExcursion;
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
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<Vacation> vacationArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllVacations());
        spinner.setAdapter(vacationArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info = editDate.getText().toString();
                if (info.equals("")) info = String.valueOf(new Date());
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
                try {
                    updateEditText();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            private void updateEditText() throws ParseException {
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Vacation currentVacation = (Vacation) spinner.getSelectedItem();
                Date start = new Date();
                Date end = new Date();
                for (Vacation vacation : repository.getAllVacations()) {
                    if (currentVacation.getVacationID() == vacation.getVacationID()) {
                        start = sdf.parse(vacation.getStartDate());
                        end = sdf.parse(vacation.getEndDate());
                    }
                }
                if (myCalendar.getTime().before(start) || myCalendar.getTime().after(end)) {
                    Toast.makeText(ExcursionDetails.this,
                            "The Excursion must be scheduled during the vacation.",
                            Toast.LENGTH_LONG).show();
                }
                editDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        Button button = findViewById(R.id.saveexcursion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vacation vacation = (Vacation) spinner.getSelectedItem();
                if (id == -1) {
                    excursion = new Excursion(0, editTitle.getText().toString(),
                            editDate.getText().toString(), vacationId);
                    repository.insert(excursion);
                    finish();

                } else {
                    excursion = new Excursion(id, editTitle.getText().toString(),
                            editDate.getText().toString(), vacation.getVacationID());
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
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String t = editTitle.getText().toString();
            String d = editDate.getText().toString();
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Excursion Name: " + t +
                    " Date: " + d);
            sendIntent.putExtra(Intent.EXTRA_TITLE, t);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify_excursion) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String note = editTitle.getText().toString();
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long trigger = myDate.getTime();
            String message = "Your " + note + " is today.";
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", message);
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            return true;
        }
        if (item.getItemId() == R.id.delete_vacation) {
            for (Excursion exc : repository.getAllExcursions()) {
                if (exc.getExcursionID() == id) currentExcursion = exc;
            }
            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionTitle()
                    + " was deleted.", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}