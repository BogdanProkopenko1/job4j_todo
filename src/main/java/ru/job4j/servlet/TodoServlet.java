package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Item;
import ru.job4j.model.User;
import ru.job4j.store.HbmStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TodoServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        int i = Integer.parseInt(req.getParameter("id"));
        Item item;
        if (i == 0) {
            HttpSession session = req.getSession();
            HbmStore.instOf().addItem(Item.of(
                    Integer.parseInt(req.getParameter("id")),
                    req.getParameter("des"),
                    Boolean.parseBoolean(req.getParameter("done")),
                    (User) session.getAttribute("user")
            ));
        } else if ((item = HbmStore.instOf().findItemById(i)) != null) {
            HbmStore.instOf().updateItem(Item.of(
                    i,
                    item.getDescription(),
                    Boolean.parseBoolean(req.getParameter("done")),
                    item.getUser()
            ));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(HbmStore.instOf().getAllItems());
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
