/*package com.qyh.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qyh.demo.entity.SysUser;

public interface UserRepository extends JpaRepository<SysUser, String>{

	//使用sql的方式查询
	@Query(value="select * from sys_user",nativeQuery=true)
	List<SysUser> findAllUserList();
	
	//使用方法名自动生成查询语句
	List<SysUser> findByUserType(String userType);
	
	//分页
	Page<SysUser> findByUserType(String userType,Pageable page);
}
*/