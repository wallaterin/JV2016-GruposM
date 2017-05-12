/** 
 * Proyecto: Juego de la vida.
 * Secuencia principal de arraque del programa.
 * Implementación del control de inicio de sesión y ejecución de la simulación por defecto.  
 * @since: prototipo1.0
 * @source: JVPrincipal.java 
 * @version: 2.1 - 2017/05/11 
 * @author: ajp
 */

import accesoDatos.Datos;
import accesoDatos.test.DatosPrueba;
import accesoUsr.control.ControlSesion;

public class JVPrincipal {

	public static void main(String[] args) {	

		//		DatosPrueba.cargarUsuariosPrueba();	
		//		Datos fachada = new Datos();
		//		System.out.println(fachada.toStringDatosUsuarios());

		if (args.length == 0) {
			new ControlSesion();			// No hay parámetro de linea de comandos. 
		}
		else {
			new ControlSesion(args[0]);
		}

		//		Presentacion presentacion = new Presentacion();
		//		if (presentacion.iniciarSesionCorrecta()) {
		//			presentacion.arrancarSimulacion();
		//		}
		//		else {		
		//			System.out.println("\nDemasiados intentos fallidos...");
		//		}	
		//		fachada.cerrar();
		//		System.out.println("Fin del programa.");

	}

} //class
