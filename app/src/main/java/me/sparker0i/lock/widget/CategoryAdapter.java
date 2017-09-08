package me.sparker0i.lock.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import me.sparker0i.lawnchair.R;
import me.sparker0i.question.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategoriesList() {
        return categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.category.setText(categories.get(pos).getName());
        holder.chkSelected.setChecked(categories.get(pos).isSelected());
        holder.chkSelected.setTag(categories.get(pos));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Category cat = (Category) cb.getTag();

                cat.setSelected(cb.isChecked());
                categories.get(pos).setSelected(cb.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        CheckBox chkSelected;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            category = itemLayoutView.findViewById(R.id.cat_name);
            chkSelected = itemLayoutView.findViewById(R.id.cat_selected);
        }
    }
}
