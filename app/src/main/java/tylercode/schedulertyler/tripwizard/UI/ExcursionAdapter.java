package tylercode.schedulertyler.tripwizard.UI;

import android.content.Context;
import android.content.Intent;
import tylercode.schedulertyler.tripwizard.R;
import tylercode.schedulertyler.tripwizard.entities.Excursion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        private ExcursionViewHolder(View itemview) {
            super(itemview);
            excursionItemView = itemview.findViewById(R.id.textViewexcursiontitle);
            excursionItemView2 = itemview.findViewById(R.id.textViewexcursiondate);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("title", current.getExcursionTitle());
                    intent.putExtra("date", current.getExcursionDate());
                    intent.putExtra("vacation_id", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder
            (@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if (mExcursions != null) {
            Excursion current = mExcursions.get(position);
            String title = current.getExcursionTitle();
            String date = current.getExcursionDate();
            holder.excursionItemView.setText(title);
            holder.excursionItemView2.setText(date);
        } else {
            String empty = "No Excursion Name";
            holder.excursionItemView.setText(empty);
            String empty2 = "No Vacation ID";
            holder.excursionItemView.setText(empty2);
        }
    }

    @Override
    public int getItemCount() {
        return mExcursions.size();
    }

    public void setExcursions(List<Excursion> excursions) {
        mExcursions = excursions;
        notifyDataSetChanged();
    }
}
