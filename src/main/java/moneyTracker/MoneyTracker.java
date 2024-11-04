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

import jakarta.servlet.*;
import jakarta.servlet.http.*;
/**
 * Servlet implementation class MoneyTracker
 */
@WebServlet("/MoneyTrack")
public class MoneyTracker extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoneyTracker() {
        super();
        // TODO Auto-generated constructor stub
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
				expenses = readFromFileAsync().get();
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
	            String expenseValue = exp.getExpense();
	            String reason = exp.getReason();

	            out.println("<p>" + date + ": " + expenseValue + " (" + reason + ")</p>");
	        }

	        out.println("</body>");
	        out.println("</html>");    
	}
	
	private List<Expense> readFromFile() 
	{
	    String filePath = getServletContext().getRealPath("/") + "Expenses.ser";
	    List<Expense> expenses = new ArrayList<>();

	    File file = new File(filePath);
	    if (!file.exists()) {
	        return expenses;
	    }

	    try (FileInputStream fis = new FileInputStream(filePath);
	         ObjectInputStream ois = new ObjectInputStream(fis)) 
	    {
	        expenses = (List<Expense>) ois.readObject();
	    } 
	    catch (ClassNotFoundException | IOException e) 
	    {
	        e.printStackTrace();
	    }

	    return expenses;
	}
	
	private CompletableFuture<List<Expense>> readFromFileAsync() 
	{ 
		return CompletableFuture.supplyAsync(this::readFromFile); 
	}
	
	private void saveToFile(String expense, String reason) 
	{
		String filePath = getServletContext().getRealPath("/") + "Expenses.ser";
	    List<Expense> expenses = new ArrayList<>();
		try {
			expenses = readFromFileAsync().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    expenses.add(new Expense(new Date(), expense, reason));

	    try (FileOutputStream fos = new FileOutputStream(filePath);
	         ObjectOutputStream oos = new ObjectOutputStream(fos)) 
	    {
	        oos.writeObject(expenses);
	        System.out.println("Saved to file successfully!");
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private CompletableFuture<Void> saveToFileAsync(String expense, String reason) 
	{ 
		return CompletableFuture.runAsync(() -> saveToFile(expense, reason)); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String amount = request.getParameter("money");
		String reason = request.getParameter("reason");
		
		System.out.println("Amount: " + amount +  " Reason: " + reason);
		
		saveToFileAsync(amount, reason);
		
		PrintWriter writer = response.getWriter();
		writer.println("<h1>The entry has been saved!</h1>");
		writer.close();
	}

}
