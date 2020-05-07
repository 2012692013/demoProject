package com.qyh.demo.base.mapper;

import java.util.List;

public interface BaseService<T> {
	
	 /**
     * 保存实体
     * @param entity
     * @return
     */
	public int saveEntity(T entity);

	/**
	 * 根据ID删除实体
	 * @param id
	 * @return
	 */
	public int deleteById(String id);
	
	/**
	 * 根据条件删除实体
	 * @param entityClass	实体的class
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public int deleteByLimit(Class<?> entityClass,List<String> whereList);

	/**
	 * 根据ID修改实体中【非空】的字段
	 * @param entity
	 * @return
	 */
	public int updateEntityById(T entity);
	
	/**
	 * 根据条件修改实体中【非空】的字段
	 * @param entity
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public int updateByEntity(T entity, List<String> whereList);
	
	/**
	 * 根据ID修改实体中【所有】的字段
	 * @param entity
	 * @return
	 */
	public int updateAll(T entity);
	
	/**
	 * 根据ID查询单个实体
	 * @param id
	 * @return
	 */
	public T findEntityById(String id);
	
	/**
	 * 根据条件查询单个实体
	 * @param entityClass	实体的class
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public T findOneByLimit(Class<?> entityClass,List<String> whereList);
	
	/**
	 * 根据条件获取多个实体
	 * @param entityClass		实体的class
	 * @param whereList			注：条件字段用表字段，非实体属性名
	 * @param orderByClause		排序字段	如："create_time desc"
	 * @return
	 */
	public List<T> findListByLimit(Class<?> entityClass,List<String> whereList,String orderByClause);
	
	
}
