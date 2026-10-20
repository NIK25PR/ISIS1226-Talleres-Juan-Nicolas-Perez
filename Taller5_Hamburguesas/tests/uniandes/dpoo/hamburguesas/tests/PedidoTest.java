package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas de la clase Pedido.
 *
 * Escenarios:
 * 1. Pedido vacío (sin productos).
 * 2. Pedido con productos del menú: corral (14000) + papas medianas (5500).
 *    Neto = 19500, IVA (19%) = 3705, Total = 23205.
 * 3. Pedido con un producto y un combo: corral (14000) + combo corral (22050).
 *    Neto = 36050, IVA = 6849, Total = 42899.
 * 4. Guardar la factura en un archivo válido y en una ruta inválida.
 */
public class PedidoTest
{
    private ProductoMenu corral;
    private ProductoMenu papasMedianas;
    private ProductoMenu gaseosa;
    private Combo comboCorral;

    private Pedido pedidoVacio;
    private Pedido pedido;

    private File archivoTemporal;

    @BeforeEach
    void setUp( ) throws Exception
    {
        corral = new ProductoMenu( "corral", 14000 );
        papasMedianas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );

        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( corral );
        items.add( papasMedianas );
        items.add( gaseosa );
        comboCorral = new Combo( "combo corral", 0.10, items );

        pedidoVacio = new Pedido( "Ana", "Calle 1 # 2-3" );

        pedido = new Pedido( "Juan", "Carrera 7 # 40-62" );
        pedido.agregarProducto( corral );
        pedido.agregarProducto( papasMedianas );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        if( archivoTemporal != null && archivoTemporal.exists( ) )
            archivoTemporal.delete( );
    }

    // ------------------------------------------------------------
    // Constructor e identificador
    // ------------------------------------------------------------

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Juan", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdsConsecutivos( )
    {
        Pedido p1 = new Pedido( "Cliente 1", "Dir 1" );
        Pedido p2 = new Pedido( "Cliente 2", "Dir 2" );
        assertEquals( p1.getIdPedido( ) + 1, p2.getIdPedido( ), "Cada pedido nuevo debe tener el siguiente identificador." );
    }

    @Test
    void testIdsDistintos( )
    {
        assertNotEquals( pedidoVacio.getIdPedido( ), pedido.getIdPedido( ), "Dos pedidos no pueden tener el mismo identificador." );
    }

    // ------------------------------------------------------------
    // Precios
    // ------------------------------------------------------------

    @Test
    void testPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedidoVacio.getPrecioTotalPedido( ), "Un pedido sin productos debe costar 0." );
    }

    @Test
    void testPrecioTotalConProductos( )
    {
        // 19500 + 19% (3705) = 23205
        assertEquals( 23205, pedido.getPrecioTotalPedido( ), "El total debe ser el neto más el IVA." );
    }

    @Test
    void testPrecioTotalConCombo( )
    {
        Pedido p = new Pedido( "Juan", "Carrera 7 # 40-62" );
        p.agregarProducto( corral );
        p.agregarProducto( comboCorral );
        // 36050 + 19% (6849) = 42899
        assertEquals( 42899, p.getPrecioTotalPedido( ), "El total con combo no es el esperado." );
    }

    @Test
    void testAgregarProductoCambiaElTotal( )
    {
        int antes = pedido.getPrecioTotalPedido( );
        pedido.agregarProducto( gaseosa );
        // 24500 + 19% (4655) = 29155
        assertEquals( 29155, pedido.getPrecioTotalPedido( ), "Agregar un producto debe aumentar el total." );
        assertTrue( pedido.getPrecioTotalPedido( ) > antes );
    }

    // ------------------------------------------------------------
    // generarTextoFactura
    // ------------------------------------------------------------

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "Cliente: Juan\n" +
                          "Dirección: Carrera 7 # 40-62\n" +
                          "----------------\n" +
                          "corral\n" +
                          "            14000\n" +
                          "papas medianas\n" +
                          "            5500\n" +
                          "----------------\n" +
                          "Precio Neto:  19500\n" +
                          "IVA:          3705\n" +
                          "Precio Total: 23205\n";
        assertEquals( esperado, pedido.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaPedidoVacio( )
    {
        String esperado = "Cliente: Ana\n" +
                          "Dirección: Calle 1 # 2-3\n" +
                          "----------------\n" +
                          "----------------\n" +
                          "Precio Neto:  0\n" +
                          "IVA:          0\n" +
                          "Precio Total: 0\n";
        assertEquals( esperado, pedidoVacio.generarTextoFactura( ), "La factura de un pedido vacío no es la esperada." );
    }

    @Test
    void testGenerarTextoFacturaConCombo( )
    {
        Pedido p = new Pedido( "Juan", "Carrera 7 # 40-62" );
        p.agregarProducto( corral );
        p.agregarProducto( comboCorral );
        String texto = p.generarTextoFactura( );

        assertTrue( texto.contains( comboCorral.generarTextoFactura( ) ), "La factura debe incluir el texto del combo." );
        assertTrue( texto.contains( "Precio Neto:  36050\n" ), "El precio neto no es el esperado." );
        assertTrue( texto.contains( "IVA:          6849\n" ), "El IVA no es el esperado." );
        assertTrue( texto.contains( "Precio Total: 42899\n" ), "El precio total no es el esperado." );
    }

    // ------------------------------------------------------------
    // guardarFactura
    // ------------------------------------------------------------

    @Test
    void testGuardarFactura( ) throws Exception
    {
        archivoTemporal = File.createTempFile( "factura_prueba", ".txt" );
        pedido.guardarFactura( archivoTemporal );

        assertTrue( archivoTemporal.exists( ), "El archivo de la factura debe existir." );
        String contenido = new String( Files.readAllBytes( archivoTemporal.toPath( ) ), StandardCharsets.UTF_8 );
        assertEquals( pedido.generarTextoFactura( ), contenido, "El archivo debe contener exactamente el texto de la factura." );
    }

    @Test
    void testGuardarFacturaSobrescribe( ) throws Exception
    {
        archivoTemporal = File.createTempFile( "factura_prueba", ".txt" );
        pedidoVacio.guardarFactura( archivoTemporal );
        pedido.guardarFactura( archivoTemporal );

        String contenido = new String( Files.readAllBytes( archivoTemporal.toPath( ) ), StandardCharsets.UTF_8 );
        assertEquals( pedido.generarTextoFactura( ), contenido, "Guardar de nuevo debe reemplazar el contenido anterior." );
    }

    @Test
    void testGuardarFacturaRutaInvalida( )
    {
        File invalido = new File( "./carpeta_que_no_existe_123/factura.txt" );
        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( invalido ),
                "Debe lanzar FileNotFoundException si no se puede crear el archivo." );
    }
}
