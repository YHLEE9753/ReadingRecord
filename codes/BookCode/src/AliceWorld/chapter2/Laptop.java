package AliceWorld.chapter2;

public class Laptop {
    public static String ON = "on";
    public static String OFF = "off";
    private String powerState = "";

    public Laptop(){
        setPowerState(Laptop.OFF);
    }

    public void setPowerState(String powerState){
        this.powerState = powerState;
    }

    public void powerPush(){
        if("on".equals(this.powerState)){
            System.out.println("전원 pff");
        }
        else{
            System.out.println("전원 on");
        }
    }
}


