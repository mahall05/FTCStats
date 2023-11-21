package UIElements;

import javax.swing.*;
import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

public class Panel extends JPanel {
    ArrayList<JComponent> components = new ArrayList<JComponent>();
    ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
    ArrayList<Point> points = new ArrayList<Point>();

    public Panel(){
        super();
    }

    public void addComponents(JComponent... c){
        for(JComponent comp : c){
            components.add(comp);
            add(comp);
            dimensions.add(comp.getSize());
            points.add(comp.getLocation());
        }
    }

    public void update(FloatingDimension d){
        for(int i = 0; i < components.size(); i++){
            components.get(i).setLocation((int) (points.get(i).getX()*d.getWidth()), (int) (points.get(i).getY()*d.getWidth()));
            components.get(i).setSize((int) (dimensions.get(i).getWidth()*d.getWidth()), (int) (dimensions.get(i).getHeight()*d.getHeight()));
        }
    }
}
