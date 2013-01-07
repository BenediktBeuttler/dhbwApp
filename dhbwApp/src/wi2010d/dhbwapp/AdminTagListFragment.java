package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AdminTagListFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ListView mainListView;
	private ArrayAdapter<Tag> tagListAdapter;
	private static Button newTag;
	private boolean buttonInvisible = false;
	private ArrayList<Tag> newTagList  = new ArrayList<Tag>();

	public AdminTagListFragment() {
	}

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.admin_new_card_tags, null);
		newTag = (Button) v.findViewById(R.id.btn_admin_new_card_new_tag);
		
		if(savedInstanceState == null){
		buttonInvisible = getArguments().getBoolean("buttonInvisible");
		if (buttonInvisible)
		{
			newTag.setVisibility(View.GONE);
		}
		}
		newTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(v
						.getContext());

				alert.setTitle("New Tag");
				alert.setMessage("Insert Tag Name");

				// Set an EditText view to get user input
				final EditText input = new EditText(v.getContext());
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String newTagName = input.getText().toString();
								boolean alreadyTaken = false;

								for (Tag tag : Tag.allTags) {
									if (tag.getTagName().equals(newTagName)) {
										alreadyTaken = true;
										break;
									}
								}

								if (alreadyTaken) {
									//ErrorHandling if TagName is already taken
									ErrorHandlerFragment newFragment = ErrorHandlerFragment
											.newInstance(R.string.error_handler_name_taken, ErrorHandlerFragment.NAME_TAKEN );
									newFragment.show(getActivity().getFragmentManager(), "dialog");	
								} else {
									//Create new Tag
									Tag newTag = Create.getInstance().newTag(newTagName);
									//Update the TagList
									getTagsWithCards();
									newTagList.add(newTag);
									tagListAdapter = new TagArrayAdapter(getActivity(), newTagList);
									mainListView.setAdapter(tagListAdapter);
									
									Toast toast;
									toast = Toast.makeText(getActivity(),
											"New Tag '" + newTagName
													+ "' has been added.",
											Toast.LENGTH_LONG);
									toast.show();
								}
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});
				alert.show();
			}
		});
		
		newTag.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast toast = Toast.makeText(getActivity(), "Döner", Toast.LENGTH_LONG);
				return true;
			}
		});

		// Find the ListView resource.
		mainListView = (ListView) v.findViewById(R.id.tagsListView);

		// When item is tapped, toggle checked properties of CheckBox and
		// Tags.
		mainListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View item,
							int position, long id) {
						Tag tag = tagListAdapter.getItem(position);
						tag.toggleChecked();
						TagViewHolder viewHolder = (TagViewHolder) item
								.getTag();
						viewHolder.getCheckBox().setChecked(tag.isChecked());
					}
				});

		// Create and populate TagList.
		// Set our custom array adapter as the ListView's adapter.
		
		tagListAdapter = new TagArrayAdapter(getActivity(), getTagsWithCards());
		mainListView.setAdapter(tagListAdapter);
		return v;
	}
	
	public ArrayList<Tag> getTagsWithCards(){
		newTagList.clear();
		for(Tag tag : Tag.allTags){
			if(tag.getTotalCards()>0){
				newTagList.add(tag);
			}
		}
		return newTagList;
	}

	/** Holds child views for one row. */
	private class TagViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public TagViewHolder() {
		}

		public TagViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public void setCheckBox(CheckBox checkBox) {
			this.checkBox = checkBox;
		}

		public TextView getTextView() {
			return textView;
		}

		public void setTextView(TextView textView) {
			this.textView = textView;
		}
	}

	/** Custom adapter for displaying an array of Tag objects. */
	private class TagArrayAdapter extends ArrayAdapter<Tag> {

		private LayoutInflater inflater;

		public TagArrayAdapter(Context context, List<Tag> tagList) {
			super(context, R.layout.admin_new_card_tags_simplerow,
					R.id.rowTextView, tagList);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// Tag to display
			Tag tag = (Tag) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textView;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.admin_new_card_tags_simplerow, null);

				// Find the child views.
				textView = (TextView) convertView
						.findViewById(R.id.rowTextView);
				checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
				convertView.setTag(new TagViewHolder(textView, checkBox));

				// If CheckBox is toggled, update the Tag
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Tag tag = (Tag) cb.getTag();
						tag.setChecked(cb.isChecked());
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				TagViewHolder viewHolder = (TagViewHolder) convertView.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			// Tag the CheckBox with the Tag it is displaying, so that we can
			// access the Tag in onClick() when the CheckBox is toggled.
			checkBox.setTag(tag);

			// Display Tag data
			checkBox.setChecked(tag.isChecked());
			textView.setText(tag.getTagName());

			return convertView;
		}

	}
}
