package fergaral.latortugaylaliebre;

public class Dado {
	
	public static int lanzar (Corredor corredor)
	{
		int resultado =  (int) (Math.random() * corredor.getDesplazamiento()) + 1;
		return resultado;
	}
}
