import java.awt.Color;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;


public class Pyramid extends Shape3D {
	private RotationInterpolator rotator;
	private BranchGroup branchGroup;
	private String id;
	
	private static MouseTranslate myMouseTranslate;
	private TransformGroup tg;
	private Transform3D changeSize;
	private Transform3D resize;
	
	private double height = 10; 
	private double width = 10; 
	private double depth = 10;
	
	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getWidth() {
		return width;
	}


	public void setWidth(double width) {
		this.width = width;
	}


	public double getDepth() {
		return depth;
	}


	public void setDepth(double depth) {
		this.depth = depth;
	}
	
	public Transform3D getResize() {
		return resize;
	}


	public void setResize(Transform3D resize) {
		this.resize = resize;
	}


	public Transform3D getChangeSize() {
		return changeSize;
	}


	public void setChangeSize(Transform3D changeSize) {
		this.changeSize = changeSize;
	} 
	
	float tx = 0.0f;
	private float ty = 0.0f;	
	
	Point3f frontL = new Point3f(-1.0f, -1.0f, 1.0f); // front left
	Point3f frontR = new Point3f(1.0f, -1.0f, 1.0f); // front right
	Point3f backR = new Point3f(1.0f, -1.0f, -1.0f); // back right
	Point3f backL = new Point3f(-1.0f, -1.0f, -1.0f); // back left
	Point3f top = new Point3f(0.0f, 1.0f, 0.0f); // top

    
    public Pyramid() {
		TriangleArray pyramidGeometry = new TriangleArray(18, TriangleArray.COORDINATES | GeometryArray.COLOR_3);
		
		face(pyramidGeometry, 0, frontR, top, frontL, Colors.ORANGE);
		face(pyramidGeometry, 3, backR, top, frontR, Colors.BLUE);
		face(pyramidGeometry, 6, backL, top, backR, Colors.RED);
		face(pyramidGeometry, 9, frontL, top, backL, Colors.GREEN);
		face(pyramidGeometry, 12, backL, backR, frontR, Colors.PURPLE);
		face(pyramidGeometry, 15, frontR, frontL, backL, Colors.PURPLE);		

		this.setGeometry(pyramidGeometry);
		this.setAppearance(new Appearance());
		
		int pyramidCount = SwingTest.getPyramidCount();
		this.setUserData( "Pyramid".concat(Integer.toString(pyramidCount)) );
		
		pyramidCount++;
	 	SwingTest.setPyramidCount(pyramidCount);

	 	Transform3D defaultSize = new Transform3D();
	 	defaultSize.setScale(new Vector3d(1.0, 1.0, 1.0));
	 	setResize(defaultSize);
    }
    
    private void face(TriangleArray pyramidGeometry, int index, Point3f coordinate1, Point3f coordinate2, Point3f coordinate3, Color3f color) {
    	pyramidGeometry.setCoordinate(index, coordinate1);
    	pyramidGeometry.setCoordinate(index+1, coordinate2);
    	pyramidGeometry.setCoordinate(index+2, coordinate3);
    	pyramidGeometry.setColor(index, color);
    	pyramidGeometry.setColor(index+1, color);
    	pyramidGeometry.setColor(index+2, color);
    }
    
    private void edge(TriangleArray pyramidEdgeGeometry, int index, Point3f coordinate1, Point3f coordinate2, Point3f coordinate3, Color3f color) {
    	pyramidEdgeGeometry.setCoordinate(index, coordinate1);
    	pyramidEdgeGeometry.setCoordinate(index+1, coordinate2);
    	pyramidEdgeGeometry.setCoordinate(index+2, coordinate3);
    	pyramidEdgeGeometry.setColor(index, color);
    	pyramidEdgeGeometry.setColor(index+1, color);
    	pyramidEdgeGeometry.setColor(index+2, color);
    }
    
    public Node pyramidEdges() {
		TriangleArray pyramidEdgeGeometry = new TriangleArray(18, TriangleArray.COORDINATES | GeometryArray.COLOR_3);
		
		edge(pyramidEdgeGeometry, 0, frontR, top, frontL, Colors.BLACK);
		edge(pyramidEdgeGeometry, 3, backR, top, frontR, Colors.BLACK);
		edge(pyramidEdgeGeometry, 6, backL, top, backR, Colors.BLACK);
		edge(pyramidEdgeGeometry, 9, frontL, top, backL, Colors.BLACK);
		edge(pyramidEdgeGeometry, 12, backL, backR, frontR, Colors.BLACK);
		edge(pyramidEdgeGeometry, 15, frontR, frontL, backL, Colors.BLACK);
				
		Appearance app = new Appearance();
		
		// Set up the polygon attributes
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(pa.POLYGON_LINE);
        pa.setCullFace(pa.CULL_NONE);
        pa.setPolygonOffsetFactor(-0.5f);
        app.setPolygonAttributes(pa);
        
        LineAttributes lineattributes = new LineAttributes();
        lineattributes.setLineWidth(5.0f);
        lineattributes.setLineAntialiasingEnable(true);
        lineattributes.setLinePattern(LineAttributes.PATTERN_SOLID);
        
        app.setLineAttributes(lineattributes);
        
        Shape3D pyramidEdges = new Shape3D();
		pyramidEdges.setGeometry(pyramidEdgeGeometry);
		pyramidEdges.setAppearance(app);
		
		return pyramidEdges;
    }
    
    
    
    TransformGroup createRotator() {
    	Transform3D yAxis = new Transform3D();

		 /* axes of rotation */
	     yAxis.rotZ(Math.PI / 2.0);  	//X AXIS
	     //yAxis.rotY( Math.PI / 2.0 ); //Y AXIS
	     //yAxis.rotX(Math.PI / 2.0);   //Z AXIS
		    
		 TransformGroup spin = new TransformGroup(yAxis);
		 spin.setUserData("TG: SPIN");
		 
		 
		 spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		 

		 spin.addChild(this); //add pyramid shape to the spin TG
		 spin.addChild(pyramidEdges());

		    
		 Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,  4000, 0, 0, 0, 0, 0);
	     rotator = new RotationInterpolator(rotationAlpha, spin, yAxis, 0.0f, (float) Math.PI* GUI_3D.rotateSpeed );

		    
		 BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
		 rotator.setSchedulingBounds(bounds);
		 spin.addChild(rotator);  //add interpolator rotator to the spin TG
		 
		 
		 TransformGroup tg = new TransformGroup() ;
		 tg.setUserData("TG: TG".concat(Integer.toString(SwingTest.getPyramidCount())));
		 tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
		 setTg(tg);
		 
		 
		 tg.addChild(spin) ;
		 
		 TransformGroup mouseTG = new TransformGroup();	
		 mouseTG.setUserData("TG: MOUSETG");
		 myMouseTranslate = new MouseTranslate();
		 myMouseTranslate.setTransformGroup(tg);
		 myMouseTranslate.setSchedulingBounds(bounds);
		 mouseTG.addChild(myMouseTranslate);
		              
		 
		 tg.addChild(mouseTG);
		 
		 return tg;
    }
	
	
	public TransformGroup getTg() {
		return tg;
	}


	public void setTg(TransformGroup tg) {
		this.tg = tg;
	}
	
	public float getTx() {
		return tx;
	}


	public void setTx(float tx) {
		this.tx = tx;
	}


	public float getTy() {
		return ty;
	}


	public void setTy(float ty) {
		this.ty = ty;
	}

}