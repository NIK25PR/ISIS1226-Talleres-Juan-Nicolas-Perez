package uniandes.dpoo.aerolinea.modelo.cliente;

import org.json.JSONObject;

/**
 * Esta clase se usa para representar a los clientes de la aerolínea que son empresas
 */
public class ClienteCorporativo extends Cliente
{
	public static final String CORPORATIVO = "Corporativo";
	public static final int PEQUENA = 1;
	public static final int MEDIANA = 2;
	public static final int GRANDE = 3;

	private String nombreEmpresa;
	private int tamanoEmpresa;

	public ClienteCorporativo(String nombreEmpresa, int tamano)
	{
	    super();
	    this.nombreEmpresa = nombreEmpresa;
	    this.tamanoEmpresa = tamano;
	}

	public String getNombreEmpresa()
	{
	    return nombreEmpresa;
	}

	public int getTamanoEmpresa()
	{
	    return tamanoEmpresa;
	}

	public String getTipoCliente()
	{
	    return CORPORATIVO;
	}

	public String getIdentificador()
	{
	    return nombreEmpresa;
	}
    }

    /**
     * Salva este objeto de tipo ClienteCorporativo dentro de un objeto JSONObject para que ese objeto se almacene en un archivo
     * @return El objeto JSON con toda la información del cliente corporativo
     */
    public JSONObject salvarEnJSON( )
    {
        JSONObject jobject = new JSONObject( );
        jobject.put( "nombreEmpresa", this.nombreEmpresa );
        jobject.put( "tamanoEmpresa", this.tamanoEmpresa );
        jobject.put( "tipo", CORPORATIVO );
        return jobject;
    }
}
