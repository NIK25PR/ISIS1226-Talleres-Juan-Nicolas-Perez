package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas de la clase Combo.
 *
 * Escenarios:
 * 1. "combo corral": corral (14000) + papas medianas (5500) + gaseosa (5000) = 24500, con 10% de descuento.
 * 2. "combo todoterreno": todoterreno (25000) + papas grandes (6900) + gaseosa (5000) = 36900, con 7% de descuento.
 * 3. Casos borde: combo sin descuento y combo sin productos.
 */
public class ComboTest
{
    private ProductoMenu corral;
    private ProductoMenu todoterreno;
    private ProductoMenu papasMedianas;
    private ProductoMenu papasGrandes;
    private ProductoMenu gaseosa;

    private ArrayList<ProductoMenu> itemsCorral;
    private Combo comboCorral;
    private Combo comboTodoterreno;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        todoterreno = new ProductoMenu( "todoterreno", 25000 );
        papasMedianas = new ProductoMenu( "papas medianas", 5500 );
        papasGrandes = new ProductoMenu( "papas grandes", 6900 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );

        itemsCorral = new ArrayList<ProductoMenu>( );
        itemsCorral.add( corral );
        itemsCorral.add( papasMedianas );
        itemsCorral.add( gaseosa );
        comboCorral = new Combo( "combo corral", 0.10, itemsCorral );

        ArrayList<ProductoMenu> itemsTodoterreno = new ArrayList<ProductoMenu>( );
        itemsTodoterreno.add( todoterreno );
        itemsTodoterreno.add( papasGrandes );
        itemsTodoterreno.add( gaseosa );
        comboTodoterreno = new Combo( "combo todoterreno", 0.07, itemsTodoterreno );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    // ------------------------------------------------------------
    // getNombre
    // ------------------------------------------------------------

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", comboCorral.getNombre( ), "El nombre del combo no es el esperado." );
        assertEquals( "combo todoterreno", comboTodoterreno.getNombre( ), "El nombre del combo no es el esperado." );
    }

    // ------------------------------------------------------------
    // getPrecio
    // ------------------------------------------------------------

    @Test
    void testGetPrecioDescuento10( )
    {
        // 24500 - 10% = 22050
        assertEquals( 22050, comboCorral.getPrecio( ), "El precio debe ser la suma de los productos menos el 10%." );
    }

    @Test
    void testGetPrecioDescuento7( )
    {
        // 36900 - 7% = 34317
        assertEquals( 34317, comboTodoterreno.getPrecio( ), "El precio debe ser la suma de los productos menos el 7%." );
    }

    @Test
    void testGetPrecioMenorQueProductosPorSeparado( )
    {
        int sinDescuento = corral.getPrecio( ) + papasMedianas.getPrecio( ) + gaseosa.getPrecio( );
        assertTrue( comboCorral.getPrecio( ) < sinDescuento, "El combo debe costar menos que los productos por separado." );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "combo sin descuento", 0, itemsCorral );
        assertEquals( 24500, sinDescuento.getPrecio( ), "Con descuento 0 el precio debe ser la suma de los productos." );
    }

    @Test
    void testGetPrecioComboVacio( )
    {
        Combo vacio = new Combo( "combo vacio", 0.10, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ), "Un combo sin productos debe costar 0." );
    }

    @Test
    void testConstructorCopiaLaLista( )
    {
        // Modificar la lista original después de crear el combo no debe afectar al combo
        itemsCorral.add( new ProductoMenu( "papas grandes", 6900 ) );
        assertEquals( 22050, comboCorral.getPrecio( ), "El combo no debe cambiar si se modifica la lista original." );
    }

    // ------------------------------------------------------------
    // generarTextoFactura
    // ------------------------------------------------------------

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Combo combo corral\n" +
                          " Descuento: 0.1\n" +
                          "            22050\n";
        assertEquals( esperado, comboCorral.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaContienePrecio( )
    {
        String texto = comboTodoterreno.generarTextoFactura( );
        assertTrue( texto.contains( "combo todoterreno" ), "La factura debe incluir el nombre del combo." );
        assertTrue( texto.contains( "34317" ), "La factura debe incluir el precio con descuento." );
    }
}
