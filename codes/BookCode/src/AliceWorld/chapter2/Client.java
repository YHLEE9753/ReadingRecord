package AliceWorld.chapter2;

public class Client {
    public static void main(String args[]){
        Laptop laptop = new Laptop();
        laptop.powerPush();
        laptop.setPowerState(Laptop.ON);
        laptop.powerPush();
    }
}
