package edu.calpoly.android.lab3;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AdvancedJokeList extends Activity {

	/** Contains the name of the Author for the jokes. */
	protected String m_strAuthorName;

	/** Contains the list of Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrJokeList;
	
	/** Contains the list of filtered Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrFilteredJokeList;

	/** Adapter used to bind an AdapterView to List of Jokes. */
	protected JokeListAdapter m_jokeAdapter;

	/** ViewGroup used for maintaining a list of Views that each display Jokes. */
	protected LinearLayout m_vwJokeLayout;

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
	//protected static final int FILTER = Menu.FIRST;
	//protected static final int FILTER_LIKE = SubMenu.FIRST;
	//protected static final int FILTER_DISLIKE = SubMenu.FIRST + 1;
	//protected static final int FILTER_UNRATED = SubMenu.FIRST + 2;
	//protected static final int FILTER_SHOW_ALL = SubMenu.FIRST + 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initLayout();
		this.initAddJokeListeners();
		Resources resource = this.getResources();
		this.m_nLastColorUsed = this.m_nDarkColor = resource.getColor(R.color.dark);
		this.m_nLightColor = resource.getColor(R.color.light);
		this.m_nTextColor = resource.getColor(R.color.text);
		this.m_arrJokeList = new ArrayList<Joke>();
		this.m_strAuthorName = resource.getString(R.string.author_name);
		String [] jokeListStrings = resource.getStringArray(R.array.jokeList);
		for(String jokeString : jokeListStrings) {
			Log.d("lab2ejowen", "Adding new joke: " + jokeString);
			this.addJoke(new Joke(jokeString, this.m_strAuthorName));
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO
        return true;
    }

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Layout for this Activity.
	 */
	protected void initLayout() {		
		setContentView(R.layout.advanced);
		this.m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
		this.m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
		this.m_vwJokeLayout = (LinearLayout) findViewById(R.id.jokeListViewGroup);
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

		this.m_vwJokeLayout.addView(new JokeView(this, joke));
	}
}