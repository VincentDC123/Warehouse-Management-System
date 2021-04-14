package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateCustomersMutation;
import com.amazonaws.amplify.generated.graphql.CreatePetMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.jakewharton.processphoenix.ProcessPhoenix;

import java.util.Map;

import javax.annotation.Nonnull;

import type.CreateCustomersInput;
import type.CreatePetInput;

public class AuthenticationActivityAddCustomer extends AppCompatActivity {
    String name;
    String id;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("WELCOME ","TO THE BLANK ACTIVITY");
        if(currentUser.loggingIn){
            //AWSMobileClient.getInstance().initialize(AuthenticationActivityAddCustomer.this);
            //backToAuth();
            Log.i("WELCOME2 ","TO THE BLANK ACTIVITY");

            Intent nextIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
            ProcessPhoenix.triggerRebirth(AuthenticationActivityAddCustomer.this, nextIntent);

        }
        else {
            try {
                name = getCognitoName();
                id = getCognitoID();
                email = getCognitoEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            save(id, name, email);
            //AuthenticationActivity.class.
        }
    }
    public String getCognitoName() throws Exception {
        Map m1= AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("given_name").toString();
    }
    public String getCognitoID() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("sub").toString();
    }
    public String getCognitoEmail() throws Exception {
        Map m1=AWSMobileClient.getInstance().getUserAttributes();
        return m1.get("email").toString();
    }
    private void save(String id,String name,String email) {
//        final String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
//        final String description = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
//        final double price=Double.parseDouble(((EditText) findViewById(R.id.editTextStreet)).getText().toString());
//        final int quantity=Integer.parseInt(((EditText) findViewById(R.id.editTextEmail)).getText().toString());

//        CreatePetInput input = CreatePetInput.builder()
//                .id(currentUser.id)
//                .name(currentUser.name)
//                .description("admin")
//                .build();
        CreateCustomersInput input = CreateCustomersInput.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
        //CreateItemsInput input = CreateItemsInput.builder().name(name).description(description).price(price).quantity(quantity).build();
//        CreatePetMutation addPetMutation = CreatePetMutation.builder()
//                .input(input)
//                .build();

        CreateCustomersMutation addCustomerMutation= CreateCustomersMutation.builder()
                .input(input)
                .build();
        //CreateItemsMutation addItemMutation = CreateItemsMutation.builder().input(input).build();
        //ClientFactory.appSyncClient().mutate(addItemMutation).enqueue(mutateCallback22);
        //ClientFactory.appSyncClient().mutate(addPetMutation).enqueue(mutateCallback22);
        ClientFactory.appSyncClient().mutate(addCustomerMutation).enqueue(mutateCallback22);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateCustomersMutation.Data> mutateCallback22 = new GraphQLCall.Callback<CreateCustomersMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateCustomersMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(DisplayItems.this, "Added Item", Toast.LENGTH_SHORT).show();
                    // DisplayItems.this.finish();
                    Log.i("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%", "Customer added to the DB table!");
                    backToAuth();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddCustomerMutation", e);

                }
            });
        }
    };
    //=============================================================================================CREATE
    public void backToAuth(){
        Log.i("LOGING IN ","AS CUSTOMER");
        Intent intent = new Intent(AuthenticationActivityAddCustomer.this, AuthenticationActivity.class);
        startActivity(intent);
    }
}
/*
type customers @model {
    id: ID!
    name: String
    email: String
    password: String
}
 */