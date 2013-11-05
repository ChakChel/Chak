package com.ixchel.vue_liste_boost;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class BoostView extends View {
	
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
         * Récupère l'action effectuée et sa position
         */
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                return true;
            case MotionEvent.ACTION_MOVE:
            	if (x<(leftJaugeOffset + jaugeLargeur) && x>(leftJaugeOffset)) {
            		/// 36
            		if ( (y>topJaugeOffset+jaugeHauteur*0) &&  (y<topJaugeOffset+jaugeHauteur*1)) {
            			Vo = 36.0f;
            		}
            		/// 30
            		if ( (y>topJaugeOffset+jaugeHauteur*1) &&  (y<topJaugeOffset+jaugeHauteur*2)) {
            			Vo = 30.0f;
            		}
            		/// 24
            		if ( (y>topJaugeOffset+jaugeHauteur*2) &&  (y<topJaugeOffset+jaugeHauteur*3)) {
            			Vo = 24.0f;
            		}
            		/// 18
            		if ( (y>topJaugeOffset+jaugeHauteur*3) &&  (y<topJaugeOffset+jaugeHauteur*4)) {
            			Vo = 18.0f;
            		}
            	}
                this.invalidate();
            case MotionEvent.ACTION_UP:
            	if (x<(leftJaugeOffset + jaugeLargeur) && x>(leftJaugeOffset)) {
            		/// 36
            		if ( (y>topJaugeOffset+jaugeHauteur*0) &&  (y<topJaugeOffset+jaugeHauteur*1)) {
            			Vo = 36.0f;
            		}
            		/// 30
            		if ( (y>topJaugeOffset+jaugeHauteur*1) &&  (y<topJaugeOffset+jaugeHauteur*2)) {
            			Vo = 30.0f;
            		}
            		/// 24
            		if ( (y>topJaugeOffset+jaugeHauteur*2) &&  (y<topJaugeOffset+jaugeHauteur*3)) {
            			Vo = 24.0f;
            		}
            		/// 18
            		if ( (y>topJaugeOffset+jaugeHauteur*3) &&  (y<topJaugeOffset+jaugeHauteur*4)) {
            			Vo = 18.0f;
            		}
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
    	drawLabel(canvas, paint, 22, labelYPos, "Pi:  "+Pi+"V");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Vo:  "+Vo+"V");labelYPos+=labelStep;
    	drawLabel(canvas, paint, 22, labelYPos, "Io:  "+Io+"mA");	
    }
    
    /// dessine un porsion de jauge
    private void drawACommandJaugePoint(Canvas myCanvas, Paint myPaint, int x, int y, int largeur,int hauteur,String indice, boolean enabled) {
    	if (enabled) {
    		if (Vo == 36.0f)
    			myPaint.setColor(IXCHELColors.danger);
    		else if(Vo==30.0f)
    			myPaint.setColor(IXCHELColors.fdjRed);
    		else if(Vo==24.0f)
    			myPaint.setColor(IXCHELColors.fdjOrange);
    		else if(Vo==18.0f)
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
    		if (Vo == 36.0f)
    			myPaint.setColor(IXCHELColors.danger);
    		else if(Vo==30.0f)
    			myPaint.setColor(IXCHELColors.fdjRed);
    		else if(Vo==24.0f)
    			myPaint.setColor(IXCHELColors.fdjOrange);
    		else if(Vo==18.0f)
    			myPaint.setColor(IXCHELColors.fdjBlue);
    			
    	}
    	else {
    		myPaint.setColor(IXCHELColors.def);
    	}
    	myPaint.setStyle(Style.FILL); 
        myCanvas.drawRect(x+largeur, y, xRelative+20, y+10, myPaint);
    }
    /// dessine la jauge complète
    private void drawCommandJauge(Canvas myCanvas, Paint myPaint, int command) {
    	boolean c36 = false, c30 = false, c24 = false, c18 = false;

    	if (Vo == 36.0f) {
    		c36 = true; c30 = true; c24 = true; c18 = true;
    	} else if (Vo == 30.0f) {
    		c30 = true; c24 = true; c18 = true;
		} else if (Vo == 24.0f) {
			c24 = true; c18 = true;
		} else if (Vo == 18.0f) {
			c18 = true;	
		}
    	
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*0,jaugeLargeur, jaugeHauteur, "36V", c36);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*1,jaugeLargeur, jaugeHauteur, "30V", c30);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*2,jaugeLargeur, jaugeHauteur, "24V", c24);
    	drawACommandJaugePoint(myCanvas, myPaint, leftJaugeOffset, topJaugeOffset+jaugeHauteur*3,jaugeLargeur, jaugeHauteur, "18V", c18);
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
}
