package com.rk_hk.nark.probandomapas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rk_hk.nark.probandomapas.fixed.Contract;
import com.rk_hk.nark.probandomapas.utilities.EncriptarPass;

import static com.rk_hk.nark.probandomapas.SeleccionPerfilActivity.passConducto;
import static com.rk_hk.nark.probandomapas.SeleccionPerfilActivity.userConducto;
import static com.rk_hk.nark.probandomapas.fixed.Contract.getSingletonContract;

public class LoginConductorActivity extends AppCompatActivity {




    Button btn_verificar ;
    EditText et_user , et_pass;
    private int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_user = (EditText) findViewById(R.id.etLoginDialog);
        et_pass = (EditText) findViewById(R.id.etPassDialog);
        btn_verificar = (Button) findViewById(R.id.btn_VeificarConductor);
        contador = 0;

        btn_verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCuenta(et_user.getText().toString(),et_pass.getText().toString());
            }
        });

    }
    public void verificarCuenta(String etUsuario, String etPass){
        if (etUsuario.equals(userConducto) && EncriptarPass.MD5(etPass).equals(passConducto)) {

            Intent intent = new Intent(this, BusMapActivity.class);
            startActivity(intent);
        } else {
            contador+=1;
            Toast.makeText(this, "CUENTA INCORRECTA", Toast.LENGTH_SHORT).show();
        }
    }
}
