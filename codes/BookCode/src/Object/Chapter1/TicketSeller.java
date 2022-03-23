package Object.Chapter1;

public class TicketSeller{
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice){
        this.ticketOffice = ticketOffice;
    }

    // getter 을 없애므로써 private ticketOffice 를 외부에서 접근할수 없다 즉 적립하는 일은 혼자 수행한다.
    public void sellTo(Audience audience){
        ticketOffice.sellTicketTo(audience);
    }
}