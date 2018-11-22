package m.gp.paneldomotica;

/**
 * Created by mac on 16/10/18.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class adaptadorHabitaciones extends RecyclerView.Adapter<adaptadorHabitaciones.habitacionViewHolder> {

    private List<habitacion> items;

    public static class habitacionViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre, tvFocos;

        public habitacionViewHolder(View v) {
            super(v);
            imagen =  v.findViewById(R.id.ivImagen);
            nombre =  v.findViewById(R.id.tvSsid);
            tvFocos= v.findViewById(R.id.tvFocos);
        }
    }

    public adaptadorHabitaciones(List<habitacion> items){
        this.items = items;
    }

    @Override
    public habitacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_habitaciones, viewGroup, false);
        return new habitacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(habitacionViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.tvFocos.setText(""+items.get(i).getnFocos());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}

