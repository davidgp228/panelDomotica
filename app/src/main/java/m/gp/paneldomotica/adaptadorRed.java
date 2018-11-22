package m.gp.paneldomotica;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mac on 29/10/18.
 */

public class adaptadorRed extends RecyclerView.Adapter<adaptadorRed.redViewHolder> {

    private List<red> items;

    public static class redViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView ssid;

        public redViewHolder(View v) {
            super(v);
            ssid =  v.findViewById(R.id.tvSsid);
        }
    }

    public adaptadorRed(List<red> items){
        this.items = items;
    }

    @Override
    public adaptadorRed.redViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_red, viewGroup, false);
        return new adaptadorRed.redViewHolder(v);
    }

    @Override
    public void onBindViewHolder(adaptadorRed.redViewHolder viewHolder, int i) {
        viewHolder.ssid.setText(items.get(i).getSsid());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
