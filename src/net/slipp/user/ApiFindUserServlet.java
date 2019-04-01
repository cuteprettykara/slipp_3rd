package net.slipp.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/api/users/find")
public class ApiFindUserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userId = req.getParameter("userId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		
		UserDAO userDao = new UserDAO();
		User user = userDao.findById(userId);
		
		if (user == null) {
			return;
		}
		
		Gson gson = new Gson();
		String jsonData = gson.toJson(user);
		
		resp.setContentType("application/json;charset=utf-8");
		
		PrintWriter out = resp.getWriter();
		out.println(jsonData);
		
		
	}
}
