package edu.calpoly.android.lab3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class JokeView extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

	/** Radio buttons for liking or disliking a joke. */
	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	
	/** The container for the radio buttons. */
	private RadioGroup m_vwLikeGroup;

	/** Displays the joke text. */
	private TextView m_vwJokeText;
	
	/** The data version of this View, containing the joke's information. */
	private Joke m_joke;

	/**
	 * Basic Constructor that takes only an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added. 
	 *            
	 * @param joke
	 * 			  The Joke this view is responsible for displaying.
	 */
	public JokeView(Context context, Joke joke) {
		/*super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.joke_view, this, true);
		
		this.m_vwLikeButton = (RadioButton) findViewById(R.id.likeButton);
		this.m_vwDislikeButton = (RadioButton) findViewById(R.id.dislikeButton);
		this.m_vwLikeGroup = (RadioGroup) findViewById(R.id.jokeListViewGroup);
		this.m_vwJokeText = (TextView) findViewById(R.id.jokeTextView);
		
		this.setJoke(joke);
		requestLayout();
		
		this.m_vwLikeGroup.setOnCheckedChangeListener(this);*/
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.joke_view, this, true);
		this.m_vwLikeButton = (RadioButton) findViewById(R.id.likeButton);
		this.m_vwDislikeButton = (RadioButton) findViewById(R.id.dislikeButton);
		//trying this (not original)
		this.m_vwLikeGroup = (RadioGroup) findViewById(R.id.ratingRadioGroup);
		//below could be error (original)
		//this.m_vwLikeGroup = (RadioGroup) findViewById(R.id.jokeListViewGroup);
		this.m_vwJokeText = (TextView) findViewById(R.id.jokeTextView);
		this.m_joke = joke;
		this.m_vwJokeText.setText(this.m_joke.getJoke());
		
		if (joke.getRating() != Joke.UNRATED) {
			if (joke.getRating() == Joke.LIKE) {
				this.m_vwLikeButton.setChecked(true);
			}
			else if (joke.getRating() == Joke.DISLIKE) {
				this.m_vwDislikeButton.setChecked(true);
			}
		}
		else {
			this.m_vwLikeGroup.clearCheck();
		}
		requestLayout();
	}

	/**
	 * Mutator method for changing the Joke object this View displays. This View
	 * will be updated to display the correct contents of the new Joke.
	 * 
	 * @param joke
	 *            The Joke object which this View will display.
	 */
	public void setJoke(Joke joke) {
		this.m_joke = joke;
		this.m_vwJokeText.setText(this.m_joke.getJoke());

		if (this.m_joke.getRating() == Joke.LIKE) {
			this.m_vwLikeButton.setChecked(true);
		}
		else if (this.m_joke.getRating() == Joke.DISLIKE) {
			this.m_vwDislikeButton.setChecked(true);
		}
		else if (this.m_joke.getRating() == Joke.UNRATED){
			this.m_vwLikeGroup.clearCheck();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.likeButton && ((RadioButton) findViewById(R.id.likeButton)).isChecked()) {
			this.m_joke.setRating(Joke.LIKE);
		}
		else if (checkedId == R.id.dislikeButton && ((RadioButton) findViewById(R.id.dislikeButton)).isChecked()) {
			this.m_joke.setRating(Joke.DISLIKE);
		}
		else {
			this.m_joke.setRating(Joke.UNRATED);
		}		
	}
}
