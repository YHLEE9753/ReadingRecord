package Object.Chpater1;

public class Audience{
    private Bag bag;

    public Audience(Bag bag){
        this.bag = bag;
    }

    // getter 을 지워버림으로써 bag 을 Audience 외에서는 접근할 수 없도록 인터페이스화시키자
//    public Bag getBag(){
//        return bag;
//    }
    public Long buy(Ticket ticket){
        return bag.hold(ticket);
    }

}