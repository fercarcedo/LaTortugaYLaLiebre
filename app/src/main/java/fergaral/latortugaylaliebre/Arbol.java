package fergaral.latortugaylaliebre;

public class Arbol {
    private int posicion;

    public Arbol() {
        setPosicion(4);
    }

    public Arbol(int posicion) {
        setPosicion(posicion);
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getRutaImagen() {
        return "/img/arbol.JPG";
    }
}
