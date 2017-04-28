/** 
 * Proyecto: Juego de la vida.
 * Implementa el concepto de contraseña de seguridad según el modelo 2.1.
 * Se hace validación de datos pero no se gestionan todavía los errores correspondientes.
 * @since: prototipo1.2
 * @source: ClaveAcceso.java 
 * @version: 2.1 - 2017.04.28
 * @author: ajp
 * @author: Grupo 1
 */

package modelo;

import java.io.Serializable;
import util.Criptografia;
import util.Formato;

public class ClaveAcceso implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	private String texto;

	public ClaveAcceso(String texto) throws ModeloException {
		setTexto(texto);
	}

	public ClaveAcceso() throws ModeloException  {
		this("Miau#0");
	}

	public ClaveAcceso(ClaveAcceso claveAcceso) throws ModeloException {
		this(claveAcceso.texto);
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) throws ModeloException  {
		if (ClaveAccesoValida(texto)) {
			this.texto = Criptografia.cesar(texto);
			return;
		}
		throw new ModeloException("El correo: " + texto + " no es válido...");
	}

	private boolean ClaveAccesoValida(String texto) {
		if (texto != null) {
			return	texto.matches(Formato.PATRON_CONTRASEÑA);
		}
		return false;
	}

	@Override
	public String toString() {
		return texto;
	}

	/**
	 * hashcode() complementa al método equals y sirve para comparar objetos de forma 
	 * rápida en estructuras Hash. 
	 * Cuando Java compara dos objetos en estructuras de tipo hash (HashMap, HashSet etc)
	 * primero invoca al método hashcode y luego el equals.
	 * @return un número entero de 32 bit.
	 */
	@Override
	public int hashCode() {
		final int primo = 31;
		int result = 1;
		result = primo * result + ((texto == null) ? 0 : texto.hashCode());
		return result;
	}

	/**
	 * Dos objetos son iguales si: 
	 * Son de la misma clase.
	 * Tienen los mismos valores en los atributos; o son el mismo objeto.
	 * @return falso si no cumple las condiciones.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			if (this == obj) {
				return true;
			}
			if (texto.equals(((ClaveAcceso) obj).texto)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Genera un clon del propio objeto realizando una copia profunda.
	 * @return el objeto clonado.
	 */
	@Override
	public Object clone() {
		// Utiliza el constructor copia.
				Object clon = null;
				try {
					clon = new ClaveAcceso(this);
				} 
				catch (ModeloException e) {
					e.printStackTrace();
				}
				return clon;
	}

} //class
