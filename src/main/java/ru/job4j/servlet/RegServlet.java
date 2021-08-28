package ru.job4j.servlet;

import ru.job4j.model.User;
import ru.job4j.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = new User(
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password")
        );
        boolean exist = HbmStore.instOf().getUserOnEmail(user.getEmail()).isPresent();
        if (exist) {
            req.getRequestDispatcher("/login.html").forward(req, resp);
        } else {
            HbmStore.instOf().save(user);
            req.getSession().setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/todo.html");
        }
    }
}
