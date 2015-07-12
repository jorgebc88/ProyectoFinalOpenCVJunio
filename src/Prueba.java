import java.awt.Point;

import javax.swing.JFrame;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point2f;

public class Prueba {
    public static void main(String[] args) throws Exception {
    	Mat prueba = new Mat();
	    System.out.println(prueba.cols()+"-"+prueba.rows()+""+prueba.arraySize());
	    //prueba.col(0).data().put(new Point2f(1));
	    System.out.println(prueba.cols()+"-"+prueba.rows()+""+prueba.total());
	    System.out.println(new Point2f());
	    for(int i=0; i<= prueba.cols(); i++){
	    	System.out.println(i);
	    	for(int j=0; j<=prueba.rows(); j++){
	    		System.out.println(j);
	    		System.out.println("HOLA - "+prueba.cols()+"-"+prueba.rows());
	    	}
	    	System.out.println("\n");
	    }
    }
}
