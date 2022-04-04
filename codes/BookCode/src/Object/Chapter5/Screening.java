package Object.Chapter5;

import Object.Chapter2.Customer;
import Object.Chapter2.Reservation;

import java.time.LocalDateTime;

public class Screening {
    // 2. 책임을 수행하는데 필요한 변수 설정
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;

    // 1. 책임을 결정
//    public Reservation reserve(Customer customer, int audienceCount);

    // 3. 협력을 통해 다른 객체에게 메시지를 통해 책임 할당
    private Money calculateFee(int audienceCount){
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public LocalDateTime getWhenScreened(){
        return whenScreened;
    }

    public int getSequence(){
        return sequence;
    }
}
