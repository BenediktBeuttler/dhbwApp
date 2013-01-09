package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * CheckBoxlist Fragment, which is used for the Tags. It is not possible to use
 * the android ListView with checkboxes, so we use a custom ListViewAdapter
 */
public class AdminTagListFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	private ListView mainListView;
	private ArrayAdapter<Tag> tagListAdapter;
	private static Button newTag;
	private boolean buttonInvisible = false;
	private ArrayList<Tag> newTagList = new ArrayList<Tag>();

	public AdminTagListFragment() {
	}

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.admin_new_card_tags, null);
		newTag = (Button) v.findViewById(R.id.btn_admin_new_card_new_tag);

		if (savedInstanceState == null) {
			buttonInvisible = getArguments().getBoolean("buttonInvisible");
			if (buttonInvisible) {
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
									// ErrorHandling if TagName is already taken
									ErrorHandlerFragment newFragment = ErrorHandlerFragment
											.newInstance(
													R.string.error_handler_name_taken,
													ErrorHandlerFragment.NAME_TAKEN);
									newFragment.show(getActivity()
											.getFragmentManager(), "dialog");
								} else {
									// Create new Tag
									Tag newTag = Create.getInstance().newTag(
											newTagName);
									// Update the TagList
									getTagsWithCards();
									newTagList.add(newTag);
									tagListAdapter = new TagArrayAdapter(
											getActivity(), newTagList);
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

		// Find the ListView resource.
		mainListView = (ListView) v.findViewById(R.id.tagsListView);
		// tell android that we want this view to create a menu when it is long
		// pressed.
		registerForContextMenu(mainListView);

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

	// Define what happens when the item in list is long pressed
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info;
		info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String tagName = tagListAdapter.getItem(info.position)
				.getTagName();

		if (true/* !tagName.equals("No stacks available") */) {
			if (item.getTitle() == "Edit") {

				// create a dialog to change the tag name
				AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				alert.setTitle("Edit");
				alert.setMessage("Edit the tag name");
				// Set an EditText view to get user input
				final EditText input = new EditText(getActivity());
				input.setText(tagName);
				// pass the string to the textView
				alert.setView(input);
				alert.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// update the ListView by finding the selected
								// tag name and edit it
								for (Tag tag : Tag.allTags) {
									if (tagName.equals(tag.getTagName())) {
										Edit.getInstance()
												.editTag(
														input.getText()
																.toString(),
														tag);
										break;
									}
								}
								tagListAdapter = new TagArrayAdapter(
										getActivity(), getTagsWithCards());
								mainListView.setAdapter(tagListAdapter);
								Toast toast;
								toast = Toast.makeText(getActivity(), tagName
										+ " changed to "
										+ input.getText().toString(),
										Toast.LENGTH_LONG);
								toast.show();
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
								dialog.cancel();
							}
						});
				alert.show();

			} else if (item.getTitle() == "Delete") {
				// create a dialog to change the tag name
				AlertDialog.Builder alert = new AlertDialog.Builder(
						getActivity());
				alert.setTitle("Delete");
				alert.setMessage("Are you sure you want to delete this tag?");
				alert.setIcon(R.drawable.question);
				// pass the string to the textView
				alert.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								for (Tag tag : Tag.allTags) {
									if (tagName.equals(tag.getTagName())) {
										Delete.getInstance().deleteTag(tag);
										break;
									}
								}
								tagListAdapter = new TagArrayAdapter(
										getActivity(), getTagsWithCards());
								mainListView.setAdapter(tagListAdapter);
								Toast toast;
								toast = Toast.makeText(getActivity(), tagName
										+ " has been deleted.",
										Toast.LENGTH_LONG);
								toast.show();
							}
						});
				alert.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
								dialog.cancel();
							}
						});
				alert.show();

			} else {
				return false;
			}
		}

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "Edit");
		menu.add(0, v.getId(), 1, "Delete");
	}

	/**
	 * Only show the Tags associated with cards
	 * 
	 * @return the changed taglist
	 */
	public ArrayList<Tag> getTagsWithCards() {
		newTagList.clear();
		for (Tag tag : Tag.allTags) {
			if (tag.getTotalCards() > 0) {
				newTagList.add(tag);
			}
		}
		return newTagList;
	}

	/**
	 * Holds child views for one row. A row has a textView and a checkbox
	 */
	private class TagViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public TagViewHolder() {
		}

		public TagViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		/**
		 * @return The CheckBox of this row
		 */
		public CheckBox getCheckBox() {
			return checkBox;
		}

		/**
		 * @return The TextView of this row
		 */
		public TextView getTextView() {
			return textView;
		}

	}

	/** 
	 * Custom adapter for displaying an array of Tag objects. 
	 */
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
