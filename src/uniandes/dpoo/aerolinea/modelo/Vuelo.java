package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo
{
    private Ruta ruta;
    private String fecha;
    private Avion avion;
    private Map<String, Tiquete> tiquetes;

    public Vuelo(Ruta ruta, String fecha, Avion avion)
    {
        this.ruta = ruta;
        this.fecha = fecha;
        this.avion = avion;
        this.tiquetes = new HashMap<>();
    }

    public Ruta getRuta()
    {
        return ruta;
    }

    public String getFecha()
    {
        return fecha;
    }

    public Avion getAvion()
    {
        return avion;
    }

    public Collection<Tiquete> getTiquetes()
    {
        return tiquetes.values();
    }

    public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) throws VueloSobrevendidoException
    {
        if(tiquetes.size() + cantidad > avion.getCapacidad())
        {
            throw new VueloSobrevendidoException(this);
        }
        
        int valorTotal = 0;
        for(int i = 0; i < cantidad; i++)
        {
            int tarifa = calculadora.calcularTarifa(this, cliente);
            String codigo = cliente.getIdentificador() + "-" + fecha + "-" + (tiquetes.size() + i);
            Tiquete tiquete = new Tiquete(codigo, this, cliente, tarifa);
            tiquetes.put(codigo, tiquete);
            valorTotal += tarifa;
        }
        
        return valorTotal;
    }

    public boolean equals(Object obj)
    {
        if(obj == null || !(obj instanceof Vuelo))
        {
            return false;
        }
        
        Vuelo otro = (Vuelo) obj;
        return ruta.equals(otro.ruta) && fecha.equals(otro.fecha);
    }
}}