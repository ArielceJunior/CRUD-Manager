package controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;

@WebServlet("/logincontroller")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		User user = null;
		try {
			user = userDAO.findByEmailAndPassword(email, password);
		} catch (ModelException e) {
			e.printStackTrace();
		}

		if (user != null) {
			HttpSession session = request.getSession();
			session.setAttribute("loggedUser", user);
			response.sendRedirect(request.getContextPath());
		} else {
			request.setAttribute("errorMessage", "E-mail ou senha inválidos.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
		}
	}
}
