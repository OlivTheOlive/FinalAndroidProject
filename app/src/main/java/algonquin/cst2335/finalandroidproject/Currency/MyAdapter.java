package algonquin.cst2335.finalandroidproject.Currency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import algonquin.cst2335.finalandroidproject.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CurrencySelected> conversionResultsList;

    public MyAdapter(Context context, ArrayList<CurrencySelected> conversionResultsList) {
        this.context = context;
        this.conversionResultsList = conversionResultsList;
    }

    public void updateList(ArrayList<CurrencySelected> newList) {
        conversionResultsList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_currency_converter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrencySelected currencySelected = conversionResultsList.get(position);
        holder.bind(currencySelected);
    }

    @Override
    public int getItemCount() {
        return conversionResultsList.size();
    }

    public int getPosition(CurrencySelected item) {
        return conversionResultsList.indexOf(item);
    }

    public void removeAt(int position) {
        conversionResultsList.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView conversionResult;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conversionResult = itemView.findViewById(R.id.conversionResult);
            time = itemView.findViewById(R.id.time);
        }

        public void bind(CurrencySelected currencySelected) {
            conversionResult.setText(currencySelected.getConversionResult());
            time.setText(currencySelected.getTime());
        }
    }
}
