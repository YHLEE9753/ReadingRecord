package EffectiveJava.chapter2.item8;

public class SampleRunner2 {

    public static void main(String[] args) throws Exception {
//        SampleResource sampleResource = null;
//        try {
//            sampleResource = new SampleResource();
//            sampleResource.hello();
//        } finally {
//            if(sampleResource != null){
//                sampleResource.close();
//            }
//        }
        try(SampleResource sampleResource = new SampleResource()){
            sampleResource.hello();
        }
    }
}
