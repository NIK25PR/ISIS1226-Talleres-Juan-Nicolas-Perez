package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas
{
    protected int COSTO_POR_KM_NATURAL = 600;
    protected int COSTO_POR_KM_CORPORATIVO = 900;
    protected double DESCUENTO_PEQ = 0.02;
    protected double DESCUENTO_MEDIANAS = 0.1;
    protected double DESCUENTO_GRANDES = 0.2;

    public CalculadoraTarifasTemporadaBaja()
    {
        super();
    }

    public int calcularCostoBase(Vuelo vuelo, Cliente cliente)
    {
        int distancia = calcularDistanciaVuelo(vuelo.getRuta());
        
        if(cliente instanceof ClienteNatural)
        {
            return distancia * COSTO_POR_KM_NATURAL;
        }
        else
        {
            return distancia * COSTO_POR_KM_CORPORATIVO;
        }
    }

    public double calcularPorcentajeDescuento(Cliente cliente)
    {
        if(cliente instanceof ClienteNatural)
        {
            return 0;
        }
        else if(cliente instanceof ClienteCorporativo)
        {
            ClienteCorporativo corporativo = (ClienteCorporativo) cliente;
            int tamano = corporativo.getTamanoEmpresa();
            
            if(tamano == ClienteCorporativo.PEQUENA)
            {
                return DESCUENTO_PEQ;
            }
            else if(tamano == ClienteCorporativo.MEDIANA)
            {
                return DESCUENTO_MEDIANAS;
            }
            else
            {
                return DESCUENTO_GRANDES;
            }
        }
        
        return 0;
    }
}