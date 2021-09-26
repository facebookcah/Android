package vn.com.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.List;

import vn.com.concatapp.R;
import vn.com.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserviewHoder>
    implements StickyRecyclerHeadersAdapter<UserAdapter.HeaderViewHoder>
{
    private List<User> mListUser;
    private final IClickIconUser iClickIconUser;

    public UserAdapter(IClickIconUser iClickIconUser) {
        this.iClickIconUser = iClickIconUser;
    }

    public interface IClickIconUser
    {
        void updateUser(User user);
        void deleteUser(User user);
    }
    public void setData(List<User> list)
    {
        this.mListUser=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserviewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new UserviewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserviewHoder holder, int position) {
        User user=mListUser.get(position);
        if(user==null) return;
        holder.tvName.setText(user.getUserName());
        holder.tvSDT.setText(user.getSdt());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickIconUser.updateUser(user);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickIconUser.deleteUser(user);
            }
        });
    }

    @Override
    public long getHeaderId(int position) {
        return mListUser.get(position).getId();
    }

    @Override
    public HeaderViewHoder onCreateHeaderViewHolder(ViewGroup parent) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);

        return new HeaderViewHoder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHoder headerViewHoder, int i) {
        String nameHeader=mListUser.get(i).getUserName().toUpperCase().substring(0,1);
        headerViewHoder.tvheader.setText(nameHeader);
        headerViewHoder.tvheader.setBackgroundColor(getColor());
    }

    private int getColor() {
        SecureRandom secureRandom=new SecureRandom();
        return Color.HSVToColor(150,new float []
                {
                        secureRandom.nextInt(359),1,1
                });
    }

    @Override
    public int getItemCount() {
        if(mListUser!=null) return mListUser.size();
        return 0;
    }

    public class UserviewHoder extends RecyclerView.ViewHolder
    {

        private TextView tvName;
        private TextView tvSDT;
        private ImageView btnEdit,btnDelete,btnMore;

        public UserviewHoder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvSDT=itemView.findViewById(R.id.tvSDT);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnDelete=itemView.findViewById(R.id.btnDelete);
            btnMore=itemView.findViewById(R.id.btnMore);

        }

    }
    public class HeaderViewHoder extends RecyclerView.ViewHolder
    {

        private TextView tvheader;
        public HeaderViewHoder(@NonNull View itemView) {
            super(itemView);
            tvheader=itemView.findViewById(R.id.tvheader);
        }
    }
}
