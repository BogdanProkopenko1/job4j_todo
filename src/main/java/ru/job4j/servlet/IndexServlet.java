package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Item;
import ru.job4j.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        int i = Integer.parseInt(req.getParameter("id"));
        Item item = HbmStore.instOf().findById(i);
        if (item != null) {
            HbmStore.instOf().update(Item.of(
                    i,
                    item.getDescription(),
                    Boolean.parseBoolean(req.getParameter("done"))
            ));
        } else {
            HbmStore.instOf().add(Item.of(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("des"),
                    Boolean.parseBoolean(req.getParameter("done"))
            ));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().println(GSON.toJson(HbmStore.instOf().getAll()));
    }
}
