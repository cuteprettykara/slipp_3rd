package net.slipp.user;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/users/updateForm")
public class UpdateFormUserServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = SessionUtils.getStringValue(session, LoginServlet.SESSION_USER_ID);
		if (userId == null) {
			resp.sendRedirect("/");
			return;			
		}
				  
		UserDAO userDao = new UserDAO(); 
		User user = null;
		
		try {
			user = userDao.findById(userId);
			req.setAttribute("isUpdate", true);
			req.setAttribute("user", user);
			
			RequestDispatcher dispatcher = req.getRequestDispatcher("/form.jsp");
			dispatcher.forward(req, resp);
		} catch (SQLException e) {
		}
		
	}
}
