package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Skin;
import model.ModelException;
import model.User;
import model.dao.SkinsDAO;
import model.dao.DAOFactory;

@WebServlet(urlPatterns = {"/skins", "/skin/form", 
		"/skin/insert", "/skin/delete", "/skin/update"})
public class SkinsController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		
		switch (action) {
		case "/crud-manager/skin/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-skins.jsp");			
			break;
		}
		case "/crud-manager/skin/update": {
			String idStr = req.getParameter("skinId");
			int idSkin = Integer.parseInt(idStr); 
			
			SkinsDAO dao = DAOFactory.createDAO(SkinsDAO.class);
			
			Skin skin = null;
			try {
				skin = dao.findById(idSkin);
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			CommonsController.listUsers(req);
			req.setAttribute("action", "update");
			req.setAttribute("skin", skin);
			ControllerUtil.forward(req, resp, "/form-skins.jsp");
			break;
		}
		default:
			listSkins(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
		
			ControllerUtil.forward(req, resp, "/skins.jsp");
		}
	}
	
	private void listSkins(HttpServletRequest req) {
		SkinsDAO dao = DAOFactory.createDAO(SkinsDAO.class);
		
		List<Skin> skins = null;
		try {
			skins = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (skins != null)
			req.setAttribute("skins", skins);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		
		switch (action) {
		case "/crud-manager/skin/insert": {
			insertSkin(req, resp);		
			break;
		}
		case "/crud-manager/skin/delete" :{
			
			deleteSkin(req, resp);
			
			break;
		}
		
		case "/crud-manager/skin/update" :{
			updateSkin(req, resp);
			break;
		}
		
		default:
			System.out.println("URL inválida " + action);
		}
		
		ControllerUtil.redirect(resp, req.getContextPath() + "/skins");
	}

	private void updateSkin(HttpServletRequest req, HttpServletResponse resp) {
		String skinIdStr = req.getParameter("skinId");
		String skinName = req.getParameter("name");
		String weapon = req.getParameter("weapon");
		Float waste = Float.parseFloat(req.getParameter("waste"));
		Integer userId = Integer.parseInt(req.getParameter("user"));
		Double price = Double.parseDouble(req.getParameter("price"));
		
		Skin skin = new Skin(Integer.parseInt(skinIdStr));
		skin.setName(skinName);
		skin.setWeapon(weapon);
		skin.setWaste(waste);
		skin.setUser(new User(userId));
		skin.setPrice(price);
		
		
		SkinsDAO dao = DAOFactory.createDAO(SkinsDAO.class);
		
		try {
			if (dao.update(skin)) {
				ControllerUtil.sucessMessage(req, "Skin '" + 
						skin.getName() + "' atualizada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Skin '" + 
						skin.getName() + "' não pode ser atualizada.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}		
	}

	private void deleteSkin(HttpServletRequest req, HttpServletResponse resp) {
		String skinIdParameter = req.getParameter("id");
		
		int skinId = Integer.parseInt(skinIdParameter);
		
		SkinsDAO dao = DAOFactory.createDAO(SkinsDAO.class);
		
		try {
			Skin skin = dao.findById(skinId);
			
			if (skin == null)
				throw new ModelException("Skin não encontrada para deleção.");
			
			if (dao.delete(skin)) {
				ControllerUtil.sucessMessage(req, "Skin '" + 
						skin.getName() + "' deletada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Skin '" + 
						skin.getName() + "' não pode ser deletado. "
								+ "Há dados relacionados à empresa.");
			}
		} catch (ModelException e) {
			// log no servidor
			if (e.getCause() instanceof 
					SQLIntegrityConstraintViolationException) {
				ControllerUtil.errorMessage(req, e.getMessage());
			}
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void insertSkin(HttpServletRequest req, HttpServletResponse resp) {
		String skinIdStr = req.getParameter("skinId");
		String skinName = req.getParameter("name");
		String weapon = req.getParameter("weapon");
		Float waste = Float.parseFloat(req.getParameter("waste"));
		Integer userId = Integer.parseInt(req.getParameter("user"));
		Double price = Double.parseDouble(req.getParameter("price"));
		
		Skin skin = new Skin();
		skin.setName(skinName);
		skin.setWeapon(weapon);
		skin.setWaste(waste);
		skin.setUser(new User(userId));
		skin.setPrice(price);
		
		SkinsDAO dao = DAOFactory.createDAO(SkinsDAO.class);
	
		try {
			if (dao.save(skin)) {
				ControllerUtil.sucessMessage(req, "Skin '" + skin.getName() 
				+ "' salva com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Skin '" + skin.getName()
				+ "' não pode ser salva.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
	
}
