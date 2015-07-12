import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Point2f;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_video.calcOpticalFlowPyrLK;
import static org.bytedeco.javacpp.opencv_video.cvCalcOpticalFlowPyrLK;
import static org.bytedeco.javacpp.opencv_imgproc.goodFeaturesToTrack;
import static org.bytedeco.javacpp.opencv_imgproc.cvGoodFeaturesToTrack;


public class FrameProcessor {
	private Mat gray;
	private Mat gray_prev;
	private IplImage aux;
	
	private ArrayList<ArrayList<Point2f>> point2f;
	private List<Mat> matPoint2f;
	private Mat[] matPoint2fVector;

	private ArrayList<Point2f> initial;
	private Point2f[] initialVector;
	
	private ArrayList<Point2f> features;
	private Point2f[] featuresVector;
	
	private int max_count;
	private double qlevel;
	private double minDist;
	
	private Mat status;
	private Mat err;
	
	OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
	OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
	
	public FrameProcessor(){
		this.max_count = 500;
		this.qlevel = 0.01;
		this.minDist = 10.0;
	}
	
	public void process(Mat frame, Mat outPut){
		cvCvtColor(frame.asCvMat(), this.aux, CV_RGB2GRAY);
		
		gray = this.converterToMat.convertToMat(converterToIplImage.convert(aux));
		
		if(this.addNewPoints()){
			this.detectFeaturePoints();
			int i = 0;
			for(Point2f feature : features){
				point2f.get(0).add(feature);
	            matPoint2f.get(0).push_back_(feature);;
				initial.add(feature);
			}
		}
		if(this.gray_prev.empty()){
			gray.copyTo(gray_prev);
		}
		Mat mat = new Mat();
		mat.step();
		outPut.row(0).put(point2f.get(0).get(0));
		calcOpticalFlowPyrLK(gray_prev, 
				gray,
				matPoint2f.get(0),
				matPoint2f.get(1),
				status,
				err);
		int k=0;
		for(int i=0;i<matPoint2f.get(0).sizeof();i++){
			if(this.acceptTrackedPoint(i)){
				initial.set(k, initial.get(i));
				matPoint2f.get(1).row(k).push_back(matPoint2f.get(1).row(i));
				k++;
				matPoint2f.get(0).data().put(features.get(0));
			}
		}
		matPoint2f.get(1).resize(k);
		
		this.handleTrackedPoints(frame, outPut);
		matPoint2f.get(1).copyTo(matPoint2f.get(0));
	}

	private void handleTrackedPoints(Mat frame, Mat outPut) {
		// TODO Auto-generated method stub
		
	}

	private boolean acceptTrackedPoint(int i) {
		return status.step1(i);
	}

	private void detectFeaturePoints() {
		Mat aux = new Mat();
		aux.put(featuresVector);
		goodFeaturesToTrack(gray, featuresVector, max_count, qlevel, minDist);	
	}

	private boolean addNewPoints() {
		
		return matPoint2f.get(0).sizeof() <= 10;
	}
}
