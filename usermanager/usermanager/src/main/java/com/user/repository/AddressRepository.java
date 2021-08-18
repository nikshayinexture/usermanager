package com.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.user.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	
	//Pagination Method
	
	@Query("from Address as a where a.user.id =:userId")
	public List<Address> findAddressByUser(@Param("userId")int userId);
}
