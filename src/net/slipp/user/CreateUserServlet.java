package net.slipp.user;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.slipp.support.MyValidatorFactory;

@WebServlet("/users/create")
public class CreateUserServlet extends HttpServlet  {
	private static final Logger log = LoggerFactory.getLogger(CreateUserServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = new User();
		
		try {
			BeanUtilsBean.getInstance().populate(user, req.getParameterMap());
		} catch (Exception e1) {
			throw new ServletException(e1);
		} 
		
		Validator validator = MyValidatorFactory.createValidator();
		Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
		if (constraintViolations.size() > 0) {
			req.setAttribute("user", user);
			String errorMessage = constraintViolations.iterator().next().getMessage();
			forwardJSP(req, resp, errorMessage);
			return;
		}
		
		log.debug("User : {}", user);

		UserDAO userDao = new UserDAO();
		try {
			userDao.addUser(user);
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
