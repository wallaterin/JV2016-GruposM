package modelo.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelo.ModeloException;
import modelo.Mundo;
import modelo.Simulacion;
import modelo.Usuario;
import modelo.Simulacion.EstadoSimulacion;
import util.Fecha;
/** Proyecto: Juego de la vida.
 *  Prueba Junit4 de la clase Simulacion según el modelo 2.1
 *  @since: prototipo2.0
 *  @source: CorreoTest.java 
 *  @version: 2.1 - 2017/05/03
 *  @author: ajp
 *  @author: Grupo 1
 */
public class SimulacionTest {
	private Simulacion simulacion1;
	private Simulacion simulacion2;
	private Usuario usr;
	private Fecha fecha;
	private Mundo mundo;


	public SimulacionTest () {
		try {
			usr = new Usuario();
			fecha = new Fecha();
			mundo = new Mundo();
			simulacion2 = new Simulacion(new Usuario(), fecha, mundo, EstadoSimulacion.PREPARADA);
		} 
		catch (ModeloException e) {	}
	}

	@Before
	public void InicializaDatosPrueba() {	
		try {
			simulacion1 = new Simulacion();
		} 
		catch (ModeloException e) {	}
	}

	@After
	public void borrarDatosPrueba() {	
		simulacion1 = null;
	}

	// Test con DATOS VALIDOS
	@Test
	public void testSimulacionConvencional() {	
		assertNotNull(simulacion2);
	}

	@Test
	public void testSimulacion() {
		assertNotNull(simulacion1);
	}

	@Test
	public void testSimulacionCopia() {
		try {
			simulacion1 = new Simulacion(simulacion2);
		} 
		catch (ModeloException e) {	}
		assertNotSame(simulacion2, simulacion1);
	}

	@Test
	public void testGetUsr() {
		assertEquals(simulacion2.getUsr(), usr);
	}

	@Test
	public void testGetMundo() {
		assertEquals(simulacion2.getMundo(), mundo);
	}

	@Test
	public void testGetFecha() {
		assertEquals(simulacion2.getFecha(), fecha);
	}

	@Test
	public void testGetEstado() {
		assertEquals(simulacion2.getEstado(), EstadoSimulacion.PREPARADA);
	}

	@Test
	public void testGetIdSimulacion() {
		assertEquals(simulacion2.getIdSimulacion(), usr.getIdUsr() + ":" + fecha.getAño() + fecha.getMes() + fecha.getDia() 
		+ fecha.getHora() + fecha.getMinuto() + fecha.getSegundo());
	}

	@Test
	public void testSetUsr() {
		simulacion1.setUsr(usr);
		assertEquals(simulacion1.getUsr(), usr);
	}

	@Test
	public void testSetMundo() {
		simulacion1.setMundo(mundo);
		assertEquals(simulacion1.getMundo(), mundo);
	}

	@Test
	public void testSetFecha() {
		simulacion1.setFecha(fecha);
		assertEquals(simulacion1.getFecha(), fecha);
	}

	@Test
	public void testSetEstado() {
		simulacion1.setEstado(EstadoSimulacion.PREPARADA);
		assertEquals(simulacion1.getEstado(), EstadoSimulacion.PREPARADA);
	}

	@Test
	public void testToString() {
		assertEquals(simulacion2.toString(), String.format(
				"Simulacion [usr=%s, fecha=%s, mundo=%s, estado=%s]", usr, fecha, mundo, EstadoSimulacion.PREPARADA));
	}

	@Test
	public void testEquals() {
		Simulacion simulacion = null;
		try {
			simulacion = new Simulacion();
		} 
		catch (ModeloException e) { }
		assertEquals(simulacion, simulacion1);
	}

	@Test
	public void testClone() {
		Simulacion simulacion = (Simulacion) simulacion2.clone();
		assertNotSame(simulacion, simulacion2);
	}

	@Test
	public void testHashCode() {
		assertEquals(simulacion2.hashCode(), -1368567182);
	}

} // class
