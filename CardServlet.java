package card.validator.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CardServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("Request : "+ req.getRequestURL());
		
		res.setStatus(200);
		res.getWriter().write(new Date().toString());
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Gson gson = new Gson();
		
		System.out.println("Request : "+ req.getRequestURL());
		////////////////////////////////////////////////
		File destFolder = new File("../SERVER");
		if(!destFolder.exists()) {
		    destFolder.mkdirs(); 
		}

        BufferedReader input = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String buffer;
        StringBuilder sb = new StringBuilder();
        while ((buffer = input.readLine()) != null) {
        	sb.append(buffer + "\n");
        }
        String strBody = sb.toString();
		input.close();		
		JsonObject jObj = gson.fromJson(strBody, JsonObject.class);
		String fileName = jObj.get("FileName").getAsString();
		String fileContent = jObj.get("FileContent").getAsString();
		    
        PrintWriter printWriter = new PrintWriter(new FileWriter(new File("../SERVER/"+fileName)));
        printWriter.print(fileContent);
		printWriter.close();
		/////////////////////////////////////////////////
		
		res.setStatus(200);
		res.getWriter().write(fileName + " saved!");
	}
}
