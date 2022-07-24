package sparkles.ddbb;

import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.models.QueryExtras;
import sparkles.utilities.Utilities;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class GestorDDBB {
	private static final Logger LOGGER = LoggerFactory.getLogger(GestorDDBB.class);
	private Connection conexion;

	@SuppressWarnings("unused")
	private Statement stmt;
	private PreparedStatement pstmt;
	private CachedRowSet crs;
	private static String url = "jdbc:mysql://localhost:3306/sparklesddbb";
	private static Properties connectionProperties;
	/**
	 * Crea una conexion con la BBDD en la pool seleccionada
	 * @param url nombre de la url
	 * @param valor autocomit
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(String url, boolean valor, Properties properties) throws SQLException, NamingException {
		connectionProperties = properties;
		conexion = DriverManager.getConnection(url, connectionProperties);
		conexion.setAutoCommit(valor);
	}
	/**
	 * Crea una conexion con autocomit a la pool seleccionada
	 * @param url
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(String url) throws SQLException, NamingException {
		crearConexion(url, true, this.generateConnProperties());
	}
	/**
	 * Crea una conexion a la pool estandar 
	 * @param valor autocomit
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(boolean valor) throws SQLException, NamingException {
		crearConexion(url, valor, this.generateConnProperties());
	}
	/**
	 * Crea una conexion estandar
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion() throws SQLException, NamingException {
		crearConexion(url, true, this.generateConnProperties());
	}

	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @param autocomit
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(String url, String user, String password, boolean autocomit) throws SQLException, NamingException {
		crearConexion(url, true, this.generateConnProperties(user, password));
	}
	private Properties generateConnProperties(){
		Properties properties;
		try {
			properties = new Utilities().getProperties("connection");
		} catch (IOException e) {
			LOGGER.error("Error buscando el recurso que contiene las credenciales para conectar a la BBDD");
			properties = new Properties();
			properties.put("user", null);
			properties.put("password", null);
		}
		return properties;
	}
	private Properties generateConnProperties(String user, String password){
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", password);
		return properties;
	}

	/**
	 * Cierra la conexion con la bbdd
	 */
	public final void cerrarConexion() {
		try {
			if (conexion != null) {
				conexion.setAutoCommit(true);
				conexion.close();
				conexion = null;
			}
		} catch (Exception ep) {
			ep.printStackTrace();
		}
	}
	/**
	 * Cierra el prepare statment predeterminado
	 * @throws SQLException
	 */
	public void cerrarPreparedStatement() throws SQLException {
		if(pstmt!=null) {
			pstmt.close();
			pstmt=null;
		}
	}
	/**
	 * Cierra el crs predeterminado
	 * @throws SQLException
	 */
	public void cerrarCachedRowSet() throws SQLException {
		if(crs!=null) {
			crs.close();
			crs=null;
		}
	}
	/**
	 * Ejecuta una query con preparsetatment siendo los parametros la lista enviada por string
	 * @param query sql de la query
	 * @param parametros parametros de la query
	 * @return
	 * @throws SQLException
	 */
	public CachedRowSet ejecutarQuery(String query, List<String> parametros) throws SQLException {
		ResultSet rs;
		pstmt= conexion.prepareStatement(query);
		for(int i=0; i<parametros.size(); i++) {
			pstmt.setString(i+1, parametros.get(i));
		}
		rs=pstmt.executeQuery();
		crs=createCachedRowSet(rs);
		return crs;
		
	}
	/**
	 * Ejecuta una query  sin parametros
	 * @param query sql
	 * @return
	 * @throws SQLException
	 */
	public CachedRowSet ejecutarQuery(String query) throws SQLException {
		ResultSet rs;
		pstmt= conexion.prepareStatement(query);
		rs=pstmt.executeQuery();
		crs=createCachedRowSet(rs);
		return crs;
		
	}
	/**
	 * Crea un crs a traves de un rs y cierra el correspondiente rs 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private CachedRowSet createCachedRowSet(ResultSet rs) throws SQLException {
		CachedRowSet crs;
		RowSetFactory factory;
		factory=RowSetProvider.newFactory();
		crs = factory.createCachedRowSet();
		crs.populate(rs);
		if(rs!=null) {
			rs.close();
			rs=null;
		}
		return crs;
	}
	/**
	 * Ejecuta una update/insert con los parametros en una lista recibida por parametro
	 * @param query sql
	 * @param parametros parametros de la query
	 * @return
	 * @throws SQLException
	 */
	public int ejecutarUpdate(String query, List<Object> parametros) throws SQLException {
		int check;
		pstmt= conexion.prepareStatement(query);
		for(int i=0; i<parametros.size(); i++) {
			pstmt.setObject(i+1, parametros.get(i));
		}
		check=pstmt.executeUpdate();
		return check;
	}
	/**
	 * Devuelve los datos de una query con filtros estandar
	 * @param tableName
	 * @param queryExtras
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 */
	public CachedRowSet getDataWithFilters(String tableName, QueryExtras queryExtras) throws SQLException, NamingException {
		int i=0;
		CachedRowSet crs;
		List<String> parametros = new ArrayList<String>();
		StringBuilder baseSql= new StringBuilder("select * from ");
		baseSql.append(tableName);
		baseSql.append(" where");
		for (Map.Entry<String, String> entry : queryExtras.getFiltros().entrySet()) {
	        if(i>0) {
	        	baseSql.append(" and ");
	        }
	        baseSql.append(entry.getKey()+"= ? ");
	        parametros.add(entry.getValue());
	        i++;
	    }
		if(queryExtras.isOrderActive()) {
			baseSql.append("order by ");
			baseSql.append(queryExtras.getOrderColumn());
			if(queryExtras.getOrderType()!=null) {
				baseSql.append(" ");
				baseSql.append(queryExtras.getOrderType());
			}
		}
		this.crearConexion();
		crs=this.ejecutarQuery(baseSql.toString(), parametros);
		this.cerrarConexion();
		return crs;
	}
	
}
