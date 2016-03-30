package com.example.eejl_.fblogin;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private CallbackManager m;
private FacebookCallback<LoginResult> mc= new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {
        AccessToken at=loginResult.getAccessToken();
        Profile p= Profile.getCurrentProfile();
        if(p!=null) {
            Log.d("bienvenido", p.getName());
            Log.d("bienvenido", p.getLinkUri().toString());
            Intent ventanitaRuta=  new Intent(getActivity().getApplicationContext(), RutasMenu.class);
            startActivity(ventanitaRuta);
        }

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
};
    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        m= CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton lb= (LoginButton) view.findViewById(R.id.login_button);
        lb.setReadPermissions("user_birthday","email","public_profile");
        lb.setFragment(this);
        lb.registerCallback(m, mc);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        m.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile p= Profile.getCurrentProfile();
        if(p!=null){
            Log.d("bienvenido", p.getName());
            Intent ventanitaRuta=  new Intent(getActivity().getApplicationContext(), RutasMenu.class);
            startActivity(ventanitaRuta);
        }
    }
}
