package sparkles.models;

import java.util.HashMap;
import java.util.Map;
/**
 * Almacena los datos correspondientes a peticiones filtradas y sus diferentes necesidades
 * @author diego
 *
 */
public class QueryExtras {
	public enum OrderType{ASC,DESC};
	private Map<String, String> listaFiltros;
	private String order;
	private OrderType orderType;
	
	public QueryExtras() {
		this.listaFiltros = new HashMap<String, String>();
		this.order = null;
	}
	
	public void addFiltro(String nombreFiltro, String filtro) {
		this.listaFiltros.put(nombreFiltro, filtro);
	}
	
	public boolean isOrderActive() {
		return this.order!=null;
	}
	
	public void disableOrder() {
		this.order=null;
	}
	
	public void setOrder(String columnName, OrderType orderType) {
		this.order=columnName;
		this.orderType=orderType;
	}
	
	public Map<String, String> getFiltros (){
		return listaFiltros;
	}
	
	public String getOrderColumn() {
		return order;
	}
	
	public String getOrderType () {
		return orderType.toString();
	}
	
}
