package model.dao;

import java.util.List;

public interface CRUD<T> {

	boolean insert(T obj);

	boolean update(T obj);

	boolean deleteById(Integer id);

	T findById(Integer id);

	List<T> findAll();
}
