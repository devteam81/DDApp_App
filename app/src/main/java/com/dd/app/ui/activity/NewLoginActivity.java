package com.dd.app.ui.activity;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dd.app.R;
import com.dd.app.ui.fragment.LoginFragment;
import com.dd.app.ui.fragment.SignupFragment;
import com.dd.app.util.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dd.app.util.Fragments.HOME_FRAGMENTS;
import static com.dd.app.util.Fragments.LOGIN_FRAGMENTS;

public class NewLoginActivity extends BaseActivity{

    private static String CURRENT_FRAGMENT ;
    private Fragment fragment;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvSignup)
    TextView tvSignup;
    @BindView(R.id.tvSignupSelector)
    View tvSignupSelector;
    @BindView(R.id.tvLoginSelector)
    View tvLoginSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ButterKnife.bind(this);

        replaceFragmentWithAnimation(new SignupFragment(), LOGIN_FRAGMENTS[0], false);

        back_btn.setOnClickListener(v -> onBackPressed());
    }


    public void replaceFragmentWithAnimation(Fragment fragment, String tag, boolean toBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CURRENT_FRAGMENT = tag;
        this.fragment = fragment;
        if (toBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @OnClick(R.id.tvSignup)
    public void onSignUpClick(){
        tvSignup.setBackgroundResource(R.drawable.bg_background_selected);
        tvSignup.setPadding(44,28,44,28);
        tvSignup.setTextColor(ContextCompat.getColor(this,R.color.white));
        tvSignupSelector.setBackgroundColor(ContextCompat.getColor(this,R.color.button_color_light));
        tvLogin.setBackgroundColor(Color.TRANSPARENT);
        tvLogin.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryHeader));
        tvLoginSelector.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryHeader));
        /*tvSignup.setBackgroundColor(ContextCompat.getColor(this,R.color.theme_green));
        tvSignup.setTextColor(ContextCompat.getColor(this,R.color.white));
        tvLogin.setBackgroundColor(ContextCompat.getColor(this,R.color.off_white));
        tvLogin.setTextColor(ContextCompat.getColor(this,R.color.theme_green));*/

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof SignupFragment){
            return;
        }
        replaceFragmentWithAnimation(new SignupFragment(), HOME_FRAGMENTS[1], false);
    }

    @OnClick(R.id.tvLogin)
    public void onLoginClick(){
        tvLogin.setBackgroundResource(R.drawable.bg_background_selected);
        tvLogin.setPadding(44,28,44,28);
        tvLogin.setTextColor(ContextCompat.getColor(this,R.color.white));
        tvLoginSelector.setBackgroundColor(ContextCompat.getColor(this,R.color.button_color_light));
        tvSignup.setBackgroundColor(Color.TRANSPARENT);
        tvSignup.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryHeader));
        tvSignupSelector.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimaryHeader));
        /*tvLogin.setBackgroundColor(ContextCompat.getColor(this,R.color.theme_green));
        tvLogin.setTextColor(ContextCompat.getColor(this,R.color.white));
        tvSignup.setBackgroundColor(ContextCompat.getColor(this,R.color.off_white));
        tvSignup.setTextColor(ContextCompat.getColor(this,R.color.theme_green))*/

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof LoginFragment){
            return;
        }
        replaceFragmentWithAnimation(new LoginFragment(), HOME_FRAGMENTS[0], false);
    }
}
