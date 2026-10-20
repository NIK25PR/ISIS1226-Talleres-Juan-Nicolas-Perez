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

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

/**
 * Pruebas de integración de la clase Restaurante.
 *
 * Archivos de prueba (carpeta data/pruebas):
 * - ingredientes.txt, menu.txt, combos.txt: datos válidos y pequeños, con líneas vacías intercaladas.
 * - ingredientes_repetidos.txt: "lechuga" aparece dos veces.
 * - menu_repetido.txt: "corral" aparece dos veces.
 * - combos_repetidos.txt: "combo corral" aparece dos veces.
 * - combos_producto_faltante.txt: "combo especial" usa el producto "especial", que no está en el menú.
 *
 * También se cargan los archivos reales de la carpeta data.
 */
public class RestauranteTest
{
    private static final String PRUEBAS = "./data/pruebas/";
    private static final String DATA = "./data/";
    private static final String CARPETA_FACTURAS = "./facturas/";

    private Restaurante restaurante;

    /**
     * Facturas creadas durante una prueba, para borrarlas al terminar
     */
    private ArrayList<File> facturasCreadas;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
        facturasCreadas = new ArrayList<File>( );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        for( File f : facturasCreadas )
        {
            if( f.exists( ) )
                f.delete( );
        }
    }

    private void cargarDatosPrueba( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ), new File( PRUEBAS + "menu.txt" ),
                new File( PRUEBAS + "combos.txt" ) );
    }

    private File archivoFactura( int idPedido )
    {
        File f = new File( CARPETA_FACTURAS + "factura_" + idPedido + ".txt" );
        facturasCreadas.add( f );
        return f;
    }

    // ------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------

    @Test
    void testRestauranteVacio( )
    {
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debe tener pedido en curso." );
        assertTrue( restaurante.getPedidos( ).isEmpty( ), "Un restaurante nuevo no debe tener pedidos." );
        assertTrue( restaurante.getIngredientes( ).isEmpty( ), "Un restaurante nuevo no debe tener ingredientes." );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ), "Un restaurante nuevo no debe tener menú base." );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ), "Un restaurante nuevo no debe tener combos." );
    }

    // ------------------------------------------------------------
    // Carga de información
    // ------------------------------------------------------------

    @Test
    void testCargarIngredientes( ) throws Exception
    {
        cargarDatosPrueba( );
        assertEquals( 3, restaurante.getIngredientes( ).size( ), "Deben cargarse 3 ingredientes (ignorando líneas vacías)." );
        assertEquals( "lechuga", restaurante.getIngredientes( ).get( 0 ).getNombre( ) );
        assertEquals( 1000, restaurante.getIngredientes( ).get( 0 ).getCostoAdicional( ) );
        assertEquals( "queso americano", restaurante.getIngredientes( ).get( 2 ).getNombre( ) );
        assertEquals( 2500, restaurante.getIngredientes( ).get( 2 ).getCostoAdicional( ) );
    }

    @Test
    void testCargarMenu( ) throws Exception
    {
        cargarDatosPrueba( );
        ArrayList<ProductoMenu> menu = restaurante.getMenuBase( );
        assertEquals( 5, menu.size( ), "Deben cargarse 5 productos (ignorando líneas vacías)." );
        assertEquals( "corral", menu.get( 0 ).getNombre( ) );
        assertEquals( 14000, menu.get( 0 ).getPrecio( ) );
        assertEquals( "papas grandes", menu.get( 4 ).getNombre( ) );
        assertEquals( 6900, menu.get( 4 ).getPrecio( ) );
    }

    @Test
    void testCargarCombos( ) throws Exception
    {
        cargarDatosPrueba( );
        ArrayList<Combo> combos = restaurante.getMenuCombos( );
        assertEquals( 2, combos.size( ), "Deben cargarse 2 combos (ignorando líneas vacías)." );
        assertEquals( "combo corral", combos.get( 0 ).getNombre( ) );
        // (14000 + 5500 + 5000) - 10% = 22050
        assertEquals( 22050, combos.get( 0 ).getPrecio( ), "El precio del combo corral no es el esperado." );
        assertEquals( "combo todoterreno", combos.get( 1 ).getNombre( ) );
        // (25000 + 6900 + 5000) - 7% = 34317
        assertEquals( 34317, combos.get( 1 ).getPrecio( ), "El precio del combo todoterreno no es el esperado." );
    }

    @Test
    void testCargarArchivosReales( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( new File( DATA + "ingredientes.txt" ), new File( DATA + "menu.txt" ),
                new File( DATA + "combos.txt" ) );
        assertEquals( 15, restaurante.getIngredientes( ).size( ), "El archivo real tiene 15 ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "El archivo real tiene 22 productos." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "El archivo real tiene 4 combos." );
    }

    @Test
    void testIngredienteRepetido( )
    {
        IngredienteRepetidoException e = assertThrows( IngredienteRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes_repetidos.txt" ),
                        new File( PRUEBAS + "menu.txt" ), new File( PRUEBAS + "combos.txt" ) ),
                "Debe lanzar IngredienteRepetidoException." );
        assertTrue( e.getMessage( ).contains( "lechuga" ), "El mensaje debe mencionar el ingrediente repetido." );
    }

    @Test
    void testProductoRepetidoEnMenu( )
    {
        ProductoRepetidoException e = assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ),
                        new File( PRUEBAS + "menu_repetido.txt" ), new File( PRUEBAS + "combos.txt" ) ),
                "Debe lanzar ProductoRepetidoException." );
        assertTrue( e.getMessage( ).contains( "corral" ), "El mensaje debe mencionar el producto repetido." );
    }

    @Test
    void testComboRepetido( )
    {
        ProductoRepetidoException e = assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ),
                        new File( PRUEBAS + "menu.txt" ), new File( PRUEBAS + "combos_repetidos.txt" ) ),
                "Debe lanzar ProductoRepetidoException para un combo repetido." );
        assertTrue( e.getMessage( ).contains( "combo corral" ), "El mensaje debe mencionar el combo repetido." );
    }

    @Test
    void testComboConProductoFaltante( )
    {
        ProductoFaltanteException e = assertThrows( ProductoFaltanteException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ),
                        new File( PRUEBAS + "menu.txt" ), new File( PRUEBAS + "combos_producto_faltante.txt" ) ),
                "Debe lanzar ProductoFaltanteException." );
        assertTrue( e.getMessage( ).contains( "especial" ), "El mensaje debe mencionar el producto faltante." );
    }

    @Test
    void testArchivoIngredientesNoExiste( )
    {
        assertThrows( FileNotFoundException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "no_existe.txt" ),
                        new File( PRUEBAS + "menu.txt" ), new File( PRUEBAS + "combos.txt" ) ),
                "Debe lanzar FileNotFoundException si el archivo no existe." );
    }

    @Test
    void testArchivoMenuNoExiste( )
    {
        assertThrows( FileNotFoundException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ),
                        new File( PRUEBAS + "no_existe.txt" ), new File( PRUEBAS + "combos.txt" ) ) );
    }

    @Test
    void testArchivoCombosNoExiste( )
    {
        assertThrows( FileNotFoundException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( new File( PRUEBAS + "ingredientes.txt" ),
                        new File( PRUEBAS + "menu.txt" ), new File( PRUEBAS + "no_existe.txt" ) ) );
    }

    // ------------------------------------------------------------
    // Pedidos
    // ------------------------------------------------------------

    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Carrera 7 # 40-62" );
        Pedido enCurso = restaurante.getPedidoEnCurso( );
        assertNotNull( enCurso, "Después de iniciar un pedido debe haber un pedido en curso." );
        assertEquals( "Juan", enCurso.getNombreCliente( ) );
    }

    @Test
    void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Carrera 7 # 40-62" );
        YaHayUnPedidoEnCursoException e = assertThrows( YaHayUnPedidoEnCursoException.class,
                ( ) -> restaurante.iniciarPedido( "Ana", "Calle 1 # 2-3" ),
                "No se debe poder iniciar un pedido si ya hay uno en curso." );
        assertTrue( e.getMessage( ).contains( "Juan" ) && e.getMessage( ).contains( "Ana" ), "El mensaje debe mencionar a ambos clientes." );
        assertEquals( "Juan", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso no debe cambiar." );
    }

    @Test
    void testCerrarSinPedidoEnCurso( )
    {
        NoHayPedidoEnCursoException e = assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ),
                "Debe lanzar NoHayPedidoEnCursoException si no hay pedido en curso." );
        assertNotNull( e.getMessage( ) );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        cargarDatosPrueba( );
        restaurante.iniciarPedido( "Juan", "Carrera 7 # 40-62" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        pedido.agregarProducto( restaurante.getMenuBase( ).get( 0 ) ); // corral
        pedido.agregarProducto( restaurante.getMenuCombos( ).get( 0 ) ); // combo corral
        File factura = archivoFactura( pedido.getIdPedido( ) );

        restaurante.cerrarYGuardarPedido( );

        assertNull( restaurante.getPedidoEnCurso( ), "Después de cerrar no debe haber pedido en curso." );
        assertTrue( factura.exists( ), "Debe existir el archivo " + factura.getPath( ) );
        String contenido = new String( Files.readAllBytes( factura.toPath( ) ), StandardCharsets.UTF_8 );
        assertEquals( pedido.generarTextoFactura( ), contenido, "El archivo debe contener la factura del pedido." );
        assertTrue( contenido.contains( "Precio Total: 42899" ), "El total de la factura no es el esperado." );
    }

    @Test
    void testPedidoCerradoQuedaEnHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Carrera 7 # 40-62" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        archivoFactura( pedido.getIdPedido( ) );

        restaurante.cerrarYGuardarPedido( );

        assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en la lista de pedidos." );
        assertSame( pedido, restaurante.getPedidos( ).get( 0 ), "El pedido guardado debe ser el que estaba en curso." );
    }

    @Test
    void testVariosPedidosSeguidos( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan", "Carrera 7 # 40-62" );
        archivoFactura( restaurante.getPedidoEnCurso( ).getIdPedido( ) );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Ana", "Calle 1 # 2-3" );
        archivoFactura( restaurante.getPedidoEnCurso( ).getIdPedido( ) );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( 2, restaurante.getPedidos( ).size( ), "Deben quedar 2 pedidos cerrados." );
        assertEquals( "Juan", restaurante.getPedidos( ).get( 0 ).getNombreCliente( ) );
        assertEquals( "Ana", restaurante.getPedidos( ).get( 1 ).getNombreCliente( ) );
        assertNotEquals( restaurante.getPedidos( ).get( 0 ).getIdPedido( ), restaurante.getPedidos( ).get( 1 ).getIdPedido( ),
                "Cada pedido debe tener un identificador distinto." );
    }
}
