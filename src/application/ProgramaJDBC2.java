package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDAO;
import model.entities.Departamento;
import model.entities.Vendedor;

public class ProgramaJDBC2 {

	public static void main(String[] args) {
				
		VendedorDAO vendedorDao = DaoFactory.createVendedorDao();
		
		System.out.println("--== Teste 1 - Buscar Vendedor Por Id ==--");
		Vendedor vendedor = vendedorDao.findById(3);
		System.out.println(vendedor);
		
		System.out.println("");
		System.out.println("--== Teste 2 - Buscar Vendedor Por Departamento ==--");
		Departamento dep = new Departamento();
		dep.setid(2);
		List<Vendedor> vendedores = vendedorDao.findByDepartamento(dep);
		
		vendedores.stream().forEach(System.out::println);
		
		System.out.println("");
		System.out.println("--== Teste 3 - Buscar Todos os Vendedores ==--");
		dep.setid(2);
		List<Vendedor> vendedores1 = vendedorDao.findAll();
		
		vendedores1.stream().forEach(System.out::println);
		
		System.out.println("");
		System.out.println("--== Teste 4 - Inserir Vendedor ==--");
		Vendedor vend = new Vendedor(null, "Matheus Glauber", "matheus@email.com", new Date(), 7500.00, dep);
		
		vendedorDao.insert(vend);
		System.out.println("inserido: " + vend.getNome());
		
		System.out.println("");
		System.out.println("--== Teste 5 - Alterar Vendedor ==--");
		vend = vendedorDao.findById(7);
		vend.setNome("Teste dos Santos");
		
		vendedorDao.update(vend);
		System.out.println("Atualização completa!");
		
		System.out.println("");
		System.out.println("--== Teste 6 - Deletar Vendedor ==--");
		
		vendedorDao.deleteById(7);
		System.out.println("Deletado com sucesso!");
		
	}

}
