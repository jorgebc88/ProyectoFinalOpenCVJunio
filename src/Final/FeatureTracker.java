package Final;
import java.util.ArrayList;
import java.util.List;


import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.cvCalcOpticalFlowPyrLK;

public class FeatureTracker {

	private int maxCount;
	private double qualityLevel;
	private double minDistance;

	private int minNumberOfTrackedPoints = 100;

	private List<CvPoint2D32f> initialPositions = new ArrayList<CvPoint2D32f>();
	private List<CvPoint2D32f> trackedPoints = new ArrayList<CvPoint2D32f>();

	private IplImage grayPrevious = null;

	public FeatureTracker(int maxCount, double qualityLevel, double minDistance) {
		this.maxCount = maxCount;
		this.qualityLevel = qualityLevel;
		this.minDistance = minDistance;
	}

	List<CvPoint2D32f> trackedPointsNewUnfiltered;
	List<CvPoint2D32f> initialPositionsNew = new ArrayList<CvPoint2D32f>();
	List<CvPoint2D32f> trackedPointsNew = new ArrayList<CvPoint2D32f>(); 
	CvPoint2D32f trackedPointsNewUnfilteredOCV;
	BytePointer trackingStatus;
	IplImage grayCurrent;
	IplImage output;
	
	CvPoint startPoint;
	CvPoint endPoint;

	CvPoint2D32f featurePoints;
	IntPointer featureCount;

	CvPoint2D32f dest;

	

	
	public IplImage process(IplImage frame) {

		grayCurrent = this.createGrayImage(frame, grayCurrent);
		cvCvtColor(frame, grayCurrent, opencv_imgproc.CV_BGR2GRAY);

		// 1. Check if additional new feature points should be added
		if (shouldAddNewPoints()) {
			List<CvPoint2D32f> features = detectFeaturePoints(grayCurrent);
			initialPositions.addAll(features);
			trackedPoints.addAll(features);
		}

		// for first image of the sequence
		if (grayPrevious == null) {
			grayPrevious = this.createImage(grayCurrent, grayPrevious, grayCurrent.nChannels());
			cvCopy(grayCurrent, grayPrevious, null);
		}

		// 2. track features
		trackedPointsNewUnfilteredOCV = new CvPoint2D32f(trackedPoints.size());
		trackingStatus = new BytePointer(trackedPoints.size());
		cvCalcOpticalFlowPyrLK(
				grayPrevious, grayCurrent, // 2 consecutive images
				null, null, // Unused
				toNativeVector(trackedPoints), // input point position in previous image
				trackedPointsNewUnfilteredOCV, // output point position in the current image
				trackedPoints.size(),
				cvSize(21, 21),3, // Defaults (WinSize and level)
				trackingStatus, // tracking success
				new FloatPointer(trackedPoints.size()), // Not used
				cvTermCriteria(CV_TERMCRIT_ITER + CV_TERMCRIT_EPS, 30, 0.01),0 // defaults
		);
		// 2. loop over the tracked points to reject the undesirables
		trackedPointsNewUnfiltered = toArray(trackedPointsNewUnfilteredOCV);
		initialPositionsNew = new ArrayList<CvPoint2D32f>();
		trackedPointsNew = new ArrayList<CvPoint2D32f>();

		for (int i = 0; i < trackedPointsNewUnfiltered.size(); i++) {
			if (acceptTrackedPoint(trackingStatus.get(i), trackedPoints.get(i), trackedPointsNewUnfiltered.get(i))) {
				initialPositionsNew.add(initialPositions.get(i));
				trackedPointsNew.add(trackedPointsNewUnfiltered.get(i));
			}
		}
		
		// Prepare output
		output = this.createImage(frame, output, frame.nChannels());
		cvCopy(frame, output, null);
		//cvSet(output, CV_RGB(0,0,0));
		// 3. handle the accepted tracked points
		visualizeTrackedPoints(initialPositionsNew, trackedPointsNew, frame, output);

		// 4. current points and image become previous ones
		trackedPoints = trackedPointsNew;
		initialPositions = initialPositionsNew;
		
		cvCopy(grayCurrent, grayPrevious, null);

		return output;

	}
	
	private void visualizeTrackedPoints(List<CvPoint2D32f> startPoints, List<CvPoint2D32f> endPoints, IplImage frame,
			IplImage output) {

		for (int i = 0; i < startPoints.size(); i++) {
			startPoint = cvPointFrom32f(startPoints.get(i));
			endPoint = cvPointFrom32f(endPoints.get(i));
			// Mark tracked point movement with a line
			cvLine(output, startPoint, endPoint, AbstractCvScalar.WHITE, 1, CV_AA, 0);
			// Mark starting point with circle
			cvCircle(output, startPoint, 3, AbstractCvScalar.WHITE, -1, CV_AA, 0);
		}

	}

	private boolean acceptTrackedPoint(int status, CvPoint2D32f point0, CvPoint2D32f point1) {
		return status != 0 &&
				// if point has moved
				(Math.abs(point0.x() - point1.x()) + Math.abs(point0.y() - point1.y()) > 1);
	}
	
	private List<CvPoint2D32f> detectFeaturePoints(IplImage grayFrame) {
		featurePoints = new CvPoint2D32f(maxCount);
		featureCount = new IntPointer(1);
		featureCount.put(0, maxCount);

		// detect the features
		cvGoodFeaturesToTrack(grayFrame, // the image
				null, null, // ignored parameters
				featurePoints, // the output detected features
				featureCount, 
				qualityLevel, // quality level
				minDistance, // min distance between two features
				null, 2, 1, 0.04); // Default parameters

		// Select only detected features, end of the vector do not have valid
		// entries
		return (toArray(featurePoints)).subList(0, (featureCount.get(0))/2);

	}

	private boolean shouldAddNewPoints() {
		return trackedPoints.size() <= minNumberOfTrackedPoints;
	}
	
	private CvPoint2D32f toNativeVector(List<CvPoint2D32f> srcObject) {
		dest = new CvPoint2D32f(srcObject.size());
		int i = 0;
		for (CvPoint2D32f srcElement : srcObject) {
			// Since there is no way to `put` objects into a vector
			// CvPoint2D32f,
			// We have to reassign all values individually, and hope that API
			// will not any new ones.
			copy(srcObject.get(i), dest.position(i));
			i++;
		}
		// Set position to 0 explicitly to avoid issues from other uses of this
		// position-based container.
		dest.position(0);
		return dest;
	}

	public void copy(Pointer src, Pointer dest) {
		dest.put(src.limit(src.position() + 1));
	}

	public List<CvPoint2D32f> toArray(CvPoint2D32f points) {
		CvPoint2D32f pointsArray;
		int oldPosition = points.position();
		List<CvPoint2D32f> dest = new ArrayList<CvPoint2D32f>();
		
		// Convert points to a Java ArrayList
		for (int i = 0; i < points.capacity(); i += 2) {
			pointsArray = new CvPoint2D32f(1);
			pointsArray.put(points.get(i), points.get(i+1));
			dest.add(pointsArray);
		}

		// Reset position explicitly to avoid issues from other uses of this
		// position-based container.
		points.position(oldPosition);

		return dest;
	}

	public IplImage createGrayImage(IplImage frame, IplImage dest){
		if(dest == null){
			dest = cvCreateImage(cvGetSize(frame), frame.depth(), 1);
		}
		return dest;
	}
	public IplImage createImage(IplImage src, IplImage dest, int nChannels){
		if(dest == null){
			dest = cvCreateImage(cvGetSize(src), src.depth(), nChannels);
		}
		return dest;
	}
	
	
}
