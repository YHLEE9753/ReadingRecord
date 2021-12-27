package Object.Chpater1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice{
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket ... tickets){
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    // 응집성 추가 - audience 와 office 간의 의존성 추가된다. 여기부터는 정답이 없다
    public void sellTicketTo(Audience audience){
        plusAmount(audience.buy(getTicket()));
    }

    private Ticket getTicket(){
        return tickets.remove(0);
    }
//
//    public void minusAmount(Long amount){
//        this.amount -= amount;
//    }

    private void plusAmount(Long amount){
        this.amount += amount;
    }
}