package fergaral.latortugaylaliebre;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Carrera carrera;
    private GridView gvLiebre, gvTortuga;
    private EditText eTPuntosLiebre, eTPuntosTortuga;
    private ImageButton btnDado;
    private TextView txDado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        carrera = new Carrera();

        btnDado = (ImageButton) findViewById(R.id.btnDado);
        eTPuntosLiebre = (EditText) findViewById(R.id.eTpuntosLiebre);
        eTPuntosLiebre.setKeyListener(null);
        eTPuntosTortuga = (EditText) findViewById(R.id.eTpuntosTortuga);
        eTPuntosTortuga.setKeyListener(null);
        txDado = (TextView) findViewById(R.id.tVtiradaDado);

        //Añadimos los botones al Grid
        gvLiebre = (GridView) findViewById(R.id.gvLiebre);
        gvTortuga = (GridView) findViewById(R.id.gvTortuga);

        gvLiebre.setAdapter(new GridAdapter(true));
        gvTortuga.setAdapter(new GridAdapter(false));

        btnDado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carrera.lanzarDado()) {
                    txDado.setText(String.valueOf(carrera.getNumeroDado()));
                    habilitarPanelCorredorActivo(carrera.getCorredorActivo());
                    btnDado.setEnabled(false);
                }else{
                    txDado.setText(String.valueOf(carrera.getNumeroDado()));
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("La jugada no es posible")
                            .setMessage("La jugada no es posible, cambia el turno. Vuelve a jugar")
                            .show();
                    deshabilitarPaneles();
                    txDado.setText("");
                }
            }
        });
    }

    public class GridAdapter extends BaseAdapter {
        private boolean liebre;

        public GridAdapter(boolean liebre) {
            this.liebre = liebre;
        }

        @Override
        public int getCount() {
            return 11;
        }

        @Override
        public Object getItem(int position) {
            return getResources().getDrawable(liebre ? R.drawable.liebre : R.drawable.tortuga);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.grid_item, parent, false);
            }

            ImageView iv = (ImageView) view.findViewById(R.id.ivgridit);

            if(liebre)
                iv.setImageResource(R.drawable.liebre); //El por defecto es la tortuga

            if(position != 0 && liebre)
                iv.setImageResource(R.drawable.black);
            else if(position != 0)
                iv.setImageResource(R.drawable.blacktortuga);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jugar(position);
                    Log.d("TAG", String.valueOf(position));
                }
            });

            if(liebre && carrera.hayArbol(position))
                iv.setImageResource(R.drawable.arbol);

            return view;
        }
    }

    private void deshabilitarPaneles() {
        modificarPanel(gvLiebre, false);
        modificarPanel(gvTortuga, false);
    }

    private void modificarPanel(GridView gView, boolean habilitado) {
        for(int i=0; i<gView.getChildCount(); i++) {
            ImageView imageView = (ImageView) gView.getChildAt(i);
            imageView.setEnabled(habilitado);
        }
    }

    private void habilitarPanelCorredorActivo(Corredor corredor) {
        deshabilitarPaneles();
        if(corredor.getNombre().equals("liebre"))
            modificarPanel(gvLiebre, true);
        else
            modificarPanel(gvTortuga, true);
    }

    private void jugar(int i) {
        if(carrera.isJugadaCorrecta(i)) {
            if (carrera.getCorredorActivo() == carrera.getLiebre())
                if (carrera.hayArbol(i))
                    new AlertDialog.Builder(this)
                            .setTitle("Liebre dormida")
                            .setMessage("La liebre se va a dormir. La tortuga gana un turno")
                            .show();
        }

        if(carrera.resolverJugada(i)) {
            representarEstadoJuego();
            deshabilitarPaneles();
        }
    }

    private void representarEstadoJuego() {
        txDado.setText("");
        pintarPuntos();
        pintarCorredores();
        if(carrera.isPartidaFinalizada()) {
            new AlertDialog.Builder(this)
                    .setTitle("Partida finalizada")
                    .setMessage("Ganó la "
                            + ((carrera.getTortuga().getPosicion() == 10) ? "tortuga" : "liebre"))
                    .show();
        }else{
            btnDado.setEnabled(true);
        }
    }

    private void pintarPuntos() {
        eTPuntosLiebre.setText(String.valueOf(carrera.getLiebre().getPuntuacion()));
        eTPuntosTortuga.setText(String.valueOf(carrera.getTortuga().getPuntuacion()));
    }

    private void pintarCorredores() {
        pintarCalle(carrera.getLiebre(), gvLiebre);
        pintarCalle(carrera.getTortuga(), gvTortuga);
    }

    private void pintarCalle(Corredor corredor, GridView gView) {
        boolean liebre = corredor.getNombre().equals("liebre");
        int imageResource = liebre ? R.drawable.liebre : R.drawable.tortuga;

        for(int i=0; i < gView.getChildCount(); i++) {
            ImageView imageView = (ImageView) gView.getChildAt(i);
            if(i == corredor.getPosicion()) {
                imageView.setImageResource(imageResource);
            }else{
                if(liebre)
                    imageView.setImageResource(R.drawable.black);
                else
                    imageView.setImageResource(R.drawable.blacktortuga);
            }
        }

        pintarArboles();
    }

    private void pintarArboles() {
        Arbol[] arboles = carrera.getArboles();

        for(Arbol arbol : arboles) {
            ImageView imageView = (ImageView) gvLiebre.getChildAt(arbol.getPosicion());
            int imageRes;

            if((carrera.getLiebre().getPosicion() == arbol.getPosicion()) && carrera.isLiebreDormida())
                imageRes = R.drawable.liebre_durmiendo;
            else if((carrera.getLiebre().getPosicion() == arbol.getPosicion()))
                imageRes = R.drawable.liebre;
            else
                imageRes = R.drawable.arbol;

            imageView.setImageResource(imageRes);
        }
    }
}
