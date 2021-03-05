package com.example.finalandroidassignment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class CityHomeAdapter extends RecyclerView.Adapter<CityHomeAdapter.ViewHolder>{

    private List<CityModel> cityModelList;
    private FirebaseFirestore firebaseFirestore;

    public CityHomeAdapter(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    @NonNull
    @Override
    public CityHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityHomeAdapter.ViewHolder holder, int position) {
        String resource=cityModelList.get(position).getCityImage();
        String title=cityModelList.get(position).getCityName();
        String id=cityModelList.get(position).getCityId();

        holder.setCityImage(resource,title);
        holder.setCityName(title);
    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private ConstraintLayout container;
        private ImageView cityImage;
        private TextView cityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            cityImage=itemView.findViewById(R.id.city_image);
            cityName=itemView.findViewById(R.id.city_name);
        }


        private void setCityImage(String resource, final String cityID){
            if(!resource.equals("null")){
                Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.back)).into(cityImage);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cityDetailsIntent=new Intent(itemView.getContext(),CityActivity.class);
                    //  categoryIntent.putExtra("CategoryName","name");
                    cityDetailsIntent.putExtra("CITY_ID",cityID);
                    System.out.println("id "+cityID);
                    //productDetailsIntent.putExtra("TYPE","1");
                    itemView.getContext().startActivity(cityDetailsIntent);
                }
            });

        }
        private void setCityName(String title){
            cityName.setText(title);
        }
    }


}
