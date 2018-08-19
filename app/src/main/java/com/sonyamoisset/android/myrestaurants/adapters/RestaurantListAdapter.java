package com.sonyamoisset.android.myrestaurants.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sonyamoisset.android.myrestaurants.R;
import com.sonyamoisset.android.myrestaurants.models.Restaurant;
import com.sonyamoisset.android.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private ArrayList<Restaurant> mRestaurants;
    private Context mContext;

    public RestaurantListAdapter(Context context, ArrayList<Restaurant> restaurants) {
        mContext = context;
        mRestaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.restaurant_list_item, parent, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder holder,
                                 int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.restaurantImageView)
        ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView)
        TextView mNameTextView;
        @BindView(R.id.categoryTextView)
        TextView mCategoryTextView;
        @BindView(R.id.ratingTextView)
        TextView mRatingTextView;

        private Context mContext;

        private RestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        private void bindRestaurant(Restaurant restaurant) {
            Picasso.get()
                    .load(restaurant.getImageUrl())
                    .into(mRestaurantImageView);

            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0));
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();

            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position", itemPosition + "");
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));

            mContext.startActivity(intent);
        }
    }
}

