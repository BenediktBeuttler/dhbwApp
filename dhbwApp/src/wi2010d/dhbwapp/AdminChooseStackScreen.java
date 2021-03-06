package wi2010d.dhbwapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.control.Exchange;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Shows all the stacks and provides methods for editing/deleting/archiving them
 */
public class AdminChooseStackScreen extends OnResumeActivity {

	public final static int RESULT_OK = 1;
	public final static int RESULT_FAIL = 2;
	private ListView lv;
	private ArrayAdapter<String> lvAdapter;
	private boolean stackAvailable = false;
	private Button btnNewTag;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();
		super.onCreate(savedInstanceState);

		context = this;
		setContentView(R.layout.admin_choose_stack_screen);
		lv = (ListView) findViewById(R.id.admin_stack_list);

		updateStackList();

		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				parent.showContextMenuForChild(v);
			}
		});
	}

	// When the registered view receives a long-click event, the system calls
	// the onCreateContextMenu() method. This is where the menu items are
	// defined.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		String sName = ((TextView) info.targetView).getText().toString();
		if (sName.startsWith("<Dyn>")) {
			sName = sName.substring(6);
		}
		boolean isDynamic = false;
		for (Stack stack : Stack.allStacks) {
			if (stack.getStackName().equals(sName)) {
				if (stack.isDynamicGenerated()) {
					isDynamic = true;
				}
			}
		}
		if (isDynamic) {
			menu.add(0, v.getId(), 0, "Change Name and Tags");
			menu.add(0, v.getId(), 1, "Reset Answers");
			menu.add(0, v.getId(), 2, "Delete");
			menu.add(0, v.getId(), 3, "Archive");
		} else {
			menu.add(0, v.getId(), 0, "Change Name");
			menu.add(0, v.getId(), 1, "Reset Answers");
			menu.add(0, v.getId(), 2, "Add Tags to all Cards");
			menu.add(0, v.getId(), 3, "Delete");
			menu.add(0, v.getId(), 4, "Archive");
		}
	}

	// Define what happens when the item in list is long pressed
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info;
		info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String stackName;
		String stackNameForIf = ((TextView) info.targetView).getText()
				.toString();
		if (stackNameForIf.startsWith("<Dyn>")) {
			stackName = stackNameForIf.substring(6);
		} else {
			stackName = ((TextView) info.targetView).getText().toString();
		}

		if (!stackName.equals("No stacks available")) {
			if (item.getTitle() == "Change Name") {

				// pass the stack name to the edit activity and whether it is an
				// dynamic generated stack or not, edit it

				// create dialog to insert name of new stack
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Edit Stack");
				alert.setMessage("Change the name of the stack");

				// Set an EditText view to get user input
				final EditText input = new EditText(this);
				input.setText(stackName);
				alert.setView(input);

				// Set the new Stack name
				alert.setPositiveButton("Update",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								boolean nameAlreadyTaken = false;
								Toast toast;
								for (Stack stack : Stack.allStacks) {
									if (stack.getStackName().equals(
											input.getText().toString())) {
										nameAlreadyTaken = true;
										break;
									}
								}
								if (nameAlreadyTaken) { //handle name already taken
									toast = Toast
											.makeText(
													getApplicationContext(),
													"The stack with the name "
															+ input.getText()
																	.toString()
															+ " is already existing, please select another name!",
													Toast.LENGTH_LONG);
									toast.show();
								} else if (input.getText().toString()
										.equals("")) { // handle
														// 'no
														// name'
									toast = Toast.makeText(
											getApplicationContext(),
											"Please insert a stack name!",
											Toast.LENGTH_SHORT);
									toast.show();
								} else if (input.getText().toString()
										.equals("All Stacks")) { // handle name
																	// 'All
																	// Stacks',
																	// this is
																	// used for
																	// some
																	// methods
									toast = Toast
											.makeText(
													getApplicationContext(),
													"Stack name cannot be 'All Stacks', please select another name!",
													Toast.LENGTH_LONG);
									toast.show();

								} else {
									Stack clickedStack = null;
									for (Stack stack : Stack.allStacks) {
										if (stack.getStackName().equals(
												stackName)) {
											clickedStack = stack;
											break;
										}
									}
									String clickedStackName = input.getText()
											.toString();
									Edit.getInstance().changeStackName(
											clickedStackName, clickedStack);
									toast = Toast.makeText(
											getApplicationContext(), "Stack "
													+ clickedStackName
													+ " changed succesfully",
											Toast.LENGTH_LONG);
									toast.show();
									updateStackList();
								}
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
				alert.show();

			} else if (item.getTitle() == "Change Name and Tags") {
				// pass the stack name to the edit activity and whether it is an
				// dynamic generated stack or not, edit it

				Intent i = new Intent(getApplicationContext(),
						AdminEditDynamicStack.class);
				i.putExtra("stackName", stackName);
				i.putExtra("buttonInvisible", true);
				startActivityForResult(i, 1);

			} else if (item.getTitle() == "Reset Answers") {
				// all cards of this stack get reseted and set back to the
				// answer:
				// don't know
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// set title
				alertDialogBuilder.setTitle("Reset Answers");
				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to reset all answers in this stack to 'don't know'?")
						.setIcon(R.drawable.question)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Stack clickedStack = null;
										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(
													stackName)) {
												clickedStack = stack;
												break;
											}
										}

										// reset all answers to don't know
										Edit.getInstance().resetDrawer(
												clickedStack);
										Toast toast = Toast
												.makeText(
														getApplicationContext(),
														stackName
																+ " has been resetted successfully",
														Toast.LENGTH_SHORT);
										toast.show();
										setResult(AdminChooseStackScreen.RESULT_OK);

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			} else if (item.getTitle() == "Add Tags to all Cards") {

				final Dialog builder = new Dialog(this);
				LayoutInflater li = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View tagView = li.inflate(R.layout.admin_new_card_tags,
						null, false);
				final ListView lv = (ListView) tagView
						.findViewById(R.id.tagsListView);
				btnNewTag = (Button) tagView
						.findViewById(R.id.btn_admin_new_card_new_tag);

				ArrayList<String> items = new ArrayList<String>();
				// populate the listView
				for (Tag tag : Tag.allTags) {
					items.add(tag.getTagName());
				}

				Collections.sort(items);
				lvAdapter = new ArrayAdapter<String>(this,
						R.layout.layout_listitem, items);
				btnNewTag.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								context);
						alert.setTitle("Add Tag to Cards");
						alert.setMessage("Please insert the name of the Tag you want to add to all Cards in this Stack");

						// Set an EditText view to get user input final
						final EditText input = new EditText(context);
						alert.setView(input);

						// Set the new Stack name
						alert.setPositiveButton("Add Tag",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// save the tagName in a variable
										String newTagName = input.getText()
												.toString();
										Stack clickedStack = null;
										Tag newTag;
										// check if TagName is already taken
										boolean alreadyTaken = false;
										for (Tag tag : Tag.allTags) {
											if (tag.getTagName().equals(
													newTagName)) {
												alreadyTaken = true;
												break;
											}
										}
										if (alreadyTaken) {
											// ErrorHandling if TagName is
											// already taken
											ErrorHandlerFragment newFragment = ErrorHandlerFragment
													.newInstance(
															R.string.error_handler_name_taken,
															ErrorHandlerFragment.NAME_TAKEN);
											newFragment.show(
													getFragmentManager(),
													"dialog");
										} else if (newTagName.equals("")) {
											// Error Handling if no name is
											// typed
											ErrorHandlerFragment newFragment = ErrorHandlerFragment
													.newInstance(
															R.string.error_handler_no_input,
															ErrorHandlerFragment.NO_INPUT);
											newFragment.show(
													getFragmentManager(),
													"dialog");

										} else {
											newTag = Create.getInstance()
													.newTag(newTagName);
											for (Stack stack : Stack.allStacks) {
												if (stack.getStackName()
														.equals(stackName)) {
													clickedStack = stack;
													Edit.getInstance()
															.addTagToStack(
																	newTag,
																	clickedStack);
													Toast.makeText(
															getApplicationContext(),
															"Tag '"
																	+ newTag.getTagName()
																	+ "' has been added to the Stack '"
																	+ stackName
																	+ "'.",
															Toast.LENGTH_SHORT)
															.show();
													builder.dismiss();
													break;
												}
											}
										}
									}
								});

						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								});
						alert.show();
					}
				});
				lv.setClickable(true);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						String chosenTagName = ((TextView) v).getText()
								.toString();
						Tag chosenTag = null;
						for (Tag tag : Tag.allTags) {
							if (tag.getTagName().equals(chosenTagName)) {
								chosenTag = tag;
								break;
							}
						}
						Stack clickedStack = null;
						for (Stack stack : Stack.allStacks) {
							if (stack.getStackName().equals(stackName)) {
								clickedStack = stack;
								Edit.getInstance().addTagToStack(chosenTag,
										clickedStack);
								break;
							}
						}

						Toast.makeText(
								getApplicationContext(),
								"Tag '" + chosenTag
										+ "' has been added to the Stack '"
										+ stackName + "'.", Toast.LENGTH_SHORT)
								.show();
						builder.dismiss();
					}
				});
				lv.setAdapter(lvAdapter);
				builder.setTitle("Choose Tags");
				builder.setContentView(tagView);
				builder.show();

			} else if (item.getTitle() == "Delete") {
				// Delete the selected stack, after asking the user

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// set title
				alertDialogBuilder.setTitle("Delete Stack");
				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Are you sure you want to delete " + stackName
										+ "?")
						.setIcon(R.drawable.question)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Stack clickedStack = null;
										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(
													stackName)) {
												clickedStack = stack;
												break;
											}
										}

										// Delete the stack
										Delete.getInstance().deleteStack(
												clickedStack);
										// Update the stackList
										updateStackList();
										// Show Toast
										Toast.makeText(
												getApplicationContext(),
												stackName
														+ " got deleted successfuly",
												Toast.LENGTH_LONG).show();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

				// Archive: Export the Stack, then delete it.
			} else if (item.getTitle() == "Archive") {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				// set title
				alertDialogBuilder.setTitle("Archive Stack");
				// set dialog message
				alertDialogBuilder
						.setMessage(
								"Do you really want to archive " + stackName
										+ "?")
						.setIcon(R.drawable.question)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Stack clickedStack = null;
										for (Stack stack : Stack.allStacks) {
											if (stack.getStackName().equals(stackName)) {
												clickedStack = stack;
												break;
											}
										}
										if (!Environment.getExternalStorageState().equals( // handle
																							// read/write
																							// problems
												Environment.MEDIA_UNMOUNTED)
												|| !Environment.getExternalStorageState().equals(
														Environment.MEDIA_MOUNTED_READ_ONLY)
												|| new File(Environment.getExternalStorageDirectory()
														.getPath() + "/knowItOwl/").canWrite()) {
											try {
												Exchange.getInstance().exportStack(
														clickedStack,
														Environment.getExternalStorageDirectory()
																.getPath() + "/knowItOwl/-archived-",
														stackName);

												Delete.getInstance().deleteStack(clickedStack);

												updateStackList();

												Toast toast = Toast.makeText(getApplicationContext(),
														stackName + " archived successfully!",
														Toast.LENGTH_SHORT);
												toast.show();
											} catch (Exception e) {
												ErrorHandlerFragment newFragment = ErrorHandlerFragment
														.newInstance(R.string.error_handler_general,
																ErrorHandlerFragment.EXPORT_ERROR);
												newFragment.show(getFragmentManager(), "dialog");
											}
										} else {
											ErrorHandlerFragment newFragment = ErrorHandlerFragment
													.newInstance(R.string.error_handler_no_sd,
															ErrorHandlerFragment.NO_SD);
											newFragment.show(getFragmentManager(), "dialog");
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * Updates the ListView
	 * 
	 * @return true, if it worked
	 */
	public boolean updateStackList() {
		stackAvailable = false;

		ArrayList<String> items = new ArrayList<String>();
		for (Stack stack : Stack.allStacks) {
			if (stack.isDynamicGenerated()) {
				if (stack.getStackName().startsWith("<Dyn>")) {
					items.add(stack.getStackName());
					stackAvailable = true;
				} else {
					items.add("<Dyn> " + stack.getStackName());
					stackAvailable = true;
				}
			} else {
				items.add(stack.getStackName());
				stackAvailable = true;
			}
		}
		if (items.size() == 0) {
			items.add("No stacks available");
			stackAvailable = false;
		}

		// tell android that we want this view to create a menu when it is long
		// pressed. Method onCreateContextMenu is further relevant
		if (stackAvailable) {
			registerForContextMenu(lv);
			lv.setClickable(true);
		} else {
			unregisterForContextMenu(lv);
			lv.setClickable(false);
		}
		Collections.sort(items);
		lvAdapter = new ArrayAdapter<String>(this, R.layout.layout_listitem,
				items);
		lv.setAdapter(lvAdapter);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_choose_stack_screen, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			updateStackList();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpEditCardsStackScreen.class));
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			return true;
		default:
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_general,
							ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(this.getFragmentManager(), "dialog");
			return false;
		}
	}

}