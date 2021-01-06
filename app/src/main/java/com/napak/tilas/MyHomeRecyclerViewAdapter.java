package com.napak.tilas;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.napak.tilas.databinding.FragmentPhotoBinding;
import com.napak.tilas.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 */
public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> values;
    private FragmentPhotoBinding binding;

    public MyHomeRecyclerViewAdapter(List<DummyItem> items) {
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
        public DummyItem item;
        private final FragmentPhotoBinding binding;

        public ViewHolder(FragmentPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            Picasso.get().load(Uri.parse(item.photoUrl)).into(binding.ivPhoto);
            binding.tvCaption.setText(item.caption);
            binding.tvTimestamp.setText(item.details);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + binding.getRoot().toString() + "'";
        }
    }
}