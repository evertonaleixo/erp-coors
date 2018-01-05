package br.com.lima.erpcoors.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lima.erpcoors.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("SELECT p FROM order_table p WHERE created BETWEEN :ini AND :end")
    public List<Order> betweenQuery(@Param("ini") Date ini, @Param("end") Date end);
	
	@Query("SELECT p FROM order_table p WHERE created < :end")
    public List<Order> lessQuery(@Param("end") Date end);

	@Query("SELECT p FROM order_table p WHERE created > :ini")
    public List<Order> greaterQuery(@Param("ini") Date ini);

	
	@Query("SELECT p FROM order_table p WHERE client_id=:filter AND created BETWEEN :ini AND :end")
	public List<Order> betweenQueryAndFilter(@Param("ini") Date ini, @Param("end") Date end, @Param("filter") long idFilter);

	@Query("SELECT p FROM order_table p WHERE client_id=:filter AND created < :end")
	public List<Order> lessQueryAndFilter(@Param("end") Date end, @Param("filter") long idFilter);

	@Query("SELECT p FROM order_table p WHERE client_id=:filter AND created > :ini")
	public List<Order> greaterQueryAndFilter(@Param("ini") Date ini, @Param("filter") long idFilter);

	@Query("SELECT p FROM order_table p WHERE client_id=:filter")
	public List<Order> filterQuery(@Param("filter") long idFilter);

	// SELECT p FROM order_table p inner join client c WHERE p.client_id=c.id LOWER(p.name) LIKE CONCAT('%',LOWER(:name),'%')
	@Query("SELECT e FROM order_table e LEFT JOIN e.client d WHERE LOWER(d.name) LIKE CONCAT('%',LOWER(:name),'%')")
	public List<Order> customQuery(@Param("name") String name);
}
