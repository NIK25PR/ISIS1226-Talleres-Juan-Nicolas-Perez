package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente
{
    private List<Tiquete> tiquetesSinUsar;
    private List<Tiquete> tiquetesUsados;

    public Cliente()
    {
        this.tiquetesSinUsar = new ArrayList<>();
        this.tiquetesUsados = new ArrayList<>();
    }

    public abstract String getTipoCliente();

    public abstract String getIdentificador();

    public void agregarTiquete(Tiquete tiquete)
    {
        tiquetesSinUsar.add(tiquete);
    }

    public int calcularValorTotalTiquetes()
    {
        int total = 0;
        for(Tiquete tiquete : tiquetesSinUsar)
        {
            total += tiquete.getTarifa();
        }
        for(Tiquete tiquete : tiquetesUsados)
        {
            total += tiquete.getTarifa();
        }
        return total;
    }

    public void usarTiquetes(Vuelo vuelo)
    {
        List<Tiquete> aTrasladar = new ArrayList<>();
        
        for(Tiquete tiquete : tiquetesSinUsar)
        {
            if(tiquete.getVuelo().equals(vuelo))
            {
                tiquete.marcarComoUsado();
                aTrasladar.add(tiquete);
            }
        }
        
        tiquetesSinUsar.removeAll(aTrasladar);
        tiquetesUsados.addAll(aTrasladar);
    }
}