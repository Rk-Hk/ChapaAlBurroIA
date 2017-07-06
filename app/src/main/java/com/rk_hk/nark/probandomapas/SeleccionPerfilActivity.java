package com.rk_hk.nark.probandomapas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rk_hk.nark.probandomapas.fixed.Contract;

import static com.rk_hk.nark.probandomapas.fixed.Contract.getSingletonContract;

public class SeleccionPerfilActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter<CharSequence> adaptador;   //Creamos un adaptador para el Spinner (Combobox en java).
    private int seleccionado;   //Almacena la opcion seleccionada en el spinner



    private Contract c = getSingletonContract();
    private final DatabaseReference dbApp = c.fireDB.getInstance().getReference().child("conductore");
    static String passConducto = null, userConducto;
    /**
     * Interfaz
     **/
    private Spinner spinner_Select;
    private Button btn_Seleccionado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {    //Método del ciclo vida de android.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_perfil);    //Relaciona con su respectivo activity(vista).

        if (getIntent().getExtras() != null) {
            Intent new_intent  = new Intent(this,NotificationActivity.class);

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                new_intent.putExtra(key,value);
                startActivity(new_intent);
            }
        }

        adaptador = ArrayAdapter.createFromResource(this, R.array.spinner_content, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /**Instanciamos los elementos de la vista**/
        spinner_Select = (Spinner) findViewById(R.id.spinner);
        spinner_Select.setAdapter(adaptador);   //Asignamos los valores del adapter al spinner
        spinner_Select.setOnItemSelectedListener(this);

        btn_Seleccionado = (Button) findViewById(R.id.btn_seleccionar);
        btn_Seleccionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionSeleccionado();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        seleccionado = position;
        Toast.makeText(this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void opcionSeleccionado() {
        switch (seleccionado) {
            case 0:
                Intent intent2 = new Intent(this, EstudianteActivity.class);
                startActivity(intent2);
                break;
            case 1:

                dbApp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userConducto = dataSnapshot.child("user").getValue().toString();
                        passConducto = dataSnapshot.child("pass").getValue().toString();
                        if(passConducto.isEmpty())
                            System.out.println("No se recupero los datos del concuctor");
                        else
                            System.out.println("Datos del conductor = " + passConducto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent1 = new Intent(this, LoginConductorActivity.class);
                startActivity(intent1);
                break;
        }
    }


}
