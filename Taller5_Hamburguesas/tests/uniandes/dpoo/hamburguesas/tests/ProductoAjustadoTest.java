package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

/**
 * Pruebas de la clase ProductoAjustado.
 *
 * Escenarios:
 * 1. Producto ajustado sin cambios (corral, 14000).
 * 2. Producto ajustado con ingredientes agregados (queso 2500, tocineta 2500) y eliminados (cebolla 1000).
 * 3. Producto ajustado con el mismo ingrediente agregado dos veces.
 *
 * Como la clase no tiene métodos públicos para agregar o eliminar ingredientes, las listas
 * 'agregados' y 'eliminados' se llenan por reflexión.
 */
public class ProductoAjustadoTest
{
    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tocineta;
    private Ingrediente cebolla;

    @BeforeEach
    void setUp( ) throws Exception
    {
        base = new ProductoMenu( "corral", 14000 );
        ajustado = new ProductoAjustado( base );
        queso = new Ingrediente( "queso americano", 2500 );
        tocineta = new Ingrediente( "tocineta express", 2500 );
        cebolla = new Ingrediente( "cebolla", 1000 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    /**
     * Retorna una de las listas privadas de ingredientes ("agregados" o "eliminados") del producto ajustado
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Ingrediente> lista( ProductoAjustado p, String nombreAtributo ) throws Exception
    {
        Field f = ProductoAjustado.class.getDeclaredField( nombreAtributo );
        f.setAccessible( true );
        return ( ArrayList<Ingrediente> )f.get( p );
    }

    // ------------------------------------------------------------
    // Escenario 1: sin ajustes
    // ------------------------------------------------------------

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", ajustado.getNombre( ), "El nombre debe ser el del producto base." );
    }

    @Test
    void testGetPrecioSinAjustes( )
    {
        assertEquals( 14000, ajustado.getPrecio( ), "Sin ajustes, el precio debe ser el del producto base." );
    }

    @Test
    void testGenerarTextoFacturaSinAjustes( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    // ------------------------------------------------------------
    // Escenario 2: con agregados y eliminados
    // ------------------------------------------------------------

    @Test
    void testGetPrecioConAgregados( ) throws Exception
    {
        lista( ajustado, "agregados" ).add( queso );
        lista( ajustado, "agregados" ).add( tocineta );

        assertEquals( 14000 + 2500 + 2500, ajustado.getPrecio( ), "El precio debe sumar el costo de los ingredientes agregados." );
    }

    @Test
    void testGetPrecioEliminarNoDescuenta( ) throws Exception
    {
        lista( ajustado, "eliminados" ).add( cebolla );

        assertEquals( 14000, ajustado.getPrecio( ), "Eliminar ingredientes no debe reducir el precio." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( ) throws Exception
    {
        lista( ajustado, "agregados" ).add( queso );
        lista( ajustado, "eliminados" ).add( cebolla );

        String esperado = "corral\n" +
                          "    +queso americano                2500\n" +
                          "    -cebolla\n" +
                          "            16500\n";
        assertEquals( esperado, ajustado.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    // ------------------------------------------------------------
    // Escenario 3: ingrediente repetido
    // ------------------------------------------------------------

    @Test
    void testGetPrecioIngredienteRepetido( ) throws Exception
    {
        lista( ajustado, "agregados" ).add( queso );
        lista( ajustado, "agregados" ).add( queso );

        assertEquals( 14000 + 2500 * 2, ajustado.getPrecio( ), "Un ingrediente agregado dos veces debe cobrarse dos veces." );
    }

    @Test
    void testNoModificaProductoBase( ) throws Exception
    {
        lista( ajustado, "agregados" ).add( queso );

        assertEquals( 14000, base.getPrecio( ), "Ajustar un producto no debe cambiar el precio del producto base." );
    }
}
