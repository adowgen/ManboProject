package com.colorworld.manbocash.tutorial.tutorials;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.colorworld.manbocash.BranchViewActivity;
import com.colorworld.manbocash.Const;
import com.colorworld.manbocash.MainActivity;
import com.colorworld.manbocash.R;
import com.colorworld.manbocash.indicator.LoadingIndicator;
import com.colorworld.manbocash.model.Duplication;
import com.colorworld.manbocash.model.Member;
import com.colorworld.manbocash.retrofit.NetRetrofit;
import com.colorworld.manbocash.room.MyInfoDatabase;
import com.colorworld.manbocash.room.StepsDatabase;
import com.colorworld.manbocash.room.dao.MyInfoDao;
import com.colorworld.manbocash.room.dao.StepsDao;
import com.colorworld.manbocash.room.entity.MyInfoEntity;
import com.colorworld.manbocash.terms.DialogTerms;
import com.colorworld.manbocash.util.AppExecutors;
import com.colorworld.manbocash.util.Util;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.auth.authorization.accesstoken.AccessToken;
//import com.kakao.usermgmt.LoginButton;

import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
//import com.kakao.usermgmt.response.model.UserAccount;
//import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;

//import static com.kakao.util.helper.Utility.getPackageInfo;

public class TutorialSupportActivity extends AppCompatActivity implements View.OnClickListener {

    public static TutorialSupportActivity loginContext;

    public static int RC_SIGN_IN = 9009;
    public static final String KEY_ROLLBACK = "key_rollback";

    private Context mContext;
    private Activity mActivity;
    private boolean noRollback, isLogin = false;

    /*email - password*/
    private EditText email, password;
    private ImageView signInBtn;
    private TextView mSignUpBtn, mFindPWBtn;

    /*kakao*/
    public ImageView login_kakao;

    /*facebook*/
    public CallbackManager mCallbackManager;
    public ImageView faceBookLoginButton;
    private com.facebook.AccessToken getFacebookAccessToken;

    /* Firebase Auth */
    private FirebaseAuth mAuth;

    /*google*/
    public ImageView googleLoginBtn;
    private GoogleSignInClient mGoogleSignInClient;

    boolean mPressFirstBackKey = false;

    Button logoutButton;

    private InputMethodManager imm;

    public LoadingIndicator loadingIndicator;

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean noRollback) {
        Intent intent = new Intent(context, TutorialSupportActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_ROLLBACK, noRollback);
        context.startActivity(intent);
        Log.e("ios", "===");
    }


        /*
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.e("d", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
    */


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("ios", "ManboCash currentUser : " + currentUser);


        if (currentUser != null && !isLogin) {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            finish();
        }


        Display deviceScreen = getWindowManager().getDefaultDisplay();
        Point size = new Point();
//        deviceScreen.getSize(size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            deviceScreen.getRealSize(size);
        }
        int width = size.x;
        int height = size.y;

        Log.e("ios", "?????? : " + width + ", ?????? : " + height);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginContext = this;

        mContext = getApplicationContext();
        mActivity = this;


        setContentView(R.layout.activity_tutorial_and_login);

        loadingIndicator = new LoadingIndicator(this);
        loadingIndicator.setCancelable(false);

        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

//        Log.e("ios", "hash key : " + getKeyHash(mContext));
        Log.e("ios", "hash key : " + Utility.INSTANCE.getKeyHash(getApplicationContext()));

        email = (EditText) findViewById(R.id.login_id_et);
        password = (EditText) findViewById(R.id.login_pw_et);
        signInBtn = (ImageView) findViewById(R.id.login_signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //????????? ?????? ????????? ????????????
                loginEmailPassword();
            }
        });


        mFindPWBtn = (TextView) findViewById(R.id.login_findPW);
        mFindPWBtn.setPaintFlags(mFindPWBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mFindPWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String myEmail = "";
                new MaterialDialog.Builder(mActivity)
                        .title("???????????? ?????????")
                        .content("???????????? ???????????? ???????????? ????????? ????????? ??????????????????")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        .input("???????????? ??????????????????", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Log.e("ios", "CharSequence input : " + input);
                            }
                        }).positiveText("?????????")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                if (Util.validateEmail(dialog.getInputEditText().getText().toString())) {
                                    sendResetPWEmail(dialog.getInputEditText().getText().toString());
                                    dialog.dismiss();
                                } else {
                                    dialog.getInputEditText().setText("");
                                    new MaterialDialog.Builder(mActivity)
                                            .title("????????? ????????? ????????????")
                                            .positiveText("??????")
                                            .positiveColorRes(R.color.color_common)
                                            .show();
                                }

