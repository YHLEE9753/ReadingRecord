package Object.Chapter13;

public class Rectangle {
    private int x,y,width,height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void resize(Rectangle rectangle, int width, int height){
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        assert rectangle.getWidth() == width && rectangle.getHeight() == height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea(){
        return width*height;
    }
}
