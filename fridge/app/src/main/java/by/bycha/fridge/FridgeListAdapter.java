package by.bycha.fridge;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class FridgeListAdapter extends RecyclerView.Adapter<FridgeListAdapter.FridgeViewHolder>{
    private ArrayList<String> data;

    public FridgeListAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public FridgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_item , parent , false);
        return new FridgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FridgeViewHolder holder, int position) {
        holder.text.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FridgeViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        TextView text;
        public FridgeViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cardView);
            text = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
