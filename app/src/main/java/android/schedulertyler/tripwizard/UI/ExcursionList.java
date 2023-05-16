package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.schedulertyler.tripwizard.R;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExcursionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExcursionList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
    }
}