package fib.asw.waslab02;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

@WebServlet(urlPatterns = {"/tweets", "/tweets/*"})
public class WoTServlet extends HttpServlet {

	@Serial
	private static final long serialVersionUID = 1L;
	
	private TweetDAO tweetRepository;
	private final String BASE_TWEETS_URI = "/waslab02/tweets/";

    public void init() {
    	tweetRepository = new TweetDAO((java.sql.Connection) this.getServletContext().getAttribute("connection"));
    }

    @Override
	// Implements GET http://localhost:8080/waslab02/tweets
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	response.setContentType("application/json");
		response.setHeader("Cache-control", "no-cache");
		List<Tweet> allTweets = tweetRepository.getAllTweets();
		JSONArray jsonResponse = new JSONArray();
		for (Tweet tweet: allTweets) {
			JSONObject jsonTweet = new JSONObject(tweet);
			jsonTweet.remove("class");
			jsonResponse.put(jsonTweet);
		}
		response.getWriter().println(jsonResponse);

    }

    @Override
	// Implements POST http://localhost:8080/waslab02/tweets/:id/likes
	//        and POST http://localhost:8080/waslab02/tweets
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String requestUri = request.getRequestURI();
		int likesIndex = requestUri.lastIndexOf("/likes");
		if (likesIndex > -1) {  // uri ends with "/likes"
			// Implements POST http://localhost:8080/waslab02/tweets/:id/likes
			long tweetId = Long.parseLong(requestUri.substring(BASE_TWEETS_URI.length(), likesIndex));
			response.setContentType("text/plain");
			response.getWriter().println(tweetRepository.likeTweet(tweetId));
		}
		else { 
			// Implements POST http://localhost:8080/waslab02/tweets
			int contentLength = request.getContentLength();
			byte[] requestBodyData = new byte[contentLength];
			ServletInputStream inputStream = request.getInputStream();
			inputStream.readLine(requestBodyData, 0, contentLength);
			String requestBody = new String(requestBodyData);
			/*      ^
		      The String variable requestBody contains the sent (JSON) Data.
		      Complete the implementation below.*/
			
		}
	}
    
    @Override
	// Implements DELETE http://localhost:8080/waslab02/tweets/:id
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

    	throw new ServletException("DELETE not yet implemented");
		
	}

}
