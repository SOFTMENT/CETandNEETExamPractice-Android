package in.softment.exampractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;

public class SignInActivity extends AppCompatActivity {
    private EditText inputcode1, inputcode2, inputcode3, inputcode4,inputcode5,inputcode6;
    private String verificationId;

    EditText mobileNumberET;
    LinearLayout otpLL;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            try {
                FirebaseAuth.getInstance().signOut();
            }
            catch (Exception ignored) {

            }

        }

        mobileNumberET = findViewById(R.id.mobileNumberET);
        otpLL = findViewById(R.id.otpLL);
        mAuth = FirebaseAuth.getInstance();

        inputcode1 = findViewById(R.id.code1);
        inputcode2 = findViewById(R.id.code2);
        inputcode3 = findViewById(R.id.code3);
        inputcode4 = findViewById(R.id.code4);
        inputcode5 = findViewById(R.id.code5);
        inputcode6 = findViewById(R.id.code6);
        setupotpinput();
        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mobileNumberET.getText().toString().trim().isEmpty()){
                    if ((mobileNumberET.getText().toString().trim()).length() == 10){

                        ProgressHud.show(SignInActivity.this,"Sending...");

                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+91"+mobileNumberET.getText().toString())
                                        .setTimeout(60L, TimeUnit.SECONDS)
                                        .setActivity(SignInActivity.this)
                                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                ProgressHud.dialog.dismiss();
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                                ProgressHud.dialog.dismiss();
                                                Services.showDialog(SignInActivity.this,"ERROR",e.getMessage());
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                ProgressHud.dialog.dismiss();
                                                otpLL.setVisibility(View.VISIBLE);
                                                verificationId = s;
                                                inputcode1.requestFocus();

                                                Services.showCenterToast(SignInActivity.this,"Verification code sent");
                                            }
                                        })
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);




                    }else {
                        Toast.makeText(SignInActivity.this,"Please enter correct number",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignInActivity.this,"Enter Mobile number",Toast.LENGTH_SHORT).show();
                }
            }
            }
        );


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputcode1.getText().toString().trim().isEmpty()
                        && !inputcode2.getText().toString().trim().isEmpty()
                        && !inputcode3.getText().toString().trim().isEmpty()
                        && !inputcode4.getText().toString().trim().isEmpty()
                        && !inputcode5.getText().toString().trim().isEmpty()
                        && !inputcode6.getText().toString().trim().isEmpty()
                        ){

                    String code = inputcode1.getText().toString() +
                            inputcode2.getText().toString() +
                            inputcode3.getText().toString() +
                            inputcode4.getText().toString() +
                            inputcode5.getText().toString() +
                            inputcode6.getText().toString();

                    if (verificationId != null) {
                        ProgressHud.show(SignInActivity.this,"Verifying...");
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationId, code
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        ProgressHud.dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Services.getCurrentUserData(SignInActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                                        } else {
                                            Toast.makeText(SignInActivity.this, "Enter the Correct otp", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }else {
                    Toast.makeText(SignInActivity.this, "Please enter the otp", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setupotpinput() {
        inputcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputcode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






    }
}
