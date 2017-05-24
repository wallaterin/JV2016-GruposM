/** 
 * Proyecto: Juego de la vida.
 * Resuelve todos los aspectos del almacenamiento del DTO Usuario 
 * utilizando alamacenamiento en una base de datos db4o.
 * Colabora en el usuario Fachada.
 * @since: prototipo2.2
 * @source: UsuariosDAO.java 
 * @version: 2.2 - 2017/05/24
 * @author: ajp - Modificación: David Olivera Jaén.
 */

package accesoDatos.db4o;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import accesoDatos.DatosException;
import accesoDatos.OperacionesDAO;
import config.Configuracion;
import modelo.ClaveAcceso;
import modelo.Correo;
import modelo.DireccionPostal;
import modelo.ModeloException;
import modelo.Nif;
import modelo.Usuario;
import modelo.Usuario.RolUsuario;
import util.Fecha;

public class UsuariosDAO  implements OperacionesDAO {

	// Requerido por el Singleton. 
	private static UsuariosDAO instancia = null;

	// Elementos de almacenamiento.
	private ObjectContainer db;
	private static Map<String,String> equivalenciasId;
	private static ArrayList<Usuario> datosUsr;
	/**
	 * Constructor por defecto de uso interno.
	 * Sólo se ejecutará una vez.
	 */
	private UsuariosDAO()  {
		db = Conexion.getDB();
		equivalenciasId = new Hashtable<String, String>();
		cargarPredeterminados();
	}

	/**
	 *  Método estático de acceso a la instancia única.
	 *  Si no existe la crea invocando al constructor interno.
	 *  Utiliza inicialización diferida.
	 *  Sólo se crea una vez; instancia única -patrón singleton-
	 *  @return instancia
	 */
	public static UsuariosDAO getInstancia() {
		if (instancia == null) {
			instancia = new UsuariosDAO();
		}
		return instancia;
	}

	/**
	 *  Método para generar de datos predeterminados.
	 */
	private void cargarPredeterminados() {
		try {
			String nombreUsr = Configuracion.get().getProperty("usuario.admin");
			String password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
			Usuario usrPredeterminado = new Usuario(new Nif("00000000T"), nombreUsr, "Admin Admin", 
					new DireccionPostal("Iglesia", "0", "30012", "Murcia"), 
					new Correo("jv.admin" + "@gmail.com"), new Fecha(), 
					new Fecha(), new ClaveAcceso(password), RolUsuario.ADMINISTRADOR);
			db.store(usrPredeterminado);
			registrarEquivalenciaId(usrPredeterminado);

			nombreUsr = Configuracion.get().getProperty("usuario.invitado");
			password = Configuracion.get().getProperty("usuario.passwordPredeterminada");	
			usrPredeterminado = new Usuario(new Nif("00000001R"), nombreUsr, "Invitado Invitado", 
					new DireccionPostal("Iglesia", "00", "30012", "Murcia"), 
					new Correo("jv.invitado" + "@gmail.com"), new Fecha(), 
					new Fecha(), new ClaveAcceso(password), RolUsuario.INVITADO);
			db.store(usrPredeterminado);
			registrarEquivalenciaId(usrPredeterminado);
		} 
		catch (ModeloException e) { }
	}

	/**
	 *  Cierra datos.
	 */
	@Override
	public void cerrar() {
		/**
		 *  Nada que hacer al ser una base de datos 
		 *  compartida con todas las clases.
		 */
	}

	//OPERACIONES DAO
	/**
	 * Búsqueda de usuario dado su idUsr, el correo o su nif.
	 * @param id - el id de Usuario a buscar.
	 * @return - el Usuario encontrado o null si no existe.
	 */
	@Override
	public Usuario obtener(String idUsr) {
		
		ObjectSet <Usuario> result;
		Query consulta = db.query();
		consulta.constrain(Usuario.class);
		consulta.descend("idUsr").equals(idUsr);
		result=consulta.execute();
		if (result.size()>0){
			return result.get(0);
		}
		return null;
	}

	/**
	 *  Obtiene por búsqueda binaria, la posición que ocupa, o ocuparía,  un usuario en 
	 *  la estructura.
	 *	@param IdUsr - id de Usuario a buscar.
	 *	@return - la posición, en base 1, que ocupa un objeto o la que ocuparía (negativo).
	 */
	
	private int obtenerPosicion(String idUsr) {
		int comparacion;
		int inicio = 0;
		int fin = datosUsr.size() - 1;
		int medio = 0;
		while (inicio <= fin) {
			medio = (inicio + fin) / 2;			// Calcula posición central.
			// Obtiene > 0 si idUsr va después que medio.
			comparacion = idUsr.compareTo(datosUsr.get(medio).getIdUsr());
			if (comparacion == 0) {			
				return medio + 1;   			// Posción ocupada, base 1	  
			}		
			if (comparacion > 0) {
				inicio = medio + 1;
			}			
			else {
				fin = medio - 1;
			}
		}	
		return -(inicio + 1);					// Posición que ocuparía -negativo- base 1
	}

