package com.qyh.demo.base.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * 调用通用mapper实现单表操作
 */
public abstract class BaseServiceImpl<T> implements BaseService<T>{

    @Autowired
    private Mapper<T> mapper;
    
    /**
     * 保存实体
     * @param entity
     * @return
     */
	public int saveEntity(T entity){
		return this.mapper.insert(entity);
	}

	/**
	 * 根据ID删除实体
	 * @param id
	 * @return
	 */
	public int deleteById(String id) {
        return this.mapper.deleteByPrimaryKey(id);
	}
	
	/**
	 * 根据条件删除实体
	 * @param entityClass	实体的class
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public int deleteByLimit(Class<?> entityClass,List<String> whereList){
		return this.mapper.deleteByExample(this.packagingExample(entityClass, whereList,null));
	}

	/**
	 * 根据ID修改实体中【非空】的字段
	 * @param entity
	 * @return
	 */
	public int updateEntityById(T entity){
		return this.mapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 根据条件修改实体中【非空】的字段
	 * @param entity
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public int updateByEntity(T entity, List<String> whereList){
		return this.mapper.updateByExampleSelective(entity, this.packagingExample(entity.getClass(), whereList,null));
	}
	
	/**
	 * 根据ID修改实体中【所有】的字段
	 * @param entity
	 * @return
	 */
	public int updateAll(T entity) {
		return this.mapper.updateByPrimaryKey(entity);
	}
	
	/**
	 * 根据ID查询单个实体
	 * @param id
	 * @return
	 */
	public T findEntityById(String id){
		return this.mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据条件查询单个实体
	 * @param entityClass	实体的class
	 * @param whereList		注：条件字段用表字段，非实体属性名
	 * @return
	 */
	public T findOneByLimit(Class<?> entityClass,List<String> whereList){
		List<T> list = this.findListByLimit(entityClass, whereList,null);
    	if(list.size() >= 1){
    		return list.get(0);
    	}
    	return null;
	}
	
	/**
	 * 根据条件获取多个实体
	 * @param entityClass		实体的class
	 * @param whereList			注：条件字段用表字段，非实体属性名
	 * @param orderByClause		排序字段	如："create_time desc"
	 * @return
	 */
	public List<T> findListByLimit(Class<?> entityClass,List<String> whereList,String orderByClause){
		return this.mapper.selectByExample(this.packagingExample(entityClass, whereList,orderByClause));
	}
	
	 /**
     * 封装条件
     * @param entity
     * @param whereList
     * @return
     */
    private Example packagingExample(Class<?> entityClass,List<String> whereList,String orderByClause){
    	Example example = new Example(entityClass);
        Example.Criteria criteria = example.createCriteria();
         
    	if(whereList != null){
    		for(String where : whereList){
        		criteria.andCondition(where);
    		}
    	}
    	
    	if(orderByClause != null && !"".equals(orderByClause)){
    		example.setOrderByClause(orderByClause);
    	}
        
    	return example;
    }
}
