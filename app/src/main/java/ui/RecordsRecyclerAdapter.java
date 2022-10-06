package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malik.msd.R;

import java.util.List;

import model.Entry;

public class RecordsRecyclerAdapter extends RecyclerView.Adapter<RecordsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Entry> entryList;

    public RecordsRecyclerAdapter(Context context, List<Entry> entryList) {
        this.context = context;
        this.entryList = entryList;
    }

    @NonNull
    @Override
    public RecordsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_record, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsRecyclerAdapter.ViewHolder viewHolder, int position) {
        Entry entry = entryList.get(position);


        viewHolder.tvRecordRowDate.setText(entry.getDate());
        viewHolder.tvRecordRowTime.setText(entry.getTime());
        viewHolder.tvRecordRowRate.setText(String.valueOf(entry.getRate()));
        viewHolder.tvRecordRowMann.setText(String.valueOf(entry.getMann()));
        viewHolder.tvRecordRowSair.setText(String.valueOf(entry.getSair()));
        viewHolder.tvRecordRowAmount.setText(String.valueOf(entry.getAmount()));

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRecordRowDate, tvRecordRowTime
                ,tvRecordRowRate, tvRecordRowMann
                ,tvRecordRowSair,tvRecordRowAmount;

        String userId;
        String username;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            tvRecordRowDate = itemView.findViewById(R.id.tvRecordRowDate);
            tvRecordRowTime = itemView.findViewById(R.id.tvRecordRowTime);
            tvRecordRowRate = itemView.findViewById(R.id.tvRecordRowRate);
            tvRecordRowMann = itemView.findViewById(R.id.tvRecordRowMann);
            tvRecordRowSair = itemView.findViewById(R.id.tvRecordRowSair);
            tvRecordRowAmount = itemView.findViewById(R.id.tvRecordRowAmount);



        }
    }
}
