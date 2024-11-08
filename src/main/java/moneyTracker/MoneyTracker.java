package moneyTracker;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
/**
 * Servlet implementation class MoneyTracker
 */
@WebServlet("/MoneyTrack")
public class MoneyTracker extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Session session;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoneyTracker() {
        super();
        Configuration config = new Configuration();
		config.configure();
		System.out.println(config);
		SessionFactory sessionFactory=config.buildSessionFactory(); 
        session = sessionFactory.openSession(); 
        System.out.println(session);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		  response.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = response.getWriter();

	        out.println("<!DOCTYPE html>");
	        out.println("<html lang=\"ru\">");
	        out.println("<head>");
	        out.println("<meta charset=\"UTF-8\">");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<h1>All of your expenses:</h1>");

	        List<Expense> expenses = null;
			try {
				expenses = readFromDbAsync().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        for (Expense exp : expenses) 
	        {
	            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(exp.getDate());
	            Long expenseValue = exp.getExpense();
	            String reason = exp.getReason();

	            out.println("<p>" + date + ": " + expenseValue + " (" + reason + ")</p>");
	        }

	        out.println("</body>");
	        out.println("</html>");    
	}
	
	private List<Expense> readFromDb() 
	{
	    return session.createQuery("from Expense E", Expense.class).getResultList();
	}
	
	private CompletableFuture<List<Expense>> readFromDbAsync() 
	{ 
		return CompletableFuture.supplyAsync(this::readFromDb); 
	}
	
	private void saveToDb(String expense, String reason) 
	{
		Expense e = new Expense(new Date(), Long.parseLong(expense), reason);
		Transaction t = session.beginTransaction();
		session.save(e);
		t.commit();
	}
	
	private CompletableFuture<Void> saveToDbAsync(String expense, String reason) 
	{ 
		return CompletableFuture.runAsync(() -> saveToDb(expense, reason)); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String amount = request.getParameter("money");
		String reason = request.getParameter("reason");
		
		System.out.println("Amount: " + amount +  " Reason: " + reason);
		
		saveToDbAsync(amount, reason);
		
		PrintWriter writer = response.getWriter();
		writer.println("<h1>The entry has been saved!</h1>");
		writer.close();
	}

}
