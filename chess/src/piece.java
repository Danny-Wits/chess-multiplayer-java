import java.awt.Image;
import java.awt.Toolkit;
public class piece {
    public String src;
    public Image img;
    public String type;
    public boolean color;
    public int points;
    public String name;
    public piece(char type) {
        this.color=Character.isUpperCase(type);
        String c=(color)?"w":"b";
        this.type =Character.toString(type).toLowerCase();
        this.src="./assets/"+type+c+".png";
        this.img= Toolkit.getDefaultToolkit().getImage(src);
    }
    public String getName(){
        return ((color)?"w":"b")+this.type;
    }
}
