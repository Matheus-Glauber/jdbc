package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDAO {

	void insert(Vendedor vendedor);

	void update(Vendedor vendedor);

	void deleteById(Integer id);

	Vendedor findById(Integer id);
	
	List<Vendedor> findByDepartamento(Departamento dep);

	List<Vendedor> findAll();

}
