package com.myappcompany.rajan.aai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {

    private EditText mComplaintEditText;
    private Button mSubmitButton;

    private FirebaseFirestore db;
    private String sn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        sn = getIntent().getStringExtra("EQUIPMENT_UUID");
        mComplaintEditText = findViewById(R.id.complaint_edit_text);
        mSubmitButton = findViewById(R.id.submit_button);

        db = FirebaseFirestore.getInstance();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = mComplaintEditText.getText().toString();

                if(TextUtils.isEmpty(s)) {
                    Toast.makeText(ComplaintActivity.this, "Add some complaint first!", Toast.LENGTH_LONG).show();
                }
                else {
                    fileComplaint(s);
                }
            }
        });
    }

    private void fileComplaint(String s) {

        Map<String, Object> map = new HashMap<>();
        map.put("description", s);
        map.put("sn", sn);

        db.collection("complaints")
                .document(sn)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ComplaintActivity.this, "Complaint filed successfully!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ComplaintActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}