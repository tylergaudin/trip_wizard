package android.schedulertyler.tripwizard.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.schedulertyler.tripwizard.R;
import android.schedulertyler.tripwizard.database.Repository;
import android.schedulertyler.tripwizard.entities.Excursion;
import android.schedulertyler.tripwizard.entities.Vacation;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        RecyclerView recyclerView = findViewById(R.id.vacationrecyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getAllVacations();
        List<Vacation> filteredVacations = new ArrayList<>();
        List<Excursion> allExcursions = repository.getAllExcursions();
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        vacationAdapter.setVacations(allVacations);
        excursionAdapter.setExcursions(allExcursions);
        editSearch = findViewById(R.id.search);


        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredVacations.clear();

                    for (Vacation vac: repository.getAllVacations()){
                        if (vac.getVacationTitle().toLowerCase().contains
                                (editSearch.getText().toString().toLowerCase())){
                            filteredVacations.add(vac);
                        }
                    }
                    vacationAdapter.setVacations(filteredVacations);
                    recyclerView.setAdapter(vacationAdapter);
                }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        List<Vacation> allVacations = repository.getAllVacations();
        List<Excursion> allExcursions = repository.getAllExcursions();
        RecyclerView recyclerView = findViewById(R.id.vacationrecyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
        excursionAdapter.setExcursions(allExcursions);
    }
}