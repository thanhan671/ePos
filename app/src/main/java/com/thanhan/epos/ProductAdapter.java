package com.thanhan.epos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductObj> mProductList;
    private IClickProductListener mIClickProductListener;

    public interface IClickProductListener {
        void onClickUpdateProduct(ProductObj pro);

        void onClickDeleteProduct(ProductObj pro);
    }

    public ProductAdapter(List<ProductObj> mProductList, IClickProductListener listener) {
        this.mProductList = mProductList;
        this.mIClickProductListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductObj pro = mProductList.get(position);
        if (pro == null) {
            return;
        }

        holder.tvId.setText("ID: " + pro.getId());
        holder.tvMaCode.setText("Mã code: " + pro.getMaCode());
        holder.tvTenHH.setText("Tên HH: " + pro.getTenHangHoa());
        holder.tvLoaiHH.setText("Loại HH: " + pro.getLoaiHangHoa());
        holder.tvDVTinh.setText("Đơn vị tính: " + pro.getDonViTinh());
        holder.tvDGNhap.setText("ĐG nhập: " + pro.getDonGiaNhap());
        holder.tvDGXuat.setText("ĐG xuất: " + pro.getDonGiaXuat());
        holder.tvTonKho.setText("Tồn kho: " + pro.getTonKho());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickProductListener.onClickUpdateProduct(pro);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickProductListener.onClickDeleteProduct(pro);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mProductList != null) {
            return mProductList.size();
        }

        return 0;
    }

    public class ProductViewHolder extends RecyclerView

            .ViewHolder {
        private TextView tvId, tvMaCode, tvTenHH, tvLoaiHH, tvDVTinh, tvDGNhap, tvDGXuat, tvTonKho;
        private Button btnUpdate, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = (TextView) itemView.findViewById(R.id.tv_itemProduct_id);
            tvMaCode = (TextView) itemView.findViewById(R.id.tv_itemProduct_maCode);
            tvTenHH = (TextView) itemView.findViewById(R.id.tv_itemProduct_tenHH);
            tvLoaiHH = (TextView) itemView.findViewById(R.id.tv_itemProduct_loaiHH);
            tvDVTinh = (TextView) itemView.findViewById(R.id.tv_itemProduct_dvTinh);
            tvDGNhap = (TextView) itemView.findViewById(R.id.tv_itemProduct_dgNhap);
            tvDGXuat = (TextView) itemView.findViewById(R.id.tv_itemProduct_dgXuat);
            tvTonKho = (TextView) itemView.findViewById(R.id.tv_itemProduct_tonKho);

            btnUpdate = (Button) itemView.findViewById(R.id.btn_itemProduct_update);
            btnDelete = (Button) itemView.findViewById(R.id.btn_itemProduct_delete);
        }
    }
}
