package model.dao;

import java.util.List;

import model.Skin;
import model.ModelException;

public interface SkinsDAO {
	boolean save(Skin skins) throws ModelException;
	boolean update(Skin skins) throws ModelException;
	boolean delete(Skin skins) throws ModelException;
	List<Skin> listAll() throws ModelException;
	Skin findById(int id) throws ModelException;
}