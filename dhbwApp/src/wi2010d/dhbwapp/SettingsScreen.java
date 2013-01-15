package wi2010d.dhbwapp;

import java.util.ArrayList;
import java.util.List;
import wi2010d.dhbwapp.control.Create;
import wi2010d.dhbwapp.control.Database;
import wi2010d.dhbwapp.control.Delete;
import wi2010d.dhbwapp.control.Edit;
import wi2010d.dhbwapp.errorhandler.ErrorHandlerFragment;
import wi2010d.dhbwapp.model.Card;
import wi2010d.dhbwapp.model.Runthrough;
import wi2010d.dhbwapp.model.Stack;
import wi2010d.dhbwapp.model.Tag;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Settings Activity, displays various options to handle the DB
 */
public class SettingsScreen extends OnResumeActivity implements OnClickListener {

	private Button resetDB, testData, resetStatistics, resetDrawers;
	private ErrorHandlerFragment newFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// reload the data, if sth got garbage collected
		this.reloadOnGarbageCollected();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);

		resetDB = (Button) findViewById(R.id.btn_reset_database);
		resetDB.setOnClickListener(this);

		testData = (Button) findViewById(R.id.btn_write_test_data);
		testData.setOnClickListener(this);

		resetStatistics = (Button) findViewById(R.id.btn_settings_reset_statistics);
		resetStatistics.setOnClickListener(this);

		resetDrawers = (Button) findViewById(R.id.btn_settings_reset_drawer);
		resetDrawers.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_start_screen:
			startActivity(new Intent(this, StartScreen.class));
			finish();
			return true;
		case R.id.menu_help:
			startActivity(new Intent(this, HelpScreen.class));
			finish();
			return true;
		default:
			// if anything goes wrong (case not found), display general error
			// dialog
			newFragment = ErrorHandlerFragment.newInstance(
					R.string.error_handler_general,
					ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// Handle item selection
		switch (v.getId()) {
		case R.id.btn_reset_database:
			// display a dialog to ask for permission to delete the database.
			// if "Yes" is clicked, DB will be deleted
			ErrorHandlerFragment newFragment = ErrorHandlerFragment
					.newInstance(R.string.error_handler_delete_db,
							ErrorHandlerFragment.RESET_DB);
			newFragment.show(getFragmentManager(), "dialog");
			break;

		case R.id.btn_write_test_data:
			// If there's data available don't write the data, because the app
			// will crash
			if (Stack.allStacks.size() == 0 && Card.allCards.size() == 0
					&& Tag.allTags.size() == 0
					&& Runthrough.allRunthroughs.size() == 0) {
				// creates TestData: 100 new Cards in various stacks with
				// various tags
				List<Tag> tags0 = new ArrayList<Tag>();
				List<Tag> tags1 = new ArrayList<Tag>();
				List<Tag> tags2 = new ArrayList<Tag>();
				List<Tag> tags3 = new ArrayList<Tag>();

				List<Card> Java = new ArrayList<Card>();
				List<Card> NachprfRecht = new ArrayList<Card>();
				List<Card> PMZert = new ArrayList<Card>();
				List<Card> BWLGrund = new ArrayList<Card>();


				Tag mundl = Create.getInstance().newTag("Mündliche Prüfung");
				Tag java = Create.getInstance().newTag("Java");
				Tag nachpruefung = Create.getInstance().newTag(
						"Nachprüfung Recht");
				Tag projektmgmt = Create.getInstance().newTag(
						"Projektmgmt. Zertifizierung");
				Tag bwlgrund = Create.getInstance().newTag(
						"BWL Grundlagen 1");

				tags0.add(mundl);
				tags0.add(java);

				tags1.add(nachpruefung);

				tags2.add(bwlgrund);
				tags3.add(projektmgmt);

				Java.add(Create.getInstance().newCard(
						"Mit welcher Methode erstellt man ein Objekt?",
						"Dem Konstruktor", tags0, "", ""));
				Java.add(Create.getInstance().newCard(
						"Wie heißt der kleinste native Datentyp?", "byte",
						tags0, "", ""));
				Java.add(Create.getInstance().newCard(
						"Aus wievielen bytes besteht der Datentyp long?",
						"8 byte.", tags0, "", ""));
				Java.add(Create.getInstance().newCard(
						"Welche Werte kann boolean annehmen?",
						"true und false", tags0, "", ""));
				Java.add(Create.getInstance().newCard(
						"Wie groß ist der int Wertebereich?",
						"- 2^31 bis 2^31 -1", tags0, "", ""));
				Java.add(Create.getInstance().newCard(
						"Wie wird ein eindimenstionales Array initialisiert?"
								+ "Als Bsp int array.",
						"int[] meinArray = new int[10];", tags0, "", ""));
				Java.add(Create
						.getInstance()
						.newCard(
								"Welches Objekt ist meist besser zum Speichern von Daten geeignet als ein Array",
								"Eine ArrayList, da diese dynamisch erweiterbar ist.",
								tags0, "", ""));
				Java.add(Create
						.getInstance()
						.newCard(
								"Nenne alle elementaren Java Datentypen",
								"boolean, byte, short, int, long, float, double und char.",
								tags0, "", ""));

				
				NachprfRecht.add(Create.getInstance().newCard(
						"Welches ist der wichtigste Paragraph bei Käufen im BGB?", "§433BGB", tags1,
						"", ""));
				NachprfRecht.add(Create.getInstance().newCard(
						"Was bedeutet Recht im objektiven Sinne?", "Ein abgrenzbarer Teilbereich der Gesamtheit der gesellschaftlichen Normen ", tags1,
						"", ""));
				NachprfRecht.add(Create.getInstance().newCard(
						"Was bedeutet Recht im subjektiven Sinne?", "Die sich aus dem objektiven Recht ableitende Befugnis des Einzelnen  ", tags1,
						"", ""));
				NachprfRecht.add(Create.getInstance().newCard(
						"Wie heißt der Hauptsatz beim bearbeiten von Rechtsfällen?", "Wer will was von wem woraus?", tags1,
						"", ""));
				
				BWLGrund.add(Create.getInstance().newCard(
						"In welche 2 Unterbereiche Gliedert sich die BWL", "Allgemeine BWL (ABWL) und Spezielle BWL (SBWL)", tags2,
						"", ""));
				BWLGrund.add(Create.getInstance().newCard(
						"Was ist das Ökonomische Prinzip?", "Das ökonomische Prinzip beschreibt die betriebswirtschaftliche Notwendigkeit, die nötigen Mittel zur Erreichung der Unternehmensziele möglichst ideal einzusetzen.", tags2,
						"", ""));
				BWLGrund.add(Create.getInstance().newCard(
						"Nenne 3 mögliche Unternehmensziele.", "Nutzen-, Gewinn-, Umsatzsteigerung usw.", tags2,
						"", ""));
				BWLGrund.add(Create.getInstance().newCard(
						"Was sind die 3 Prinzipien, um die Unternehmensziele zu erreichen?.", "Minimalprinzip, Maximalprinzip und Optimalprinzip.", tags2,
						"", ""));
				
				PMZert.add(Create.getInstance().newCard(
						"Nenne die Definition von Projektmanagement?.", "Als Projektmanagement (PM) bezeichnet man das Planen, Steuern und Kontrollieren von Projekten.", tags3,
						"", ""));
				PMZert.add(Create.getInstance().newCard(
						"Nenne die Phasen des PMs","Analyse, Machbarkeitsstudie,Entwurf,Umsetzung,Test,Pilotierung,Rollout bei den Anwendern, Abschluss", tags3,
						"", ""));
				PMZert.add(Create.getInstance().newCard(
						"Nenne 5 der 9 Wissensgebiete des PMs?.", "Integrationsmanagement 	Umfangsmanagement 	Terminmanagement Kostenmanagement Qualitätsmanagement Personalmanagement Kommunikationsmanagement 	Risikomanagement 	Beschaffungsmanagement", tags3,
						"", ""));
				PMZert.add(Create.getInstance().newCard(
						"Was sind die 3 Erwartungsbereiche der Stakeholder?.", "Zeit, Kosten und Inhalt/Umfang des Projekts.", tags3,
						"", ""));


					Database.getInstance().addNewStack(
							new Stack(false, "BWL Grundlagen 1", BWLGrund));
					Database.getInstance().addNewStack(
							new Stack(false, "Java", Java));
					Database.getInstance().addNewStack(
							new Stack(false, "Nachprüfung Recht", NachprfRecht));
					Database.getInstance().addNewStack(
							new Stack(false, "Projektmanagement Zertifizierung", PMZert));
				

				Toast toast;
				toast = Toast.makeText(getApplicationContext(),
						"Test data written!", Toast.LENGTH_SHORT);
				toast.show();
				break;
			} else {
				Toast.makeText(
						getApplicationContext(),
						"There's data available, please reset the Database first",
						Toast.LENGTH_LONG).show();
				break;
			}
		case R.id.btn_settings_reset_statistics:

			// Check if there is any Stack available
			if (Stack.allStacks.size() != 0) {
				// Get every Stack to delete every Runthrough
				for (Stack stack : Stack.allStacks) {
					for (Runthrough run : stack.getLastRunthroughs()) {

						// Delete Runthrough in DB
						Delete.getInstance().deleteRunthrough(run);
					}

					stack.getLastRunthroughs().clear();
					
					// Reset overall learning time for each stack
					Runthrough overallRunthrough = stack.getOverallRunthrough();
					overallRunthrough.setDurationSecs(0);
					Database.getInstance().changeRunthrough(overallRunthrough);
				}
	

				Toast toastStatistics = Toast.makeText(getApplicationContext(),
						"Statistics has been resetted successfully",
						Toast.LENGTH_LONG);
				toastStatistics.show();
				// if there are no stacks available
			} else {
				Toast toastStatistics = Toast.makeText(getApplicationContext(),
						"There are no Stacks available", Toast.LENGTH_LONG);
				toastStatistics.show();
			}

			break;
		case R.id.btn_settings_reset_drawer:

			// Check if there is any Stack available
			if (Stack.allStacks.size() != 0) {
				// Reset drawers for each stack
				// Get all stacks
				for (Stack stack : Stack.allStacks) {
					Edit.getInstance().resetDrawer(stack);
				}

				Toast toastDrawers = Toast.makeText(getApplicationContext(),
						"Answers have been resetted successfully",
						Toast.LENGTH_LONG);
				toastDrawers.show();
				// if there are no stacks available
			} else {
				Toast toastDrawers = Toast.makeText(getApplicationContext(),
						"There are no Stacks available", Toast.LENGTH_LONG);
				toastDrawers.show();
			}

			break;

		default:
			// if anything goes wrong (case not found), display general error
			// dialog
			newFragment = ErrorHandlerFragment.newInstance(
					R.string.error_handler_general,
					ErrorHandlerFragment.GENERAL_ERROR);
			newFragment.show(getFragmentManager(), "dialog");
			break;
		}
	}
}
