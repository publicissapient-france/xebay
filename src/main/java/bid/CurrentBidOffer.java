package bid;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currentBidOffer")
public class CurrentBidOffer extends HttpServlet {
    private final BidServer bidServer;

    public CurrentBidOffer() {
        bidServer = new BidServer(new Items(new Item("an item", 4.3)));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            writer.println(new Gson().toJson(bidServer.currentBidOffer()));
        }
    }
}