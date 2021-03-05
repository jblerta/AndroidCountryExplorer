package com.example.finalandroidassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<CommentModel> commentModelList;
    private FirebaseFirestore firebaseFirestore;

    public CommentsAdapter(List<CommentModel> commentModelList) {
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout,parent,false);

        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {

        String name=commentModelList.get(position).getName();
        String comment=commentModelList.get(position).getComment();
        String date=commentModelList.get(position).getDate();

        holder.setName(name);
        holder.setComment(comment);
        holder.setDate(date);

    }

    @Override
    public int getItemCount() {
        return commentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout container;
        private TextView name,date;
        private TextView comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            name=itemView.findViewById(R.id.nameUser);
            comment=itemView.findViewById(R.id.commentUser);
            date=itemView.findViewById(R.id.date);
        }
        private void setName(String nameUser){
            name.setText(nameUser);
        }
        private void setDate(String date1){
            date.setText(date1);
        }
        private void setComment(String comment1){
            comment.setText(comment1);
        }



    }
}
