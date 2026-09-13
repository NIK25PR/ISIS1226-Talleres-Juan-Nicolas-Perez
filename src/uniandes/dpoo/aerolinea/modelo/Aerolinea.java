package uniandes.dpoo.aerolinea.modelo;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaTiquetes;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaAlta;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifasTemporadaBaja;

/**
 * En esta clase se organizan todos los aspectos relacionados con una Aerolínea.
 * 
 * Por un lado, esta clase cumple un rol central como estructurador para todo el resto de elementos: directa o indirectamente, todos están contenidos y se pueden acceder a
 * través de la clase Aerolínea.
 * 
 * Por otro lado, esta clase implementa algunas funcionalidades adicionales a su rol como estructurador, para lo cual se apoya en las otras clases que hacen parte del
 * proyecto.
 */
public class Aerolinea
{
    /**
     * Una lista con los aviones de los que dispone la aerolínea
     */
    private List<Avion> aviones;

    /**
     * Un mapa con las rutas que cubre la aerolínea.
     * 
     * Las llaves del mapa son el código de la ruta, mientras que los valores son las rutas
     */
    private Map<String, Ruta> rutas;

    /**
     * Una lista de los vuelos programados por la aerolínea
     */
    private List<Vuelo> vuelos;

    /**
     * Un mapa con los clientes de la aerolínea.
     * 
     * Las llaves del mapa son los identificadores de los clientes, mientras que los valores son los clientes
     */
    private Map<String, Cliente> clientes;

    /**
     * Construye una nueva aerolínea con un nombre e inicializa todas las contenedoras con estructuras vacías
     */
    public Aerolinea( )
    {
        aviones = new LinkedList<Avion>( );
        rutas = new HashMap<String, Ruta>( );
        vuelos = new LinkedList<Vuelo>( );
        clientes = new HashMap<String, Cliente>( );
    }

    // ************************************************************************************
    //
    // Estos son los métodos que están relacionados con la manipulación básica de los atributos
    // de la aerolínea (consultar, agregar)
    //
    // ************************************************************************************

    /**
     * Agrega una nueva ruta a la aerolínea
     * @param ruta
     */
    public void agregarRuta( Ruta ruta )
    {
        this.rutas.put( ruta.getCodigoRuta( ), ruta );
    }

    /**
     * Agrega un nuevo avión a la aerolínea
     * @param avion
     */
    public void agregarAvion( Avion avion )
    {
        this.aviones.add( avion );
    }

    /**
     * Agrega un nuevo cliente a la aerolínea
     * @param cliente
     */
    public void agregarCliente( Cliente cliente )
    {
        this.clientes.put( cliente.getIdentificador( ), cliente );
    }

    /**
     * Verifica si ya existe un cliente con el identificador dado
     * @param identificadorCliente
     * @return Retorna true si ya existía un cliente con el identificador, independientemente de su tipo
     */
    public boolean existeCliente( String identificadorCliente )
    {
        return this.clientes.containsKey( identificadorCliente );
    }

    /**
     * Busca el cliente con el identificador dado
     * @param identificadorCliente
     * @return Retorna el cliente con el identificador, o null si no existía
     */
    public Cliente getCliente( String identificadorCliente )
    {
        return this.clientes.get( identificadorCliente );
    }

    /**
     * Retorna todos los aviones de la aerolínea
     * @return
     */
    public Collection<Avion> getAviones( )
    {
        return aviones;
    }

    /**
     * Retorna todas las rutas disponibles para la aerolínea
     * @return
     */
    public Collection<Ruta> getRutas( )
    {
        return rutas.values( );
    }

    /**
     * Retorna la ruta de la aerolínea que tiene el código dado
     * @param codigoRuta El código de la ruta buscada
     * @return La ruta con el código, o null si no existe una ruta con ese código
     */
    public Ruta getRuta( String codigoRuta )
    {
        return rutas.get( codigoRuta );
    }

