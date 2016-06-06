package com.anibalosorio.practicavsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Button bAgregar, bBuscar, bEliminar, bActualizar, bInventario, bVenta, bGanancias;
    EditText eId, eNombre, eCantidad, eValor;
    TextView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAgregar = (Button) findViewById(R.id.bAgregar);
        bBuscar = (Button) findViewById(R.id.bBuscar);
        bEliminar = (Button) findViewById(R.id.bEliminar);
        bActualizar = (Button) findViewById(R.id.bActualizar);
        bInventario = (Button) findViewById(R.id.bInventario);
        bVenta = (Button) findViewById(R.id.bVenta);
        bGanancias = (Button) findViewById(R.id.bGanancias);

        eId = (EditText) findViewById(R.id.eId);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eCantidad = (EditText) findViewById(R.id.eCantidad);
        eValor = (EditText) findViewById(R.id.eValor);

        resultado = (TextView) findViewById(R.id.resultado);

        UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this);
        db = usuario.getWritableDatabase();



        bAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Registro Agregado", Toast.LENGTH_SHORT).show();
                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("cantidad", cantidad);
                nuevoRegistro.put("valor", valor);
                db.insert("Usuarios", null, nuevoRegistro);
                ver_Tabla();
            }
        });

        bBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();
                String id = eId.getText().toString();

                String[] campos = new String[]{"id","nombre","cantidad","valor"};
                String[] args = new String[]{nombre};

                Cursor c=db.query("Usuarios",campos,"nombre=?",args,null,null,null);
                if (c.moveToFirst()){
                    resultado.setText("");
                    do {
                        String codigo = c.getString(0);
                        String name = c.getString(1);
                        int cant = c.getInt(2);
                        int value = c.getInt(3);
                        resultado.append(" "+codigo+"-"+name+"-"+cant+"-"+value+"\n");
                    }while (c.moveToNext());
                }else
                {Toast.makeText(MainActivity.this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
                ver_Tabla();}
            }
        });

        bActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Registro Actualizado", Toast.LENGTH_SHORT).show();
                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();
                String codigo = eId.getText().toString();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("cantidad", cantidad);
                nuevoRegistro.put("valor", valor);

                db.update("Usuarios", nuevoRegistro,"id="+codigo,null);
                ver_Tabla();
            }
        });

        bEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Registro Eliminado", Toast.LENGTH_SHORT).show();

                String codigo = eId.getText().toString();
                db.delete("Usuarios","id= "+codigo,null);
                ver_Tabla();
            }
        });

        bInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ver_Tabla();

            }
        });
    }

    protected void ver_Tabla(){
        Cursor c = db.rawQuery("SELECT id, nombre, cantidad, valor FROM Usuarios", null);
        resultado.setText("");
        if (c.moveToFirst()){
            do {
                String id = c.getString(0);
                String nombre = c.getString(1);
                int cantidad = c.getInt(2);
                int valor = c.getInt(3);
                resultado.append("  "+id+" - "+nombre+" - "+cantidad+" - "+valor+"\n");
            }while (c.moveToNext());
        }
    }
}