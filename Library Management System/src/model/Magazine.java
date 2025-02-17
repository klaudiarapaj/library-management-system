package model;

public class Magazine extends LibraryItem  {
    private final int issueNumber;

    // parameterized constructor
    public Magazine(int id,String title, int year, double price, int issueNumber){
        super(id, title, year, price);
        this.issueNumber=issueNumber;
    }

    @Override
    public void displayDetails(){
        super.displayDetails();
        System.out.println("IssueNumber:  "+issueNumber);
        System.out.println();
    }


    public int getIssueNumber(){
        return issueNumber;
    }

}