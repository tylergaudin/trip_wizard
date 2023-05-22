package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;

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

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        /*Vacation vacation=new Vacation(0, "Taniti", "Marriott",
                "2024-01-22","2024-01-24");
        Repository repository=new Repository(getApplication());
        repository.insert(vacation);*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, VacationList.class);
                startActivity(intent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addSampleData) {

                Vacation vacation=new Vacation(0, "Taniti", "Marriott",
                        "2024-01-22","2024-01-24");
                Repository repository=new Repository(getApplication());
                repository.insert(vacation);
                Excursion excursion=new Excursion(0, "Snorkeling",
                        "2024-01-23", 0);
                repository.insert(excursion);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}