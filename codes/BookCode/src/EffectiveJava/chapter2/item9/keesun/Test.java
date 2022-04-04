package EffectiveJava.chapter2.item9.keesun;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {

        try{
            throw(new FirstException());
        }catch(Exception e){
            e.printStackTrace();

        }finally {
            try{
                System.out.println(1/0);
                throw(new IOException());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                System.out.println(2);
                throw(new SecondException());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(3);
    }
}
