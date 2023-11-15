package UIElements;

public class FloatingDimension{
    private double width;
    private double height;

    public FloatingDimension(double width, double height){
        this.width = width;
        this.height = height;
    }

    public void setWidth(double width){
        this.width = width;
    }
    public void setHeight(double height){
        this.height = height;
    }

    public double getWidth(){
        return width;
    }
    public double getHeight(){
        return height;
    }
}
