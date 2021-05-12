package application.database.dao;

import java.util.List;

public interface GenericDAO<T> {
	public Long create(T object);

	public T getByPK(Long key);

	public void update(T object);

	public void delete(Long key);

	public List<T> getAll();

}
