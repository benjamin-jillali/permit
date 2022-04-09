/*
 * package service;
 * 
 * import java.io.InputStream; import java.nio.file.Paths;
 * 
 * import javax.ejb.Stateless; import javax.servlet.annotation.MultipartConfig;
 * import javax.servlet.annotation.WebServlet; import
 * javax.servlet.http.HttpServlet; import javax.servlet.http.HttpServletRequest;
 * import javax.servlet.http.HttpServletResponse; import
 * javax.servlet.http.Part;
 * 
 *//**
	 * 
	 * @author osbot
	 * @deprecated //pas utilise
	 *
	 *//*
		 * @WebServlet
		 * 
		 * @MultipartConfig public class GererFichier extends HttpServlet{ private
		 * InputStream image = null; private String description = null; private String
		 * fileName = null;
		 * 
		 * //method for multiform file upload request protected void
		 * doPost(HttpServletRequest request, HttpServletResponse response) { try {
		 * this.description = request.getParameter("description"); //get the description
		 * paramater of a multipart request e.g: <input type="text" name="description">
		 * Part filePart = request.getPart("file"); //get the file part of the request
		 * <input type="file" name="file"> this.fileName =
		 * Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE
		 * fix. this.image = filePart.getInputStream(); }catch (Exception e) {
		 * System.out.println(e.getStackTrace()); }
		 * 
		 * }
		 * 
		 * }
		 */
