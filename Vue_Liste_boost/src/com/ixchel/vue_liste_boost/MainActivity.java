
package com.ixchel.vue_liste_boost;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

	Random random = new Random();
	public ViewPager pager = null;
	SampleAdapter sampleAdapter = null;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
    
    pager=(ViewPager)findViewById(R.id.pager);
    sampleAdapter = new SampleAdapter(this);
    pager.setAdapter(sampleAdapter);

  }

  /*
   * Inspired by
   * https://gist.github.com/8cbe094bb7a783e37ad1
   */
  private class SampleAdapter extends PagerAdapter {
	  
	  private Timer myTimer;	
	  private MainActivity mActivity;
	  int nombreMaxBoosts = 12;
	  BoostView boostViews[] = new BoostView[nombreMaxBoosts];
	 	 
	  DataInc datasInc[] = new DataInc[nombreMaxBoosts];;
	  DataOut datasOut[] = new DataOut[nombreMaxBoosts];

	  private BoostView view1 = null;
	  private BoostView view2 = null;
	  private BoostView view3 = null;
	  private BoostView view4 = null;

	 public SampleAdapter(MainActivity activity) {
		 	super();
		    mActivity = activity;
		    myTimer = new Timer();
		    myTimer.schedule(new TimerTask() {          
		        @Override
		        public void run() {
		            TimerMethod();
		        }

		    }, 0, 500);
		}

	  private Runnable Timer_Tick = new Runnable() {
		    public void run() {
		    //This method runs in the same thread as the UI.               
		    	for (int i=0; i<boostViews.length; i++) {
		    		BoostView cBV = boostViews[i];
		    		if (cBV != null) {
		    		
		    			updateBoostViewWith(cBV, null);
		    			cBV.invalidate();
		    		}
		    	}
		    //Do something to the UI thread here
		    }
		};

		/* Le seul moyen de mettre ˆ jour l'interface puisqu'il faut passer par un Thread spŽcial
		 * 
		 * */
		  private void TimerMethod()
		  {
		      //This method is called directly by the timer
		      //and runs in the same thread as the timer.

			  
		      //We call the method that will work with the UI
		      //through the runOnUiThread method.
			  mActivity.runOnUiThread(Timer_Tick);
		  }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View page= getLayoutInflater().inflate(R.layout.page, container, false);

      view1=(BoostView)page.findViewById(R.id.view1);
      position=position * 4;
      populateBoostView(view1, position);
      view1.setTag(position);
      boostViews[position] = view1;
      position++;
      
      
      view2=(BoostView)page.findViewById(R.id.view2);
      populateBoostView(view2, position);
      view2.setTag(position);
      boostViews[position] = view2;
      position++;
      
      
      view3=(BoostView)page.findViewById(R.id.view3);
      populateBoostView(view3, position);
      view3.setTag(position);
      boostViews[position] = view3;
      position++;
      
      
      view4=(BoostView)page.findViewById(R.id.view4);
      view4.setTag(position);
      boostViews[position] = view4;
      
      
      if (position < getRealCount()) {
    	  populateBoostView(view4, position);
    	  view4.setVisibility(View.VISIBLE);
      }
      else {
    	  view4.setVisibility(View.INVISIBLE);
      }

      /// met a jour la vue
      container.addView(page);
      return(page);
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
      container.removeView((View)object);
    }

    @Override
    public int getCount() {
      return((getRealCount() /4) + 0);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

    	return(view == object);
    }

    private int getRealCount() {
      return(nombreMaxBoosts);
    }
    /*
     * Met ˆ jour l'id de la BoostView
     * */
    private void populateBoostView(BoostView tv, int position) {
      tv.position = position + 1;
    }
    
    /*
     * Permet de reevoir la commande
     * */
    private DataOut getCommandBoostFromBoostView(BoostView bv) {
    	DataOut dataOut = new DataOut((char)bv.position, (char)bv.Vo);
    	return dataOut;
    }
    
    /*
     * Raffraichir la vue via un objet 'DataInc'
     * */
    private void updateBoostViewWith(BoostView bv, DataInc dataInc) {
    	
    	/* integration
    	bv.Vi = dataInc.getVi();
    	bv.Il = dataInc.getIl();
    	bv.Pi = dataInc.getPi();
    	bv.Vo = dataInc.getVo();
    	bv.Io = dataInc.getIo();
    	*/
    	
    	
    	float consignes[] = {18.0f, 24.0f, 30.0f, 36.0f}; 
    	
    	
    	bv.boostState = bv.boostState == true?false:true;
    	bv.Vi = (float)getRandom() % 40;
    	bv.Il = (float)getRandom() % 30;
    	bv.Pi = (float)getRandom() % 20;
    	bv.Vo = (float)consignes[getRandom() % 3];
    	bv.Io = (float)getRandom() % 1000;
    	
    }
    
    private int getRandom() {
    	Random rand = new Random();
		int i = rand.nextInt() % 10000000; // range -255  +255
		i = Math.abs(rand.nextInt() % 100); // range 0  +11
		return i;
    }
    
  }
}
