package simulador;

public class Camion
{
    public p posicion;
    public int abordando;
    public int pasajeros;
    public int indice;
    
    public Camion(p inicial, int ind)
    {
        posicion = inicial;
        indice = ind;
        abordando = 0;
        pasajeros = (int)Math.rint(Math.random()*(65-10)+10);
    }
    
}
