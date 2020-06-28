package com.example.knitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<Post> posts;
    private Context context;
    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        posts = new ArrayList<>();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }

        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_item_post, parent, false);
        viewHolder = new PostVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Post postItem = posts.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                PostVH postVH = (PostVH) holder;

                postVH.title.setText(postItem.getTitle());
                postVH.userId.setText("USER ID : "+postItem.getUser_id().toString());
                postVH.description.setText(postItem.getBody());
                postVH.postId.setText("POST ID : "+postItem.getId().toString());

                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == posts.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Post postAdd) {
        posts.add(postAdd);
        notifyItemInserted(posts.size() - 1);
    }

    public void addAll(List<Post> postList) {
        for (Post postAdd : postList) {
            add(postAdd);
        }
    }

    public void remove(Post postRemove) {
        int position = posts.indexOf(postRemove);
        if (position > -1) {
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Post());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = posts.size() - 1;
        Post item = getItem(position);

        if (item != null) {
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Post getItem(int position) {
        return posts.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class PostVH extends RecyclerView.ViewHolder {
        LinearLayout postLayout;
        TextView title;
        TextView userId;
        TextView description;
        TextView postId;
        public CardView cardView;
        Button buttonToggle;

        public PostVH(View v) {
            super(v);
            postLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            title = (TextView) v.findViewById(R.id.title);
            userId = (TextView) v.findViewById(R.id.userId);
            description = (TextView) v.findViewById(R.id.description);
            postId = (TextView) v.findViewById(R.id.postId);
            buttonToggle = v.findViewById(R.id.button_toggle);
            cardView=(CardView) v.findViewById(R.id.card_view);
            cardView.setRadius(30);
            cardView.setCardElevation(10);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}