package sparkles.ddbb.conversores;
/**
 * Clase que gestiona las diferentes necesidades de conversion de datos
 * Actualmente en deshuso
 * @author diego
 *
 */
public class DataConversor {
	
	/**
	 * Convierte el nombre mandado en el formato asignado para la base de datos
	 * Actualmente: pone todo en minisculas y cambia todos los espacios por _
	 * @param str
	 * @return
	 */
	public String databaseStringConversor(String str) {
		str=str.toLowerCase();
		str=str.replaceAll("\\s","_");
		return str;
	}
}
