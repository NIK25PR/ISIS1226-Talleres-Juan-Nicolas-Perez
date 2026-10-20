package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas de la clase ProductoMenu.
 *
 * Escenario: un producto del menú "corral" con precio base 14000.
 */
public class ProductoMenuTest
{
    private ProductoMenu producto1;

    @BeforeEach
    void setUp( ) throws Exception
    {
        producto1 = new ProductoMenu( "corral", 14000 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", producto1.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, producto1.getPrecio( ), "El precio del producto no es el esperado." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, producto1.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaContieneNombreYPrecio( )
    {
        String texto = producto1.generarTextoFactura( );
        assertTrue( texto.contains( "corral" ), "La factura debe incluir el nombre del producto." );
        assertTrue( texto.contains( "14000" ), "La factura debe incluir el precio del producto." );
    }
}
