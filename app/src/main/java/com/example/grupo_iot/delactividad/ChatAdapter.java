package com.example.grupo_iot.delactividad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<ChatMessage> chatMessages;


    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del mensaje (puede ser el que proporcionaste anteriormente)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.getSender() != null && message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            // Si el mensaje es del usuario autenticado, mostrar en el contenedor de usuario autenticado
            holder.userMessageContainer.setVisibility(View.VISIBLE);
            holder.otherUserMessageContainer.setVisibility(View.GONE);
            holder.textViewMessage.setBackgroundResource(R.drawable.background7);
            holder.textViewSender.setText(message.getSender());
            holder.textViewMessage.setText(message.getMessage());
        } else {
            // Si el mensaje es de otro usuario, mostrar en el contenedor de otros usuarios
            holder.userMessageContainer.setVisibility(View.GONE);
            holder.otherUserMessageContainer.setVisibility(View.VISIBLE);

            holder.textViewOtherUser.setText(message.getSender());
            holder.textViewOtherMessage.setText(message.getMessage());
            holder.textViewMessage.setBackgroundResource(R.drawable.background8);

        }
    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSender;
        TextView textViewMessage;

        LinearLayout userMessageContainer;
        LinearLayout otherUserMessageContainer;

        TextView textViewOtherUser;

        TextView textViewOtherMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSender = itemView.findViewById(R.id.textViewSender);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            userMessageContainer = itemView.findViewById(R.id.userMessageContainer);
            otherUserMessageContainer = itemView.findViewById(R.id.otherUserMessageContainer);
            textViewOtherUser= itemView.findViewById(R.id.textViewOtherUser);
            textViewOtherMessage = itemView.findViewById(R.id.textViewOtherMessage);
        }
    }
}


