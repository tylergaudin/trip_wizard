package android.schedulertyler.tripwizard.UI;

import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.schedulertyler.tripwizard.R;
import android.schedulertyler.tripwizard.entities.Vacation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        private VacationViewHolder(View itemview) {
            super(itemview);
            vacationItemView = itemview.findViewById(R.id.textView);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("title", current.getVacationTitle());
                    intent.putExtra("lodging", current.getLodging());
                    intent.putExtra("start_date", current.getStartDate());
                    intent.putExtra("end_date", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder
            (@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);
            String name = current.getVacationTitle();
            holder.vacationItemView.setText(name);
        } else {
            String empty = "No Vacation Name";
            holder.vacationItemView.setText(empty);
        }
    }

    @Override
    public int getItemCount() {
        return mVacations.size();
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}
