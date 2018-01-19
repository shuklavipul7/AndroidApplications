package group16.mad.com.tripvisor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Vipul.Shukla on 4/20/2017.
 */

public class MessageLayoutAdapter extends RecyclerView.Adapter<MessageLayoutAdapter.RowHolder>{
    private final User currentUser;
    private final Context context;
    private final Trip currentTrip;
    private ArrayList<Messages> itemList;
    TextView messageText;
    public View MessageLayoutAdapter;
    View itemView;
    ImageView imageMessage;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public class RowHolder extends RecyclerView.ViewHolder {

        public RowHolder(View itemView) {
            super(itemView);
            MessageLayoutAdapter.this.itemView = itemView;
            messageText = (TextView) itemView.findViewById(R.id.messageText);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);
        }
    }

    public MessageLayoutAdapter(ArrayList<Messages> itemList, User currentUser, Context context, Trip currentTrip) {
        this.itemList = itemList;
        this.currentUser = currentUser;
        this.context = context;
        this.currentTrip = currentTrip;
    }

    @Override
    public MessageLayoutAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageLayoutAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final Messages message = itemList.get(position);
        PrettyTime pt = new PrettyTime();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            if(message.getImage()==false) {
                imageMessage.setVisibility(View.GONE);
                messageText.setText(message.getText() + "\n" + "   -" + message.getUser().getfName() + " " + message.getUser().getlName() + ", " + pt.format(simpleDateFormat.parse(message.getTime())));
                if (message.getUser().equals(currentUser)) {
                    messageText.setTextColor(Color.BLUE);
                }
            }else{
                StorageReference imagePath = mStorageRef.child(currentTrip.getLocation()+"/" + message.getId() + "/trippic.png");
                imageMessage.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(imagePath)
                        .into(imageMessage);
                messageText.setText("   -" + message.getUser().getfName() + " " + message.getUser().getlName() + ", " + pt.format(simpleDateFormat.parse(message.getTime())));
                if (message.getUser().equals(currentUser)) {
                    messageText.setTextColor(Color.BLUE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
