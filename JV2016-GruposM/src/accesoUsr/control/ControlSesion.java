/** Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos relacionados con el control 
 *  de inicio de sesión de usuario. Colabora en el patron MVC
 *  @since: prototipo2.1
 *  @source: ControlSesion.java 
 *  @version: 2.1 - 2017.05.08
 *  @author: ajp
 */

package accesoUsr.control;

import accesoDatos.DatosException;
import accesoDatos.Datos;
import accesoUsr.Presentacion;
import accesoUsr.VistaTexto.VistaSesionTexto;
import config.Configuracion;
import modelo.ClaveAcceso;
import modelo.ModeloException;
import modelo.SesionUsuario;
import modelo.SesionUsuario.EstadoSesion;
import modelo.Usuario;
import util.Fecha;

public class ControlSesion {
	private VistaSesionTexto vista;
	private Usuario usrSesion;
	private SesionUsuario sesion;
	private Datos fachada;

	public ControlSesion(String idUsr) {
		initControlSesion(idUsr);
	}
	
	public ControlSesion() {
		this(null);
	}

	/**
	 * Inicialización y secuencia principal del control de sesión.
	 * @param credencialUsr ya obtenida, puede ser null.
	 */
	private void initControlSesion(String idUsr) {
		fachada = new Datos();
		vista = new VistaSesionTexto();
		iniciarSesionUsuario(idUsr); 
		new Presentacion().arrancarSimulacion();
		//new ControlSimulacion(fachada.obtenerSimulacionesUsuario(idUsr).get(0));
		fachada.cerrar();
		vista.mostrar("Fin de programa...");
	}

	/**
	 * Controla el acceso de usuario 
	 * y registro de la sesión correspondiente.
	 * @param credencialUsr ya obtenida, puede ser null.
	 */
	private void iniciarSesionUsuario(String idUsr) {
		int intentos = new Integer(Configuracion.get().getProperty("sesion.intentosFallidos"));			// Contandor de intentos.
		String credencialUsr = idUsr;
		vista.mostrar("JV-2016");
		do {
			if (idUsr == null) {
				// Pide credencial usuario si llega null.
				credencialUsr = vista.pedirIdUsr();	
			}
			credencialUsr = credencialUsr.toUpperCase();
			// Pide contraseña.
			String clave = vista.pedirClaveAcceso();

			// Busca usuario coincidente con credencial.
			vista.mostrar(credencialUsr);
			usrSesion = fachada.obtenerUsuario(credencialUsr);
			if ( usrSesion != null) {			
				try {
					if (usrSesion.getClaveAcceso().equals(new ClaveAcceso(clave))) {
						registrarSesion();
						break;
					}
				} 
				catch (ModeloException e) {
					//e.printStackTrace();
				}
				intentos--;
				vista.mostrar("Credenciales incorrectas...");
				vista.mostrar("Quedan " + intentos + " intentos... ");
			}
		}
		while (intentos > 0);

		if (intentos <= 0){
			vista.mostrar("Fin de programa...");
			fachada.cerrar();
			System.exit(0);	
		}
	}

	/**
	 * Crea la sesion de usuario 
	 */
	private void registrarSesion() {
		// Registra sesión.
		// Crea la sesión de usuario en el sistema.
		try {
			sesion = new SesionUsuario(usrSesion, new Fecha(), EstadoSesion.ACTIVA);
		} 
		catch (ModeloException e1) {
			e1.printStackTrace();
		}
		try {
			fachada.altaSesion(sesion);
		} catch (DatosException e) {
			e.printStackTrace();
		}	
		vista.mostrar("Sesión: " + sesion.getIdSesion()
		+ '\n' + "Iniciada por: " + usrSesion.getNombre());	
	}

} //class
