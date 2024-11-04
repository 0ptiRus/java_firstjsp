package moneyTracker;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Date date;
    private String expense;
    private String reason;

    public Expense(Date date, String expense, String reason) {
        this.date = date;
        this.expense = expense;
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}