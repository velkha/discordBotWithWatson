package sparkles.ddbb;

import sparkles.models.QueryExtras;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class GestorDDBB {
	
	private Connection conexion;
	@SuppressWarnings("unused")
	private Statement stmt;
	private PreparedStatement pstmt;
	private CachedRowSet crs;
	private Context envCtx;
	private static String pool = "DNDIBERIA_BD";
	
	/**
	 * Crea una conexion con la BBDD en la pool seleccionada
	 * @param sPool nombre de la pool
	 * @param valor autocomit
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(String sPool, boolean valor) throws SQLException, NamingException {
		envCtx = new InitialContext();
		DataSource ds = (javax.sql.DataSource) envCtx.lookup(sPool);
		conexion = ds.getConnection();
		conexion.setAutoCommit(valor);
	}
	/**
	 * Crea una conexion con autocomit a la pool seleccionada
	 * @param sPool
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(String sPool) throws SQLException, NamingException {
		crearConexion(sPool, true);
	}
	/**
	 * Crea una conexion a la pool estandar 
	 * @param valor autocomit
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion(boolean valor) throws SQLException, NamingException {
		crearConexion(pool, valor);
	}
	/**
	 * Crea una conexion estandar
	 * @throws SQLException
	 * @throws NamingException
	 */
	public final void crearConexion() throws SQLException, NamingException {
		crearConexion(pool, true);
	}

	/**
	 * Crea conexion de BBDD
	 *
	 * @param url
	 * @param user
	 * @param password
	 * @throws SQLException
	 */
	public void createDatabaseConnectionWithoutPool(String url, String user, String password) throws SQLException {
		conexion = DriverManager.getConnection(url, user, password);
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
