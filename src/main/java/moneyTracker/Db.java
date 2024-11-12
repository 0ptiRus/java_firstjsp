package moneyTracker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Db {
	private SessionFactory factory;
	private Session session;
	
	public Db() {
		Configuration config = new Configuration();
		config.configure();
		factory = config.buildSessionFactory();
    	System.out.println("Connected to db!");
	}
	
	private List<Expense> readFromDb() 
	{
		session = factory.openSession();
	    List<Expense> expenses = session.createQuery("from Expense E", Expense.class).getResultList();
	    session.close();
	    return expenses;
	}
	
	public CompletableFuture<List<Expense>> readFromDbAsync() 
	{ 
		return CompletableFuture.supplyAsync(this::readFromDb); 
	}
	
	private void saveToDb(String expense, String reason) 
	{
		session = factory.openSession();		
		Expense e = new Expense(new Date(), Long.parseLong(expense), reason);
		Transaction t = session.beginTransaction();
		session.save(e);
		t.commit();
		session.close();
	}
	
	public CompletableFuture<Void> saveToDbAsync(String expense, String reason) 
	{ 
		return CompletableFuture.runAsync(() -> saveToDb(expense, reason)); 
	}
}