    /**
     * Retorna todos los vuelos de la aerolínea
     * @return
     */
    public Vuelo getVuelo( String codigoRuta, String fechaVuelo )
    {
        for(Vuelo vuelo : vuelos)
        {
            if(vuelo.getRuta().getCodigoRuta().equals(codigoRuta) && vuelo.getFecha().equals(fechaVuelo))
            {
                return vuelo;
            }
        }
        return null;
    }

    
    public Collection<Tiquete> getTiquetes( )
    {
        List<Tiquete> todosLosTiquetes = new ArrayList<>();
        for(Vuelo vuelo : vuelos)
        {
            todosLosTiquetes.addAll(vuelo.getTiquetes());
        }
        return todosLosTiquetes;
    }


    public void cargarAerolinea( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
       // IPersistenciaAerolinea cargador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
       // cargador.cargarAerolinea(archivo, this);
    }

   
    public void salvarAerolinea( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException
    {
       // IPersistenciaAerolinea guardador = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
       // guardador.salvarAerolinea(archivo, this);
    }

    public void cargarTiquetes( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes( tipoArchivo );
        cargador.cargarTiquetes( archivo, this );
    }

    public void salvarTiquetes( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException
    {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes( tipoArchivo );
        cargador.salvarTiquetes( archivo, this );
    }

    public void programarVuelo( String fecha, String codigoRuta, String nombreAvion ) throws Exception
    {
        Ruta ruta = getRuta(codigoRuta);
        if(ruta == null)
        {
            throw new Exception("La ruta no existe");
        }
        
        Avion avion = null;
        for(Avion a : aviones)
        {
            if(a.getNombre().equals(nombreAvion))
            {
                avion = a;
                break;
            }
        }
        
        if(avion == null)
        {
            throw new Exception("El avión no existe");
        }
        
        for(Vuelo vuelo : vuelos)
        {
            if(vuelo.getAvion().equals(avion) && vuelo.getFecha().equals(fecha))
            {
                throw new Exception("El avión ya está ocupado en esa fecha");
            }
        }
        
        Vuelo nuevoVuelo = new Vuelo(ruta, fecha, avion);
        vuelos.add(nuevoVuelo);
    }
    public int venderTiquetes( String identificadorCliente, String fecha, String codigoRuta, int cantidad ) throws VueloSobrevendidoException, Exception
    {
        Cliente cliente = getCliente(identificadorCliente);
        if(cliente == null)
        {
            throw new Exception("El cliente no existe");
        }
        
        Vuelo vuelo = getVuelo(codigoRuta, fecha);
        if(vuelo == null)
        {
            throw new Exception("El vuelo no existe");
        }
        
        String[] partes = fecha.split("-");
        int mes = Integer.parseInt(partes[1]);
        
        CalculadoraTarifas calculadora;
        if((mes >= 1 && mes <= 5) || (mes >= 9 && mes <= 11))
        {
            calculadora = new CalculadoraTarifasTemporadaBaja();
        }
        else
        {
            calculadora = new CalculadoraTarifasTemporadaAlta();
        }
        
        int valorTotal = vuelo.venderTiquetes(cliente, calculadora, cantidad);
        return valorTotal;
    }
    
    public void registrarVueloRealizado( String fecha, String codigoRuta )
    {
        Vuelo vuelo = getVuelo(codigoRuta, fecha);
        if(vuelo != null)
        {
            for(Cliente cliente : clientes.values())
            {
                cliente.usarTiquetes(vuelo);
            }
        }
    }
    public String consultarSaldoPendienteCliente( String identificadorCliente )
    {
        Cliente cliente = getCliente(identificadorCliente);
        if(cliente == null)
        {
            return "Cliente no encontrado";
        }
        
        int saldo = cliente.calcularValorTotalTiquetes();
        return "El cliente tiene un saldo pendiente de: " + saldo;
    }

}
