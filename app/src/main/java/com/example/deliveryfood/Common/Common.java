package com.example.deliveryfood.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.AutoCompleteTextView;

import com.example.deliveryfood.Model.User;

public class Common {
  public static User currentUser;
  public static final String UPDATE = "Update";
  public static final String DELETE= "Delete";
  public static final String USER_KEY = "User";
  public static final String PWD_KEY= "Password";
  public static boolean isConnectedToInternet(Context context){
    ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if(connectivityManager!=null){
      NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
      if(infos!=null){
        for (int i=0;i<infos.length;i++){
          if (infos[i].getState()==NetworkInfo.State.CONNECTED)
            return true;
        }
      }
    }
    return false;
  }
  public static String changeCodeToStatus(String status) {
    if(status.equals("0"))
      return "Place";
    else if(status.equals("1"))
      return "On the way";
    else
      return "Shipped";
  }
}
