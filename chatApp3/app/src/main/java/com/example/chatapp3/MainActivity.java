package com.example.chatapp3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final int RC_GET_IMAGE = 101;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference reference;
    private RecyclerView recyclerViewMessages;
    private MessagesAdapter adapter;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private ImageView imageViewAddImage;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Create a storage reference from our app
        reference = storage.getReference();

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        adapter = new MessagesAdapter(this);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(adapter);
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(editTextMessage.getText().toString().trim(), null);
            }
        });
        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, RC_GET_IMAGE);
            }
        });
        if (mAuth.getCurrentUser() != null) {
            preferences.edit().putString("author", mAuth.getCurrentUser().getDisplayName()).apply();
        } else {
            signOut();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_signOut) {
            mAuth.signOut();
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMessage(String textOfMessage, String urlToImage) {
        Message message = null;
        String author = preferences.getString("author", "Anonim");
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            message = new Message(author, textOfMessage, System.currentTimeMillis(), null);
        } else if (urlToImage != null && !urlToImage.isEmpty()) {
            message = new Message(author, null, System.currentTimeMillis(), urlToImage);
        }
        db.collection("messages")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        editTextMessage.setText("");
                        recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Ошибка при отправке", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("messages")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            List<Message> messages = value.toObjects(Message.class);
                            adapter.setMessages(messages);
                            recyclerViewMessages.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                final Uri uri = data.getData();
                if (uri != null) {
                    final StorageReference referenceToImage = reference.child("images/" + uri.getLastPathSegment());
                    referenceToImage.putFile(uri)
                            .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }

                                    // Continue with the task to get the download URL
                                    return referenceToImage.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        if (downloadUri != null) {
                                            sendMessage(null, downloadUri.toString());
                                        }
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            });
                }
            }
        }

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    preferences.edit().putString("author", user.getDisplayName()).apply();
                }
            } else {
                if (response != null) {
                    Toast.makeText(this, "Error: " + response.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Choose authentication providers
                            List<AuthUI.IdpConfig> providers = Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build());
                            startActivityForResult(
                                    AuthUI.getInstance()
                                            .createSignInIntentBuilder()
                                            .setAvailableProviders(providers)
                                            .build(),
                                    RC_SIGN_IN
                            );
                        }
                    }
                });
    }
}