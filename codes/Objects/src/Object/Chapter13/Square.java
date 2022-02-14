package Object.Chapter13;

public class Square extends Rectangle{
    public Square(int x, int y, int size) {
        super(x, y, size, size);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    }

//    @Override
//    public void resize(Rectangle rectangle, int size) {
//        super.resize(rectangle, size, size);
//    }
}
