package modelo.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelo.Correo;
import modelo.ModeloException;

/** Proyecto: Juego de la vida.
 *  Prueba Junit4 de la clase Correo según el modelo 2.1
 *  @since: prototipo2.0
 *  @source: CorreoTest.java 
 *  @version: 2.1 - 2017/05/03
 *  @author: ajp
 *  @author: Grupo 1
 */

public class CorreoTest {
	private Correo correo1;
	private Correo correo2;

	public CorreoTest () {
		try {
			correo2 = new Correo("correo@correo.com");
		} 
		catch (ModeloException e) {	}
	}
	
	@Before
	public void inicializaDatosPrueba() {
		try {
			correo1 = new Correo();
			correo2 = new Correo("correo@correo.com");
		} 
		catch (ModeloException e) {	}
	}

	@After
	public void borrarDatosPrueba() {
		correo1 = null;
	}
	
	// Test con DATOS VALIDOS

	@Test
	public void testCorreoConvencional() {
		assertNotNull(correo2);
		assertEquals(correo2.getTexto(), "correo@correo.com");
	}

	@Test
	public void testCorreoDefecto() {
		assertNotNull(correo1);
		assertEquals(correo1.getTexto(), "correo@correo.com");
	}

	@Test
	public void testCorreoCopia() {
		try {
			correo1 = new Correo(correo2);
		} 
		catch (ModeloException e) {	}

		assertNotSame(correo2, correo1);
	}

	@Test
	public void testSetTexto() {
		try {
			correo2.setTexto("correo@correo.com");
		} 
		catch (ModeloException e) {	}
		
		assertEquals(correo2.getTexto(), "correo@correo.com");
	}

	@Test
	public void testCorreoAutentico() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTexto() {
		assertEquals(correo2.getTexto(), "correo@correo.com");
	}

	@Test
	public void testToString() {
		assertEquals("correo@correo.com", correo2.toString());
	}
	
	@Test
	public void testEqualsObject() {
		Correo correo = null;
		try {
			correo = new Correo("correo@correo.com");
		} 
		catch (ModeloException e) {	}
		assertEquals(correo, correo1);
	}

	@Test
	public void testClone() {
		Correo correo = (Correo) correo2.clone();
		assertNotSame(correo, correo2);
	}

	@Test
	public void testHashCode() {
		assertEquals(correo1.hashCode(), -2034667982);
	}
	
	// Test con DATOS NO VALIDOS
		@Test
		public void testCorreoConvencionalTextoNull() {	
			Correo correo = null;
			try {
				String texto = null;
				correo = new Correo(texto);
				fail("No debe llegar aquí...");
			} 
			catch (ModeloException e) { }
			assertNull(correo);
		}

		@Test
		public void testCorreoConvencionalTextoMalFormato() {	
			Correo correo = null;
			try {
				correo = new Correo("correocorreo.com");
				fail("No debe llegar aquí...");
			} 
			catch (ModeloException e) { }
			assertNull(correo);
		}

		@Test
		public void testSetTextoNull() {
			try {
				correo1.setTexto(null);
				fail("No debe llegar aquí...");
			} 
			catch (ModeloException e) { }
			assertEquals(correo1.getTexto(), "correo@correo.com");
		}

		@Test
		public void testSetTextoMalFormato() {
			try {
				correo1.setTexto("correocorreo.com");
				fail("No debe llegar aquí...");
			} 
			catch (ModeloException e) { }
			assertEquals(correo1.getTexto(), "correo@correo.com");
		}
} //class
