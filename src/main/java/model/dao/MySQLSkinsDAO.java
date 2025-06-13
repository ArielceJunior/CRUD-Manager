package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Skin;
import model.ModelException;
import model.User;

public class MySQLSkinsDAO implements SkinsDAO {

	@Override
	public boolean save(Skin skin) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO skins VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, skin.getName());
		db.setString(2, skin.getWeapon());
		db.setFloat(3, skin.getWaste());
		db.setInt(4, skin.getUser().getId());
		db.setDouble(5, skin.getPrice());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Skin skin) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE skins "
				+ " SET name = ?, "
				+ " weapon = ?, "
				+ " waste = ?, "
				+ " user = ?, "
				+ " price = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, skin.getName());
		db.setString(2, skin.getWeapon());
		db.setFloat(3, skin.getWaste());
		db.setInt(4, skin.getUser().getId());
		db.setDouble(5, skin.getPrice());
		db.setInt(6, skin.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Skin skin) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM skins "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, skin.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Skin> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Skin> skins = new ArrayList<Skin>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT s.id as 'skin_id', s.*, u.* \n"
				+ " FROM skins s \n"
				+ " INNER JOIN users u \n"
				+ " ON s.user = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Skin skin = new Skin(db.getInt("skin_id"));
			skin.setName(db.getString("name"));
			skin.setWeapon(db.getString("weapon"));
			skin.setWaste(db.getFloat("waste"));
			skin.setUser(user);
			skin.setPrice(db.getDouble("price"));
			
			
			skins.add(skin);
		}
		
		return skins;
	}

	@Override
	public Skin findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM skins WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Skin s = null;
		while (db.next()) {
			s = new Skin(id);
			s.setName(db.getString("name"));
			s.setWeapon(db.getString("weapon"));
			s.setWaste(db.getFloat("waste"));
			s.setPrice(db.getDouble("price"));
			
			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
			User user = userDAO.findById(db.getInt("user"));
			s.setUser(user);

			break;
		}
		
		return s;
	}
}
