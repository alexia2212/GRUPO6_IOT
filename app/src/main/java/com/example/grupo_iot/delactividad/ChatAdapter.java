package com.example.grupo_iot.delactividad;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grupo_iot.R;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Locale;

import java.util.List;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<ChatMessage> chatMessages;




    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16); // Ajusta el valor de margen inferior según sea necesario
        view.setLayoutParams(layoutParams);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        if (message.getTimestamp() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            dateFormat.setTimeZone(TimeZone.getDefault());
            String formattedDate = dateFormat.format(message.getTimestamp());
            holder.horaMensaje.setText(formattedDate);
        } else {
            holder.horaMensaje.setText("Cargando...");
        }

        if (message.getSenderId() != null && message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.layoutmsj.setGravity(Gravity.END);
            Glide.with(holder.itemView.getContext())
                    .load(message.getImagen())
                    .placeholder(R.drawable.usuariosinfoto)
                    .into(holder.clienteLogo);
            holder.nombreMensaje.setText(message.getNombre());
            holder.mensajeMensaje.setText(message.getMessage());
            holder.managerLogo.setVisibility(View.INVISIBLE);

        } else {
            holder.layoutmsj.setGravity(Gravity.START);
            holder.clienteLogo.setVisibility(View.INVISIBLE);
            holder.nombreMensaje.setText(message.getNombre());
            holder.mensajeMensaje.setText(message.getMessage());
            Glide.with(holder.itemView.getContext())
                    .load(message.getImagen())
                    .placeholder(R.drawable.usuariosinfoto)
                    .into(holder.managerLogo);
        }
    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutCajita;

        ImageView managerLogo;
        ImageView clienteLogo;
        TextView nombreMensaje;
        TextView horaMensaje;
        TextView mensajeMensaje;

        LinearLayout layoutmsj;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutCajita = itemView.findViewById(R.id.layoutCajita);
            managerLogo = itemView.findViewById(R.id.managerLogo);
            clienteLogo = itemView.findViewById(R.id.clienteLogo);
            nombreMensaje = itemView.findViewById(R.id.nombreMensaje);
            horaMensaje = itemView.findViewById(R.id.horaMensaje);
            mensajeMensaje = itemView.findViewById(R.id.mensajeMensaje);
            layoutmsj = itemView.findViewById(R.id.layoutMsg);
        }
    }
}
