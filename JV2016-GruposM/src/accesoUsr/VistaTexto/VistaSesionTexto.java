/** Proyecto: Juego de la vida.
 *  Resuelve todos los aspectos relacionados con la presentación
 *  del inicio de sesión de usuario. 
 *  Colabora en el patron MVC
 *  @since: prototipo2.1
 *  @source: VistaSesionTexto.java 
 *  @version: 2.1 - 2017.05.11
 *  @author: ajp
 */

package accesoUsr.VistaTexto;

import java.util.Scanner;

public class VistaSesionTexto {
	
	private Scanner teclado;
	
	public VistaSesionTexto() {
		teclado = new Scanner(System.in);
	}
	
	public String pedirIdUsr() {	
		System.out.println("Introduce el idUsr: ");
		return teclado.nextLine();
	}
	
	public String pedirClaveAcceso() {	
		System.out.println("Introduce clave acceso: ");
		return teclado.nextLine();
	}

	public void mostrar(String mensaje) {
		System.out.println(mensaje);
	}
}
