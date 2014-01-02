package bid.api;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static bid.BidServer.BID_SERVER;

@WebServlet("/api/currentBidOffer")
public class CurrentBidOffer extends HttpServlet {
    private Gson gson;

    public CurrentBidOffer() {
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter writer = resp.getWriter()) {
            writer.println(gson.toJson(BID_SERVER.bidEngine().currentBidOffer()));
        }
    }
}