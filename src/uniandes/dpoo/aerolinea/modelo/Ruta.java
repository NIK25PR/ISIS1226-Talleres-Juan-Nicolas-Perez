package uniandes.dpoo.aerolinea.modelo;

/**
 * Esta clase tiene la información de una ruta entre dos aeropuertos que cubre una aerolínea.
 */
public class Ruta
{
	private Aeropuerto origen;
	private Aeropuerto destino;
	private String horaSalida;
	private String horaLlegada;
	private String codigoRuta;

	public Ruta(Aeropuerto origen, Aeropuerto destino, String horaSalida, String horaLlegada, String codigoRuta)
	{
	    this.origen = origen;
	    this.destino = destino;
	    this.horaSalida = horaSalida;
	    this.horaLlegada = horaLlegada;
	    this.codigoRuta = codigoRuta;
	}

	public String getCodigoRuta()
	{
	    return codigoRuta;
	}

	public Aeropuerto getOrigen()
	{
	    return origen;
	}

	public Aeropuerto getDestino()
	{
	    return destino;
	}

	public String getHoraSalida()
	{
	    return horaSalida;
	}

	public String getHoraLlegada()
	{
	    return horaLlegada;
	}
	
	public static int getMinutos( String horaCompleta )
	{
	    int minutos = Integer.parseInt( horaCompleta ) % 100;
	    return minutos;
	}
	

	public int getDuracion()
	{
	    int horasSalida = getHoras(horaSalida);
	    int minutosSalida = getMinutos(horaSalida);
	    int horasLlegada = getHoras(horaLlegada);
	    int minutosLlegada = getMinutos(horaLlegada);
	    
	    int totalMinutosSalida = horasSalida * 60 + minutosSalida;
	    int totalMinutosLlegada = horasLlegada * 60 + minutosLlegada;
	    
	    return totalMinutosLlegada - totalMinutosSalida;
	}
	
    public static int getHoras( String horaCompleta )
    {
        int horas = Integer.parseInt( horaCompleta ) / 100;
        return horas;
    }

    
}
