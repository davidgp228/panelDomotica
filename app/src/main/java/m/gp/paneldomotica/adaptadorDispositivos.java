package m.gp.paneldomotica;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mac on 16/10/18.
 */

public class adaptadorDispositivos extends RecyclerView.Adapter<adaptadorDispositivos.dispositivoViewHolder > {

    private List<dispositivo> items;

    public static class dispositivoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public RelativeLayout rlcontenedor;
        public ImageView imagen;
        public TextView nombre;
        public TextView nombrehabitacion;

        public dispositivoViewHolder(View v) {
            super(v);
            this.imagen =  v.findViewById(R.id.ivImagen);
            this.nombre =  v.findViewById(R.id.tvNombreDispositivo);
            this.nombrehabitacion = v.findViewById(R.id.tvNombreHabitacion);
            this.rlcontenedor=v.findViewById(R.id.rlContenedor);
        }
    }

    public adaptadorDispositivos(List<dispositivo> items){
        this.items = items;
    }

    @Override
    public adaptadorDispositivos.dispositivoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_dispositivos, viewGroup, false);
        return new adaptadorDispositivos.dispositivoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(dispositivoViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.rlcontenedor.setBackgroundResource(items.get(i).getImagen());
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
