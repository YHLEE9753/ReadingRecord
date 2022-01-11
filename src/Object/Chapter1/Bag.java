package Object.Chapter1;

public class Bag{
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    public Long hold(Ticket ticket){
        if (hasInvitation()){
            setTicket(ticket);
            return 0L;
        }else{
            setTicket(ticket);
            minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
    // 생성자 오버로딩 - () 는 반드시 첫줄에 작성
//    public Bag(long amount){
//        this(null, amount);
//    }

//    public Bag(Invitation invitation, long amount){
//        this.invitation = invitation;
//        this.amount = amount;
//    }

    private boolean hasInvitation(){
        return invitation !=null;
    }
//
//    public boolean hasTicket(){
//        return ticket !=null;
//    }

    private void setTicket(Ticket ticket){
        this.ticket = ticket;
    }

    private void minusAmount(Long amount){
        this.amount -= amount;
    }
//
//    public void plusAmount(Long amount){
//        this.amount += amount;
//    }
}
