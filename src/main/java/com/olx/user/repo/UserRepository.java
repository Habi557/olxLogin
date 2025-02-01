package com.olx.user.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.olx.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	
	public List<UserEntity> findByUserName(String name);
	public List<UserEntity> findByEmail(String name);
	@Query(value = "SELECT * FROM users WHERE BINARY username = :username", nativeQuery = true)
	List<UserEntity> findByUserNameCaseSensitive(@Param("username") String username);



}
