package fergaral.latortugaylaliebre;


public class Carrera {

	private Corredor liebre;
	private Corredor tortuga;
	private Corredor corredorActivo;
	private Arbol[] arboles;
	private int numeroDado;
	private boolean liebreDormida;

	public Carrera() {
		inicializarJuego();
	}

	public void inicializarJuego() {
		liebre = new Corredor(new Calle(),"liebre","liebre.JPG",4);
		tortuga = new Corredor(new Calle(),"tortuga","tortuga.JPG",2);
		arboles = new Arbol[] { new Arbol(), new Arbol() };

		for(Arbol arbol : arboles) {
			int posicionArbol = (int) ((Math.random() * 9) + 1); //Entre 1 y 9
			//Nos aseguramos de que la posición no esté repetida (no puede haber más de
			//1 árbol en la misma casilla)
			while(posicionArbolRepetida(posicionArbol, arboles))
				posicionArbol = (int) ((Math.random() * 9) + 1);

			arbol.setPosicion(posicionArbol);
		}

		numeroDado = 0;
		// Establecemos el corredor activo apuntando a la tortuga ya que sale la primera
		this.setCorredorActivo(tortuga);
	}

	public boolean lanzarDado() {
		boolean isPosible = false;
		if(getCorredorActivo() == liebre && liebreDormida) {
			cambiarTurnos();
			liebreDormida = false;
		}

		numeroDado = Dado.lanzar(getCorredorActivo());
		if (corredorActivo.getPosicion() + numeroDado < Calle.DIM)
			isPosible = true;
		else {
			cambiarTurnos();
		}
		return isPosible;
	}


	public boolean isJugadaCorrecta(int i) {
		return (corredorActivo.getPosicion() + numeroDado == i);
	}

	public boolean resolverJugada(int i) {
		boolean resuelta = false;
		// Comprobamos que se trata de avanzar a la casilla correcta
		if (isJugadaCorrecta (i)){
			corredorActivo.setPosicion(corredorActivo.getPosicion() + numeroDado);
			// Incrementamos la puntuaci�n del corredor
			corredorActivo.incrementaPuntuacion(corredorActivo.getCalleAsignada().puntosCasilla(corredorActivo.getPosicion()));

			//Si el corredor activo es la liebre y cayó en un árbol, la liebre dormirá y perderá un turno
			if(corredorActivo == liebre) {
				if(cayoEnUnArbol(corredorActivo)) {
					liebreDormida = true;
				}
			}

			// Intercambiamos los turnos
			cambiarTurnos();
			resuelta = true;
		}
		return resuelta;
	}

	public Corredor getCorredorActivo() {
		return corredorActivo;
	}

	public Corredor getCorredorNoActivo() {
		if (corredorActivo == liebre) {
			return tortuga;
		}
		return liebre;
	}

	private void cambiarTurnos() {
		// Cambiamos el turno
		corredorActivo = getCorredorNoActivo();
	}

	public boolean isPartidaFinalizada() {
		return (liebre.getPosicion() == Calle.POSICION_META || tortuga.getPosicion() == Calle.POSICION_META);
	}

	public int getNumeroDado() {
		return numeroDado;
	}

	public Corredor getLiebre() {
		return liebre;
	}

	public Corredor getTortuga() {
		return tortuga;
	}

	public Arbol[] getArboles() {
		return arboles;
	}

	private void setCorredorActivo(Corredor corredorActivo) {
		this.corredorActivo = corredorActivo;
	}

	private boolean cayoEnUnArbol(Corredor corredor) {
		for(Arbol arbol : arboles) {
			if(arbol.getPosicion() == corredor.getPosicion())
				return true;
		}

		return false;
	}

	private boolean posicionArbolRepetida(int posicion, Arbol[] arboles) {
		for(Arbol arbol : arboles) {
			if(arbol.getPosicion() == posicion)
				return true;
		}

		return false;
	}

	public boolean hayArbol(int posicion) {
		for(Arbol arbol : arboles) {
			if(arbol.getPosicion() == posicion)
				return true;
		}

		return false;
	}

	public boolean isLiebreDormida() {
		return liebreDormida;
	}
}