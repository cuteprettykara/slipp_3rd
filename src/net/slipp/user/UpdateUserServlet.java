package net.slipp.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.beanutils.BeanUtilsBean;

import net.slipp.support.MyValidatorFactory;

@WebServlet("/users/update")
public class UpdateUserServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String sessionUserId = SessionUtils.getStringValue(session, LoginServlet.SESSION_USER_ID);
		
		if (sessionUserId == null) {
			resp.sendRedirect("/");
			return;			
		}
		
		User user = new User();
		
		try {
			BeanUtilsBean.getInstance().populate(user, req.getParameterMap());
		} catch (Exception e1) {
			throw new ServletException(e1);
		}
		
		System.out.println("User : " + user);
		
		if (!user.isSameUser(sessionUserId)) {
			return;
		}
		
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		if (constraintViolations.size() > 0) {
			req.setAttribute("user", user);
			req.setAttribute("isUpdate", true);
			
			String errorMessage = constraintViolations.iterator().next().getMessage();
			forwardJSP(req, resp, errorMessage);
			return;
		}
		
		
		UserDAO userDao = new UserDAO();
		
		try {
			userDao.update(user);
		} catch (SQLException e) {
		}
		
		resp.sendRedirect("/");
	}
	
	private void forwardJSP(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
		req.setAttribute("errorMessage", errorMessage);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("/form.jsp");
		dispatcher.forward(req, resp);
	}
}