	/**
	 * Búsqueda de Usuario dado un objeto, reenvía al método que utiliza idUsr.
	 * @param obj - el Usuario a buscar.
	 * @return - el Usuario encontrado; null si no existe.
	 */
	@Override
	public Usuario obtener(Object obj)  {
		return this.obtener(((Usuario) obj).getIdUsr());
	}	

	/**
	 *  Alta de un nuevo usuario en orden y sin repeticiones según el campo idUsr. 
	 *  Localiza previamente la posición de inserción, en orden, que le corresponde.
	 *  Si hay coincidencia de identificador hace 23 intentos de variar la última letra
	 *  procedente del NIF. Llama al generarVarianteIdUsr() de la clase Usuario.
	 *	@param obj - Objeto a almacenar.
	 *  @throws DatosException - si ya existe.
	 */
	@Override
	public void alta(Object obj) throws DatosException {
		assert obj != null;
		Usuario usrNuevo = (Usuario) obj;										// Para conversión cast
		int posicionInsercion = obtenerPosicion(usrNuevo.getNombre()); 
		if (posicionInsercion < 0) {
			datosUsr.add(-posicionInsercion - 1, usrNuevo); 			// Inserta la sesión en orden.
			return;
		}
		throw new DatosException("(ALTA) El Usuario: " + usrNuevo.getNombre() + " ya existe...");
	}

	/**
	 *  Añade nif y correo como equivalencias de idUsr para el inicio de sesión. 
	 *	@param usr - Usuario a registrar equivalencias. 
	 */
	private void registrarEquivalenciaId(Usuario usr) {
		assert usr != null;
		equivalenciasId.put(usr.getIdUsr(), usr.getIdUsr());
		equivalenciasId.put(usr.getNif().getTexto(), usr.getIdUsr());
		equivalenciasId.put(usr.getCorreo().getTexto(), usr.getIdUsr());
	}

	/**
	 * Elimina el objeto, dado el id utilizado para el almacenamiento.
	 * @param idUsr - el identificador del objeto a eliminar.
	 * @return - el Objeto eliminado.
	 * @throws DatosException - si no existe.
	 */
	@Override
	public Usuario baja(String nombre) throws DatosException {
		assert (nombre != null);
		int posicion = obtenerPosicion(nombre); 									// En base 1
		if (posicion > 0) {
			return datosUsr.remove(posicion - 1); 								// En base 0
		}
		throw new DatosException("(BAJA) El Usuario: " + nombre + " no existe...");
	}

	/**
	 *  Actualiza datos de un Usuario reemplazando el almacenado por el recibido. 
	 *  No admitirá cambios en el idUsr.
	 *	@param obj - Usuario con los cambios.
	 *  @throws DatosException - si no existe.
	 */
	@Override
	public void actualizar(Object obj) throws DatosException, ModeloException{
		Usuario usrActualizado = (Usuario) obj;
		Usuario usrAlmacenado = obtener(usrActualizado);
		if (usrAlmacenado != null){
			usrAlmacenado.setNombre(usrActualizado.getNombre());
			usrAlmacenado.setApellidos(usrActualizado.getApellidos());
			usrAlmacenado.setDomicilio(usrActualizado.getDomicilio());
			usrAlmacenado.setCorreo(usrActualizado.getCorreo());
			usrAlmacenado.setFechaNacimiento(usrActualizado.getFechaNacimiento());
			usrAlmacenado.setNif(usrActualizado.getNif());
			usrAlmacenado.setFechaAlta(usrActualizado.getFechaAlta());
			usrAlmacenado.setRol(usrActualizado.getRol());
			db.store(usrAlmacenado);
			}
		throw new DatosException("(ACTUALIZAR) El Usuario: " + usrActualizado.getNombre() + " no existe...");
		}

	
	private List<Usuario> obtenerTodos() {
		Query consulta = db.query();
		consulta.constrain(Usuario.class);
		return consulta.execute();
		
	}
	/**
	 * Obtiene el listado de todos los usuarios almacenados.
	 * @return el texto con el volcado de datos.
	 */
	@Override
	public String listarDatos() {
		StringBuilder listado = new StringBuilder();
		for (Usuario usuario: obtenerTodos()) {
			if (usuario != null) {
				listado.append("\n" + usuario); 
			}
		}
		return listado.toString();
	}

	
	/**
	 * Elimina todos los usuarios almacenados y regenera los predeterminados.
	 */
	@Override
	public void borrarTodo() {
		for (Usuario usuario: obtenerTodos()) {
			db.delete(usuario);
		}
	}

	/**
	 *  Método vacío por requerimiento de la interfaz.
	 */
	@Override
	public String listarId() {
		
		return null;
	}
	
} //class