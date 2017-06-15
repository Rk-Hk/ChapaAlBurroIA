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

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private ArrayAdapter<CharSequence> adaptador;   //Creamos un adaptador para el Spinner (Combobox en java).
    private int seleccionado;   //Almacena la opcion seleccionada en el spinner
    /**Interfaz**/
    private Spinner spinner_Select;
    private Button btn_Seleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    //Método del ciclo vida de android.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);    //Relaciona con su respectivo activity(vista).

        adaptador = ArrayAdapter.createFromResource(this, R.array.spinner_content,android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /**Instanciamos los elementos de la vista**/
        spinner_Select = (Spinner) findViewById(R.id.spinner);
        spinner_Select.setAdapter(adaptador);   //Asignamos los valores del adapter al spinner
        spinner_Select.setOnItemSelectedListener(this);

        btn_Seleccionado = (Button) findViewById(R.id.btn_seleccionar);
        btn_Seleccionado.setOnClickListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            seleccionado=position;
            Toast.makeText(this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (seleccionado){
            case 0:
                Intent intent2 = new Intent(this,EstudianteActivity.class);
                startActivity(intent2);
                break;
            case 1:
                Intent intent = new Intent(this, BusMapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
