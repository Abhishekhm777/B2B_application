package com.example.compaq.b2b_application.chips_package;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.compaq.b2b_application.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionsAdapter extends BaseAdapter implements Filterable {
  private LayoutInflater layoutInflater;
  private List<ChipItem> categoryTags;
  private List<ChipItem> filterableTags;

  public SuggestionsAdapter(Context context, List<ChipItem> categoryTags) {
    this.categoryTags = new ArrayList<>(categoryTags);
    this.filterableTags = new ArrayList<>(categoryTags);
    layoutInflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {
    return filterableTags.size();
  }

  @Override public Object getItem(int i) {
    return filterableTags.get(i);
  }

  @Override public long getItemId(int i) {
    return filterableTags.get(i).getId();
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    ChipItem categoryTag = filterableTags.get(i);
    TagsHolder tagsHolder;

    if (view == null) {
      view = layoutInflater.inflate(R.layout.item_chip_drop_down, viewGroup, false);
      tagsHolder = new TagsHolder(view);
      view.setTag(tagsHolder);
    } else {
      tagsHolder = (TagsHolder) view.getTag();
    }

    tagsHolder.title.setText(categoryTag.getTitle());
    return view;
  }

  @Override public Filter getFilter() {
    return new TagsFilter();
  }

  private final class TagsFilter extends Filter {

    @Override protected FilterResults performFiltering(CharSequence charSequence) {
      final FilterResults results = new FilterResults();
      List<ChipItem> list = new ArrayList<>();

      for (int i = 0; i < categoryTags.size(); i++) {
        if (categoryTags.get(i).getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
          list.add(categoryTags.get(i));
        }
      }

      results.values = list;
      results.count = list.size();
      return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
      if (filterResults.values == null) return;

      filterableTags.clear();
      List<ChipItem> list = (List<ChipItem>) filterResults.values;
      if (!list.isEmpty()) {
        filterableTags.addAll(list);
      }
      notifyDataSetChanged();
    }
  }

  class TagsHolder {
    @BindView(R.id.title) TextView title;

    TagsHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
