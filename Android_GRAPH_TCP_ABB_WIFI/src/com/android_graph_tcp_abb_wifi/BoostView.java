package com.android_graph_tcp_abb_wifi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BoostView extends View implements View.OnClickListener{
	
	MainActivity mainActivity = null;
	
	///// DESSINS AJUSTABLES
	private int leftJaugeOffset = 20;
	private int topJaugeOffset = 70;
	private int jaugeLargeur = 0;
	private int jaugeHauteur = 0;
	
	int labelStep =30;
	int labelYPos = topJaugeOffset+jaugeHauteur*4 + 50;
	
    //// MESURES + COMMANDES
    public float Vi = -1.0f;
    public float Il = -1.0f;
    public float Pi = -1.0f;
    public float Vo = -1.0f;
    public float Io = -1.0f;
    public boolean boostState = false;
    public int V = -1;
    
    Paint paint;
    //// LA POSITION ET L'ID DE LA VUE
    public int position = -1;

    public BoostView(Context context) {
        super(context);
        paint = new Paint();
    }
    
    public BoostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }
    public BoostView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * Recupere l'action effectuee et sa position
         */
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
        
            case MotionEvent.ACTION_DOWN:

            	//leaving this case is necessary, leaving it blank prevents any undesired effect
            	
            	/*System.out.println("Activity : "+ mainActivity);
            	
            	if (mainActivity != null)
            		mainActivity.commandHasBeenChanged(this);
            	
            	
            	
            	this.invalidate();*/
                return true;
                
            case MotionEvent.ACTION_MOVE:
            	if (x<(leftJaugeOffset + jaugeLargeur) && x>(leftJaugeOffset)) {
            		/// 36
            		if ( (y>topJaugeOffset+jaugeHauteur*0) &&  (y<topJaugeOffset+jaugeHauteur*1)) {
            			Vo = 36;
            			V = 36;
            		}
            		/// 30
            		if ( (y>topJaugeOffset+jaugeHauteur*1) &&  (y<topJaugeOffset+jaugeHauteur*2)) {
            			Vo = 30;
            			V = 30;
            		}
            		/// 24
            		if ( (y>topJaugeOffset+jaugeHauteur*2) &&  (y<topJaugeOffset+jaugeHauteur*3)) {
            			Vo = 24;
            			V = 24;

            		}
            		/// 18
            		if ( (y>topJaugeOffset+jaugeHauteur*3) &&  (y<topJaugeOffset+jaugeHauteur*4)) {
            			Vo = 18;
            			V = 18;

            		}
            	}

            case MotionEvent.ACTION_UP:
            	if (x<(leftJaugeOffset + jaugeLargeur) && x>(leftJaugeOffset)) {
            		/// 36
            		if ( (y>topJaugeOffset+jaugeHauteur*0) &&  (y<topJaugeOffset+jaugeHauteur*1)) {
            			Vo = 36;
            			V = 36;

            		}
            		/// 30
            		if ( (y>topJaugeOffset+jaugeHauteur*1) &&  (y<topJaugeOffset+jaugeHauteur*2)) {
            			Vo = 30;
            			V = 30;

            		}
            		/// 24
            		if ( (y>topJaugeOffset+jaugeHauteur*2) &&  (y<topJaugeOffset+jaugeHauteur*3)) {
            			Vo = 24;
            			V = 24;

            		}
            		/// 18
            		if ( (y>topJaugeOffset+jaugeHauteur*3) &&  (y<topJaugeOffset+jaugeHauteur*4)) {
            			Vo = 18;
            			V = 18;

            		}
                	if (mainActivity != null)
                		mainActivity.commandHasBeenChanged(this);
                	
                	
                	//this.invalidate();


            	}
            	

            	
                this.invalidate();

                return true;
        }
        
        
        
        
        
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    	jaugeLargeur = (int)((float)this.getWidth() * 0.40f);
    	jaugeHauteur = (int)((float)this.getHeight() * 0.11f);
    	
    	///// PARTIE AFFECTATIONS VARIABLES POUR QU'ILS PUISSENT ETRE RE-DESSINEES lors d'une invalidation
        paint.setColor(IXCHELColors.lightWhite);
        paint.setStrokeWidth(5);
        canvas.drawRect(2, 3,  this.getWidth()-2, this.getHeight()-3, paint);

        int subPosition = position;
        drawBoostTitle(canvas, paint, "boost : " +subPosition++, boostState);
        drawMainFrame(canvas, paint);
        drawCommandJauge(canvas, paint, 0);
    	paint.setColor(IXCHELColors.inverse); 
    	paint.setTextSize(19); 
    	canvas.drawText("Output voltage : ", 20, 55, paint);

    	int labelStep =30;
    	int labelYPos = topJaugeOffset+jaugeHauteur*4 + 50;

    	drawLabel(canvas, paint, 22, labelYPos, "Vi:  "+Vi+"V");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Il:  "+Il+"A");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Pi:  "+Pi+"W");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Vo:  "+Vo+"V");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Io:  "+Io+"mA");	
    }
    
    /// dessine un portion de jauge
    private void drawACommandJaugePoint(Canvas myCanvas, Paint myPaint, int x, int y, int largeur,int hauteur,String indice, boolean enabled) {
    	if (enabled) {
    		if (Vo<=36.0f && Vo>30.0f)
    			myPaint.setColor(IXCHELColors.danger);
    		else if(Vo<=30.0f && Vo>24.0f)
    			myPaint.setColor(IXCHELColors.fdjRed);
    		else if(Vo<=24.0f && Vo>18.0f)
    			myPaint.setColor(IXCHELColors.fdjOrange);
    		else if(Vo<=18.0f && Vo>12.0f)
    			myPaint.setColor(IXCHELColors.fdjBlue);
    	}
    	else {
    		myPaint.setColor(IXCHELColors.def);
    	}

    	myPaint.setStyle(Style.FILL); 

    	int xRelative = x + largeur;
    	int yRelative = y + hauteur;
    	
    	//left, top, right, bottom
    	myCanvas.drawRect(x, y, xRelative, yRelative, myPaint);

    	///// TEXTE INDICATEUR
    	myPaint.setColor(IXCHELColors.midnightblue); 
    	myPaint.setTextSize(20); 
    	myPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        myCanvas.drawText(indice, xRelative+25, y+12, myPaint);
        
        //// LIGNE
    	if (enabled) {
    		if (Vo<=36.0f && Vo>30.0f)
    			myPaint.setColor(IXCHELColors.danger);
    		else if(Vo<=30.0f && Vo>24.0f)
    			myPaint.setColor(IXCHELColors.fdjRed);
    		else if(Vo<=24.0f && Vo>18.0f)
    			myPaint.setColor(IXCHELColors.fdjOrange);
    		else if(Vo<=18.0f && Vo>12.0f)
    			myPaint.setColor(IXCHELColors.fdjBlue);
    			
    	}
    	else {
    		myPaint.setColor(IXCHELColors.def);
    	}
    	myPaint.setStyle(Style.FILL); 
        myCanvas.drawRect(x+largeur, y, xRelative+20, y+10, myPaint);
    }
    /// dessine la jauge complete
    private void drawCommandJauge(Canvas myCanvas, Paint myPaint, int command) {

    	int level = 0;
    	float cDelta = 0;
    	
    	if (Vo<=36.0f && Vo>30.0f) {
    		level = 1; cDelta = 36.0f - Vo;
    	}
    	else if(Vo>36.0f) { /// SI LA TENSION DEPASSE 36 V -> garde le meme parametrage que 36.0v
    		level = 1; cDelta = 36.0f - Vo;
    	}
    	else if(Vo<=30.0f && Vo>24.0f) {
    		level = 2; cDelta = 30.0f - Vo;
    	}
    	else if(Vo<=24.0f && Vo>18.0f) {
    		level = 3; cDelta = 24.0f - Vo;
    	}
    	else if(Vo<=18.0f && Vo>12.0f) {
    		level = 4; cDelta = 18.0f - Vo;
    	}
    	else {
    		level = 4;
    		cDelta = -1;
    	}
    	
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*0,jaugeLargeur, jaugeHauteur, "36V", false);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*1,jaugeLargeur, jaugeHauteur, "30V", false);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*2,jaugeLargeur, jaugeHauteur, "24V", false);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*3,jaugeLargeur, jaugeHauteur, "18V", false);
    	
    	if (cDelta == -1) return;

    	if (Vo<=36.0f && Vo>30.0f)
			myPaint.setColor(IXCHELColors.danger);
    	else if(Vo>36.0f) /// SI LA TENSION DEPASSE 36 V -> garde le meme parametrage que 36.0v
    		myPaint.setColor(IXCHELColors.danger);
		else if(Vo<=30.0f && Vo>24.0f)
			myPaint.setColor(IXCHELColors.fdjRed);
		else if(Vo<=24.0f && Vo>18.0f)
			myPaint.setColor(IXCHELColors.fdjOrange);
		else if(Vo<=18.0f && Vo>12.0f)
			myPaint.setColor(IXCHELColors.fdjBlue);

    	myPaint.setStyle(Style.FILL); 
    	System.out.println("cDelta = " + cDelta);
    	
	    //left, top, right, bottom
	    myCanvas.drawRect(leftJaugeOffset, topJaugeOffset+jaugeHauteur*level - jaugeHauteur / ( cDelta + 1) ,jaugeLargeur+20,topJaugeOffset+jaugeHauteur*4, myPaint);
   
    
    }

    private void drawMainFrame(Canvas myCanvas, Paint myPaint) {
    	myPaint.setColor(IXCHELColors.midnightblue);
    	myPaint.setStyle(Style.STROKE); 
    	
    	myPaint.setStrokeWidth(1);
        int offSet = 2;
    	myCanvas.drawRect(2+offSet, 3+28,  this.getWidth()-2-offSet, this.getHeight()-3-offSet, myPaint);
    }
    
    private void drawBoostTitle(Canvas myCanvas, Paint myPaint,String title, boolean state) {
    	myPaint.setColor(IXCHELColors.inverse); 
    	myPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    	myPaint.setTextSize(25); 
        myCanvas.drawText(title, 5, 25, myPaint);
        drawBoostStateLed(myCanvas, myPaint, state);
    }
    
    private void drawBoostStateLed(Canvas myCanvas, Paint myPaint,boolean enabled) {
    	myPaint.setColor(IXCHELColors.inverse);  
    	myPaint.setStyle(Style.STROKE); 
    	myPaint.setStrokeWidth(1);
    	
    	int relativeX = this.getWidth()-25; 
    	
    	myCanvas.drawCircle(relativeX, 16, 11, myPaint);  
    	if (enabled) {
        	myPaint.setColor(IXCHELColors.vertGazon);  
        	myPaint.setStyle(Style.FILL); 
        	myCanvas.drawCircle(relativeX, 16, 7, myPaint);  
    	} else {
        	myPaint.setColor(IXCHELColors.danger);  
        	myPaint.setStyle(Style.FILL); 
        	myCanvas.drawCircle(relativeX, 16, 7, myPaint); 	
    	}
    }
    
    private void drawLabel(Canvas myCanvas, Paint myPaint,int x, int y, String text) {
    	myPaint.setColor(IXCHELColors.inverse); 
    	myPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    	myPaint.setTextSize(20); 
    	myPaint.setTypeface(Typeface.MONOSPACE);
    	myPaint.setTextSkewX(-0.35f);
        myCanvas.drawText(text, x, y, myPaint);
        myPaint.setTextSkewX(-0.0f);
    	
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
}
