package dao;

import java.util.List;

/**
 * 数据库操作基础接口
 * @author xanthondont
 *
 */
public interface IBasicDao<TEntity> {
	/**
	 * 查找全部实体
	 * @return
	 */
	List<TEntity> all();
	
	
	/**
	 * 添加实体到数据库
	 * @param entities
	 * @return
	 */
	IBasicDao<TEntity> insert(TEntity... entities);
	
	/**
	 * 删除实体
	 * @param entities
	 * @return
	 */
	IBasicDao<TEntity> delete(TEntity... entities);
	
	/**
	 * 更新实体
	 * @param entity
	 * @return
	 */
	IBasicDao<TEntity> update(TEntity entity);
	
	/**
	 * 提交数据
	 */
	void commit();
}
