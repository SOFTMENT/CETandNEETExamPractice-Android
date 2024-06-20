package in.softment.exampractice.Fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.w3c.dom.Text;

import in.softment.exampractice.BuildConfig;
import in.softment.exampractice.Model.UserModel;
import in.softment.exampractice.NotificationActivity;
import in.softment.exampractice.PrivacyPolicyActivity;
import in.softment.exampractice.R;
import in.softment.exampractice.SignInActivity;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;
import in.softment.exampractice.WelcomeActivity;


public class UserFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        RoundedImageView profileImage = view.findViewById(R.id.profileImage);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.emailAddress);
        TextView city = view.findViewById(R.id.city);

        if (!UserModel.data.profileImage.isEmpty()) {
            Glide.with(getContext()).load(UserModel.data.profileImage).placeholder(R.drawable.profile_placeholder).into(profileImage);
        }

        name.setText(UserModel.data.fullName);
        email.setText(UserModel.data.email);
        city.setText(UserModel.data.cityName);

        view.findViewById(R.id.shareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "CET abd NEET Exam Practice");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        view.findViewById(R.id.rateApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(intent);

            }
        });

        view.findViewById(R.id.delete_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
                builder.setTitle("Delete Account");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setCancelable(false);
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProgressHud.show(getContext(),"Deleting ...");
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(FirebaseAuth.getInstance().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ProgressHud.dialog.dismiss();
                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            startActivity(new Intent(getContext(), SignInActivity.class));

                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
                                            builder.setTitle("LOGIN AGAIN");
                                            builder.setMessage("Please login and try again delete account.");
                                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(getContext(), SignInActivity.class));

                                                }
                                            });

                                            builder.show();

                                        }
                                    }
                                });
                            }
                        });
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });

        view.findViewById(R.id.membership).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services.goMembership(getContext(), view);
            }
        });

        view.findViewById(R.id.help_cener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abc@gmail.com" });
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
            }
        });

        return view;
    }
}
