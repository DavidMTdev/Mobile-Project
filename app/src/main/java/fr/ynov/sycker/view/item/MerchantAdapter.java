package fr.ynov.sycker.view.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import fr.ynov.sycker.models.merchant.Fields;

import fr.ynov.sycker.R;

public class MerchantAdapter extends ArrayAdapter<Fields> {

    // déclaration
    private int resId;

    public MerchantAdapter(@NonNull Context context, int resource, @NonNull List<Fields> objects) {
        super(context, resource, objects);

        resId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // déclaration
        ViewHolder myViewHolder;

        if(convertView == null) {
            // affichage du layout item_restaurant
            convertView = LayoutInflater.from(getContext()).inflate(resId, null); // R.layout.item_restaurant

            myViewHolder = new ViewHolder(); // instance

            // récupération des vues
            myViewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            myViewHolder.textViewType = convertView.findViewById(R.id.textViewType);

            // enregistrement de l'objet myViewHolder dans le convertView
            convertView.setTag(myViewHolder);
        } else {

            // récupration de l'objet myViewHolder
            myViewHolder = (ViewHolder) convertView.getTag();
        }

        // Objet Restaurant
        Fields item = getItem(position);

        // affichage des informations
        myViewHolder.textViewName.setText(item.getNom_du_commerce());
        myViewHolder.textViewType.setText(item.getType_de_commerce());

        return convertView;

    }

    class ViewHolder {
        TextView textViewName, textViewType;
    }
}