//
                            }
                        })
                        .positiveColorRes(R.color.color_common)
                        .widgetColorRes(R.color.color_common)
                        .autoDismiss(false)
                        .show();

            }
        });

        mSignUpBtn = (TextView) findViewById(R.id.login_signUpBtn);
        mSignUpBtn.setPaintFlags(mSignUpBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isLogin = true;

                imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                Intent intent = new Intent(mContext, DialogTerms.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("loginType", Const.LoginType.PASSWORD);
                startActivity(intent);
            }
        });


        login_kakao = (ImageView) findViewById(R.id.ivFirstBtn);
        login_kakao.setOnClickListener(this);


        /*facebook login*/
        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        faceBookLoginButton = (ImageView) findViewById(R.id.ivSecondBtn);
        // Callback registration

        faceBookLoginButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn = (ImageView) findViewById(R.id.ivThirdBtn);
        googleLoginBtn.setOnClickListener(this);


        noRollback = getIntent().getBooleanExtra(KEY_ROLLBACK, false);

        if (savedInstanceState == null) {
            replaceTutorialFragment(noRollback);
        }

    }


    @Override
    public void onClick(View v) {

        //?????????????????? ?????? ??????
        //?????????????????? ?????? ?????????
        //????????? ???????????? ?????? ??? ?????????
        loadingIndicator.show();

        switch (v.getId()) {
            case R.id.ivFirstBtn:
                startKakaoLogin();
                break;
            case R.id.ivSecondBtn:
                startFacebookLogin();
                break;
            case R.id.ivThirdBtn:
                startGoogleLogin();
//                showTermsDialogActivityWithLoginType(Const.LoginType.GOOGLE);
                break;
        }
    }

    private void sendResetPWEmail(String myEmail) {
        loadingIndicator.show();
        mAuth.sendPasswordResetEmail(myEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("ios", "sendPasswordResetEmail - onSuccess");
                loadingIndicator.cancel();
                new MaterialDialog.Builder(mActivity)
                        .title("????????? ?????? ??????")
                        .content("???????????? ???????????? ????????? ?????? ???????????????\n?????? ???????????? ??????????????????")
                        .positiveText("??????")
                        .positiveColorRes(R.color.color_common)
                        .show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //error Msg : delete

                Log.e("ios", "sendPasswordResetEmail - onFailure", e);


                new MaterialDialog.Builder(mActivity)
                        .title("??????")
                        .content("???????????? ???????????? ????????? ??????????????? ????????????")
                        .positiveText("??????")
                        .positiveColorRes(R.color.color_common)
                        .show();

            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("ios", "sendPasswordResetEmail - onComplete");
            }
        });
    }


    private void loginEmailPassword() {

        //????????? ?????????
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

        if (email.getText().length() == 0 || password.getText().length() == 0) {  //????????? ????????????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("???????????? ??????????????????")
                    .positiveText("??????")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                            if (email.getText().length() == 0) {
                                email.requestFocus();
                                imm.showSoftInput(email, 0);
                            } else {
                                password.requestFocus();
                                imm.showSoftInput(password, 0);
                            }

                        }
                    })
                    .positiveColorRes(R.color.color_common)
                    .show();

        } else if (!Util.validateEmail(email.getText().toString())) { //????????? ?????? ??????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("????????? ????????? ????????????")
                    .positiveText("??????")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                            email.requestFocus();
                            imm.showSoftInput(email, 0);

                        }
                    })
                    .positiveColorRes(R.color.color_common)
                    .show();


        } else if (!Util.validatePassword(password.getText().toString())) { //???????????? ?????? ??????

            new MaterialDialog.Builder(this)
                    .title("??????")
                    .content("??????????????? ??????/??????/???????????? ??? 2????????? ????????? 8~15??? ?????????")
                    .positiveText("??????")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                            password.requestFocus();
                            imm.showSoftInput(password, 0);
                        }
                    })
                    .positiveColorRes(R.color.color_common)
                    .show();


        } else {

            loadingIndicator.show();

            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.e("ios", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Log.e("ios", "signInWithCredential:success"
                                        + "\nuser : " + user.getDisplayName()
                                        + "\n" + user.getPhoneNumber()
                                        + "\n" + user.getEmail()
                                        + "\n" + user.getPhotoUrl()
                                        + "\n" + user.getProviderData()
                                        + "\n" + user.getMetadata().getCreationTimestamp()
                                        + "\n" + user.getMetadata().getLastSignInTimestamp()
                                        + "\n" + user.getUid());

                                // TODO : ?????????????????? ??????????????? ????????????

                                checkAlreadyRegisteredUser(user, Const.RegisteredBy.MANBOCASH);


//                            updateUI(user);
                            } else {
                                Log.e("ios", "signInWithEmail:failure" + ", getCause : " + task.getException().getLocalizedMessage(), task.getException());

                                //identifier
                                //password
                                //We have blocked all requests from this device due to unusual activity. Try again later. [ Too many unsuccessful login attempts. Please try again later. ]

                                loadingIndicator.cancel();

                                String body = "";

                                if (task.getException().getMessage().contains("identifier")) {
                                    body = "???????????? ?????? ????????? ?????????\n?????? ????????? ?????????";
                                } else if (task.getException().getMessage().contains("password")) {
                                    body = "??????????????? ?????? ????????????\n?????? ????????? ?????????";
                                }

                                new MaterialDialog.Builder(mActivity)
                                        .title("??????")
                                        .content(body)
                                        .positiveText("??????")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                                                if (task.getException().getMessage().contains("identifier")) {
                                                    email.requestFocus();
                                                    imm.showSoftInput(email, 0);
                                                } else if (task.getException().getMessage().contains("password")) {
                                                    password.requestFocus();
                                                    imm.showSoftInput(password, 0);
                                                }

                                            }
                                        })
                                        .positiveColorRes(R.color.color_common)
                                        .show();


                            }

                        }
                    });

        }

    }

    public void startKakaoLogin() {
        if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)) {
            UserApiClient.getInstance().loginWithKakaoTalk(this, kakaoCallback);
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(TutorialSupportActivity.this, kakaoCallback);
        }
    }// end startKakaoLogin

    Function2<OAuthToken, Throwable, Unit> kakaoCallback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if (oAuthToken != null) {
                Log.e("ios", "kakao AccessToken : " + oAuthToken.getAccessToken());

                getFirebaseJwt(oAuthToken.getAccessToken()).continueWithTask(new Continuation<String, Task<AuthResult>>() {
                    @Override
                    public Task<AuthResult> then(@NonNull Task<String> task) throws Exception {
                        String firebaseToken = task.getResult();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        Log.e("ios", "firebaseToken(before signIn): " + firebaseToken);

                        return auth.signInWithCustomToken(firebaseToken);
                    }
                }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("ios", "Firebase custom login success");


                            loadingIndicator.cancel();

                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e("ios", "Firebase user :success"
                                    + "\ndi : " + user.getDisplayName()
                                    + "\n" + user.getPhoneNumber()
                                    + "\n" + user.getEmail()
                                    + "\n" + user.getPhotoUrl()
                                    + "\n" + user.getProviderData()
                                    + "\n" + user.getMetadata().getCreationTimestamp()
                                    + "\n" + user.getMetadata().getLastSignInTimestamp()
                                    + "\n" + user.getUid());

                            getKaKaoProfile();// ????????? ??????(?????? ???)

                            String testMsg = "Firebase login : success"
                                    + "\ndisplayName : " + user.getDisplayName()
                                    + "\nphoneNum : " + user.getPhoneNumber()
                                    + "\nEmail : " + user.getEmail()
                                    + "\nPhotoURL : " + user.getPhotoUrl()
                                    + "\nProviderData : " + user.getProviderData()
                                    + "\nCreationTime : " + user.getMetadata().getCreationTimestamp()
                                    + "\nLastSignInTime : " + user.getMetadata().getLastSignInTimestamp()
                                    + "\nUID : " + user.getUid();

                            new MaterialDialog.Builder(TutorialSupportActivity.this)
                                    .title("[?????????]????????? ??????")
                                    .content(testMsg)
                                    .positiveText("??????")
                                    .positiveColorRes(R.color.color_common)
                                    .negativeColorRes(R.color.color_common)
                                    .show();


                            //TODO : ???????????? ????????? ?????? ?????? ??????...


                            checkAlreadyRegisteredUser(user, Const.RegisteredBy.KAKAO);
                        } else {
                            Log.e("ios", "kakao OAuthToken is null");
                            Log.e("ios", "kakao OAuthToken throawable : " + throwable.getMessage());

                            new MaterialDialog.Builder(TutorialSupportActivity.this)
                                    .title("????????? ??????")
                                    .content(throwable.getMessage())
                                    .positiveText("??????")
                                    .positiveColorRes(R.color.color_common)
                                    .show();

                        }

                    }
                });


            }
            if (throwable != null) {
                Log.d("ios", "Message : " + throwable.getLocalizedMessage());
            }

            return null;
        }
    };

    private void getKaKaoProfile() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null) {//??????????????? ?????? ?????? ??????
                    Log.e("ios", "Kakao id =" + user.getId());
                    Log.e("ios", "Kakao email =" + user.getKakaoAccount().getEmail());
                    Log.e("ios", "Kakao nickname =" + user.getKakaoAccount().getProfile().getNickname());
                    Log.e("ios", "Kakao gender =" + user.getKakaoAccount().getGender());
                    Log.e("ios", "Kakao ageRange =" + user.getKakaoAccount().getAgeRange());

                } else {//??????????????? ????????????

                }
                if (throwable != null) {
                    Log.e("ios", "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }

    public void startFacebookLogin() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(TutorialSupportActivity.this, Arrays.asList("public_profile", "email"));
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("ios", "facebook:onSuccess:" + loginResult.getRecentlyGrantedPermissions());


                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("ios", "========facebook login cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("ios", "========facebook login error : " + exception);
            }
        });
    }

    public void startGoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /*??????????????? ?????????????????? ??????*/
    private void showTermsDialogActivityWithLoginType(int loginType) {
        Intent intent = new Intent(mContext, DialogTerms.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("loginType", loginType);
        startActivityForResult(intent, loginType);
    }


    /**
     * @param kakaoAccessToken Access token retrieved after successful Kakao Login
     * @return Task object that will call validation server and retrieve firebase token
     */
    private Task<String> getFirebaseJwt(final String kakaoAccessToken) {
        final TaskCompletionSource<String> source = new TaskCompletionSource<>();

//        Toast.makeText(mActivity, "Kakao AccessToken=> " + kakaoAccessToken, Toast.LENGTH_LONG).show();
        RequestQueue queue = Volley.newRequestQueue(mActivity);
        String url = getResources().getString(R.string.validation_server_domain) + "/verifyToken";
        HashMap<String, String> validationObject = new HashMap<>();
        validationObject.put("token", kakaoAccessToken);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(validationObject), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String firebaseToken = response.getString("firebase_token");
                    Log.e("ios", "firebaseToken(From Server->JWT): " + firebaseToken);
                    source.setResult(firebaseToken);
                } catch (Exception e) {
                    source.setException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ios", error.toString());
                source.setException(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", kakaoAccessToken);
                return params;
            }
        };

        queue.add(request);
        return source.getTask();
    }


    /**
     * OnActivityResult() should be overridden for Kakao Login because Kakao Login uses startActivityForResult().
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("ios", "LogindActivity Result - requestCode : " + requestCode + " , resultCode : " + resultCode);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("ios", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("ios", "Google sign in failed : ", e);
                // ...
            }
        } else if (requestCode == Const.LoginType.KAKAO) {
//            if (resultCode == RESULT_OK) startKakaoLogin();
        } else if (requestCode == Const.LoginType.FACEBOOK) {
//            if (resultCode == RESULT_OK) startFacebookLogin();
        } else if (requestCode == Const.LoginType.GOOGLE) {
//            if (resultCode == RESULT_OK) startGoogleLogin();
        } else {
//            Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e("ios", "signInWithCredential:success"
                                    + "\nuser : " + user.getDisplayName()
                                    + "\n" + user.getPhoneNumber()
                                    + "\n" + user.getEmail()
                                    + "\n" + user.getPhotoUrl()
                                    + "\n" + user.getProviderData()
                                    + "\n" + user.getMetadata().getCreationTimestamp()
                                    + "\n" + user.getMetadata().getLastSignInTimestamp()
                                    + "\n" + user.getUid());


                            checkAlreadyRegisteredUser(user, Const.RegisteredBy.GOOGLE);

//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("ios", "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.e("ios", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("ios", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("ios", "facebook currentUser : " + user.getDisplayName() + "\n"
                                    + user.getEmail() + "\n"
                                    + user.getPhoneNumber() + "\n"
                                    + user.getProviderId() + "\n"
                                    + user.getUid() + "\n"
                                    + user.getMetadata() + "\n"
                                    + user.getPhotoUrl() + "\n"
                                    + user.getProviderData() + "\n");


                            checkAlreadyRegisteredUser(user, Const.RegisteredBy.FACEBOOK);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("ios", "signInWithCredential:failure", task.getException());
//                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void replaceTutorialFragment(boolean noRollback) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, CustomTutorialSupportFragment.newInstance(noRollback))
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (mPressFirstBackKey == false) {
            Toast.makeText(this, "?????? ??? ????????? ???????????????.", Toast.LENGTH_LONG).show();
            mPressFirstBackKey = true;
        } else {
            ActivityCompat.finishAffinity(mActivity);

        }
    }


    private void checkAlreadyRegisteredUser(FirebaseUser user, String registerType) {

        //uid ???????????? --> ?????? ??????? ??????(?????? ?????????) : ?????????(???????????? ??? ???????????? ?????? -> ?????????)


        Call<Duplication> retrofit = NetRetrofit.getInstance().getService().duplicationUidCheck(user.getUid());
        retrofit.enqueue(new Callback<Duplication>() {
            @Override
            public void onResponse(Call<Duplication> call, retrofit2.Response<Duplication> response) {

                loadingIndicator.cancel();

                if (response.isSuccessful()) {


                    Duplication duplication = response.body();

                    Log.e("ios", "uid response.body : " + response.body().result + "\n " + response.message());
                    Log.e("ios", "uid duplication : " + duplication.getResult());

                    //???????????? ???????????? ????????? ????????? -  TODO ?????????????????? ????????????????????? ?????????????????????
                    if (duplication.getResult().toString().equals("??????"/*????????????*/)) {


                        //?????????????????? ??? ??????????????? ?????? ??????
                        //?????? ???????????? ???????????? ???

                        MyInfoEntity myInfo = new MyInfoEntity();
                        File infoDB = new File(String.valueOf(getDatabasePath("my_info.db")));


                        //???????????? (????????????????????? ?????????????????????) ??????????????? ????????? ???????????? ????????? ????????????????????? ??????
                        //????????? ???????????? ???????????? ???????????? ????????? - ????????? ???????????????
                        if (infoDB.exists()) {
                            Log.e("ios", "my_info.db  ??????");


                        } else {
                            Log.e("ios", "my_info.db  ??????");


                            //???????????? ????????? ???????????? ????????? ???????????????????????? ????????? ????????? ????????? --> ??????????????????????????? ???????????????
                            myInfo.registerby = duplication.getRegisteredBy();
                            myInfo.photourl = duplication.getPhotoUrl();
                            myInfo.nickname = duplication.getNickName();
                            myInfo.creationTimestamp = duplication.getCreationTimestamp();
                            myInfo.lastSignInTimestamp = duplication.getLastSignInTimestamp(); //?????? ??????????????????????????? ????????? ??????????????? ??????
                            myInfo.refercode = duplication.getReferCode();
                            myInfo.referee = duplication.getReferee();
                            myInfo.uid = duplication.getUid();
                            myInfo.email = duplication.getEmail();
                            myInfo.height = duplication.getHeight();
                            myInfo.weight = duplication.getWeight();


                            MyInfoDatabase myInfoDatabase = MyInfoDatabase.getInstance(getApplicationContext());
                            MyInfoDao myInfoDao = myInfoDatabase.getMyInfoDao();
                            AppExecutors executors = new AppExecutors();

                            executors.diskIO().execute(() -> {
                                myInfoDao.insertMyInfo(myInfo);
                            });

                        }


                        Intent main = new Intent(getApplicationContext(), BranchViewActivity.class);
                        main.putExtra("infoData", myInfo);
//                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);

                        finish();

                    } else /*????????? ??????*/ {


                        //????????????
                        isLogin = true;
                        switch (registerType) {
                            case Const.RegisteredBy.FACEBOOK:
                                showTermsDialogActivityWithLoginType(Const.LoginType.FACEBOOK);
                                break;
                            case Const.RegisteredBy.GOOGLE:
                                showTermsDialogActivityWithLoginType(Const.LoginType.GOOGLE);
                                break;
                            case Const.RegisteredBy.KAKAO:
                                showTermsDialogActivityWithLoginType(Const.LoginType.KAKAO);
                                break;
                        }


                    }

                }
            }

            @Override
            public void onFailure(Call<Duplication> call, Throwable t) {
                loadingIndicator.cancel();
            }
        });

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (loadingIndicator.isShowing()) {
//            loadingIndicator.cancel();
//        }
//    }
}
