package ru.job4j.servlet;

import ru.job4j.model.User;
import ru.job4j.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        Optional<User> optional = HbmStore.instOf().getUserOnEmail(req.getParameter("email"));
        if (optional.isPresent() && optional.get().getPassword().equals(req.getParameter("password"))) {
            req.getSession().setAttribute("user", optional.get());
            resp.sendRedirect(req.getContextPath() + "/todo.html");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login.html");
        }
    }
}
