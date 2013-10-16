package edu.calpoly.android.lab3;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class AdvancedJokeList extends SherlockActivity implements ActionMode.Callback {

	/** Contains the name of the Author for the jokes. */
	protected String m_strAuthorName;

	/** Contains the list of Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrJokeList;
	
	/** Contains the list of filtered Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrFilteredJokeList;

	/** Adapter used to bind an AdapterView to List of Jokes. */
	protected JokeListAdapter m_jokeAdapter;

	/** ViewGroup used for maintaining a list of Views that each display Jokes. */
	protected ListView m_vwJokeLayout;

	/** EditText used for entering text for a new Joke to be added to m_arrJokeList. */
	protected EditText m_vwJokeEditText;

	/** Button used for creating and adding a new Joke to m_arrJokeList using the
	 *  text entered in m_vwJokeEditText. */
	protected Button m_vwJokeButton;
	
	/** Menu used for filtering Jokes. */
	protected Menu m_vwMenu;

	/** Background Color values used for alternating between light and dark rows
	 *  of Jokes. Add a third for text color if necessary. */
	protected int m_nDarkColor;
	protected int m_nLightColor;
	protected int m_nLastColorUsed;
	protected int m_nTextColor;
		
	/**
	 * Context-Menu MenuItem IDs.
	 * IMPORTANT: You must use these when creating your MenuItems or the tests
	 * used to grade your submission will fail. These are commented out for now.
	 */
	protected static final int FILTER = Menu.FIRST;
	protected static final int FILTER_LIKE = SubMenu.FIRST;
	protected static final int FILTER_DISLIKE = SubMenu.FIRST + 1;
	protected static final int FILTER_UNRATED = SubMenu.FIRST + 2;
	protected static final int FILTER_SHOW_ALL = SubMenu.FIRST + 3;
	
	protected com.actionbarsherlock.view.ActionMode actionMode;
	protected com.actionbarsherlock.view.ActionMode.Callback callBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.initLayout();
		//this.initAddJokeListeners();
		Resources resource = this.getResources();
		this.m_nLastColorUsed = this.m_nDarkColor = resource.getColor(R.color.dark);
		this.m_nLightColor = resource.getColor(R.color.light);
		this.m_nTextColor = resource.getColor(R.color.text);
		this.m_arrJokeList = new ArrayList<Joke>();
		this.m_arrFilteredJokeList = new ArrayList<Joke>();
		this.m_jokeAdapter = new JokeListAdapter(this, this.m_arrFilteredJokeList);
		this.m_strAuthorName = resource.getString(R.string.author_name);
		String [] jokeListStrings = resource.getStringArray(R.array.jokeList);
		
		this.initLayout();
		this.initAddJokeListeners();
		
		for(String jokeString : jokeListStrings) {
			Log.d("lab2ejowen", "Adding new joke: " + jokeString);
			this.addJoke(new Joke(jokeString, this.m_strAuthorName));
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		this.m_vwMenu = menu;
		
        return true;
    }
	
	private void filter(int filterType) {
		for (Joke filteredJoke : this.m_arrFilteredJokeList) {
			for (Joke allJoke : this.m_arrJokeList) {
				if (filteredJoke.equals(allJoke)) {
					allJoke.setRating(filteredJoke.getRating());
				}
			}
		}
		
		this.m_arrFilteredJokeList.clear();
		
		if (filterType == FILTER_SHOW_ALL) {
			this.m_arrFilteredJokeList.addAll(m_arrJokeList);
			//Toast.makeText(this, "Found a joke in category: " + filterType, Toast.LENGTH_LONG).show();
		}
		else {
			for (Joke j : this.m_arrJokeList) {
				if (j.getRating() == filterType) {
					//Toast.makeText(this, "Found a joke in category: " + filterType, Toast.LENGTH_LONG).show();
					this.m_arrFilteredJokeList.add(j);
				}
			}
		}
		
		this.m_jokeAdapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.submenu_like:
	            this.filter(Joke.LIKE);
	            return true;
	        case R.id.submenu_dislike:
	            this.filter(Joke.DISLIKE);
	            return true;
	        case R.id.submenu_unrated:
	            this.filter(Joke.UNRATED);
	            return true;
	        case R.id.submenu_show_all:
	            this.filter(FILTER_SHOW_ALL);
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Layout for this Activity.
	 */
	protected void initLayout() {		
		setContentView(R.layout.advanced);
		this.m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
		this.m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
		this.m_vwJokeLayout = (ListView) findViewById(R.id.jokeListViewGroup);
		this.m_vwJokeLayout.setAdapter(this.m_jokeAdapter);
		
		this.callBack = new ActionMode.Callback() {
		    // Called when the action mode is created; startActionMode() was called
		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		        // Inflate a menu resource providing context menu items
		        MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.actionmenu, menu);
		        return true;
		    }

		    // Called each time the action mode is shown. Always called after onCreateActionMode, but
		    // may be called multiple times if the mode is invalidated.
		    @Override
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        return false; // Return false if nothing is done
		    }

		    // Called when the user selects a contextual menu item
		    @Override
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		        switch (item.getItemId()) {
		            case R.id.menu_remove:
		                //shareCurrentItem();
		            	Toast.makeText(getBaseContext(), "Remove button clicked", Toast.LENGTH_LONG).show();
		                mode.finish(); // Action picked, so close the CAB
		                return true;
		            default:
		                return false;
		        }
		    }

		    // Called when the user exits the action mode
		    @Override
		    public void onDestroyActionMode(ActionMode mode) {
		        //mode = null;
		    }
		};
		
	}

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Event Listeners which will respond to requests to "Add" a new Joke to the
	 * list.
	 */
	protected void initAddJokeListeners() {
		m_vwJokeEditText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
						String userJoke = m_vwJokeEditText.getText().toString();
						if(!userJoke.isEmpty() && !userJoke.equals(null)) {
							m_vwJokeEditText.setText("");
							addJoke(new Joke(userJoke, m_strAuthorName));
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
						}
						return true;
					}
				}
				return false;
			}
		});

		m_vwJokeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String userJoke = m_vwJokeEditText.getText().toString();
				if(!userJoke.isEmpty() && !userJoke.equals(null)) {
					m_vwJokeEditText.setText("");
					addJoke(new Joke(userJoke, m_strAuthorName));
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
				}
			}
		});
		
		this.m_vwJokeLayout.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				/*if (actionMode != null) {
					return false;
				}*/
				
				Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
				actionMode = startActionMode(callBack);
				return true;
			}
		});
	}

	/**
	 * Method used for encapsulating the logic necessary to properly add a new
	 * Joke to m_arrJokeList, and display it on screen.
	 * 
	 * @param joke
	 *            The Joke to add to list of Jokes.
	 */
	protected void addJoke(Joke joke) {
		this.m_arrJokeList.add(joke);
		//ADDED LINE BELOW, COULD BE WRONG
		this.m_arrFilteredJokeList.add(joke);
		this.m_jokeAdapter.notifyDataSetChanged();
		//this.m_vwJokeLayout.addView(new JokeView(this, joke));
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}
	
}