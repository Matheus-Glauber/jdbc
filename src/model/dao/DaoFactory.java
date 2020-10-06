package model.dao;

import db.DB;
import model.dao.impl.VendedorDaoImpl;

public class DaoFactory {
	
	public static VendedorDAO createVendedorDao() {
		return new VendedorDaoImpl(DB.getConnection());
	}

}
