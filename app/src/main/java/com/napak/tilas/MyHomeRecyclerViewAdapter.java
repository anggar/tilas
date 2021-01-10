package com.napak.tilas;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.napak.tilas.api.ApiClient;
import com.napak.tilas.databinding.FragmentPhotoBinding;
import com.napak.tilas.dummy.DummyContent.DummyItem;
import com.napak.tilas.model.PhotoListItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 */
public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.ViewHolder> {

    private final List<PhotoListItem> values;
    private FragmentPhotoBinding binding;

    public MyHomeRecyclerViewAdapter(List<PhotoListItem> items) {
        values = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = FragmentPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        binding = null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = values.get(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PhotoListItem item;
        private final FragmentPhotoBinding binding;

        public ViewHolder(FragmentPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            String url = ApiClient.baseUrl + item.getPhoto_url();
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault());


            Picasso.get().load(Uri.parse(url)).into(binding.ivPhoto);
            binding.tvCaption.setText(item.getTitle());
            binding.tvTimestamp.setText(format.format(item.getCreated_at()));
            binding.ivPhoto.setOnClickListener(view -> {
                Intent intent = new Intent(binding.getRoot().getContext(), DetailActivity.class);
                intent.putExtra("id", item.getId());
                binding.getRoot().getContext().startActivity(intent);
            });
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + binding.getRoot().toString() + "'";
        }
    }
}