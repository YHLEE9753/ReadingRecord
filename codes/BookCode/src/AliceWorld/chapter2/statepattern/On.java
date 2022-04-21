package AliceWorld.chapter2.statepattern;

public class On implements PowerState{
    @Override
    public void powerPush() {
        System.out.println("전원 on");
    }
}
