package com.thanhan.epos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PTAdapter extends RecyclerView.Adapter<PTAdapter.PTViewHolder> {
    private List<ProductType> mPTList;
    private IClickPTListener mIClickListener;

    public interface IClickPTListener {
        void onClickEditPT(ProductType pt);
        void onClickDeletePT(ProductType pt);
    }

    public PTAdapter(List<ProductType> mPTList, IClickPTListener listener) {
        this.mPTList = mPTList;
        this.mIClickListener = listener;
    }

    @NonNull
    @Override
    public PTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pt, parent, false);
        return new PTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PTViewHolder holder, int position) {
        ProductType pt = mPTList.get(position);
        if (pt == null) {
            return;
        }
        holder.tvId.setText("ID: " + pt.getId());
        holder.tvName.setText("Tên loại SP: " + pt.getTypeName());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickEditPT(pt);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickDeletePT(pt);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPTList != null) {
            return mPTList.size();
        }
        return 0;
    }

    public class PTViewHolder extends RecyclerView

            .ViewHolder {
        private TextView tvId, tvName;
        private Button btnUpdate, btnDelete;

        public PTViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_itemPT_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_itemPT_name);
            btnUpdate = (Button) itemView.findViewById(R.id.btn_itemPT_update);
            btnDelete = (Button) itemView.findViewById(R.id.btn_itemPT_delete);
        }
    }
}
