package com.example.grp2_foodbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends BaseAdapter implements Filterable {

    Context context;
    private List<RestaurantModel> restaurantModel;
    private List<RestaurantModel> restaurantModelFiltered;

    LayoutInflater inflater;

    public GridAdapter(Context context, List<RestaurantModel>restaurantModelList){
        this.context = context;
        this.restaurantModel = restaurantModelList;
        this.restaurantModelFiltered = restaurantModelList;
    }


    @Override
    public int getCount() {
        return restaurantModelFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantModelFiltered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(view == null){
            view = inflater.inflate(R.layout.grid_item, null);
        }

        ImageView imageView = view.findViewById(R.id.grid_image);
        TextView textView = view.findViewById(R.id.item_name);

        imageView.setImageResource(restaurantModelFiltered.get(i).getImage());
        textView.setText(restaurantModelFiltered.get(i).getName());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = restaurantModel.size();
                    filterResults.values = restaurantModel;

                }else{
                    List<RestaurantModel> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(RestaurantModel itemsModel:restaurantModel){
                        if(itemsModel.getName().toLowerCase().contains(searchStr.toLowerCase())){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                restaurantModelFiltered = (List<RestaurantModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
