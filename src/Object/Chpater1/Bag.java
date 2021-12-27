package Object.Chpater1;

public class Bag{
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    // 생성자 오버로딩 - 반드시 첫줄에 작성
    public Bag(long amount){
        this(null, amount);
    }

    public Bag(Invitation inviation, long amount){
        this.invitation = inviation;
        this.amount = amount;
    }

    public boolean hasInvitation(){
        return invitation !=null;
    }

    public boolean hasTicket(){
        return ticket !=null;
    }

    public void setTicket(Ticket ticket){
        this.ticket = ticket;
    }

    public void minusAmount(Long amount){
        this.amount -= amount;
    }

    public void plusAmount(Long amount){
        this.amount += amount;
    }
}
